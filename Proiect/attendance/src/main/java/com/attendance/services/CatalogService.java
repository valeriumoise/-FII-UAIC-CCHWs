package com.attendance.services;

import com.attendance.CodeGenerator;
import com.attendance.data.model.Classroom;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.PersonRepository;
import com.attendance.dto.CreateClassroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {


    public List<Classroom> getAllClassroomThatPersonJoined(String email) {
        var person = personRepository.findByEmail(email);
        var classrooms = person.get().getClassroms();
        return classrooms.stream()
                .map(repository::findById)
                .map(e -> e.orElse(null)) //todo to solve if classroom is deleted
                .collect(Collectors.toList());
    }

    public String createClassRoom(CreateClassroom classroomInput, String person) {
        String code = CodeGenerator.generateCode(person, classroomInput.getName());
        Classroom classroom = new Classroom(classroomInput.getName(), person, classroomInput.getDescription(), code);
        repository.insert(classroom);
        return code;
    }

    public List<Classroom> getAlClassroomCreatedByUser(String user) {
        return repository.getClassroomByAuthor(user);
    }


    public CatalogService(@Autowired ClassroomRepository repository, @Autowired PersonRepository personRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
    }

    private final PersonRepository personRepository;
    private final ClassroomRepository repository;

}
