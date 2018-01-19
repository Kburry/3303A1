package hosts;

import java.net.DatagramPacket;
import java.util.Arrays;

public class Host {

    protected static final byte READ[] = new byte[] { (byte)0x00, (byte)0x01 };
    protected static final byte WRITE[] = new byte[] { (byte)0x00, (byte)0x02 };

    protected void display(byte[] data){
        System.out.println("Byte Format:");
        System.out.println(Arrays.toString(data));
        System.out.println("String Format:");
        System.out.println(new String(data));
        System.out.println("");
    }

    protected byte[] copyByteData(DatagramPacket packet) {
        byte data[] = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
        return data;
    }

}

