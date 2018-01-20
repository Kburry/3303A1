package hosts;

import java.io.IOException;
import java.net.*;

public class ClientHost extends Host {

    private static final String FILENAME = "TESTFILEPLEASEIGNORE.txt";
    private static final String MODE = "NeTascii";
    private static final byte ZERO[] = new byte[]{(byte) 0x00};
    private static final byte INVALID[] = new byte[] { (byte)0x01, (byte)0x01 };
    private DatagramSocket sendReceiveSocket;

    private ClientHost(){
        try {
            sendReceiveSocket = new DatagramSocket();
            //Timeout is purely so that ClientHost will stop when ServerHost Crashes (convenience) catch can be changed from Exit to Return for constant running
            sendReceiveSocket.setSoTimeout(10000);
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates the Packet Data.
     * @return - The Packet Data
     */
    private byte[] createByteData(byte[] format){
        byte filename[] = FILENAME.getBytes();
        byte mode[] = MODE.getBytes();
        byte data[] = new byte[format.length+ filename.length + ZERO.length + mode.length + ZERO.length];
        System.arraycopy(format, 0, data, 0, format.length);
        System.arraycopy(filename, 0, data, format.length, filename.length);
        System.arraycopy(ZERO,0, data,format.length+filename.length, ZERO.length);
        System.arraycopy(mode, 0, data, format.length+filename.length+ZERO.length, mode.length);
        System.arraycopy(ZERO,0,data, format.length+ filename.length + ZERO.length + mode.length, ZERO.length);
        return data;
    }

    /**
     * Send 10 valid packets and a invalid packet
     */
    private void sendMultiple(){
        for(int i = 0; i < 10; i++){
            sendAndReceive(i % 2 == 0 ? Host.READ : Host.WRITE);
        }
        sendAndReceive(INVALID);
        sendReceiveSocket.close();
    }

    /**
     * send a Packet and Get a Response.
     * @param format - HEADER For the Data
     * */
    private void sendAndReceive(byte[] format){
        //Create Data
        byte packetData[] = createByteData(format);
        System.out.println("Data Sent:");
        display(packetData);

        //Send Packet
        try {
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, InetAddress.getLocalHost(), 23);
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        //Create Buffer
        byte buffer[] = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        //Receive Data, With Timeout will crash after 10 seconds (for convenience)
        try {
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        byte data[] = copyByteData(receivePacket);
        System.out.println("Data Received:");
        display(data);
    }

    public static void main(String[] args) {
        ClientHost c = new ClientHost();
        c.sendMultiple();
    }
}
