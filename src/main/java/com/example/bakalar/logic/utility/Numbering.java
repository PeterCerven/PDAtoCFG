package com.example.bakalar.logic.utility;

public class Numbering {

    public static String createNumberedString(char beg, char end, char current, String previous) {
        if (current == end) {
            current = beg;
        } else {
            current++;
        }

        if (previous == null || previous.isEmpty()) {
            return String.valueOf(beg);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = beg; i <= end; i++) {
            sb.append(i);
            sb.append(current);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
