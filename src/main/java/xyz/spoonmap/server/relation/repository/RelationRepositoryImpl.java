package xyz.spoonmap.server.relation.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import xyz.spoonmap.server.member.entity.Member;
import xyz.spoonmap.server.member.entity.QMember;
import xyz.spoonmap.server.relation.entity.QRelation;
import xyz.spoonmap.server.relation.enums.RelationStatus;

@RequiredArgsConstructor
public class RelationRepositoryImpl implements RelationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Member> findFollowers(Long id) {
        QMember member = QMember.member;
        QRelation relation = QRelation.relation;

        return jpaQueryFactory
            .select(member)
            .from(member)
            .where(member.id.in(
                JPAExpressions.select(relation.sender.id)
                              .from(relation)
                              .where(relation.receiver.id.eq(id)
                                                       .and(relation.relationStatus.eq(RelationStatus.ACCEPTED))))
            )
            .fetch();
    }

    @Override
    public List<Member> findFollows(Long id) {
        QMember member = QMember.member;
        QRelation relation = QRelation.relation;

        return jpaQueryFactory
            .select(member)
            .from(member)
            .where(member.id.in(
                JPAExpressions.select(relation.receiver.id)
                              .from(relation)
                              .where(relation.sender.id.eq(id)
                                                         .and(relation.relationStatus.eq(RelationStatus.ACCEPTED))))
            )
            .fetch();
    }

    /**
     * 팔로우 신청 조회 (나 -> 다른 유저)
     */
    @Override
    public List<Member> findMyFollowRequest(Long id) {
        QMember member = QMember.member;
        QRelation relation = QRelation.relation;

        return jpaQueryFactory
            .select(member)
            .from(member)
            .where(member.id.in(
                JPAExpressions.select(relation.receiver.id)
                              .from(relation)
                              .where(relation.sender.id.eq(id)
                                                       .and(relation.relationStatus.eq(RelationStatus.REQUESTED))))
            )
            .fetch();
    }

    /**
     * 팔로워 신청 조회 (나에게 팔로우 신청 한 사람 조회)
     */
    @Override
    public List<Member> findMyFollowerRequest(Long id) {
        QMember member = QMember.member;
        QRelation relation = QRelation.relation;

        return jpaQueryFactory
            .select(member)
            .from(member)
            .where(member.id.in(
                JPAExpressions.select(relation.sender.id)
                              .from(relation)
                              .where(relation.receiver.id.eq(id)
                                                         .and(relation.relationStatus.eq(RelationStatus.REQUESTED))))
            )
            .fetch();
    }

}
