package hosts;

import java.io.IOException;
import java.net.*;

public class IntermediateHost extends Host {

    private DatagramSocket port23Socket, sendReceiveSocket;

    private IntermediateHost() {
        try {
            sendReceiveSocket = new DatagramSocket();
            sendReceiveSocket.setSoTimeout(10000);
            port23Socket = new DatagramSocket(23);
        } catch (SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    private  void loop(){
        while (true) receiveSendPacket();
    }

    private void receiveSendPacket(){

        byte buffer[] = new byte[100];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        try {
            port23Socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        byte data[] = copyByteData(receivePacket);
        System.out.println("Data Received from Client:");
        display(data);

        DatagramPacket sendPacket;

        System.out.println("Data Sent to Server:");
        display(data);

        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 69);
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        buffer = new byte[100];
        DatagramPacket receiveServerPacket = new DatagramPacket(buffer, buffer.length);

        try{
            sendReceiveSocket.receive(receiveServerPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        data = copyByteData(receiveServerPacket);
        System.out.println("Data Received from Server:");
        display(data);

        sendPacket = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());

        System.out.println("Data Sent to Client:");
        display(data);

        try{
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        IntermediateHost i = new IntermediateHost();
        i.loop();
    }
}
