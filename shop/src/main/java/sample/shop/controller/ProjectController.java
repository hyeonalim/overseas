/**
 * 
 ㄴ등록한 상품 조회
=> 세션으로 회원 정보를 통해 등록한 상품 조회

프로젝트
ㄴ등록
 ㄴ판매자 등록
=> 값 선택 다양하게 가능하도록

 ㄴ구매자 등록
=> HELP 만 가능하도록
 
ㄴ조회 (생성 필요)
 ㄴ일반 조회
 ㄴ상세 조회
 ㄴ구매방식 조회
 ㄴ태그 조회
 ㄴ회원 조회
=> 드롭박스로 조회할 것 선택 후 검색어 입력

ㄴ수정/삭제
=> 판매자 <=> 구매자 서로 변경 불가
=> 삭제 시, 모든 캐쉬 취소나 환불 확인한 뒤 삭제 가능하도록 (잠시 값은 close로 변경됨)
=> 없는 값은 기존 값으로 변경
 */
package sample.shop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Member;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectStatus;
import sample.shop.service.MemberService;
import sample.shop.service.ProjectService;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MemberService memberService;

    private final MemberController memberController;

    // 프로젝트 생성
    @RequestMapping("/projectCreate")
    public String projectCreate(HttpSession httpSession, Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/project/product-create";
    }

    @RequestMapping("/projectHelpCreate")
    public String projectHelpCreate(HttpSession httpSession, Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/project/product-help-create";
    }

    // 프로젝트 조회
    @RequestMapping("/project")
    public String project(HttpSession httpSession, Model model) {
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/project/blog-grid";
    }

    // 프로젝트 상세조회
    @RequestMapping("/project{projectNo}")
    public String projectSingle(@PathVariable("projectNo") Long projectNo, HttpSession httpSession, Model model) {
        Project project = projectService.surchOne(projectNo);

        model.addAttribute("project", project);
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/project/project-single";
    }

    // 프로젝트 조회 (회원)
    @RequestMapping("/projectByMember{memberNo}")
    public String projectMember(@PathVariable("memberNo") Long memberNo, HttpSession httpSession, Model model) {
        Member member = memberService.myInfo(memberNo);
        Member nowLoginMember = memberController.modelMember(httpSession);

        model.addAttribute("member", member);
        model.addAttribute("nowLoginMember", nowLoginMember);

        if (member != nowLoginMember) {
            return "sample/project/blog-gridByMember";
        } else {
            return "sample/project/blog-gridByMyMember";
        }
    }

    // 프로젝트 수정
    @RequestMapping("/projectEdit{projectNo}")
    public String projectEdit(@PathVariable("projectNo") Long projectNo, HttpSession httpSession, Model model) {
        Project project = projectService.surchOne(projectNo);
        model.addAttribute("project", project);
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        if (project.getStatus().equals(ProjectStatus.HELP)) {
            return "sample/project/product-help-edit";
        } else if (project.getStatus().equals(ProjectStatus.RESEARCH)) {
            return "sample/project/product-research-edit";
        } else if (project.getStatus().equals(ProjectStatus.SALE)) {
            return "sample/project/product-sale-edit";
        } else if (project.getStatus().equals(ProjectStatus.GRAD)) {
            return "sample/project/product-grad-edit";
        } else {
            return "sample/project/product-finish-edit";
        }
    }
}