package xyz.spoonmap.server.authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.spoonmap.server.member.entity.Member;

@RequiredArgsConstructor
public class CustomUserDetail implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Objects.isNull(member.getDeletedAt());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Objects.isNull(member.getDeletedAt());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.isNull(member.getDeletedAt());
    }

}
