package com.attendance;

import java.util.Date;

public class CodeGenerator {
    public static String generateCode(String person, String classroomName) {
        return String.valueOf((person + classroomName + new Date().toString()).hashCode());
    }
}
