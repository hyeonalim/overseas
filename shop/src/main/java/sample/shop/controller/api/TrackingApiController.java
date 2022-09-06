package sample.shop.controller.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;

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
import sample.shop.controller.data.TrackingForm;
import sample.shop.controller.data.UpdateTrackingRequest;
import sample.shop.domain.TrackingStatus;
import sample.shop.domain.Order;
import sample.shop.domain.Tracking;
import sample.shop.service.MemberService;
import sample.shop.service.OrderService;
import sample.shop.service.TrackingService;

@RestController
@RequiredArgsConstructor
public class TrackingApiController {

    private final TrackingService trackingService;
    private final OrderService orderService;
    private final MemberService memberService;

    // 생성
    @PostMapping("/api/tracking/save/{orderNo}")
    public Long saveMember(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @PathVariable("orderNo") Long orderNo, @RequestBody @Valid TrackingForm request) {
        Long no = trackingService.save(request, nowLoginMemberNo, orderNo);

        return no;

    }

    // 조회
    @GetMapping("/api/{orderNo}/tracking/list")
    public List<TrackingInfoDto> trackingInfo(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @PathVariable("orderNo") Long orderNo) {
        Order order = orderService.findOne(orderNo);

        List<Tracking> findTrackings = trackingService.findOrder(order, memberService.myInfo(nowLoginMemberNo));
        List<TrackingInfoDto> collect = findTrackings.stream()
                .map(t -> new TrackingInfoDto(t.getStatus(), t.getDate(), t.getLocal(), t.getMemo()))
                .collect(Collectors.toList());

        return collect;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class TrackingInfoDto {
        @Enumerated(EnumType.STRING)
        private TrackingStatus status;

        private LocalDateTime date;
        private String local;
        private String memo;
    }

    // 수정
    @PutMapping("/api/tracking/edit/{trackingNo}")
    public UpdateProjectResponse updateMemberV2(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @PathVariable("trackingNo") Long trackingNo,
            @RequestBody @Valid UpdateTrackingRequest request) {
        trackingService.update(trackingNo, memberService.myInfo(nowLoginMemberNo), request);
        return new UpdateProjectResponse(trackingNo, request.getDate());
    }

    @Data
    @AllArgsConstructor
    static class UpdateProjectResponse {
        private Long no;
        private LocalDateTime date;
    }

}