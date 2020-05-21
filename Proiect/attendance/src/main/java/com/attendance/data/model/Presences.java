package com.attendance.data.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document
@Getter
@Setter
public class Presences {
    String code;
    String clssroomId;
    String date;
    @Field("_ts")
    @Indexed(name = "expire_after_seconds_index", expireAfterSeconds = 600)
    Date expired;

    public Presences(String code, String clssroomId, String date) {
        this.code = code;
        this.clssroomId = clssroomId;
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClssroomId() {
        return clssroomId;
    }


}

