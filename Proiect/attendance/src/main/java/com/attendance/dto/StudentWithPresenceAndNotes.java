package com.attendance.dto;

import com.attendance.data.model.Classroom;
import com.attendance.data.model.Person;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class StudentWithPresenceAndNotes {
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> dates;
    private List<Double> notes;


    public StudentWithPresenceAndNotes(Person person, Classroom classroom) {
        email = person.getEmail();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        dates = classroom.getStudents().get(person.getId());
        notes = classroom.getNotes().get(person.getId());
    }
}
