package CaC.Grupo2.FlySky.helperDate;

import java.util.Date;

import java.text.SimpleDateFormat;

public class DateFormatHelper {
    public String fechaStirng(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("Y-M-d");
        return formatter.format(date);
    }
}
