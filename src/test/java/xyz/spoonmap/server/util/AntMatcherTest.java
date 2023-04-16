package xyz.spoonmap.server.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

public class AntMatcherTest {

    @Test
    void test() {
        assertThat(matches("/**/members/profile/**", "/v1/members/profile/image")).isTrue();
    }

    public static boolean matches(String pattern, String inputStr) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return antPathMatcher.match(pattern, inputStr);
    }

}
