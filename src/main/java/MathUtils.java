public class MathUtils {
    public static int[][] array_to_matrix(int[] dkv_array) {
        int[][] dkv = new int[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                dkv[i][j] = dkv_array[(j * 8) + i];
        return dkv;
    }

    public static int[][] array_to_matrix_byte(byte[] dkv_array) {
        int[][] dkv = new int[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                dkv[i][j] = dkv_array[(j * 8) + i];
        return dkv;
    }

    public static int[] matrix_to_array(int[][] mat) {
        int[] vec = new int[mat.length * mat[0].length];
        int s = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                vec[s] = mat[i][j];
                s++;
            }
        return vec;

    }

    public static void print(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "  |  ");
            }
            System.out.println();
        }
    }


    // Xor + matrix multiplication + matrix in java
}
