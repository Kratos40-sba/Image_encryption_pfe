public class Encryption {
    public static int[][] enc(int[][] image_sub_bloc, byte[] dkv) throws Throwable {

         int[][] im = Init.Im1(dkv);
        int[][] y  = new int[8][8];
        for(int i =0 ; i<8 ; i++)
            for (int j =0 ; j <8 ; j++) {
                y[i][j] = Math.floorMod((byte) image_sub_bloc[i][j] ^ im[i][j],256) ;
            }
        // multiplication
        int[][] g = Init.G(dkv);
        int[][] encrypted_image = new int[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    encrypted_image[i][j] += g[i][k] * y[k][j];
                }
                encrypted_image[i][j] = (Math.floorMod(encrypted_image[i][j], 256));
            }
        /*
                int []y_vec = MathUtils.matrix_to_array(y);
        // z = apply_s_box(S-box , y)
        HashMap<Integer,Integer> Z_map = new HashMap<>();
        for(int i = 0 ; i< y_vec.length ; i++){
            Z_map.put(y_vec[i],Init.s_box[Math.floorMod(y_vec[i],64)]);
        }
        int[] Z_array = new int[64];
        int ss =0 ;
        for(int k : Z_map.keySet()){
            Z_array[ss] = Z_map.get(k);
            ss++;
        };
        int[][] z = MathUtils.array_to_matrix(Z_array);
         */
        return encrypted_image;
    }

    public static int[][] dec(int[][] enc_matrix, byte[] dkv) throws Throwable {
        int[][] D = new int[8][8];
        int[][] inv_G = Init.Inv_G(dkv);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    D[i][j] += (inv_G[i][k] * enc_matrix[k][j]);
                }
                D[i][j] = Math.floorMod((byte) D[i][j], 256);
            }
              int[][] im = Init.Im1(dkv);
        for(int i =0 ; i<8 ; i++)
            for (int j =0 ; j <8 ; j++) {
                enc_matrix[i][j] = Math.floorMod((byte)D[i][j] ^ im[i][j]  ,256) ;
            }
        // Inv sub
        //int[][] decrypted_image = new int[8][8];
        return enc_matrix;
    }

}
