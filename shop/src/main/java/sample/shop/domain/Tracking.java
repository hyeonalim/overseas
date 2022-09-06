package sample.shop.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tracking {

    // id
    @Id
    @GeneratedValue
    @Column(name = "tracking_no")
    private Long no;

    // kids
    @Enumerated(EnumType.STRING)
    private TrackingStatus status;

    private LocalDateTime date;
    private String local;
    private String memo;

    @OneToOne(mappedBy = "tracking", fetch = FetchType.LAZY)
    private Order order;

}