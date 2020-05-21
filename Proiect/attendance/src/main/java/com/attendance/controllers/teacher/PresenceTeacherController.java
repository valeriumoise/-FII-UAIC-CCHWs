package com.attendance.controllers.teacher;

import com.attendance.data.model.Attendence;
import com.attendance.data.model.Classroom;
import com.attendance.data.model.Person;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.util.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/classroom/")
public class PresenceTeacherController {
    @GetMapping("/{id}/presences")
    public ModelAndView get(@PathVariable("id") final String id, @RequestParam(value = "page", defaultValue = "1") final int page, @RequestParam(value = "search", defaultValue = "") final String search) {
        var classroom = classroomRepository.findById(id);
        if (classroom.isPresent()) {
            var model = new ModelAndView("teacher.presences");
            var dates = getAllDates(classroom.get()).stream().sorted().collect(Collectors.toList());
            var tempStudentsPresences = getStudentPresences(dates, classroom.get());

            LinkedHashMap<String, List<Attendence>> studentsPresences = new LinkedHashMap<>();
            if (!search.isEmpty())
                tempStudentsPresences
                        .entrySet().stream()
                        .filter(el -> el.getKey().toLowerCase().contains(search.toLowerCase()))
                        .forEach(el -> studentsPresences.put(el.getKey(), el.getValue()));
            else
                studentsPresences.putAll(tempStudentsPresences);


            var list = studentsPresences.entrySet().stream().skip((page - 1) * 50)
                    .limit(50).collect(Collectors.toList());
            LinkedHashMap<String, List<Attendence>> temp = new LinkedHashMap<>();
            list.forEach(e -> temp.put(e.getKey(), e.getValue()));
            model.addObject("students", temp);
            model.addObject("id", id);
            model.addObject("currentPage", page);
            model.addObject("totalPages", Pages.getTotalPages(studentsPresences.size()));
//            model.addObject("prevPage", page > 1 ? page - 1 : 1);
//            model.addObject("nextPage", page < (int) Math.ceil((double) studentsPresences.size() / 50) ? page + 1 : page);
            model.addObject("nextPageLink", Pages.getNextPageLink(search, page, Pages.getTotalPages(studentsPresences.size())));
            model.addObject("prevPageLink", Pages.getPrevPageLink(search, page, Pages.getTotalPages(studentsPresences.size())));
            model.addObject("dates", dates);
            return model;
        }
        return new ModelAndView("redirect:" + "/");
    }

    private int getPrev(int page) {
        return page > 1 ? page - 1 : 1;
//        int totalPage = (int) Math.ceil((double) count / 20);

    }

    @Autowired
    private ClassroomRepository classroomRepository;

    @Autowired
    private PersonRepository personRepository;


    private List<String> getAllDates(Classroom classroom) {
        Set<String> dates = new HashSet<>();
        for (Set<String> datesOfStudent : classroom.getStudents().values())
            dates.addAll(datesOfStudent);
        return new ArrayList<>(dates);
    }

    private LinkedHashMap<String, List<Attendence>> getStudentPresences(List<String> dates, Classroom classroom) {
        Map<String, List<Attendence>> presences = new HashMap<>();
        List<String> ids = new ArrayList<>();
        for (var student : classroom.getStudents().entrySet()) {
            var listDates = new ArrayList<Attendence>();
            dates.forEach(e -> listDates.add(new Attendence(e, false)));
            for (var date : student.getValue()) {
                for (var item : listDates)
                    if (item.getDate().equals(date))
                        item.setPresent(true);
            }
            ids.add(student.getKey());
            presences.put(student.getKey(), listDates);
        }
        Map<String, List<Attendence>> finalPresences = new HashMap<>();

        personRepository.findByIdIn(ids).forEach(student -> finalPresences.put(student.getFirstName() + " " + student.getLastName(), presences.get(student.getId())));
//        var studentEntity = personRepository.findById(student.getKey());
//        studentEntity.ifPresent(person -> presences.put(person.getFirstName() + " " + person.getLastName(), listDates));

        LinkedHashMap<String, List<Attendence>> sortedMap = new LinkedHashMap<>();

        finalPresences.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }

    private List<Person> getAllPersonOfClass(Classroom classroom) {
        List<Person> personList = new ArrayList<>();
        for (String idStudent : classroom.getStudents().keySet()) {
            var student = personRepository.findById(idStudent);
            student.ifPresent(personList::add);
        }
        return personList;
    }
}
