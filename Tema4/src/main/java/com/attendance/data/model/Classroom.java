package com.attendance.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
public class Classroom {


    @Id
    private String id;
    private String name;
    private String author;
    private String description;
    private String code;
    //    user and his presences
    private Map<String, List<Attendence>> attendencesOnDay;
    private List<String> reports;

    public String getId() {
        return id;
    }

    public List<String> getReports() {
        return reports;
    }

    public void addReport(String uri) {
        reports.add(uri);
    }

    public String getLinkGenerateReport() {
        return "/report/" + id;
    }


    public Map<String, List<Attendence>> getAttendencesOnDay() {
        return attendencesOnDay;
    }

    public List<Attendence> getAttendencesOfPerson(String person) {
        List<Attendence> attendenceList = attendencesOnDay.get(person);
        if (attendenceList == null)
            return new ArrayList<>();
        return attendenceList;
    }

    public Classroom(String name, String author, String description, String code) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.code = code;
        this.attendencesOnDay = new HashMap<>();
        this.reports = new ArrayList<>();
    }

    public void addPresenceToPerson(String email) {
        var at = attendencesOnDay.get(email);
        if (at != null) {
            at.add(new Attendence(new Date().toString(), true));
            attendencesOnDay.put(email, at);
            return;
        }
        var list = new ArrayList<Attendence>();
        list.add(new Attendence(new Date().toString(), true));
        attendencesOnDay.put(email, list);
    }

    public String getLastReport(){
        if(reports==null)
            return "#";
        if(reports.size()==0)
            return "#";
        return reports.get(reports.size()-1);
    }

    public String getLink() {
        return "/classroom/open/" + id;
    }

    public String getPresenceLink() {
        return "/classroom/code/" + id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

