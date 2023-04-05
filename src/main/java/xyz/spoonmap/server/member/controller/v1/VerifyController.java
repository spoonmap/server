package xyz.spoonmap.server.member.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.spoonmap.server.member.dto.response.SignupResponse;
import xyz.spoonmap.server.member.service.MemberService;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class VerifyController {

    private final MemberService memberService;

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam Long code) {
        SignupResponse response = memberService.verify(code);
        // TODO: 서비스 시작 페이지로 이동 (spoonmap.xyz)
        String redirectUrl = "google.com";
        return String.format("redirect:http://%s?verify=true&email=%s", redirectUrl, response.email());
    }

}
