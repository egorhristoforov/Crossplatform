import javax.swing.*;
import java.awt.*;

public class Application {
    public static void main(String[] args) {
        JFrame frame = new JFrame("JsonViewer");
        frame.setMinimumSize(
                new Dimension(480, 480)
        );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JsonViewController rootViewController = new JsonViewController();
        rootViewController.startFrom(frame.getContentPane());

        frame.setVisible(true);
    }
}
