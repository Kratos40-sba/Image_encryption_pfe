import java.io.File;
import java.util.List;

public class Main {
    static String url = "/home/kratos40/IdeaProjects/PFE_V0/src/main/pfe_files/Lena.bmp";
    static String WriteUrl = "/home/kratos40/IdeaProjects/PFE_V0/src/main/pfe_files/";
    static byte[] Mk = new byte[]{(byte) Math.floorMod(0x80, 256), (byte) Math.floorMod(0xb1, 256), (byte) Math.floorMod(0x53, 256), (byte) Math.floorMod(0x69, 256),
            (byte) Math.floorMod(0x30, 256), (byte) Math.floorMod(0x11, 256), (byte) Math.floorMod(0x2b, 256), (byte) Math.floorMod(0x09, 256)};

    public static void main(String[] args) {
        try {
            var dkv = Init.Dkv(Mk);
            List<int[][]> blocks = ImageUtils.decompose(new File(url));
            List<int[][]> encrypted_image = Encryption.encrypt_image(blocks, dkv);
            ImageUtils.compose(encrypted_image, new File(WriteUrl + "/encrypted.bmp"), 512, 512);
            List<int[][]> decrypted_image = Encryption.decrypt_image(encrypted_image, dkv);
            ImageUtils.compose(decrypted_image, new File(WriteUrl + "/decrypted.bmp"), 512, 512);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}