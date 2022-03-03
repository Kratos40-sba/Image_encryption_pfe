import java.util.ArrayList;
import java.util.List;

public class Encryption {
    public static int[][] encrypt_block(int[][] image_sub_bloc, byte[] dkv, int[][] im) throws Throwable {
        int[][] y = new int[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                y[i][j] = Math.floorMod((byte) image_sub_bloc[i][j] ^ im[i][j], 256);
            }
        // substitution layer
        int[] s_box = Init.s_box_generation(dkv);
        int[] y_arr = MathUtils.matrix_to_array(y);
        for (int i = 0; i < 64; i++) {
            y_arr[i] = s_box[y_arr[i]];
        }
        int[][] z = MathUtils.array_to_matrix(y_arr);
        // multiplication
        int[][] g = Init.G(dkv);
        int[][] encrypted_image = new int[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    encrypted_image[i][j] += g[i][k] * z[k][j];
                }
                encrypted_image[i][j] = (Math.floorMod(encrypted_image[i][j], 256));
            }
        return encrypted_image;
    }

    public static int[][] decrypt_block(int[][] enc_matrix, byte[] dkv, int[][] im) throws Throwable {
        int[][] D = new int[8][8];
        int[] s_box = Init.s_box_generation(dkv);
        int[] inv_s = Init.inv_s_box(s_box);
        int[][] inv_G = Init.Inv_G(dkv);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    D[i][j] += (inv_G[i][k] * enc_matrix[k][j]);
                }
                D[i][j] = Math.floorMod(D[i][j], 256);
            }
        //  inv_sub
        int[] d_arr = MathUtils.matrix_to_array(D);
        for (int i = 0; i < 64; i++) {
            d_arr[i] = inv_s[d_arr[i]];
        }
        int[][] E = MathUtils.array_to_matrix(d_arr);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                enc_matrix[i][j] = Math.floorMod((byte) E[i][j] ^ im[i][j], 256);
            }
        // Inv sub
        return enc_matrix;
    }

    public static List<int[][]> encrypt_image(List<int[][]> blocks, byte[] dkv) throws Throwable {
        // first & second round
        int[][] im = Init.Im1(dkv);
        int[][] im_2 = Init.Im2(dkv);
        int size = blocks.size();
        List<int[][]> w = new ArrayList<>();
        List<int[][]> w_t = new ArrayList<>();
        List<int[][]> encrypted_image = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                w.add(encrypt_block(blocks.get(i), dkv, im));
            } else {
                w.add(encrypt_block(blocks.get(i - 1), dkv, blocks.get(i)));
            }
        }
        for (int[][] encrypted_block : w) {
            w_t.add(MathUtils.transpose(encrypted_block));
        }
        // second round encryption
        for (int i = size - 1; i >= 0; i--) {
            if (i == size - 1) {
                encrypted_image.add(encrypt_block(w_t.get(i), dkv, im_2));
            } else {
                encrypted_image.add(encrypt_block(w_t.get(i), dkv, w_t.get(i + 1)));
            }


        }
        return encrypted_image;
    }

    public static List<int[][]> decrypt_image(List<int[][]> encrypted_blocks, byte[] dkv) throws Throwable {
        int[][] im = Init.Im1(dkv);
        int[][] im_2 = Init.Im2(dkv);
        List<int[][]> decrypted_image = new ArrayList<>();
        List<int[][]> w = new ArrayList<>();
        List<int[][]> w_t = new ArrayList<>();

        for (int i = 0; i < encrypted_blocks.size(); i++) {
            if (i == 0) {
                w.add(decrypt_block(encrypted_blocks.get(i), dkv, im_2));
            } else {
                w.add(decrypt_block(encrypted_blocks.get(i), dkv, encrypted_blocks.get(i - 1)));
            }
        }

        var size = encrypted_blocks.size();
        for (int[][] encrypted_block : w) {
            w_t.add(MathUtils.transpose(encrypted_block));
        }
        for (int i = w_t.size() - 1; i >= 0; i--) {
            if (i == w_t.size() - 1) {
                decrypted_image.add((decrypt_block((w_t.get(i)), dkv, im)));
            } else {
                decrypted_image.add(decrypt_block((w_t.get(i)), dkv, w_t.get(i + 1)));
            }
        }
        return decrypted_image;
    }
}
