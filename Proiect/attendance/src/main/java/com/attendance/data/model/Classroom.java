package com.attendance.data.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
@Getter
@Setter
public class Classroom {


    @Id
    private String id;
    private String code;
    private String name;
    private String author;
    private String description;

    private String report;
    private Map<String, Set<String>> students;
    private Map<String, List<Double>> notes;

    //    user and his presences


    public void addReport(String uri) {
        report=uri;
    }


    public void addStudentIfNotExists(String student) {
        if (!students.containsKey(student)) {
            students.put(student, new HashSet<>());
            notes.put(student, new ArrayList<>(Collections.nCopies(50, 0.0)));
        }
    }

    public Set<String> getAttendencesOfPerson(String person) {
        Set<String> attendenceList = students.get(person);
        if (attendenceList == null)
            return new HashSet<>();
        return attendenceList;
    }

    public Classroom(String name, String author, String description, String code) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.code = code;
        this.students = new HashMap<>();
        this.report ="";
        this.notes = new HashMap<>();
    }

    public void addPresenceToPerson(String id, String date) {
        var at = students.get(id);
        if (at != null) {
            at.add(date);
            students.put(id, at);
            return;
        }
        var list = new HashSet<String>();
        list.add(date);
        students.put(id, list);
    }

    public String getLink() {
        return "/student/classroom/" + id;
    }

    public String getPresenceLink() {
        return "/classroom/code/" + id;
    }

    public String getLastReport() {
        return report;
    }
}

