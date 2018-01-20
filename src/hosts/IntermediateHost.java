package hosts;

import java.io.IOException;
import java.net.*;

public class IntermediateHost extends Host {

    private DatagramSocket port23Socket, sendReceiveSocket;

    private IntermediateHost() {
        try {
            sendReceiveSocket = new DatagramSocket();
            //Timeout is purely so that IntermediateHost will stop when ServerHost Crashes (convenience)
            sendReceiveSocket.setSoTimeout(10000);
            port23Socket = new DatagramSocket(23);
        } catch (SocketException se){
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * loop forever
     */
    private  void loop(){
        while (true) receiveSendPacket();
    }

    /**
     * Receive Data from one host and forward to another. Then return response to original host.
     */
    private void receiveSendPacket(){

        byte buffer[] = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        //Receive Original Data
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

        // Forward Original Data
        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 69);
            sendReceiveSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        buffer = new byte[BUFFER_SIZE];
        DatagramPacket receiveServerPacket = new DatagramPacket(buffer, buffer.length);

        //Grab Response from Second Host
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

        //Send Response to Original Host
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
