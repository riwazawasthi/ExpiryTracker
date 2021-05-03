package com.example.expirytracker;

public class dateFilter {

    String givenDate;
    String day, month, year;


//only works for formats mm/yy and mm/dd/yy for now

    public dateFilter(String date) {

        this.givenDate = date;
        int countSlash = 0;
        int indexFirstSlash = 0, indexSecondSlash = 0;


        for (int i = 0; i < givenDate.length(); i++) {
            if (givenDate.charAt(i) == '/') {
                countSlash++;
                if (countSlash == 1) {
                    indexFirstSlash = i;
                } else if (countSlash == 2) {
                    indexSecondSlash = i;
                }
            }
        }

        if (countSlash == 1) {

            day = "1";
            month = givenDate.substring(indexFirstSlash - 2, indexFirstSlash);
            year = "20" + givenDate.substring(indexFirstSlash + 1, indexFirstSlash + 3);

        } else if (countSlash == 2) {

            day = givenDate.substring(indexFirstSlash + 1, indexFirstSlash + 3);
            month = givenDate.substring(indexFirstSlash - 2, indexFirstSlash);
            year = "20" + givenDate.substring(indexSecondSlash + 1, indexSecondSlash + 3);

        }
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDay() {
        return day;
    }

}
