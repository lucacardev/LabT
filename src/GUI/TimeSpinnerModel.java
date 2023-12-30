package GUI;

import java.util.Calendar;
import java.util.Date;
import javax.swing.SpinnerDateModel;

public class TimeSpinnerModel extends SpinnerDateModel {
    public TimeSpinnerModel(Date value) {
        super(value, null, null, Calendar.HOUR_OF_DAY);
    }

}
