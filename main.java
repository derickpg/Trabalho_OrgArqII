
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws FileNotFoundException{
        Mapeamento m = new Mapeamento();
         
        
       /* int[][] cache = new int[2][4];
        cache[0][0] = 0;
        cache[1][0] = 1;
        cache[0][1] = 1;
        cache[0][2] = 8;
        cache[0][3] = 7;
        cache[1][1] = 0;
        cache[1][2] = 9;
        cache[1][3] = 5;
        m.printCache(cache, 2, 4);*/

       Leitor l = new Leitor();
     /*  int[] dados = l.lerLinhas("txt.txt");
        for (int i = 0; i < dados.length; i++) {
            System.out.println(dados[i]);
        }
     */
     int[] a = l.lerLinhas("txt.txt");
             m.direto(1024, 8, 4,a);
    }
}
