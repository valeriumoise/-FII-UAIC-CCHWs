package com.attendance.data.model;


import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document
public class Presences {
    String code;
    String clssroomId;
    @Field("_ts")
    @Indexed(name = "expire_after_seconds_index", expireAfterSeconds = 600)
    Date expired;

    public Presences(String code, String clssroomId) {
        this.code = code;
        this.clssroomId = clssroomId;
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

    public void setClssroomId(String clssroomId) {
        this.clssroomId = clssroomId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}

