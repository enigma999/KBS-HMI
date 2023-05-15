import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class SerialComm implements Runnable{
    private int baudRate = 250000;
    private OutputStream outputStream;
    private Scanner scanner = new Scanner(System.in);
    private String command;
    private boolean isSendingData = false;
    private InputStream inputStream;

    public SerialComm(String comPort) {
        SerialPort serialPort = SerialPort.getCommPort(comPort);
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
        command = "c " + "(" + x + ", " + y + ")";
        byte[] bytes = command.getBytes();
        scanner.nextLine();
        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void noodstop() {
        command = "n";
        byte[] bytes = command.getBytes();
        scanner.nextLine();

        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void besturing(boolean automatisch) {
        if (automatisch) {
            command = "a";
        } else {
            command = "h";
        }
        byte[] bytes = command.getBytes();
        scanner.nextLine();

        try {
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) {
        SerialComm sc = new SerialComm("COM5");
        sc.stuurCoords(3,4);
    }

    public void run() {
        while (true) {
            if (!isSendingData) {
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

            // Sleep for a short duration before checking again
            try {
                Thread.sleep(100); // Adjust the delay as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
