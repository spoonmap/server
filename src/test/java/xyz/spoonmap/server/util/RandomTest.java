package xyz.spoonmap.server.util;

import org.junit.jupiter.api.Test;
import xyz.spoonmap.server.member.entity.Member;

public class RandomTest {

    @Test
    void test() {
        Member withId = RandomEntityGenerator.createWithId(Member.class);
        Member withoutId = RandomEntityGenerator.create(Member.class);
        System.out.println(withId);
        System.out.println(withoutId);
    }

}
