package com.attendance.controllers.teacher;

import com.attendance.data.model.Classroom;
import com.attendance.data.model.Person;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.ManualPresence;
import com.attendance.util.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/classroom")
public class ClassroomTeacherController {
    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") final String id, @RequestParam(value = "page", defaultValue = "1") final int page, @RequestParam(value = "search", defaultValue = "") final String search) {
        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            var persons = getAllPersonOfClass(classroom.get());
            var model = new ModelAndView("classroom.teacher");
            model.addObject("classroom", classroom.get());
            model.addObject("noStudents", persons.size());

//
            long totalPages = Pages.getTotalPages(persons.size());
//            model.addObject("students", persons.stream().skip(Pages.getNextPage(page))
//                    .limit(SIZE_PAGE).collect(Collectors.toList()));
//
            if (!search.isEmpty())
                totalPages = persons.stream()
                        .filter(e -> e.contains(search))
                        .count();

            model.addObject("students", Pages.getPage(persons, page, search.toLowerCase()));

            model.addObject("currentPage", page);
            model.addObject("totalPages", totalPages);
            model.addObject("nextPageLink", Pages.getNextPageLink(search, page, totalPages));
            model.addObject("prevPageLink", Pages.getPrevPageLink(search, page, totalPages));
            model.addObject("newPresence", new ManualPresence());
            return model;

        }
        return new ModelAndView("redirect:" + "/");
    }

    public ClassroomTeacherController(@Autowired ClassroomRepository repository, @Autowired PersonRepository personRepository) {
        this.classroomRepository = repository;
        this.personRepository = personRepository;
    }

    private final ClassroomRepository classroomRepository;
    private final PersonRepository personRepository;

    private List<Person> getAllPersonOfClass(Classroom classroom) {
        List<Person> personList = new ArrayList<>();
        return personRepository.findByIdIn(new ArrayList<>(classroom.getStudents().keySet()));
//        for (String idStudent : classroom.getStudents().keySet()) {
//            var student = personRepository.findById(idStudent);
//            student.ifPresent(personList::add);
//        }
//        return personList;
    }


    private final static int SIZE_PAGE = 50;
}
