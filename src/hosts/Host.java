package hosts;

import java.net.DatagramPacket;
import java.util.Arrays;

public abstract class Host {

    protected static final byte READ[] = new byte[] { (byte)0x00, (byte)0x01 };
    protected static final byte WRITE[] = new byte[] { (byte)0x00, (byte)0x02 };
    protected static final int  BUFFER_SIZE = 100;

    /**
     * Display Byte Data in Byte Format and StringFormat.
     * @param data - Byte Data to Display
     */
    protected void display(byte[] data){
        System.out.println("Byte Format:");
        System.out.println(Arrays.toString(data));
        System.out.println("String Format:");
        System.out.println(new String(data));
        System.out.println("");
    }

    /**
     * copies Packet Data into Bytes of correct size.
     * @param packet - packet to convert.
     * @return - Data in byte[] format.
     */
    protected byte[] copyByteData(DatagramPacket packet) {
        byte data[] = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
        return data;
    }

}

