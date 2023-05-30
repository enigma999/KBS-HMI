package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import database.Stockitems;
import java.sql.SQLException;
import java.util.Arrays;
import BPP.BPP;

public class GUIMainpanel extends JFrame {
    private ArrayList<Integer> clickedSquares = new ArrayList<>();

    public GUIMainpanel() throws SQLException {
        setTitle("Drie-panel GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        // Bovenste Menubalk met stoplicht en 2 knoppen
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

        topPanel.add(button1);
        topPanel.add(button2);

        add(topPanel, BorderLayout.NORTH);

        // Linkerpaneel voor de tekst
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(220, 220, 220)); // Lichtgrijs
        leftPanel.setPreferredSize(new Dimension(400, getHeight()));
        leftPanel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        String text = "Order: #29\n" +
                "Route: 5 --> 9 --> 22\n" +
                "Tijd: 13,85 seconde(s)\n" +
                "BPP: Bin 1: [x, x], Bin 2: [x]\n" +
                "*VOORBEELD*";
        textArea.setText(text);
        textArea.setBackground(new Color(220, 220, 220));
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));

        leftPanel.add(textArea, BorderLayout.WEST);

        add(leftPanel, BorderLayout.WEST);

        // Rechterpaneel voor de grid
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(220, 220, 220)); // Lichtgrijs
        rightPanel.setLayout(new GridLayout(5, 5));

        ActionListener squareClickListener = new ActionListener() {
            private int clickCount = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton square = (JButton) e.getSource();
                if (clickCount < 3) {
                    square.setBackground(Color.GRAY);
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

        // Onderste menubalk met een klop
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JButton saveButton = new JButton("Verstuur");

        // Create the ActionListener for the saveButton
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
                int[] clickedSquaresArray = new int[clickedSquares.size()];

                for (int i = 0; i < clickedSquares.size(); i++) {
                    clickedSquaresArray[i] = clickedSquares.get(i);
                }
                Stockitems coordinaten = new Stockitems();
                BPP binpacking = new BPP();

                try {
                    System.out.println(coordinaten.getCoordinaten(clickedSquaresArray)); //coordinaten
                    System.out.println(Arrays.toString(coordinaten.getGewicht(clickedSquaresArray))); //gewicht
                    int[] BinPP = coordinaten.getGewicht(clickedSquaresArray);
                    System.out.println(binpacking.bestFit(BinPP)); //bpp

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        bottomPanel.add(saveButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createColorButton(Color color) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(20, 20));
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        return button;
    }
}