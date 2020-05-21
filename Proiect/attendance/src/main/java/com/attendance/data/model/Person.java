package com.attendance.data.model;

import com.attendance.constants.Roles;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class Person {

    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private int role;
    //    classroom ids teacher-owned/student joined
    private Set<String> classroms;

    public Person(String email, String firstName, String lastName) {
        this.email = email;
        classroms = new HashSet<>();
        role = Roles.TEACHER;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Person(String email, String firstName, String lastName, int role) {
        this.email = email;
        classroms = new HashSet<>();
        this.role = role;
        this.lastName = lastName;
        this.firstName = firstName;

    }

    public boolean contains(String value) {
        return email.toLowerCase().contains(value) || lastName.toLowerCase().contains(value) || firstName.toLowerCase().contains(value);
    }

    public void addClassroom(String code) {
        classroms.add(code);
    }

}
