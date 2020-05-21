package com.attendance.services;

import com.attendance.constants.Roles;
import com.attendance.data.model.Person;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.IPresenceRepository;
import com.attendance.data.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PersonService {

    public void addIfNotExistUser(OAuth2User user) {
        var email = ((DefaultOidcUser) user).getEmail();
        var firstName = ((DefaultOidcUser) user).getGivenName();
        var lastName = ((DefaultOidcUser) user).getFamilyName();
        if (personRepository.findByEmail(email).isEmpty())
            personRepository.insert(new Person(email, firstName, lastName));
    }

    public void joinToClassroom(OAuth2User userInput, String code) {
        var classroom = classroomRepository.findByCode(code);
        var user = personRepository.findByEmail(((DefaultOidcUser) userInput).getEmail());
        personRepository.delete(user.get());
        classroom.ifPresent(value -> user.get().addClassroom(value.getId()));
        personRepository.save(user.get());
    }

    public Set<String> getAllPresencesOf(OAuth2User userInput, String code) {
        var classroom = classroomRepository.findById(code);// todo solve if classroom was deleted
        return classroom.orElseThrow().getAttendencesOfPerson(((DefaultOidcUser) userInput).getEmail());
    }

    public void addPresence(OAuth2User userInput, String code) {
        var ac = presenceRepository.findByCode(code);
        if (ac != null) {
            var person=personRepository.findByEmail(((DefaultOidcUser) userInput).getEmail());

            if(person.isPresent()) {
                var a = classroomRepository.findById(ac.getClssroomId()).orElseThrow();
                a.addPresenceToPerson(person.get().getId(), ac.getDate());
                classroomRepository.save(a);
            }
        }
    }

    public boolean isTeacher(DefaultOidcUser userInput) {
        var person = personRepository.findByEmail(userInput.getEmail());
        return person.get().getRole() == Roles.TEACHER;
    }

    @Autowired
    private IPresenceRepository presenceRepository;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
}
