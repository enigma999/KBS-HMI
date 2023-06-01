package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import BPP.BPP;
import database.Stockitems;
import SerialCom.SerialComm;

public class GUIMainpanel extends JFrame implements Runnable{
    private ArrayList<Integer> clickedSquares = new ArrayList<>();
    private JTextArea textArea;
    private SerialComm communicatie;
    private boolean coords = false;

    public GUIMainpanel() throws SQLException {
        communicatie = new SerialComm("COM7");
        Thread thread = new Thread(communicatie);
        thread.start();
        new Thread(this).start();


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
// Noodstop
        redButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicatie.noodstop();
            }
        });
// Manual
        yellowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicatie.besturing(false);
            }
        });
// Automatisch
        greenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                communicatie.besturing(true);
            }
        });

        topPanel.add(redButton);
        topPanel.add(yellowButton);
        topPanel.add(greenButton);

        JButton button1 = createButton("TSP Test");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform the TSP test action here
                // Add your code logic for the TSP test
                // You can call any methods or perform any actions you need

                // Example:
                communicatie.besturing(true);
            }
        });
        JButton button2 = createButton("Orders");

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
                Stockitems coordinaten = new Stockitems();
                BPP binpacking = new BPP();
                try {
                    // BinPacking
                    int[] BinPP = coordinaten.getGewicht(clickedSquaresArray);
                    ArrayList<ArrayList<Integer>> result = binpacking.bestFit(BinPP, clickedSquaresArray);
                    // Coordinaten sturen
//                    ArrayList<ArrayList<String>> coord = coordinaten.getCoordinaten(clickedSquaresArray);
//                    for (ArrayList<String> coordinate : coord) {
//                        int x = Integer.parseInt(coordinate.get(0));
//                        int y = Integer.parseInt(coordinate.get(1));
//                        communicatie.stuurCoords(x, y);
//                    }
//                    new Scanner(System.in).nextLine();
                    coords = true;
                    displayResult(clickedSquares, result);

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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

    private void displayResult(ArrayList<Integer> clickedSquares, ArrayList<ArrayList<Integer>> result) {
        StringBuilder orderBuilder = new StringBuilder();
        for (int i = 0; i < clickedSquares.size(); i++) {
            if (i > 0) {
                orderBuilder.append(", ");
            }
            orderBuilder.append(clickedSquares.get(i));
        }
        String order = orderBuilder.toString();

        StringBuilder binBuilder = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            ArrayList<Integer> bin = result.get(i);
            StringBuilder binContents = new StringBuilder();
            for (int j = 0; j < bin.size(); j++) {
                if (j > 0) {
                    binContents.append(", ");
                }
                binContents.append(bin.get(j));
            }
            String binString = "Bin " + (i + 1) + ": " + binContents.toString();
            binBuilder.append(binString);
            if (i < result.size() - 1) {
                binBuilder.append(", ");
            }
        }
        String bins = binBuilder.toString();

        textArea.setText("Order = " + order + "\nBPP = " + bins);
    }



    public static void main(String[] args) {
        try {
            new GUIMainpanel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        ArrayList<int[]> arrayList = new ArrayList<>();
        int i = 0;
        int[] array1 = {1, 2};
        int[] array2 = {3, 3};
        int[] array3 = {2, 4};
        int[] array4 = {4,0};

        arrayList.add(array1);
        arrayList.add(array2);
        arrayList.add(array3);

        while (true) {

            if (coords) {
                System.out.println(i);
                int drietal = (i) % 3;
                boolean lossen = drietal ==0 && i!=0;
                if (lossen) {
                    communicatie.leveren();
                }
                if (communicatie.getSendNext() && !lossen) {
                    communicatie.setSendNext(false);
                    int[] coordinaten = arrayList.get(i);
                    communicatie.stuurCoords(coordinaten[0], coordinaten[1]);
                    i++;
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
