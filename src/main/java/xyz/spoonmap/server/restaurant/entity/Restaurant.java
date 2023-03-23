package xyz.spoonmap.server.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@Getter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "restaurant_no")
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 50)
    private String address;

    @Column(nullable = false)
    private Float x;

    @Column(nullable = false)
    private Float y;

    public Restaurant(String name, String address, Float x, Float y) {
        this.name = name;
        this.address = address;
        this.x = x;
        this.y = y;
    }
}
