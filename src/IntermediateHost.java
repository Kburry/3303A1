import java.io.IOException;
import java.net.*;

public class IntermediateHost {

    private DatagramPacket sendPacket, receivePacket, receiveServerPacket;
    private DatagramSocket port23Socket, sendReceiveSocket;

    private IntermediateHost() {
        try {
            sendReceiveSocket = new DatagramSocket();
            port23Socket = new DatagramSocket(23);
        } catch (SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void receiveSendPacket(){

        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            port23Socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        data = receivePacket.getData();
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 69);
        } catch(UnknownHostException uhe){
            uhe.printStackTrace();
            System.exit(1);
        }

        try {
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        data = new byte[100];
        receiveServerPacket = new DatagramPacket(data, data.length);

        try{
            sendReceiveSocket.receive(receiveServerPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        data = receiveServerPacket.getData();
        sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
        try{
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        port23Socket.close();
        sendReceiveSocket.close();

    }

    public static void main(String[] args) {
        IntermediateHost i = new IntermediateHost();
        i.receiveSendPacket();
    }
}
