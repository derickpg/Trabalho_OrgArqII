
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws FileNotFoundException{
        Mapeamento m = new Mapeamento();

       Leitor l = new Leitor();
       int[] a = l.lerLinhas("txt1.txt");
       
       
             //m.direto(1024, 8, 4,a);
             // m.direto(2048, 8, 4,a);
     //m.direto(1024, 8, 4,a); 
 /*     //tamCache, tamPalav, nPalav, nWay, entrada
      m.associativo(512, 16, 8, 4, true, a);

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
*/
    }
}
