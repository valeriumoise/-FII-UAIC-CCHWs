package com.attendance.controllers.student;

import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.PersonPresence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student/classroom")
public class ClassroomStudentController {
    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") final String id, OAuth2AuthenticationToken user) {
        ModelAndView model = new ModelAndView("classroom.student");
        var email = ((DefaultOidcUser) user.getPrincipal()).getEmail();
        var student = personRepository.findByEmail(email);
        Set<String> presences = new HashSet<>();
        List<Double> notes = new ArrayList<>();
        String name = "";
        String description = "";

        if (student.isPresent()) {
            var classroom = classroomRepository.findById(id);
            if (classroom.isPresent()) {
                presences = classroom.get().getStudents().get(student.get().getId());
                notes = classroom.get().getNotes().get(student.get().getId());
                name=classroom.get().getName();
                description=classroom.get().getDescription();
            }
        }
        model.addObject("presence", new PersonPresence(id));

        model.addObject("name",name);
        model.addObject("description",description);
        model.addObject("presences", String.join("; ", presences));
        model.addObject("notes", notes.stream().map(Object::toString).collect(Collectors.joining("; ")));
        return model;
    }

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

}
