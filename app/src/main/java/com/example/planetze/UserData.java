package com.example.planetze;

import java.util.ArrayList;

public class UserData {
    private static ArrayList<String[]> userArray;

    public UserData() {
        ArrayList<String[]> userArray = new ArrayList<String[]>();
    }

    public static void add(String[] user) {
        userArray.add(user);
    }

    public static void reset() {
        userArray = new ArrayList<String[]>();
    }

    public static boolean isUser(String[] user) {
        return userArray.contains(user);
    }

    public static int size() {
        if (userArray==null) {
            return 0;
        }
        return userArray.size();
    }


}
