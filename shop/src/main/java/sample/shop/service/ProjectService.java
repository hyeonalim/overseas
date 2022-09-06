package sample.shop.service;

import java.util.List;

import sample.shop.controller.data.ProjectForm;
import sample.shop.controller.data.UpdateProjectRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Project;

public interface ProjectService {

    // 생성
    String upload(ProjectForm request, Member member, String photoUrl);

    // 조회
    List<Project> surchAll();

    // 상세 조회
    Project surchOne(Long no);

    // 프로젝트 조회(회원)
    List<Project> surchMember(Member member);

    // 삭제
    void delete(Long no, Member member);

    // 업데이트
    Project update(Long no, UpdateProjectRequest request);

    List<Project> surchOneList(Long id);

    // 검색
    List<Project> surch(String type, String word);

}
