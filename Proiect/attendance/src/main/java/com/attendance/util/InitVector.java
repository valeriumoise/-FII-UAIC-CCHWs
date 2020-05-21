package com.attendance.util;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InitVector {

    public static List<String> getHeader() {
        return IntStream.range(1, 51)
                .mapToObj(n -> "N" + n)
                .collect(Collectors.toList());
    }
}
