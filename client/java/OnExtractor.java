import java.io.*;

public class OnExtractor {
    public static void main(String[] args) throws Exception {
        String onFile = args.length > 0 ? args[0] : "imgPopup0.on";
        String outFile = args.length > 1 ? args[1] : "output.png";

        FileInputStream fis = new FileInputStream(onFile);
        byte[] data = fis.readAllBytes();
        fis.close();
        
        // Check nếu là PNG (89 50 4E 47)
        if (data.length > 4 && data[0] == (byte)0x89 && data[1] == 0x50 && data[2] == 0x4E && data[3] == 0x47) {
            // Đã là PNG, copy trực tiếp
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(data);
            fos.close();
            System.out.println("Extracted PNG: " + outFile + " (" + data.length + " bytes)");
        } else {
            // Format khác - thử decode
            System.out.println("Not PNG, trying decode...");
            byte[] code = new byte[]{78, 103, 117, 121, 101, 110, 86, 97, 110, 77, 105, 110, 104};
            for (int i = 0; i < data.length; i++) {
                data[i] ^= code[i % code.length];
            }
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(data);
            fos.close();
            System.out.println("Extracted (decoded): " + outFile + " (" + data.length + " bytes)");
        }
    }
}
