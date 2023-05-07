package by.bsuir.housing.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "estate")
@RequiredArgsConstructor
public class Estate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "city")
    private String city;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "flat_number")
    private Integer flatNumber;

    @Column(name = "square")
    private Integer square;

    @Column(name = "type")
    private Integer type;

    @Column(name = "price")
    private Integer price;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estate", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @JsonIgnore
    private List<Deal> deals;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estate")
    @ToString.Exclude
    @JsonIgnore
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "favourites",
            joinColumns = @JoinColumn(name = "estate_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    @JsonIgnore
    private Set<User> favourites;

    public boolean addToFavourites(User user) {
        return this.favourites.add(user);
    }

    public boolean removeFromFavourites(User user) {
        return this.favourites.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Estate estate = (Estate) o;
        return getId() != null && Objects.equals(getId(), estate.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
