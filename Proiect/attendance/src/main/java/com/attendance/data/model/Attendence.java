package com.attendance.data.model;

public class Attendence {
    private String date;
    private boolean present;

    public Attendence(String date, boolean present) {
        this.date = date;
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }


    public String asP() {
        return present ? "p" : " ";
    }
}
