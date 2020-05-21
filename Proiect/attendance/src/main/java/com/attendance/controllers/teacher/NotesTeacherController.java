package com.attendance.controllers.teacher;

import com.attendance.data.model.Attendence;
import com.attendance.data.model.Classroom;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.NotesDto;
import com.attendance.util.InitVector;
import com.attendance.util.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@RequestMapping("/teacher/classroom")
public class NotesTeacherController {

    @GetMapping("/{id}/notes")
    public ModelAndView get(@PathVariable("id") final String id, @RequestParam(value = "page", defaultValue = "1") final int page, @RequestParam(value = "search", defaultValue = "") final String search) {
        var classroom = classroomRepository.findById(id);
        ModelAndView model = new ModelAndView("teacher.notes");
        TreeMap<String, List<Double>> notes = new TreeMap<>();
        String name = "";
        if (classroom.isPresent()) {
            notes = getStudents(classroom.get());
            name = classroom.get().getName();
        }

        model.addObject("name", name);
        model.addObject("id", id);
        model.addObject("currentPage", page);
        model.addObject("header", InitVector.getHeader());
        var temp = Pages.getPageMap(notes, page, search.toLowerCase());
        model.addObject("notes", new NotesDto(temp));
        model.addObject("totalPages", Pages.getTotalPages(temp.size()));
        model.addObject("nextLink", Pages.getNextPageLink(search, page, Pages.getTotalPages(temp.size())));
        model.addObject("prevLink", Pages.getPrevPageLink(search, page, Pages.getTotalPages(temp.size())));
        return model;
    }

    @PostMapping("/{id}/notes")
    public ModelAndView update(@ModelAttribute NotesDto newNotes, @PathVariable("id") final String id) {

        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            for (int i = 0; i < newNotes.values.length; i++) {
                classroom.get()
                        .getNotes()
                        .put(newNotes.values[i].id,
                                convertToList(newNotes.values[i].notes));
            }
            classroomRepository.save(classroom.get());
        }
        return new ModelAndView("redirect:/teacher/classroom/" + id + "/notes");
    }

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private PersonRepository personRepository;

    private TreeMap<String, List<Double>> getStudents(Classroom classroom) {
        TreeMap<String, List<Double>> notes = new TreeMap<>();
        for (var student : classroom.getNotes().entrySet()) {
            var studentEntity = personRepository.findById(student.getKey());
            studentEntity.ifPresent(person -> notes.put(person.getFirstName() + " " + person.getLastName() + "__" + student.getKey(), student.getValue()));
        }
        return notes;
    }

    public List<Double> convertToList(double[] t) {
        var temp = new ArrayList<Double>();
        for (int i = 0; i < t.length; i++) {
            temp.add(t[i]);
        }
        return temp;
    }
}
