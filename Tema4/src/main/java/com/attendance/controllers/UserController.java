package com.attendance.controllers;

import com.attendance.dto.PersonPresence;
import com.attendance.services.PersonService;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {


    @PostMapping("/presence")
    public void presence(OAuth2AuthenticationToken auth, HttpServletResponse httpServletResponse, @ModelAttribute PersonPresence presence) {
        service.addPresence(auth.getPrincipal(), presence.getCode());
        httpServletResponse.setHeader("Location", "/classroom/open/" + presence.getId());
        httpServletResponse.setStatus(302);
    }

    @Autowired
    private PersonService service;
}
