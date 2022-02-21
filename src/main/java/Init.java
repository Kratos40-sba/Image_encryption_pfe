import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Init {
    public static int[] s_box = {
            144, 25, 58, 33, 69, 50, 102, 200,
            222, 250, 95, 133, 169, 150, 2, 208,
            3, 28, 147, 110, 206, 101, 173, 244,
            44, 25, 159, 37, 18, 29, 147, 110,
            22, 65, 199, 207, 47, 27, 154, 122,
            9, 5, 180, 211, 39, 119, 54, 141,
            24, 45, 158, 11, 92, 211, 4, 193,
            14, 31, 76, 7, 19, 251, 1, 209,
    };

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
}
