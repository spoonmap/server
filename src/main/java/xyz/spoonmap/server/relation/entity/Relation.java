package xyz.spoonmap.server.relation.entity;

import static lombok.AccessLevel.PROTECTED;
import static xyz.spoonmap.server.relation.enums.RelationStatus.ACCEPTED;
import static xyz.spoonmap.server.relation.enums.RelationStatus.REJECTED;
import static xyz.spoonmap.server.relation.enums.RelationStatus.REQUESTED;

import java.io.Serializable;
import java.util.Objects;
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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.relation.enums.RelationStatus;

@Table(name = "relations")
@Entity
@NoArgsConstructor(access = PROTECTED)
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
    @Column(name = "relation_status")
    @Enumerated(value = EnumType.STRING)
    private RelationStatus relationStatus;

    @Embeddable
    @NoArgsConstructor(access = PROTECTED)
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

    public Relation(Member sender, Member receiver) {
        if (Objects.equals(sender.getId(), receiver.getId())) {
            throw new IllegalArgumentException();
        }
        this.id = new Pk(sender.getId(), receiver.getId());
        this.sender = sender;
        this.receiver = receiver;
        this.relationStatus = REQUESTED;
    }

    public void accept() {
        this.relationStatus = ACCEPTED;
    }

    public void reject() {
        this.relationStatus = REJECTED;
    }

}
