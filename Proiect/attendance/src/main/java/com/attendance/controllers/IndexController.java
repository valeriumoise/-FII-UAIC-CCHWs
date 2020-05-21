package com.attendance.controllers;

import com.attendance.dto.CreateClassroom;
import com.attendance.dto.Join;
import com.attendance.services.CatalogService;
import com.attendance.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(Model model, OAuth2AuthenticationToken auth) {
        var user = auth.getPrincipal();
        var email = ((DefaultOidcUser) user).getEmail();
        personService.addIfNotExistUser(auth.getPrincipal());
        var list = service.getAlClassroomCreatedByUser(email);
        if (personService.isTeacher((DefaultOidcUser) user)) {
            model.addAttribute("classrooms", list);
            model.addAttribute("classroom", new CreateClassroom());

            return "index.teacher";
        } else {
            model.addAttribute("join", new Join());
            model.addAttribute("classroomsJoined", service.getAllClassroomThatPersonJoined(email));
            return "index.student";
        }

    }

    @Autowired
    private CatalogService service;
    @Autowired
    private PersonService personService;
}
