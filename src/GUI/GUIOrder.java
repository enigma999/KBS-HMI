package GUI;

import database.Order;
import database.Stockitems;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GUIOrder extends JFrame {
    private Order order;
    private Stockitems stockitems;
    private JTextArea ordersTextArea;

    public GUIOrder() {
        setTitle("Orders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel middlePanel = new JPanel();
        middlePanel.setBackground(new Color(220, 220, 220));
        middlePanel.setLayout(new BorderLayout());

        ordersTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(ordersTextArea);
        middlePanel.add(scrollPane, BorderLayout.CENTER);

        add(middlePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 50));

        add(bottomPanel, BorderLayout.SOUTH);

        order = new Order();
        try {
            ArrayList<ArrayList<String>> orders = order.queryResult("SELECT * FROM ordertabel");
            displayOrders(ordersTextArea, orders);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stockitems = new Stockitems();

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JButton backButton = new JButton("Terug");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add code to go back to the previous screen
            }
        });

        JButton adjustOrderButton = new JButton("Order aanpassen");
        adjustOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the order adjustment screen
                openOrderAdjustmentScreen();
            }
        });

        JButton createOrderButton = new JButton("Order aanmaken");
        createOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the order creation screen
                openOrderCreationScreen();
            }
        });

        topPanel.add(backButton);
        topPanel.add(adjustOrderButton);
        topPanel.add(createOrderButton);

        add(topPanel, BorderLayout.NORTH);

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public void displayOrders(JTextArea ordersTextArea, ArrayList<ArrayList<String>> orders) {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<String> order : orders) {
            sb.append("Order ID: ").append(order.get(0)).append("\n");
            sb.append("Order Date: ").append(order.get(1)).append("\n");
            sb.append("Picked: ").append(order.get(2)).append("\n");
            sb.append("Customer ID: ").append(order.get(3)).append("\n");
            sb.append("----------------------------------\n");
        }
        ordersTextArea.setText(sb.toString());
    }

    public void openOrderAdjustmentScreen() {
        String orderIDText = JOptionPane.showInputDialog("Enter Order ID:");
        if (orderIDText != null && !orderIDText.isEmpty()) {
            try {
                int orderID = Integer.parseInt(orderIDText);

                if (order.orderExists(orderID)) {
                    JFrame adjustmentFrame = new JFrame("Adjust Order");
                    adjustmentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    adjustmentFrame.setLayout(new BorderLayout());

                    JPanel contentPanel = new JPanel();
                    contentPanel.setLayout(new GridLayout(6, 2, 10, 10));
                    contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JLabel orderIDLabel = new JLabel("Order ID:");
                    JTextField orderIDField = new JTextField();

                    JLabel stockitemLabel = new JLabel("Stock Item IDs:");
                    JTextField stockitemField = new JTextField();

                    JLabel quantityLabel = new JLabel("Quantities:");
                    JTextField quantityField = new JTextField();

                    JButton saveChangesButton = new JButton("Save Changes");
                    saveChangesButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String stockitemText = stockitemField.getText();
                            String quantityText = quantityField.getText();

                            if (stockitemText.isEmpty() || quantityText.isEmpty()) {
                                JOptionPane.showMessageDialog(adjustmentFrame, "Please fill in all fields.", "Incomplete Data", JOptionPane.WARNING_MESSAGE);
                            } else {
                                try {
                                    // Split stock item IDs and quantities
                                    String[] stockitemArray = stockitemText.split(",");
                                    String[] quantityArray = quantityText.split(",");

                                    if (stockitemArray.length != quantityArray.length) {
                                        JOptionPane.showMessageDialog(adjustmentFrame, "Number of stock items should match the number of quantities.", "Invalid Data", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        int[] stockitemIDs = new int[stockitemArray.length];
                                        int[] quantities = new int[quantityArray.length];

                                        for (int i = 0; i < stockitemArray.length; i++) {
                                            stockitemIDs[i] = Integer.parseInt(stockitemArray[i].trim());
                                            quantities[i] = Integer.parseInt(quantityArray[i].trim());
                                        }

                                        // Update the order in the database
                                        try {
                                            order.updateOrder(orderID, quantities);
                                            JOptionPane.showMessageDialog(adjustmentFrame, "Order updated successfully.", "Order Updated", JOptionPane.INFORMATION_MESSAGE);

                                            // Refresh the orders display
                                            try {
                                                ArrayList<ArrayList<String>> orders = order.queryResult("SELECT * FROM ordertabel");
                                                displayOrders(ordersTextArea, orders);
                                            } catch (SQLException ex) {
                                                ex.printStackTrace();
                                                JOptionPane.showMessageDialog(adjustmentFrame, "Failed to retrieve orders from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                                            }

                                            // Close the adjustment screen
                                            adjustmentFrame.dispose();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                            JOptionPane.showMessageDialog(adjustmentFrame, "Failed to update the order in the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(adjustmentFrame, "Invalid quantity. Please enter numeric values.", "Invalid Data", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    });

                    contentPanel.add(orderIDLabel);
                    contentPanel.add(orderIDField);
                    contentPanel.add(stockitemLabel);
                    contentPanel.add(stockitemField);
                    contentPanel.add(quantityLabel);
                    contentPanel.add(quantityField);
                    contentPanel.add(new JLabel());
                    contentPanel.add(saveChangesButton);

                    ArrayList<ArrayList<String>> orderInfo = order.getOrderInfo(orderID);
                    if (orderInfo != null) {
                        orderIDField.setText(orderIDText);

                        int[] orderStockIDs = order.getOrderStockID(orderID);
                        StringBuilder stockitemsBuilder = new StringBuilder();
                        StringBuilder quantitiesBuilder = new StringBuilder();
                        for (int i = 0; i < orderStockIDs.length; i++) {
                            stockitemsBuilder.append(orderStockIDs[i]).append(", ");
                            quantitiesBuilder.append(orderInfo.get(i).get(1)).append(", ");
                        }
                        if (stockitemsBuilder.length() > 0) {
                            stockitemField.setText(stockitemsBuilder.substring(0, stockitemsBuilder.length() - 2));
                        }
                        if (quantitiesBuilder.length() > 0) {
                            quantityField.setText(quantitiesBuilder.substring(0, quantitiesBuilder.length() - 2));
                        }
                    } else {
                        JOptionPane.showMessageDialog(adjustmentFrame, "Failed to retrieve order information from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    }

                    adjustmentFrame.add(contentPanel, BorderLayout.CENTER);
                    adjustmentFrame.pack();
                    adjustmentFrame.setLocationRelativeTo(GUIOrder.this);
                    adjustmentFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(GUIOrder.this, "Order not found.", "Order Not Found", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(GUIOrder.this, "Invalid Order ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void openOrderCreationScreen() {
        JFrame creationFrame = new JFrame("Create Order");
        creationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        creationFrame.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(6, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel customerIDLabel = new JLabel("Customer ID:");
        JTextField customerIDField = new JTextField();

        JLabel stockitemLabel = new JLabel("Stock Item IDs:");
        JTextField stockitemField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantities:");
        JTextField quantityField = new JTextField();

        JButton completeButton = new JButton("Complete");
        completeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerIDText = customerIDField.getText();
                String stockitemText = stockitemField.getText();
                String quantityText = quantityField.getText();

                if (customerIDText.isEmpty() || stockitemText.isEmpty() || quantityText.isEmpty()) {
                    JOptionPane.showMessageDialog(creationFrame, "Please fill in all fields.", "Incomplete Data", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int customerID = Integer.parseInt(customerIDText);

                        // Split stock item IDs and quantities
                        String[] stockitemArray = stockitemText.split(",");
                        String[] quantityArray = quantityText.split(",");

                        if (stockitemArray.length != quantityArray.length) {
                            JOptionPane.showMessageDialog(creationFrame, "Number of stock items should match the number of quantities.", "Invalid Data", JOptionPane.ERROR_MESSAGE);
                        } else {
                            int[] stockitemIDs = new int[stockitemArray.length];
                            int[] quantities = new int[quantityArray.length];

                            for (int i = 0; i < stockitemArray.length; i++) {
                                stockitemIDs[i] = Integer.parseInt(stockitemArray[i].trim());
                                quantities[i] = Integer.parseInt(quantityArray[i].trim());
                            }

                            try {
                                // Insert the order into the database
                                order.insertOrder(LocalDate.now(), stockitemIDs, quantities, customerID);

                                JOptionPane.showMessageDialog(creationFrame, "Order created successfully.", "Order Created", JOptionPane.INFORMATION_MESSAGE);

                                // Refresh the orders display
                                try {
                                    ArrayList<ArrayList<String>> orders = order.queryResult("SELECT * FROM ordertabel");
                                    displayOrders(ordersTextArea, orders);
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                    JOptionPane.showMessageDialog(creationFrame, "Failed to retrieve orders from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                                }

                                // Clear the input fields
                                customerIDField.setText("");
                                stockitemField.setText("");
                                quantityField.setText("");

                                // Close the creation screen
                                creationFrame.dispose();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(creationFrame, "Failed to insert the order into the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(creationFrame, "Invalid customer ID or quantity. Please enter numeric values.", "Invalid Data", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(creationFrame, "Invalid customer ID. Please enter a numeric value.", "Invalid Data", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        contentPanel.add(customerIDLabel);
        contentPanel.add(customerIDField);
        contentPanel.add(stockitemLabel);
        contentPanel.add(stockitemField);
        contentPanel.add(quantityLabel);
        contentPanel.add(quantityField);
        contentPanel.add(new JLabel());
        contentPanel.add(completeButton);

        creationFrame.add(contentPanel, BorderLayout.CENTER);
        creationFrame.pack();
        creationFrame.setLocationRelativeTo(this);
        creationFrame.setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUIOrder();
            }
        });
    }
}
