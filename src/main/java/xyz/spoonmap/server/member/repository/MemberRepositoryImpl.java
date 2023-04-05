package xyz.spoonmap.server.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import xyz.spoonmap.server.member.dto.response.MemberResponse;
import xyz.spoonmap.server.member.entity.QMember;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberResponse> findMembersByNickname(final String nickname) {
        QMember member = QMember.member;
        return queryFactory
            .select(Projections.constructor(
                MemberResponse.class,
                member.id, member.name, member.nickname, member.avatar
            ))
            .from(member)
            .where(member.nickname.like(nickname + "%"))
            .fetch();
    }

}
