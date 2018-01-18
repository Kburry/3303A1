import java.io.IOException;
import java.net.*;

public class ClientHost {

    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendReceiveSocket;

    private static final byte READ[] = new byte[] { (byte)0x00, (byte)0x01 };
    private static final byte WRITE[] = new byte[] { (byte)0x00, (byte)0x02 };
    private static final byte INVALID[] = new byte[] { (byte)0x01, (byte)0x01 };
    private static final String FILENAME = "TESTFILEPLEASEIGNORE.txt";

    private ClientHost(){
        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void sendAndReceive(){
        byte filename[] = FILENAME.getBytes();

        try {
            sendPacket = new DatagramPacket(filename, filename.length, InetAddress.getLocalHost(), 23);
        }catch (UnknownHostException se){
            se.printStackTrace();
            System.exit(1);
        }

        try{
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        sendReceiveSocket.close();


    }


    public static void main(String[] args) {
        ClientHost c = new ClientHost();
        c.sendAndReceive();
    }
}
