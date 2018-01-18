import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ServerHost {

    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket port69Socket, sendSocket;

    private ServerHost(){

        try {
            port69Socket = new DatagramSocket(69);
            sendSocket = new DatagramSocket();
        } catch (SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    private void receiveSendPacket(){
        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            port69Socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        data = receivePacket.getData();
        sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());

        try {
            sendSocket.send(sendPacket);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }


        port69Socket.close();
        sendSocket.close();
    }

    public static void main(String[] args) {
        ServerHost sh = new ServerHost();
        sh.receiveSendPacket();
    }
}
