package com.attendance.dto;

public class CreateClassroom {
    private String name;
    private String description;

    public CreateClassroom() {
        name = "";
        description = "";
    }

    public CreateClassroom(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
