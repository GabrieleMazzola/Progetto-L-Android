package com.example.andrea.androiduser;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;
        import java.util.TimeZone;

public class DateOperations {

    private static DateOperations instance;

    private DateOperations(){
    }

    public static synchronized DateOperations getInstance() {
        if (instance == null) {
            instance = new DateOperations();
        }
        return instance;
    }

    public Date parse(String input) throws java.text.ParseException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.parse(input);
    }

    public String toString( Date date ) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ITALY);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}