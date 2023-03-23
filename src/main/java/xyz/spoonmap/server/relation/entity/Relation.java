package xyz.spoonmap.server.relation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.spoonmap.server.member.entity.Member;

@Table(name = "tables")
@Entity
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Pk id;

    @MapsId(value = "senderNo")
    @ManyToOne
    @JoinColumn(name = "member_no")
    @NotNull
    private Member sender;

    @MapsId(value = "receiverNo")
    @ManyToOne
    @JoinColumn(name = "member_no")
    @NotNull
    private Member receiver;

    private

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    @EqualsAndHashCode
    public static class Pk implements Serializable {

        @Column(name = "sender_no")
        private Long senderNo;

        @Column(name = "receiver_no")
        private Long receiverNo;

        public Pk(final Long memberId, final Long productId) {
            this.senderNo = memberId;
            this.receiverNo = productId;
        }

    }

}
