package de.hochrad.backend.utils;

import org.joda.time.DateTime;
import org.threeten.extra.YearWeek;

public class UrlUtils {

    public static String getClassURL(int weekOfYear, int classID) {
        return ParseUtils.BASE_URL
                + toNDigits(weekOfYear, 2)
                + "/w/w"
                + toNDigits(classID, 5)
                + ".htm";
    }

    public static int getWeekOfYear(int offset) {
        int tempWeekOfYear;

        if (DateTime.now().dayOfWeek().get() > 5) {
            //Its the weekend end I want to see info for next week
            tempWeekOfYear = DateTime.now().weekOfWeekyear().get() + offset + 1;
        } else {
            tempWeekOfYear =  DateTime.now().weekOfWeekyear().get() + offset;
        }

        if (tempWeekOfYear > YearWeek.now().lengthOfYear()) {
            tempWeekOfYear = tempWeekOfYear -  YearWeek.now().lengthOfYear();
        }
        return tempWeekOfYear;
    }

    private static String toNDigits(int i, int count) {
        return String.format("%0" + count + "d", i);
    }
}
