package com.attendance.services;

import com.attendance.data.model.Attendence;
import com.attendance.data.model.Person;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.IPresenceRepository;
import com.attendance.data.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    public void addIfNotExistUser(OAuth2User user) {
        var email = ((DefaultOidcUser) user).getEmail();
        if (repository.findByEmail(email) == null)
            repository.insert(new Person(email));
    }

    public void joinToClassroom(OAuth2User userInput, String code) {
        var classroom = classroomRepository.findByCode(code);
        var user = repository.findByEmail(((DefaultOidcUser) userInput).getEmail());
        repository.delete(user);
        user.addClassroom(classroom.getId());
        repository.save(user);
    }

    public List<Attendence> getAllPresencesOf(OAuth2User userInput, String code) {
        var classroom = classroomRepository.findById(code);// todo solve if classroom was deleted
        return classroom.orElseThrow().getAttendencesOfPerson(((DefaultOidcUser) userInput).getEmail());
    }

    public void addPresence(OAuth2User userInput, String code) {
        var ac = presenceRepository.findByCode(code);
        if (ac != null) {
            var a = classroomRepository.findById(ac.getClssroomId()).orElseThrow();
            a.addPresenceToPerson(((DefaultOidcUser) userInput).getEmail());
            classroomRepository.save(a);
        }
    }

    @Autowired
    private IPresenceRepository presenceRepository;

    @Autowired
    private PersonRepository repository;
    @Autowired
    private ClassroomRepository classroomRepository;
}
