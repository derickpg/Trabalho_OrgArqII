
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class main {

    private static int menu() {
        Object[] options = {"Mapeamento Direto", "Mapeamento Conjunto Associativo"};
        int n = JOptionPane.showOptionDialog(null,
                "Qual tipo de Mapeamento você deseja usar?",
                "Mapeamento de Cache", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return n;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Mapeamento m = new Mapeamento();
        Leitor l = new Leitor();

        // *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-* MENU *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*
        JOptionPane.showMessageDialog(null, "<><><><><><><><><><><><><><><><><><><><><> Mapeamento de Cache <><><><><><><><><><><><><><><><><><><><><><><> \n"
                + " Autores: Derick Garcez e Vinicius Dos Santos \n"
                + " Disciplina de Organização e Arquitetura de Computadores II,  \n"
                + " Ministrada pelo Dr. Prof. Edson Moreno. \n"
                + " Clique [OK] para entrar no programa.");
        String nomeTxt = JOptionPane.showInputDialog("Qual o nome do arquivo de leitura? (Exemplo de arquivo: txt.txt)", "txt.txt");
        if (nomeTxt == null) {
            System.exit(0); // Se não digitar nada ou fechar a janela fecha o programa.
        }
        int[] a = l.lerLinhas(nomeTxt);
        int opcao = 3;
        while (true) {
            opcao = menu();
            switch (opcao) {
                case 0: { //Mapeamento Direto
                    String tamCache = JOptionPane.showInputDialog("Qual o Tamanho da cache? ");
                    if (tamCache == null || tamCache.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    String tamPalavra = JOptionPane.showInputDialog("Qual o Tamanho da Palavra? ");
                    if (tamPalavra == null || tamPalavra.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    String tamBloco = JOptionPane.showInputDialog("Qual a quantidade de Palavras por bloco da cache?");
                    if (tamBloco == null || tamBloco.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    m.direto(Integer.parseInt(tamCache), Integer.parseInt(tamPalavra), Integer.parseInt(tamBloco), a);
                    JOptionPane.showMessageDialog(null, "SIMULAÇÃO CONCLUÍDA \n"
                            + "Saída da simulação via terminal.");
                    break;
                }
                case 1: { //Mapeamento Conj. Associativo
                    String tamCache = JOptionPane.showInputDialog("Qual o Tamanho da cache? ");
                    if (tamCache == null || tamCache.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    String tamPalavra = JOptionPane.showInputDialog("Qual o Tamanho da Palavra? ");
                    if (tamPalavra == null || tamPalavra.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    String tamBloco = JOptionPane.showInputDialog("Qual a quantidade de Palavras por bloco da cache?");
                    if (tamBloco == null || tamBloco.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    String numConj = JOptionPane.showInputDialog("Qual a quantidade de conjuntos?");
                    if (numConj == null || numConj.compareToIgnoreCase("") == 0) {
                        break;
                    }
                    Object[] options = {"Random", "LRU"};
                    int tipoBressan = JOptionPane.showOptionDialog(null,
                            "Qual a Política de Troca você deseja usar?",
                            "Política de Substitição", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    Boolean bressan = false;
                    if (tipoBressan == 0) {
                        bressan = true; //Random
                    } else {
                        bressan = false; // LRU
                    }
                    m.associativo(Integer.parseInt(tamCache), Integer.parseInt(tamPalavra), Integer.parseInt(tamBloco), Integer.parseInt(numConj), bressan, a);
                    JOptionPane.showMessageDialog(null, "SIMULAÇÃO CONCLUÍDA \n"
                            + "Saída da simulação via terminal.");
                    break;
                }
                default:
                    System.exit(0);

            }
        }
    }
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-* FIM DO MENU *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*

//*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-* AREA DE DEBUG *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*
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
// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-* FIM DO DEBUG *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*    
}
