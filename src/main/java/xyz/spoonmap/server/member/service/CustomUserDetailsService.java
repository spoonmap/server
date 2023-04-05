package xyz.spoonmap.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.spoonmap.server.exception.member.MemberNotFoundException;
import xyz.spoonmap.server.member.repository.MemberRepository;
import xyz.spoonmap.server.authentication.CustomUserDetail;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                               .map(CustomUserDetail::new)
                               .orElseThrow(MemberNotFoundException::new);
    }
}
