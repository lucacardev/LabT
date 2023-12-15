package GUI;
import UTILITIES.Controller;
import UTILITIES.DB_Connection;

public class Applicazione {


        public static void main (String[]args){

            Controller controller = new Controller();
            MainWindow mainWindow = new MainWindow(controller);


        }


}
