// Deze class is voor de communicatie tussen de arduino en de core. Hij implement runnable voor een thread om alle inkomende informatie
// van de arduino te printen in de console.

package SerialCom;
import GUI.GUIMainpanel;
import com.fazecast.jSerialComm.*;
import database.Connectie;
import database.Stockitems;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class SerialComm implements Runnable{
    private int baudRate = 9600;
    private boolean sendNext = true;
    private int iterator = 0;
    private OutputStream outputStream;
    private Scanner scanner = new Scanner(System.in);
    private String command;
    private boolean isSendingData = false;
    private InputStream inputStream;
    private SerialPort serialPort;

    //Openen van connectie.
    public SerialComm(String comPort) {
        serialPort = SerialPort.getCommPort(comPort);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()) {
            System.out.println("Serial port opened successfully.");
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
        } else {
            System.err.println("Niet connected");
        }

    }

    //Stuurt de coordinaten naar de arduino.
    public void stuurCoords(int x, int y) {
        isSendingData = true;
        command = "c " + x + " " + y;
        stuurCommand(command);

    }

    //Stuurt een sein voor de noodstop.
    public void noodstop() {
        isSendingData = true;
        command = "n";
        stuurCommand(command);
    }

    //Hiermee kun je de besturing aanpassen, true voor automatisch en false voor handmatig.
    public void besturing(boolean automatisch) {
        isSendingData = true;
        if (automatisch) {
            command = "a";
        } else {
            command = "h";
        }
        stuurCommand(command);


    }

    public ArrayList<Integer> getCoords(int stockID) throws SQLException {
        ArrayList<Integer> coordinaten = new ArrayList<>();
        Stockitems connectie = new Stockitems();
        for (String coord : connectie.getCoordinaten(stockID)) {
            Integer coordinaat = Integer.parseInt(coord);
            coordinaten.add(coordinaat);
        }
        return coordinaten;
    }


    public void leveren() {
        isSendingData = true;
        command = "l";
        stuurCommand(command);
    }

    public void pakOp() {
        isSendingData = true;
        command = "o";
        stuurCommand(command);
    }

    public void setSendNext(boolean sendNext) {
        this.sendNext = sendNext;
    }
    public boolean getSendNext() {
        return sendNext;
    }

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int iterator) {
        this.iterator = iterator;
    }

    //private methode die wordt gebruikt door elke stuur methode om de informatie te sturen.
    private void stuurCommand(String command) {
        byte[] bytes = command.getBytes();
        serialPort.writeBytes(bytes, bytes.length);
        isSendingData = false;
    }

    //Voor de thread, checkt constant voor inkomende informatie en print deze.
    public void run() {
        while (true) {
            try {
                if (!isSendingData && inputStream.available() > 0) {
                    // Leest data, buffer grootte kan nog veranderd worden
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    try {
                        bytesRead = inputStream.read(buffer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    String data = new String(buffer, 0, bytesRead);
                    String receivedData = data.trim();
                    if (receivedData.equals("Gelukt")) {
                        System.out.println("gelukt");
                        sendNext = true;
                        iterator++;
                    }
//                    System.out.println("Received data: " + receivedData);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Neemt een kort pauze voordat hij weer checkt.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}