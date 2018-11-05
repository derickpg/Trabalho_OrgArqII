
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
             //m.direto(1024, 8, 4,a);
             // m.direto(2048, 8, 4,a);

      //tamCache, tamPalav, nPalav, nWay, entrada
      m.associativo(2048, 8, 4, 4, a);

      int bin = 4096;
      System.out.print("entrada "+ bin);
      System.out.println(" ("+ Integer.toBinaryString(bin) + ")");
      int pl = 8;
      int cj = 4;
      int bitPl = Integer.toBinaryString(pl).length() - 1;
      int bitCj = Integer.toBinaryString(cj).length() - 1;

//bin é a entrada em inteiro, da onde vai tirar a tag, linha/conjunto, palavra
//CALCULO PARA ARMAZENAR OS BITS DA PALAVRA bin & pl-1
//a ideia aqui eh usar uma mascara para obter somente os bits da palavra 00000000011111
      System.out.print("Palv " + Integer.toBinaryString(bin & pl-1));
      System.out.println(" (" + (bin & pl-1) + ")");

//CALCULO PARA ARMAZENAR OS BITS DO CONJUNTO (OU DA LINHA) (bin >> bitPl) & cj-1
// a ideia aqui eh deslocar >> o numero de bits da palavra e usar mascara 0000000011111
      System.out.print("Conj " + Integer.toBinaryString((bin >> bitPl) & cj-1));
      System.out.println(" ("+ ((bin >> bitPl) & cj-1) + ")");

//CALCULO PARA ARMAZENAR OS BITS DA TAG (bin >> (bitCj + bitPl)) & 0xFFFFFFFF
//ideia é deslocar >> o numero de bits que nao pertenca a tag (bits de pl + cj ou linha)
// a mascara aqui e completa 111111111111111111111111 pq sobrou apenas a tag, entao queremos todos valores
      System.out.print("Tag? " + Integer.toBinaryString((bin >> (bitCj + bitPl)) & 0xFFFFFFFF));
      System.out.println(" (" + ((bin >> (bitCj + bitPl)) & 0xFFFFFFFF) + ")");



      // System.out.println(Integer.toBinaryString((cj << bitPl) + pl-1));
      // System.out.println((cj << bitPl) -1);
    }
}
