package GUI;

import java.util.Calendar;
import java.util.Date;
import javax.swing.SpinnerDateModel;

public class TimeSpinnerModel extends SpinnerDateModel {

    public TimeSpinnerModel() {
        super(new Date(0), null, null, Calendar.MINUTE);
    }
}
