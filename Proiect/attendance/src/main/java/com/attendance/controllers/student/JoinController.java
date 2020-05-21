package com.attendance.controllers.student;

import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.Join;
import com.attendance.dto.PersonPresence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/student/classroom")
public class JoinController {
    @PostMapping("/join")
    public void join(HttpServletResponse httpServletResponse, @ModelAttribute Join code, OAuth2AuthenticationToken auth, PersonPresence presence) {
        var classroom = classroomRepository.findByCode(code.getCode());
        if (classroom.isPresent()) {
            var user = personRepository.findByEmail(((DefaultOidcUser) auth.getPrincipal()).getEmail());
            personRepository.delete(user.get());
            classroom.get().addStudentIfNotExists(user.get().getId());
            user.get().addClassroom(classroom.get().getId());

            classroomRepository.save(classroom.get());
            personRepository.save(user.get());
        }

        httpServletResponse.setHeader("Location", "/");
        httpServletResponse.setStatus(302);
    }

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
}
