import java.io.*;

public class DumpHex {
    public static void main(String[] args) throws Exception {
        String file = args[0];
        FileInputStream fis = new FileInputStream(file);
        byte[] data = fis.readAllBytes();
        fis.close();
        
        System.out.println("File size: " + data.length);
        System.out.println("First 50 bytes:");
        for (int i = 0; i < Math.min(50, data.length); i++) {
            System.out.printf("%02X ", data[i]);
            if ((i+1) % 16 == 0) System.out.println();
        }
        System.out.println();
    }
}
