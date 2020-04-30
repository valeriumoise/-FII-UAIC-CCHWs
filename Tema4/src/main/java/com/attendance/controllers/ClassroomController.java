package com.attendance.controllers;

import com.attendance.data.model.Person;
import com.attendance.dto.CreateClassroom;
import com.attendance.dto.Join;
import com.attendance.dto.PersonPresence;
import com.attendance.services.CatalogService;
import com.attendance.services.PersonService;
import com.attendance.services.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/classroom")
public class ClassroomController {

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("classroom", new CreateClassroom());
        return "create";
    }

    @PostMapping("/create")
    public void created(HttpServletResponse httpServletResponse, Model model, OAuth2AuthenticationToken auth, @ModelAttribute CreateClassroom classroom) {
        var user = auth.getPrincipal();
        var email = ((DefaultOidcUser) user).getEmail();
        service.createClassRoom(classroom, email);
        model.addAttribute("classrooms", service.getAlClassroomCreatedByUser(email));
        httpServletResponse.setHeader("Location", "/");
        httpServletResponse.setStatus(302);
    }

    @PostMapping("/join")
    public void join(HttpServletResponse httpServletResponse, @ModelAttribute Join code, OAuth2AuthenticationToken auth, PersonPresence presence) {
        personService.joinToClassroom(auth.getPrincipal(), code.getCode());
        httpServletResponse.setHeader("Location", "/");
        httpServletResponse.setStatus(302);
    }

    @GetMapping("/open/{id}")
    public String openClassroom(@PathVariable("id") final String id, Model model, OAuth2AuthenticationToken auth) {
        model.addAttribute("presences", personService.getAllPresencesOf(auth.getPrincipal(), id));
        model.addAttribute("presence", new PersonPresence(id));
        return "classroom";
    }

    @GetMapping("/code/{id}")
    public String code(@PathVariable("id") final String id, Model model) {
        var code = presenceService.makePresence(id);
        model.addAttribute("code", code);
        return "code";
    }

    @Autowired
    private CatalogService service;
    @Autowired
    private PersonService personService;
    @Autowired
    private PresenceService presenceService;
}
