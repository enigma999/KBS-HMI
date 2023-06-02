package GUI;
import TSP.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class TSPTestFrame extends JFrame {

    private Magazijn magazijn;
    private AlgValues bb;
    private AlgValues bf;
    private AlgValues nn;

    public TSPTestFrame(Magazijn magazijn) {
        this.magazijn = magazijn;
        bb = TSPTest.execTSP(magazijn, new BranchAndBound());
        bf = TSPTest.execTSP(magazijn, new BruteForce());
        nn = TSPTest.execTSP(magazijn, new NearestNeighbour());

        setTitle("TSP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Bovenste witte menu bar met drie knoppen
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Dikkere menubalk

        JButton button1 = new JButton("Terug");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current GUIOrder screen
                // Add code here to go back to the main GUI screen (GUIMainpanel)
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            new GUIMainpanel(); // Create a new instance of GUIMainpanel
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });


        topPanel.add(button1);


        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);

        // Add multiple info blocks to the main panel
        addInfoBlock(mainPanel, bb.getNaam(), bb.getTime(), bb.getDistance());
        addVerticalSpacing(mainPanel, 30); // Add vertical spacing between blocks
        addInfoBlock(mainPanel, bf.getNaam(), bf.getTime(), bf.getDistance());
        addVerticalSpacing(mainPanel, 30); // Add vertical spacing between blocks
        addInfoBlock(mainPanel, nn.getNaam(), nn.getTime(), nn.getDistance());


        add(mainPanel, BorderLayout.CENTER);

        // Onderste witte bar met dezelfde dikte als de menubalk
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Dikkere onderste balk

        add(bottomPanel, BorderLayout.SOUTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in fullscreen
        setVisible(true);
    }

    //maakt een infoblok voor 1 algoritme
    public static void addInfoBlock(JPanel panel, String name, long time, double length) {

        JPanel infoBlock = new JPanel();
        infoBlock.setBackground(Color.GRAY);
        infoBlock.setLayout(new BoxLayout(infoBlock, BoxLayout.Y_AXIS));


        JLabel nameLabel = new JLabel("Naam: " + name);
        JLabel timeLabel = new JLabel("Tijd: " + time);
        JLabel lengthLabel = new JLabel("Lengte: " + length);

        infoBlock.add(Box.createVerticalStrut(5));
        infoBlock.add(nameLabel);
        infoBlock.add(timeLabel);
        infoBlock.add(lengthLabel);
        infoBlock.add(Box.createVerticalStrut(5));


        panel.add(infoBlock);
    }

    public static void addVerticalSpacing(JPanel panel, int spacing) {
        panel.add(Box.createVerticalStrut(spacing));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TSPTestFrame(new Magazijn(3,5,5));
        });
    }
}


