package com.attendance.services;

import com.attendance.CodeGenerator;
import com.attendance.data.model.Presences;
import com.attendance.data.repository.ClassroomRepository;
import com.attendance.data.repository.IPresenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PresenceService {
    public String makePresence(String id,String date) {
        var classroom = classroomRepository.findById(id);
        var code = CodeGenerator.generateCode(id, id);
        presenceRepository.save(new Presences(code, id,date));
        return code;
    }

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private IPresenceRepository presenceRepository;
}
