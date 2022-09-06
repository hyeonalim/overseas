package sample.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.ProjectForm;
import sample.shop.controller.data.UpdateProjectRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectOrderStatus;
import sample.shop.domain.ProjectStatus;
import sample.shop.repository.ProjectRepository;
import sample.shop.repository.JPA.JPAProjectRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JPAProjectRepository ProjectRepositoryJPA;

    private CashService cashService;

    // 생성
    @Override
    @Transactional
    public String upload(ProjectForm request, Member member, String photoUrl) {
        Project project = new Project();

        project.setName(request.getName());

        if (request.getStatus().equals("RESEARCH")) {
            project.setStatus(ProjectStatus.RESEARCH);
        } else if (request.getStatus().equals("SALE")) {
            project.setStatus(ProjectStatus.SALE);
        } else if (request.getStatus().equals("HELP")) {
            project.setStatus(ProjectStatus.HELP);
        } else {
            throw new IllegalStateException("프로젝트 구분을 정해주세요");
        }

        if (request.getFirstPrice() != 0) {
            project.setFirstPrice(request.getFirstPrice());
            if (request.getSecondPrice() != 0) {
                project.setSecondPrice(request.getSecondPrice());
                project.setThirdPrice(request.getThirdPrice());
            }
        }

        if (request.getQuantity() != 0) {
            project.setQuantity(request.getQuantity());
        }

        if (request.getOrderStatus().equals("ACCOUNT")) {
            project.setOrderStatus(ProjectOrderStatus.ACCOUNT);
        } else if (request.getOrderStatus().equals("CASH")) {
            project.setOrderStatus(ProjectOrderStatus.CASH);
        } else {
            throw new IllegalStateException("프로젝트 구매 방법을 선택해주세요");
        }

        project.setCountry(request.getCountry());
        project.setExp(request.getExp());

        if (photoUrl != null) {
            member.setPhotoUrl(photoUrl);
        } else {
            member.setPhotoUrl("/img/upload/KakaoTalk_20220413_004209234.jpg");
        }

        project.setMember(member);

        projectRepository.save(project);

        if (request.getQuantity() * request.getFirstPrice() > 150000) {
            return "통관세가 부여됩니다";
        } else {
            return "통관세가 부여되지 않습니다";
        }
    };

    // 조회
    @Override
    public List<Project> surchAll() {
        return projectRepository.findAll();
    };

    // 상세 조회
    @Override
    public Project surchOne(Long no) {
        return projectRepository.findOne(no);
    };

    // 프로젝트 조회(회원)
    @Override
    public List<Project> surchMember(Member member) {
        return projectRepository.findByMember(member);
    };

    // 삭제
    @Override
    @Transactional
    public void delete(Long no, Member member) {
        Project project = surchOne(no);
        if (project.getMember() == member) {
            ProjectRepositoryJPA.deleteById(project.getNo());
        } else {
            throw new IllegalStateException("회원님이 생성한 프로젝트가 아니라 삭제 불가합니다.");
        }
    };

    // 업데이트
    /**
     * => 판매자 <=> 구매자 서로 변경 불가
     * => 삭제 시, 모든 캐쉬 취소나 환불 확인한 뒤 삭제 가능하도록 (잠시 값은 close로 변경됨)
     * => 없는 값은 기존 값으로 변경
     */
    @Override
    @Transactional
    public Project update(Long no, UpdateProjectRequest request) {
        Project project = projectRepository.findOne(no);

        if (request.getName().length() != 0) {
            project.setName(request.getName());
        }

        if (project.getStatus().equals(ProjectStatus.HELP)) {
            if (request.getStatus().equals("CLOSED")) {
                project.setStatus(ProjectStatus.CLOSED);

                if (project.getOrderStatus().equals(ProjectOrderStatus.CASH)) {
                    cashService.cashUpdateByOrder(project);
                }
            }
        } else {
            if (project.getStatus().equals(ProjectStatus.RESEARCH)) {
                if (request.getStatus().equals("SALE")) {
                    project.setStatus(ProjectStatus.SALE);
                } else if (request.getStatus().equals("RESEARCH")) {
                    project.setStatus(ProjectStatus.RESEARCH);
                }
            } else if (project.getStatus().equals(ProjectStatus.SALE)) {
                if (request.getStatus().equals("GRAD")) {
                    project.setStatus(ProjectStatus.GRAD);
                } else if (request.getStatus().equals("SALE")) {
                    project.setStatus(ProjectStatus.SALE);
                } else if (request.getStatus().equals("RESEARCH")) {
                    project.setStatus(ProjectStatus.RESEARCH);
                }
            } else if (project.getStatus().equals(ProjectStatus.GRAD)) {
                if (request.getStatus().equals("FINISH")) {
                    project.setStatus(ProjectStatus.FINISH);
                } else if (request.getStatus().equals("GRAD")) {
                    project.setStatus(ProjectStatus.GRAD);
                } else if (request.getStatus().equals("SALE")) {
                    project.setStatus(ProjectStatus.SALE);
                }
            } else if (project.getStatus().equals(ProjectStatus.FINISH)) {
                if (request.getStatus().equals("FINISH")) {
                    project.setStatus(ProjectStatus.FINISH);
                } else if (request.getStatus().equals("GRAD")) {
                    project.setStatus(ProjectStatus.GRAD);
                }
            }
        }

        if (request.getFirstPrice() > 0) {
            project.setFirstPrice(request.getFirstPrice());

            if (request.getSecondPrice() > 0) {
                project.setSecondPrice(request.getSecondPrice());

                if (request.getThirdPrice() > 0) {
                    project.setThirdPrice(request.getThirdPrice());
                }
            }
        }

        if (request.getQuantity() > 0) {
            project.setQuantity(request.getQuantity());
        }

        if (request.getExp().length() != 0) {
            project.setExp(request.getExp());
        }

        return project;
    }

    @Override
    public List<Project> surchOneList(Long no) {
        return projectRepository.findOneById(no);
    }

    // 검색
    public List<Project> surch(String type, String word) {
        if (type.equals("구매방식")) {
            return projectRepository.surchOrderStatus(word);
        } else if (type.equals("나라")) {
            return projectRepository.surchCountry(word);
        } else {
            throw new IllegalStateException("검색이 되지 않았습니다.");
        }
    }

}