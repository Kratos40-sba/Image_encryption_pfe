import java.io.File;
import java.util.List;

public class Main {
    static String url = "/home/kratos40/IdeaProjects/PFE_V0/src/main/pfe_files/Lena.bmp";
    static String SubImageUrl = "/home/kratos40/IdeaProjects/PFE_V0/src/main/pfe_files/results/image0.bmp";
    static String WriteUrl = "/home/kratos40/IdeaProjects/PFE_V0/src/main/pfe_files/";
    static byte[] Mk = new byte[]{(byte) Math.floorMod(0x80, 256), (byte) Math.floorMod(0xb1, 256), (byte) Math.floorMod(0x53, 256), (byte) Math.floorMod(0x69, 256),
            (byte) Math.floorMod(0x30, 256), (byte) Math.floorMod(0x11, 256), (byte) Math.floorMod(0x2b, 256), (byte) Math.floorMod(0x09, 256)};

    public static void main(String[] args) {
        try {
            var dkv = Init.Dkv(Mk);
            List<int[][]> blocks = ImageUtils.decompose(new File(url));
            // ImageUtils.compose(blocks,new File(WriteUrl+"/composed.bmp"),512,512);
            List<int[][]> encrypted_image = Encryption.encrypt_image(blocks, dkv);
            ImageUtils.compose(encrypted_image, new File(WriteUrl + "/encrypted.bmp"), 512, 512);
            List<int[][]> decrypted_image = Encryption.decrypt_image(encrypted_image, dkv);
            System.out.println(decrypted_image.size() + " " + encrypted_image.size());
            ImageUtils.compose(decrypted_image, new File(WriteUrl + "/decrypted.bmp"), 512, 512);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
/*
  MathUtils.print(blocks.get(100));
            var encrypted_image1 = Encryption.encrypt_block(blocks.get(100), dkv,Init.Im1(dkv));
            System.out.println("******************* Encrypted Bloc ******************");
            MathUtils.print(encrypted_image1);
            var decrypted_image2 = Encryption.decrypt_block(encrypted_image1, dkv,Init.Im1(dkv));
            System.out.println("******************* Decrypted Bloc ******************");
            MathUtils.print(decrypted_image2);
 */
/*
    var g = Init.G(dkv);
            var g_inv = Init.Inv_G(dkv);
            int[][] M = {
                    {dkv[0], dkv[8], dkv[16], dkv[24]},
                    {dkv[1], dkv[9], dkv[17], dkv[25]},
                    {dkv[2], dkv[10], dkv[18], dkv[26]},
                    {dkv[3], dkv[11], dkv[19], dkv[27]}
            };
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++) {
                    M[i][j] = Math.floorMod(M[i][j], 256);
                }
            System.out.println("******************* M *********************");
            MathUtils.print(M);
            System.out.println("******************* G *********************");
            MathUtils.print(g);
            System.out.println("******************* G-INV *****************");
            MathUtils.print(g_inv);

 */
/*
    var g = Init.G(dkv);
            var g_inv = Init.Inv_G(dkv);
            System.out.println("******************* G ******************");
            MathUtils.print(g);
            System.out.println("******************* G-INV ******************");
            MathUtils.print(g_inv);
              var index = 1 ;
            for(byte b: dkv){
                System.out.println("value = " + b + "index = " + index++);
            }
            int[][] M = {
                    {dkv[0],dkv[8],dkv[16],dkv[24]},
                    {dkv[1],dkv[9],dkv[17],dkv[25]},
                    {dkv[2],dkv[10],dkv[18],dkv[26]},
                    {dkv[3],dkv[11],dkv[19],dkv[27]}
            };
            MathUtils.print(M);
 */