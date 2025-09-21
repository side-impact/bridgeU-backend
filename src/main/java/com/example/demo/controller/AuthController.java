//package com.example.bridgeu.controller;
package com.example.demo.controller;

//import com.example.bridgeu.model.CustomOAuth2User;
import com.example.demo.model.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/welcome")
    public String welcome(@AuthenticationPrincipal CustomOAuth2User user) {
        return "Welcome, " + user.getName() + " (" + user.getEmail() + ")";
    }
}
