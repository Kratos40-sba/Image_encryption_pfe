import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Init {

    public static byte[] Dkv(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] dkv = messageDigest.digest(key);
        for (int i = 0; i < dkv.length; i++) {
            dkv[i] = (byte) Math.floorMod(dkv[i], 256);
        }
        return dkv;
    }

    public static int[][] Im1(byte[] dkv) throws Throwable {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] O1 = messageDigest.digest(dkv);

        for (int i = 0; i < O1.length; i++) {
            O1[i] = (byte) ((byte) (Math.floorMod(O1[i], 256)));
        }

        return MathUtils.array_to_matrix_byte(O1);
    }

    public static int[][] Im2(byte[] dkv) throws Throwable {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] O1 = messageDigest.digest(dkv);
        byte[] O2 = messageDigest.digest(O1);

        for (int i = 0; i < O2.length; i++) {
            O2[i] = (byte) ((byte) (Math.floorMod(O2[i], 256)));
        }
        return MathUtils.array_to_matrix_byte(O2);
    }

    public static int[][] G(byte[] dkv) {
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
        int[][] IdM = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        int[][] G = new int[8][8];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                G[i][j] = M[i][j];
            }
        for (int i = 4, i2 = 0; i < 8; i++, i2++)
            for (int j = 4, j2 = 0; j < 8; j++, j2++) {
                G[i][j] = M[i2][j2];
            }
        for (int i = 0; i < 4; i++)
            for (int j = 4, j2 = 0; j < 8; j++, j2++) {
                G[i][j] = M[i][j2] + IdM[i][j2];
            }
        for (int i = 4, i2 = 0; i < 8; i++, i2++)
            for (int j = 0; j < 4; j++) {
                G[i][j] = M[i2][j] - IdM[i2][j];
            }
        return G;
    }

    public static int[][] Inv_G(byte[] dkv) {
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


        int[][] IdM = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        int[][] G = new int[8][8];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {

                G[i][j] = M[i][j];

            }
        for (int i = 4, i2 = 0; i < 8; i++, i2++)
            for (int j = 4, j2 = 0; j < 8; j++, j2++) {
                G[i][j] = M[i2][j2];
            }
        for (int i = 0; i < 4; i++)
            for (int j = 4, j2 = 0; j < 8; j++, j2++) {
                G[i][j] = ((-1 * M[i][j2] - IdM[i][j2]));
            }
        for (int i = 4, i2 = 0; i < 8; i++, i2++)
            for (int j = 0; j < 4; j++) {
                G[i][j] = (-1 * M[i2][j] + IdM[i2][j]);

            }
        return G;
    }

    public static int[] s_box_generation(byte[] dkv) {
        int[] ks = new int[8];
        int[] r = new int[4];
        int[] t = new int[4];
        int[][] temp = MathUtils.array_to_matrix_byte(dkv);
        int[] v = new int[256];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ks[i] = Math.floorMod((byte) temp[i][j] ^ ks[i], 256);
            }
        }
        int ir = 0;
        int it = 0;
        for (int k : ks) {
            if (k % 2 == 0) {
                r[ir] = k;
                // change the lsb to 0
                r[ir] = r[ir] ^ (r[ir] & 1);
                ir++;
            } else {
                t[it] = k;
                // change lsb to 1
                t[it] |= 1;
                it++;
            }
        }
        for (int i = 0; i < 256; i++) {
            v[i] = i;
        }
        for (int j = 1; j < 256; j++) {
            for (int i = 0; i < 4; i++) {
                v[j] = Math.floorMod((v[j] * ((v[j - 1] * r[i]) + t[i])), 256);
            }

        }

        return v;


    }

    public static int[] inv_s_box(int[] s_box) {
        int[] inv_s_box = new int[256];
        for (int i = 0; i < 256; i++) {
            inv_s_box[s_box[i]] = i;
        }
        return inv_s_box;
    }
}
