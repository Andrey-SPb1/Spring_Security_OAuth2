package org.javacode.spring_security_oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("id", principal.getAttribute("id"));
        model.addAttribute("username", principal.getAttribute("username"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("role", principal.getAttribute("role"));
        return "user";
    }

}
