import javax.swing.SwingUtilities;

public class Calculator{
	
    /**
     * Main method to launch the calculator
     * @param args Command-line arguments (unused)
     */
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        Cal calc = new Cal();
        calc.setVisible(true);
    });
}
}