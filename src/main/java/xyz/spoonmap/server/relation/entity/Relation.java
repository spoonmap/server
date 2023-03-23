package xyz.spoonmap.server.relation.entity;

import static xyz.spoonmap.server.relation.enums.RelationStatus.ACCEPTED;
import static xyz.spoonmap.server.relation.enums.RelationStatus.REJECTED;
import static xyz.spoonmap.server.relation.enums.RelationStatus.REQUESTED;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.relation.enums.RelationStatus;

@Table(name = "tables")
@Entity
@NoArgsConstructor
@Getter
public class Relation {

    @EmbeddedId
    private Pk id;

    @MapsId(value = "senderNo")
    @ManyToOne
    @JoinColumn(name = "sender_no")
    private Member sender;

    @MapsId(value = "receiverNo")
    @ManyToOne
    @JoinColumn(name = "receiver_no")
    private Member receiver;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "relation_status")
    @Enumerated(value = EnumType.STRING)
    private RelationStatus relationStatus;

    @Embeddable
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class Pk implements Serializable {

        @NotNull
        @Column(name = "sender_no")
        private Long senderNo;

        @NotNull
        @Column(name = "receiver_no")
        private Long receiverNo;

    }

    private Relation(Pk id, Member sender, Member receiver, RelationStatus relationStatus) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.relationStatus = relationStatus;
    }

    public static Relation of(Member sender, Member receiver) {
        return new Relation(new Pk(sender.getId(), receiver.getId()), sender, receiver, REQUESTED);
    }

    public void accept() {
        this.relationStatus = ACCEPTED;
    }

    public void reject() {
        this.relationStatus = REJECTED;
    }

}
