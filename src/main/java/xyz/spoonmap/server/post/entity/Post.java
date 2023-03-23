package xyz.spoonmap.server.post.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Post {

    @Id
    private Long id;

}
