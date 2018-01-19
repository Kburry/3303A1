package hosts;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class ServerHost extends Host {

    private static final int HEADER_SIZE = 2;
    private static final int FOOTER_SIZE = 1;
    private static final byte READ_RESPONSE[] = new byte[]{(byte) 0x00, 0x03, 0x00, 0x01};
    private static final byte WRITE_RESPONSE[] = new byte[]{(byte) 0x00, 0x04, 0x00, 0x00};
    private DatagramSocket port69Socket;

    private ServerHost() {
        try {
            port69Socket = new DatagramSocket(69);
        } catch (SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void loop(){
        while (true) {
            try {
                receiveSendPacket();
            } catch (DataFormatException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void receiveSendPacket() throws DataFormatException {
        byte buffer[] = new byte[100];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        try {
            port69Socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        byte data[] = copyByteData(receivePacket);
        System.out.println("Received from Intermediate Host:");
        display(data);

        data = parsePacket(data);

        DatagramPacket sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());

        System.out.println("Sending to Intermediate Host:");
        display(data);

        DatagramSocket sendSocket;
        try {
            sendSocket = new DatagramSocket();
            sendSocket.send(sendPacket);
            sendSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Parse Packet to Ensure it follows the correct format.
     * Checks for Initial READ or WRITE. Middle Zero and Final Zero.
     * |HEADER|FILENAME|0|MODE|0|
     * This supports FILENAME and MODE to be empty as this was not specified whether it needed to have data or not.
     * @param data - The byte data to parse.
     * @return - The response to the data received.
     * @throws DataFormatException - When Formatted Wrong.
     */
    private byte[] parsePacket(byte[] data) throws DataFormatException{
        byte[] format = new byte[HEADER_SIZE];
        byte[] middle = new byte[data.length - (HEADER_SIZE + FOOTER_SIZE)];
        System.arraycopy(data,0,format,0,format.length);
        System.arraycopy(data, HEADER_SIZE, middle,0, middle.length);

        //check for 1 Middle Zero
        if(!checkForOneZero(middle)){
            throw new DataFormatException();
        }

        //Check for Final Zero
        if (data[data.length- FOOTER_SIZE] != 0){
            throw new DataFormatException();
        }

        //CheckFormat
        if(Arrays.equals(format, Host.READ)){
            return READ_RESPONSE;
        } else if (Arrays.equals(format, Host.WRITE)){
            return WRITE_RESPONSE;
        } else throw new DataFormatException();
    }

    /**
     * Checks a byte Array for a single 0 byte.
     * @param data - data array
     * @return - whether one Zero was found.
     */
    private boolean checkForOneZero(byte[] data){
        int i;
        i = 0;
        for(byte b: data){
            if(b == (byte)0x00) i++;
        }
        return i == 1;
    }

    public static void main(String[] args) {
        ServerHost sh = new ServerHost();
        sh.loop();
    }
}
