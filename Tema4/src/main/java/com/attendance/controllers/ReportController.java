package com.attendance.controllers;

import com.attendance.data.repository.ClassroomRepository;
import com.attendance.services.StorageService;
import com.attendance.services.WriteExcel;
import com.microsoft.azure.storage.StorageException;
import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

@Controller
@RequestMapping("/report")
public class ReportController {


    @GetMapping("/{id}")
    public void generate(@PathVariable("id") final String id, HttpServletResponse httpServletResponse) throws WriteException, IOException, URISyntaxException, StorageException {
        WriteExcel test = new WriteExcel();
        test.setOutputFile("c:/temp/lars.xls");
        var classroom = repository.findById(id).orElseThrow();
        test.write(classroom);
        var name = id + new Date().toString() + ".xls";
        var pathFile = service.uploadFile(test.getStream(), name);
        classroom.addReport(pathFile);
        repository.save(classroom);
        httpServletResponse.setHeader("Location", "/");
        httpServletResponse.setStatus(302);
    }

    @Autowired
    private StorageService service;
    @Autowired
    private ClassroomRepository repository;
}
