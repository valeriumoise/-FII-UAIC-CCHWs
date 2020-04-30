package com.attendance.data.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Person {

    @Id
    private String id;
    private String email;
    //    classroom ids
    private List<String> classroms;

    public Person(String email) {
        this.email = email;
        classroms = new ArrayList<>();
    }


    public void addClassroom(String code) {
        classroms.add(code);
    }

    public List<String> getClassroms() {
        return classroms;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
