public class Mapeamento {
    public void direto(int tamCache, int tamPalavra, int nPalavras, int[] ender){ //Mapeamento Direto
        int blocoBits = Integer.toBinaryString(nPalavras).length()-1;// Bloco = 4 , blocobits = 2 (00,01,10,11)   
        int bloco = tamPalavra * nPalavras; // Tamanho em Bits do bloco
        int l = 2; // Numero de linhas
        int tag = 32 - blocoBits - 1; // Numero de Bits da tag
        int linha = bloco + 1; // Tamanho da Linha em bits
        int aux = 0; // Int Auxiliar para calculo do tamanho da cache
        int espacoUsado = 0; // Numero efetivo de espaco util da cache
        while(true){
            aux = l * (linha + tag);
            if(aux > tamCache){ // Ver se esse tamanho da certo!
                l = l / 2; // Volta para o numero de linhas anterior
                tag = tag + 1; // Volta para o numero de bits de tag anterior
                break;
            }            
            l = l * 2;
            tag = tag - 1;
            espacoUsado = aux;
        }
        System.out.println("Espaço efetivo usado -> " + espacoUsado + " TAG = " + tag + "Bloco " + linha + "numero de linhas " + l);
        int[][] cache = new int[l][1+tag+blocoBits]; // [Numero de Linhas][Tamanho da linha]
        // Agora tem que carregar a matriz com os dados da leitura
        int miss = 0;
        int hit = 0;
        for (int i = 0; i < ender.length; i++) {
           String endereco = bits32(ender[i]);
           int li = Integer.toBinaryString(l).length()-1;
           System.out.println("[Bits da Linha] = " + (Integer.toBinaryString(l).length()-1));
           System.out.println(li + " | " + endereco + " TAMANHO " + endereco.length() + "TAG " + tag);
           System.out.println("[TAG] = " + endereco.substring(0 , tag)); 
           System.out.println(" [Linha] = " + endereco.substring(tag, tag + li) + " Linha Decimal " + Integer.parseInt(endereco.substring(tag, tag + li),2));
           System.out.println(" [Posicao] = " + endereco.substring(tag + li, 32));
           System.out.println("------------------------------------------------"  + Integer.valueOf(endereco.substring(tag, tag + li)));
           if(cache[Integer.parseInt(endereco.substring(tag, tag + li),2)][0] == 1){ // SE o BV == 1        
               System.out.println("HIT");
           }else{
               System.out.println("MISS");
           }
        }
    }

    
    
    private String bits32(int endereco){
       String bit = Integer.toBinaryString(endereco);
       System.out.println("[Endereço Solicitado] = " + endereco + " [ele em bits] = " + bit);
       System.out.println("[Tamanho do Endereço em bits]" + bit.length());
       int tam = 32 - bit.length();
       for (int i = 0; i < tam; i++) {
            bit = "0"+bit;
       }
       return bit;
    }
    
    public void printCache(int[][] cache, int l, int b){
        System.out.println("              BV |TAG|Bloco ");
        for(int i = 0; i < l; i++){
            String aux = "";
            for (int j = 0; j < b; j++) {
                aux = aux + " | " + cache[i][j];
            }
            System.out.println("Linha [" + i + "] ->" + aux);
        }
    }
    
    

    public void conjAssociativo(){
        
    }
}