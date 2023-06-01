package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class GUIMainpanel extends JFrame {
    private ArrayList<Integer> clickedSquares = new ArrayList<>();
    private JTextArea textArea;

    public GUIMainpanel() {
        setTitle("Drie-panel GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Bovenste Menubalk
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JButton redButton = createColorButton(Color.RED);
        JButton yellowButton = createColorButton(Color.YELLOW);
        JButton greenButton = createColorButton(Color.GREEN);

        topPanel.add(redButton);
        topPanel.add(yellowButton);
        topPanel.add(greenButton);

        JButton button1 = createButton("TSP Test");
        JButton button2 = createButton("Orders");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current GUIMainpanel screen

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new GUIOrder();
                    }
                });
            }
        });

        topPanel.add(button1);
        topPanel.add(button2);

        add(topPanel, BorderLayout.NORTH);

        // Linkerpanel voor tekst
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(220, 220, 220));
        leftPanel.setPreferredSize(new Dimension(400, getHeight()));
        leftPanel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        textArea.setBackground(new Color(220, 220, 220));

        leftPanel.add(textArea, BorderLayout.WEST);

        add(leftPanel, BorderLayout.WEST);

        // Rechterpanel met de grid
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(220, 220, 220));
        rightPanel.setLayout(new GridLayout(5, 5));

        ActionListener squareClickListener = new ActionListener() {
            private int clickCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton square = (JButton) e.getSource();
                if (clickCount < 3) {
                    square.setBackground(Color.GRAY);
                    square.setPreferredSize(new Dimension(10, 10));
                    clickCount++;
                }
            }
        };

        for (int i = 1; i <= 25; i++) {
            JButton square = new JButton(String.valueOf(i));
            square.setPreferredSize(new Dimension(30, 30));
            square.addActionListener(squareClickListener);
            rightPanel.add(square);
        }

        add(rightPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JButton saveButton = new JButton("Verstuur");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickedSquares.clear();
                Component[] components = rightPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JButton) {
                        JButton square = (JButton) component;
                        if (square.getBackground() == Color.GRAY) {
                            int value = Integer.parseInt(square.getText());
                            clickedSquares.add(value);
                        }
                    }
                }
                int[] clickedSquaresArray = clickedSquares.stream().mapToInt(Integer::intValue).toArray();
                // Perform your desired actions with the clicked squares here
                // ...
                displayResult(clickedSquares);
            }
        });

        bottomPanel.add(saveButton);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private JButton createColorButton(Color color) {
        JButton button = new JButton();
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        return button;
    }

    private void displayResult(ArrayList<Integer> clickedSquares) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < clickedSquares.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(clickedSquares.get(i));
        }
        String result = builder.toString();
        textArea.setText("Clicked Squares: " + result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUIMainpanel();
            }
        });
    }
}
