package com.attendance.controllers;

import com.attendance.constants.Roles;
import com.attendance.data.model.Person;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.IPresenceRepository;
import com.attendance.data.repository.PersonRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

@Controller
//@Profile("dev")
@RequestMapping("/test/")
public class TesController {


    @GetMapping("/generate/students")
    public ModelAndView populateWithStudents() {
        Faker faker = new Faker();
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i <= 500; i++) {
            personList.add(new Person(faker.internet().emailAddress(), faker.name().firstName(), faker.name().lastName(), Roles.STUDENT));
        }
        personRepository.saveAll(personList);
        return new ModelAndView("redirect:" + "/");
    }

    @GetMapping("/generate/add/{id}")
    public ModelAndView addStudentsToClassroom(@PathVariable("id") final String id) {
        List<Person> personList = personRepository.findAll();
        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            for (var user : personList) {
                classroom.get().addStudentIfNotExists(user.getId());
                user.addClassroom(classroom.get().getId());
            }
            personRepository.saveAll(personList);
            classroomRepository.save(classroom.get());
        }
        return new ModelAndView("redirect:" + "/");
    }

    @GetMapping("/3")
    public ModelAndView delete() {
        List<Person> personList = personRepository.findAll();
        var person = personList.stream().filter(e -> e.getEmail().equals("filosgabriel@gmail.com")).findFirst();
        personList.remove(person.get());
        personRepository.deleteAll(personList);
        return new ModelAndView("redirect:" + "/");
    }


    @GetMapping("/generate/presences/{id}/{date}")
    public ModelAndView addPresences(@PathVariable("id") final String id, @PathVariable("date") String date) {
        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            for (var pair : classroom.get().getStudents().entrySet()) {
                if (Math.random() < 0.3)
                    pair.getValue().add(date);
            }
            classroomRepository.save(classroom.get());
        }
        return new ModelAndView("redirect:" + "/");
    }

    @GetMapping("/generate/notes/{id}")
    public ModelAndView addNotes(@PathVariable("id") final String id) {
        var classroom = classroomRepository.findById(id);
        classroom.get().getNotes().clear();
        var keys = classroom.get().getStudents().keySet();
        keys.forEach(key -> classroom.get().getNotes().put(key, new ArrayList<>()));
        for (var pair : classroom.get().getNotes().entrySet()) {
            if (Math.random() < 0.8) {
                pair.getValue().clear();
                pair.getValue().addAll(createNotes());
            } else {
                pair.getValue().clear();
                pair.getValue().addAll(Collections.nCopies(50, 0.0));
            }
        }
        classroomRepository.save(classroom.get());
        return new ModelAndView("redirect:" + "/");
    }

    @GetMapping("/generate/delete/all")
    public ModelAndView deleteAll() {
        personRepository.deleteAll();
        presenceRepository.deleteAll();
        classroomRepository.deleteAll();
        return new ModelAndView("redirect:" + "/");
    }


    private List<Double> createNotes() {
        return IntStream
                .range(0, 50)
                .mapToObj(el -> {
                    if (Math.random() < 0.4)
                        return Math.floor(Math.random() * 10 * 100) / 100;
                    return 0.0;
                }).collect(Collectors.toList());
    }


    @Autowired
    private IPresenceRepository presenceRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private PersonRepository personRepository;
}
