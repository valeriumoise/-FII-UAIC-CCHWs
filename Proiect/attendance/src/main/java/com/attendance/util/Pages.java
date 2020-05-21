package com.attendance.util;

import com.attendance.data.model.Person;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Pages {

    public static String getNextPageLink(String search, int currentPage, long maxPages) {

        if (currentPage < maxPages) {
            search = search.isEmpty() ? "" : "&search=" + search;
            return "?page=" + (currentPage + 1) + search;
        } else
            return "";
    }

    public static String getPrevPageLink(String search, int currentPage, long maxPages) {

        if (currentPage > 1) {
            search = search.isEmpty() ? "" : "&search=" + search;
            return "?page=" + (currentPage - 1) + search;
        } else
            return "";
    }

    public static List<Person> getPage(List<Person> list, int page, String search) {
        if (search.isEmpty())
            return list.stream()
                    .skip(Pages.getNextPage(page))
                    .limit(SIZE_PAGE)
                    .collect(Collectors.toList());
        return list.stream()
                .filter(e -> e.contains(search))
                .skip(Pages.getNextPage(page))
                .limit(SIZE_PAGE)
                .collect(Collectors.toList());
    }


    public static TreeMap<String, List<Double>> getPageMap(TreeMap<String, List<Double>> list, int page, String search) {
        TreeMap<String, List<Double>> temp = new TreeMap<>();
        if (search.isEmpty()) {
            var keys = list.keySet()
                    .stream()
                    .skip(Pages.getNextPage(page))
                    .limit(SIZE_PAGE)
                    .collect(Collectors.toSet());

            for (var key : keys)
                temp.put(key, list.get(key));
            return temp;
        }
        var keys = list.keySet()
                .stream()
                .filter(key -> key.toLowerCase().contains(search))
                .skip(Pages.getNextPage(page))
                .limit(SIZE_PAGE)
                .collect(Collectors.toSet());

        for (var key : keys)
            temp.put(key, list.get(key));
        return temp;
    }


    public static int getNextPage(int page) {
        return (page - 1) * SIZE_PAGE;
    }

    public static int getTotalPages(int size) {
        return (int) Math.ceil((double) size / SIZE_PAGE);
    }

    public final static int SIZE_PAGE = 50;
}
