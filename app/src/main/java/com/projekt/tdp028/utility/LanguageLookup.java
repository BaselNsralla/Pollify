package com.projekt.tdp028.utility;

import android.util.Log;
import android.util.SparseArray;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageLookup {
    static String lookup(String language) {
        return "";
    }


    public static List<String> parseForUI(String[] stringArray) {
        String[] array = new String[stringArray.length];
        List<String> outputList = new ArrayList<>();

        for (String entry : stringArray) {
            String[] splitResult = entry.split("\\|", 2);
            outputList.add(splitResult[1]);
        }
        return outputList;
    }

    public static String parseKey(String[] stringArray, int position) {
        String entry = stringArray[position];
        String[] splitResult = entry.split("\\|", 2);
        return splitResult[0];
    }
}
