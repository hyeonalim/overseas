package sample.shop.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.ProjectForm;
import sample.shop.controller.data.UpdateProjectRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectStatus;
import sample.shop.service.FileService;
import sample.shop.service.MemberService;
import sample.shop.service.OrderService;
import sample.shop.service.ProjectService;
import sample.shop.service.TrackingService;

@RestController
@RequiredArgsConstructor
public class ProjectApiController {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final FileService fileService;
    private final OrderService orderService;
    private final TrackingService trackingService;

    // 생성
    @PostMapping("/api/project/save")
    public String saveProject(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @RequestBody @Valid ProjectForm request) {
        Member member = memberService.myInfo(nowLoginMemberNo);
        String result = projectService.upload(request, member, fileService.fileUrl);

        return result;
    }

    @PostMapping("/api/project/help/save")
    public String saveHelpProject(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @RequestBody @Valid ProjectForm request) {
        request.setStatus("HELP");
        request.setQuantity(1);
        request.setOrderStatus("CASH");

        String result = projectService.upload(request, memberService.myInfo(nowLoginMemberNo), fileService.fileUrl);

        return result;
    }

    // 조회
    @GetMapping("/api/project/list")
    public List<ProjectInfoDto> projectCard() {
        List<Project> findProjects = projectService.surchAll();
        List<ProjectInfoDto> collect = findProjects.stream()
                .map(p -> new ProjectInfoDto(p.getNo(), p.getName(),
                        p.getFirstPrice() + p.getSecondPrice() + p.getThirdPrice(), p.getMember().getName(),
                        p.getPhotoUrl(), p.getQuantity(),
                        p.getStatus()))
                .collect(Collectors.toList());

        return collect;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class ProjectInfoDto {
        private Long no;
        private String name;
        private int price;
        private String memberName;
        private String photoUrl;
        private int quantity;

        @Enumerated(EnumType.STRING)
        private ProjectStatus status;
    }

    // 상세조회
    @PostMapping("/api/project/detail")
    public List<ProjectInfoOneDto> projectCard(@RequestBody @Valid Info request) {
        Long no = request.getNo();

        List<Project> findProject = projectService.surchOneList(no);
        List<ProjectInfoOneDto> collect = findProject.stream()
                .map(p -> new ProjectInfoOneDto(p.getName(), p.getExp(),
                        p.getFirstPrice() + p.getSecondPrice() + p.getThirdPrice(), p.getFirstPrice(),
                        p.getSecondPrice(), p.getThirdPrice(), p.getStatus(),
                        p.getMember().getNo()))
                .collect(Collectors.toList());
        return collect;
    }

    @Data
    static class Info {
        private Long no;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class ProjectInfoOneDto {
        private String name;
        private String exp;
        private int price;
        private int firstPrice;
        private int secondPrice;
        private int thirdPrice;
        private ProjectStatus status;
        private Long memberId;
    }

    // 프로젝트 조회(회원)
    @PostMapping("/api/project/member")
    public List<ProjectByMemberDto> projectMember(@RequestBody @Valid Info request) {
        Member member = memberService.myInfo(request.getNo());

        List<Project> projectByMember = projectService.surchMember(member);
        List<ProjectByMemberDto> project = projectByMember.stream()
                .map(p -> new ProjectByMemberDto(p.getNo(), p.getName(),
                        p.getFirstPrice() + p.getSecondPrice() + p.getThirdPrice(), p.getQuantity(),
                        p.getStatus(), p.getMember().getName()))
                .collect(Collectors.toList());
        return project;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class ProjectByMemberDto {
        private Long no;
        private String name;
        private int price;
        private int quantity;
        private ProjectStatus status;
        private String memberName;
    }

    // 업뎃
    @PutMapping("/api/project/edit/{projectNo}")
    public UpdateProjectResponse updateMemberV2(@PathVariable("projectNo") Long projectNo,
            @SessionAttribute("mSession") Long nowLoginMemberNo,
            @RequestBody @Valid UpdateProjectRequest request) {
        Project project = projectService.update(projectNo, request);

        if (project.getStatus().equals(ProjectStatus.GRAD)) {
            List<Order> orders = orderService.updateByProject(project);
            trackingService.saveByGRAD(orders, project);
        }

        return new UpdateProjectResponse(projectNo, request.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateProjectResponse {
        private Long no;
        private String name;
    }

    // 삭제
    @DeleteMapping("/api/project/remove/{projectNo}")
    public void deleteMember(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @PathVariable("projectNo") Long projectNo) {
        Project project = projectService.surchOne(projectNo);

        // 예치금 돌려주기
        orderService.deleteByProject(project.getOrders(), project);
        projectService.delete(projectNo, memberService.myInfo(nowLoginMemberNo));
    }

    // 검색 (구매방식, 나라, 태그)
    @PostMapping("/api/project/search")
    public List<ProjectByMemberDto> projectSearch(@RequestBody @Valid SearchInfo request) {
        List<Project> projectByMember = projectService.surch(request.getType(), request.getWord());
        List<ProjectByMemberDto> project = projectByMember.stream()
                .map(p -> new ProjectByMemberDto(p.getNo(), p.getName(),
                        p.getFirstPrice() + p.getSecondPrice() + p.getThirdPrice(), p.getQuantity(),
                        p.getStatus(), p.getMember().getName()))
                .collect(Collectors.toList());
        return project;
    }

    @Data
    static class SearchInfo {
        private String type;
        private String word;
    }

}