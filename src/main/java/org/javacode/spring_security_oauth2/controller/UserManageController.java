package org.javacode.spring_security_oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.javacode.spring_security_oauth2.model.entity.User;
import org.javacode.spring_security_oauth2.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserManageController {

    private final UserService userService;

    @GetMapping("/all")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/users";
    }
}
