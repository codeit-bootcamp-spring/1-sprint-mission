package com.sprint.mission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> ss = Arrays.asList("a", "b", "c");
        ss.remove("d");
        System.out.println(ss.toString());

    }
}