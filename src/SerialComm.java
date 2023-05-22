import com.fazecast.jSerialComm.*;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class SerialComm implements Runnable{
    private int baudRate = 9600;
    private OutputStream outputStream;
    private Scanner scanner = new Scanner(System.in);
    private String command;
    private boolean isSendingData = false;
    private InputStream inputStream;
    private SerialPort serialPort;

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

    public void stuurCoords(int x, int y) {
        isSendingData = true;
        command = "c " + "(" + x + ", " + y + ")";
        stuurCommand(command);

    }

    public void noodstop() {
        isSendingData = true;
        command = "n";
        stuurCommand(command);
    }

    public void besturing(boolean automatisch) {
        isSendingData = true;
        if (automatisch) {
            command = "a";
        } else {
            command = "h";
        }
        stuurCommand(command);


    }

    private void stuurCommand(String command) {
        byte[] bytes = command.getBytes();
        serialPort.writeBytes(bytes, bytes.length);
        isSendingData = false;
    }
    public static void main(String[] args) {
        Scanner scanner1 = new Scanner(System.in);
        SerialComm sc = new SerialComm("COM6");
        scanner1.nextLine();
        Thread thread = new Thread(sc);
        thread.start();
        sc.stuurCoords(3,4);
        while (true) {
            scanner1.nextLine();
            sc.noodstop();
            scanner1.nextLine();
            sc.besturing(true);

        }
    }



    public void run() {
        while (true) {
            try {
                if (!isSendingData && inputStream.available() > 0) {
                    // Read data from the serial port
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    try {
                        bytesRead = inputStream.read(buffer);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Process the received data
                    String receivedData = new String(buffer, 0, bytesRead);
                    System.out.println("Received data: " + receivedData);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Sleep for a short duration before checking again
            try {
                Thread.sleep(100); // Adjust the delay as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
