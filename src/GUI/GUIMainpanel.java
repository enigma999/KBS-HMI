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
import TSP.*;
import database.Stockitems;
import SerialCom.SerialComm;

public class GUIMainpanel extends JFrame implements Runnable{
    private ArrayList<Integer> clickedSquares = new ArrayList<>();
    private JTextArea textArea;
    private SerialComm communicatie;
    private boolean sturen = false;
    private ArrayList<int[]> tspcoord;
    private boolean lossen = false;

    public GUIMainpanel() throws SQLException {
        communicatie = new SerialComm("COM6");
        Thread thread = new Thread(communicatie);
        thread.start();
        new Thread(this).start();


        setTitle("Drie-panel GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1920, 1040));

        setVisible(true);

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


        // TSP scherm
        JButton tspknop = createButton("TSP Test");
        tspknop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current GUIMainpanel screen
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Magazijn testmagazijn = new Magazijn(6, 10, 10);
                        new TSPTestFrame(testmagazijn);
                    }
                });
            }
        });
        // Order
        JButton orderbutton = createButton("Orders");
        orderbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current GUIMainpanel screen

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new GUIOrder();
                    }
                });
            }
        });

        topPanel.add(tspknop);
        topPanel.add(orderbutton);

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
                TSP tsp = new TSP();
                try {
                    // BinPacking
                    int[] BinPP = coordinaten.getGewicht(clickedSquaresArray);
                    ArrayList<ArrayList<Integer>> result = binpacking.bestFit(BinPP, clickedSquaresArray);
                    // Coordinaten sturen
                    ArrayList<int[]> coord = coordinaten.getCoordinaten(clickedSquaresArray);
                    tspcoord = tsp.getBranchBound(coord);

                    sturen = true;
                    displayResult(clickedSquares, result, tspcoord);

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

    private void displayResult(ArrayList<Integer> clickedSquares, ArrayList<ArrayList<Integer>> result, ArrayList<int[]> tspcoord) {
        //Order
        StringBuilder orderBuilder = new StringBuilder();
        for (int i = 0; i < clickedSquares.size(); i++) {
            if (i > 0) {
                orderBuilder.append(", ");
            }
            orderBuilder.append(clickedSquares.get(i));
        }
        String order = orderBuilder.toString();
        //BPP
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
        //TSP
        StringBuilder tspCoordBuilder = new StringBuilder();
        for (int i = 0; i < tspcoord.size(); i++) {
            int[] coords = tspcoord.get(i);
            if (i > 0) {
                tspCoordBuilder.append(" ---> ");
            }
            tspCoordBuilder.append(coords[0]).append(", ").append(coords[1]);
        }
        String tspCoords = tspCoordBuilder.toString();

        textArea.setText("Order = " + order + "\nBPP = " + bins + "\nTSP route = " + tspCoords);
    }



    public static void main(String[] args) {
        try {
            new GUIMainpanel();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        int i = 0;


        while (true) {

            if (sturen) {
                ArrayList<int[]> arrayList = tspcoord;
                int drietal = (communicatie.getIterator()) % 3;
                lossen = drietal ==0 && communicatie.getIterator()!=0;
                System.out.println(communicatie.getIterator());
                if (lossen) {
                    communicatie.leveren();


                }
                if (communicatie.getSendNext() && !lossen) {
                    if (i < arrayList.size()) {
                        communicatie.setSendNext(false);
                        int[] coordinaten = arrayList.get(i);
                        communicatie.stuurCoords(coordinaten[0], coordinaten[1]);
                        i++;

                    }

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
