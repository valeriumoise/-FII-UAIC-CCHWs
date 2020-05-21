package com.attendance.controllers;

import com.attendance.data.model.Person;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.StudentWithPresenceAndNotes;
import com.attendance.services.StorageService;
import com.attendance.services.WriteExcel;
import com.microsoft.azure.storage.StorageException;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {


    @GetMapping("/{id}")
    public ModelAndView generate(@PathVariable("id") final String id) throws WriteException, IOException, URISyntaxException, StorageException {
        WriteExcel test = new WriteExcel();
        test.setOutputFile("c:/temp/lars.xls");
        var classroom = repository.findById(id).orElseThrow();
        List<StudentWithPresenceAndNotes> studentWithPresenceAndNotes = new ArrayList<>();
        classroom.getStudents().keySet()
                .forEach(idStudent -> {
                    var temp = personRepository.findById(idStudent);
                    temp.ifPresent(person -> studentWithPresenceAndNotes.add(new StudentWithPresenceAndNotes(person, classroom)));
                });
        test.write(studentWithPresenceAndNotes);
        var name = id + new Date().toString() + ".xls";
//        FileOutputStream out = new FileOutputStream("c:/temp/lars.xls");
//        out.write(test.getStream());
//        out.close();
        var pathFile = service.uploadFile(test.getStream(), name);
        classroom.addReport(pathFile);
        repository.save(classroom);
        return new ModelAndView("redirect:/teacher/classroom/" + id);
    }

    @Autowired
    private StorageService service;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ClassroomRepository repository;
}
