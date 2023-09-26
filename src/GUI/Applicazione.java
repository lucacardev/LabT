package GUI;
import UTILITIES.Controller;
public class Applicazione {

    public static void main(String[] args) {

        Controller controller = new Controller();
        MainWindow mainWindow = new MainWindow(controller);

    }

}
