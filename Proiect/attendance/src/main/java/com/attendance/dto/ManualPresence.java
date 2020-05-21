package com.attendance.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ToString
@Getter
@Setter
public class ManualPresence {
    @NotNull
    @Email
    public String email;
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    public String date;
}
