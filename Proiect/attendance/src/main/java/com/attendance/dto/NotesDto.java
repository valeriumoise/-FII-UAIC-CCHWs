package com.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NotesDto {
    public TempNote[] values;

    public NotesDto(Map<String, List<Double>> values) {
        this.values = new TempNote[values.size()];
        int i = 0;
        for (var pair : values.entrySet()) {
            var name = pair.getKey().split("__")[0];
            var id = pair.getKey().split("__")[1];
            this.values[i] = new TempNote(name, id, convert(pair.getValue()));
            i++;
        }

    }

    public NotesDto() {

    }

    private double[] convert(List<Double> data) {
        double[] temp = new double[data.size()];
        for (int i = 0; i < data.size(); i++) {
            temp[i] = data.get(i);
        }
        return temp;
    }
}
