
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Leitor {
    
    Scanner arq;
    ArrayList<String> docA = new ArrayList<String>();
    private ArrayList<String> leArquivo(String txt) throws FileNotFoundException{
        Scanner arq = new Scanner(new FileReader(txt));
        ArrayList<String> linha = new ArrayList<String>();
        while (arq.hasNextLine()){
            linha.add(arq.nextLine());
        }
        return linha;
    }
    
    public int[] lerLinhas(String txt) throws FileNotFoundException{
        ArrayList<String> l = leArquivo(txt);
        int[] retorno = new int[l.size()];
        for (int i = 0; i < l.size(); i++) {
            retorno[i] = Integer.parseInt(l.get(i));
        }
        return retorno;
    }
}
