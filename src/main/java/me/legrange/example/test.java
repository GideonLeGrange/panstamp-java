package me.legrange.example;

/**
 *
 * @author gideon
 */
public class test {

    public static void main(String... args) throws Exception {

        final byte[] bb = new byte[]{0x1, 0x2, (byte)(0x84-256)};
        String s = new String(bb);
        for (byte b : s.getBytes()) {
            System.out.printf("%02x ", b);
        }
        System.out.println();
    }

}
