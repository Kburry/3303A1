import java.net.*;

public class ClientHost {

    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket sendReceiveSocket;

    private static final byte READ[] = new byte[] { (byte)0x00, (byte)0x01 };
    private static final byte WRITE[] = new byte[] { (byte)0x00, (byte)0x02 };
    private static final byte INVALID[] = new byte[] { (byte)0x01, (byte)0x01 };
    private static final String FILENAME = "TESTFILEPLEASEIGNORE.txt";

    public ClientHost(){
        try {
            sendReceiveSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
    }

    public void sendAndReceive(){
        byte filename[] = FILENAME.getBytes();

        try {
            sendPacket = new DatagramPacket(filename, filename.length, InetAddress.getLocalHost(), 5000);
        }catch (UnknownHostException se){
            se.printStackTrace();
            System.exit(1);
        }


    }


    public static void main(String[] args) {

    }
}
