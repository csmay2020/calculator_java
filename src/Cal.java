import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Swing-based calculator application supporting basic arithmetic operations.
 * Features include:
 * Basic operations: addition, subtraction, multiplication, division
 * Error handling for division by zero
 * Sign inversion and decimal point support
 * Input validation and error state management
 * Clear entry (CE) and full clear (C) functionality
 * @author Shan Cai
 * @version 1.0
 * @since April,2025
 */
public class Cal extends JFrame implements ActionListener {
    
    /** Main display text field for input and results */
    private JTextField textField;
    
    /** First operand for calculations */
    private double num1 = 0;
    
    /** Second operand for calculations */
    private double num2 = 0;
    
    /** Current arithmetic operator (+, -, *, /) */
    private String operator = "";
    
    /** Flag indicating start of new number input */
    private boolean startNewNumber = true;
    
    /** Flag indicating error state (e.g., division by zero) */
    private boolean inErrorState = false;

    /**
     * Constructs the calculator GUI with the following components:
     * 400x500 pixel window with title "Calculator"
     * Text field with 36px Arial font (right-aligned)
     * 5x4 grid of buttons with 32px Arial font
     * Button labels: CE, C, DEL, /, 7-9, *, 4-6, -, 1-3, +, +/-, 0, ., =
     */
    public Cal() {
        // Window configuration
        setTitle("Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize text field
        JPanel textPanel = new JPanel();
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(380, 60));
        textField.setFont(new Font("Arial", Font.PLAIN, 36));
        textField.setText("0");
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textPanel.add(textField);

        // Create button grid
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4));
        String[] buttons = {
            "CE", "C", "DEL", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };
        
        Font buttonFont = new Font("Arial", Font.PLAIN, 32);
        for (String btnText : buttons) {
            JButton btn = new JButton(btnText);
            btn.addActionListener(this);
            btn.setFont(buttonFont);
            buttonPanel.add(btn);
        }

        // Assemble main interface
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
    }

    /**
     * Handles all button click events with state management and input validation.
     * Behavior includes:
     *Digit input building
     *Operator handling with operand storage
     *Error state management
     *Input validation for decimal points
     * @param e The {@link ActionEvent} triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Block input in error state except for clear operations
        if (inErrorState) {
            handleErrorStateOperations(command);
            return;
        }

        // Handle numeric input
        if (Character.isDigit(command.charAt(0))) {
            handleDigitInput(command);
        } else if (command.equals(".")) {
            handleDecimalPoint();
        }
        // Handle control buttons
        else switch (command) {
            case "C":
                resetCalculator();
                break;
            case "CE":
                clearEntry();
                break;
            case "DEL":
                deleteLastChar();
                break;
            case "+/-":
                invertSign();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                storeOperator(command);
                break;
            case "=":
                performCalculation();
                break;
        }
    }

    /**
     * Processes digit button input (0-9)
     * @param digit The pressed digit as String
     */
    private void handleDigitInput(String digit) {
        if (startNewNumber || textField.getText().equals("0")) {
            textField.setText(digit);
            startNewNumber = false;
        } else {
            textField.setText(textField.getText() + digit);
        }
    }

    /**
     * Handles decimal point input with validation
     * @throws NumberFormatException if invalid decimal format
     */
    private void handleDecimalPoint() {
        if (!textField.getText().contains(".")) {
            textField.setText(textField.getText() + ".");
            startNewNumber = false;
        }
    }

    /**
     * Full calculator reset:
     * Clears display
     * Resets all operands and operator
     * Exits error state
     */
    private void resetCalculator() {
        textField.setText("0");
        num1 = 0;
        num2 = 0;
        operator = "";
        startNewNumber = true;
        inErrorState = false;
    }

    /**
     * Clears current display entry while maintaining calculation state
     */
    private void clearEntry() {
        textField.setText("0");
        startNewNumber = true;
    }

    /**
     * Deletes last entered character with validation
     */
    private void deleteLastChar() {
        String current = textField.getText();
        if (current.length() > 1) {
            textField.setText(current.substring(0, current.length() - 1));
        } else {
            textField.setText("0");
            startNewNumber = true;
        }
    }

    /**
     * Inverts sign of displayed number
     * @throws NumberFormatException if display contains invalid number
     */
    private void invertSign() {
        double value = Double.parseDouble(textField.getText());
        textField.setText(String.valueOf(-value));
    }

    /**
     * Stores operator and first operand for calculation
     * @param op The arithmetic operator (+, -, *, /)
     */
    private void storeOperator(String op) {
        num1 = Double.parseDouble(textField.getText());
        operator = op;
        startNewNumber = true;
    }

    /**
     * Performs calculation with stored values and operator
     * Handles special cases:
     * Division by zero
     * Integer result formatting
     * @throws ArithmeticException on division by zero attempt
     */
    private void performCalculation() {
        if (operator.isEmpty()) return;
        
        num2 = Double.parseDouble(textField.getText());
        double result = 0;
        
        try {
            switch (operator) {
                case "+": 
                    result = num1 + num2; 
                    break;
                case "-": 
                    result = num1 - num2; 
                    break;
                case "*": 
                    result = num1 * num2; 
                    break;
                case "/": 
                    validateDivision();
                    result = num1 / num2; 
                    break;
            }
            
            formatAndDisplayResult(result);
            prepareForNextOperation(result);
            
        } catch (ArithmeticException ex) {
            handleCalculationError();
        }
    }

    /**
     * Validates division operation
     * @throws ArithmeticException if divisor is zero
     */
    private void validateDivision() {
        if (num2 == 0) {
            throw new ArithmeticException("Division by zero");
        }
    }

    /**
     * Formats numerical result for display
     * @param result The calculation result
     */
    private void formatAndDisplayResult(double result) {
        textField.setText(result % 1 == 0 ? 
            String.valueOf((int) result) : 
            String.valueOf(result));
    }

    /**
     * Prepares calculator state for subsequent operations
     * @param result Current calculation result
     */
    private void prepareForNextOperation(double result) {
        num1 = result;
        operator = "";
        startNewNumber = true;
    }

    /**
     * Handles calculation errors and enters error state
     */
    private void handleCalculationError() {
        textField.setText("Cannot divide by zero");
        inErrorState = true;
    }

    /**
     * Manages operations allowed in error state
     * @param command User input command
     */
    private void handleErrorStateOperations(String command) {
        if (command.equals("C") || command.equals("CE")) {
            resetCalculator();
        }
    }
}