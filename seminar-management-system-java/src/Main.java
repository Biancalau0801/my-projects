import javax.swing.SwingUtilities;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        //import all data
        Data.init();
        Locale.setDefault(Locale.ENGLISH); //Language Setting as English
        //Run Login Page
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}