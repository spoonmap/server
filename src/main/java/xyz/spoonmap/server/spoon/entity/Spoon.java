package xyz.spoonmap.server.spoon.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "spoons")
@Entity
@NoArgsConstructor
@Getter
public class Spoon {



    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class Pk implements Serializable {

        @Column(name = "sender_no")
        private Long senderNo;

        @Column(name = "receiver_no")
        private Long receiverNo;

    }

}
