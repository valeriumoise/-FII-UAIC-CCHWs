package com.attendance.controllers.teacher;

import com.attendance.CodeGenerator;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.ManualPresence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/teacher/actions")
public class ActionsTeacherController {
    @GetMapping("/classroom/{id}")
    public ModelAndView deleteClassroom(@PathVariable("id") final String id) {
        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            classroomRepository.delete(classroom.get());
            var list = personRepository.findByClassromsContaining(id);
            for (var i : list)
                i.getClassroms().remove(id);

            personRepository.saveAll(list);
        }
        var model = new ModelAndView("redirect:" + "/");
        model.setStatus(HttpStatus.NO_CONTENT);
        return model;
    }

    @GetMapping("/classroom/{id}/change/code")
    public ModelAndView changeCode(@PathVariable("id") final String id, OAuth2AuthenticationToken auth) {
        var classroom = classroomRepository.findById(id);
        classroom.ifPresent(value -> value.setCode(CodeGenerator.generateCode(((DefaultOidcUser) auth.getPrincipal()).getEmail(), value.getName())));
        classroom.ifPresent(value -> classroomRepository.save(value));
        return new ModelAndView("redirect:" + "/");
    }


    //    /teacher/actions/classroom/5ec25f21a692a4426f80ab18/presences
    @PostMapping("/classroom/{id}/presence")
    public ModelAndView addManualPresence(@ModelAttribute ManualPresence presence, @PathVariable("id") final String id, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            System.out.println(presence.toString());
            var student = personRepository.findByEmail(presence.email);
            if (student.isPresent()) {
                var classroom = classroomRepository.findById(id);
                if (classroom.isPresent()) {
                    var date = presence.date.split("-");
                    classroom.get().getStudents().get(student.get().getId()).add(date[2] + "." + date[1] + "." + date[0]);
                    classroomRepository.save(classroom.get());
                }
            }
        }
        return new ModelAndView("redirect:" + "/teacher/classroom/" + id);
    }


    @GetMapping("/classroom/{id}/student/delete/{idStudent}")
    public ModelAndView deleteStudentFromClassroom(@PathVariable("idStudent") final String idStudent, @PathVariable("id") final String id) {
        var student = personRepository.findById(idStudent);
        if (student.isPresent()) {
            student.get().getClassroms().remove(id);
            personRepository.save(student.get());
        }
        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            classroom.get().getStudents().remove(idStudent);
            classroom.get().getNotes().remove(idStudent);
            classroomRepository.save(classroom.get());
        }
        return new ModelAndView("redirect:" + "/teacher/classroom/" + id);
    }


    @Autowired
    public ClassroomRepository classroomRepository;

    @Autowired
    public PersonRepository personRepository;
}
