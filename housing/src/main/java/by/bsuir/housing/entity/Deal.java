package by.bsuir.housing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "deal")
@RequiredArgsConstructor
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estate_id")
//    @ToString.Exclude
    private Estate estate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
//    @ToString.Exclude
    private User user;

    @Column(name = "price")
    private Integer price;

    @Column(name = "days")
    private Integer days;

    @Column(name = "people_count")
    private Integer peopleCount;

    @Column(name = "arriving")
    private LocalDate arriving;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Deal deal = (Deal) o;
        return getId() != null && Objects.equals(getId(), deal.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
