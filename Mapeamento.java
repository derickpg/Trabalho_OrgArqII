
public class Mapeamento {

    public void direto(int tamCache, int tamPalavra, int nPalavras, int[] ender) { //Mapeamento Direto
        int blocoBits = Integer.toBinaryString(nPalavras).length() - 1;// Bloco = 4 , blocobits = 2 (00,01,10,11)   
        int bloco = tamPalavra * nPalavras; // Tamanho em Bits do bloco
        int l = 2; // Numero de linhas
        int tag = 32 - blocoBits - 1; // Numero de Bits da tag
        int linha = bloco + 1; // Tamanho da Linha em bits
        int aux = 0; // Int Auxiliar para calculo do tamanho da cache
        int espacoUsado = 0; // Numero efetivo de espaco util da cache
        while (true) {
            aux = l * (linha + tag);
            if (aux > tamCache) { // Ver se esse tamanho da certo!
                l = l / 2; // Volta para o numero de linhas anterior
                tag = tag + 1; // Volta para o numero de bits de tag anterior
                break;
            }
            l = l * 2;      // Avança vezes 2 (pontecia de 2) o número de linhas
            tag = tag - 1; // Tira 1 bit da tag e adiciona para a linha
            espacoUsado = aux; //Maior espaço usado é atualizado
        }
        
        System.out.println("--------------------------------------------Informações Básicas----------------------------------------------------------------");
        System.out.println("Espaço efetivo usado -> " + espacoUsado + "bytes ,");
        System.out.println("A tag terá " + tag + " bits,");
        System.out.println("A linha terá " + (Integer.toBinaryString(l).length() - 1) + " bits, e teremos " + l + " linhas");
        System.out.println("e o bloco terá " + nPalavras + " palavras por bloco.");
        System.out.println("--------------------------------------------FIM---------------------------------------------------------------------------------");
        System.out.println("");
        
        String[][] cache = new String[l][nPalavras + 2]; // [Numero de Linhas][Tamanho da linha]
        cache = zeraCache(cache, l, nPalavras + 2); //Inicia a cache toda com Zeros
        int miss = 0; // Variavel que anota os miss's
        int hit = 0; // Variavel que anota os hit´s
        for (int i = 0; i < ender.length; i++) {
            String endereco = bits32(ender[i]); // Converto o endereço para o formato binario em 32bits
            int li = Integer.toBinaryString(l).length() - 1; // Tamnho da linha em bits 
           
            /*
            // ----------- DEBUG ----------------------------------------------
            System.out.println("[Bits da Linha] = " + (Integer.toBinaryString(l).length() - 1));
            System.out.println(li + " | " + endereco + " TAMANHO " + endereco.length() + "TAG " + tag);
            System.out.println("[TAG] = " + endereco.substring(0, tag));
            System.out.println(" [Linha] = " + endereco.substring(tag, tag + li) + " Linha Decimal " + Integer.parseInt(endereco.substring(tag, tag + li), 2));
            System.out.println(" [Posicao] = " + endereco.substring(tag + li, 32));
            // --------------- END DEBUG  ----------------------------------------------
            */
            
            
            if ((cache[Integer.parseInt(endereco.substring(tag, tag + li), 2)][0]).equalsIgnoreCase("1")) { // SE o BV == 1        
                System.out.println("HIT");
                if ((cache[Integer.parseInt(endereco.substring(tag, tag + li), 2)][1]).equalsIgnoreCase(endereco.substring(0, tag))) {//Se a tag for igual
                    //System.out.println("HIT TAG " + "  - " + (Integer.parseInt(endereco.substring(tag + li, 32), 2) + 2) + " sub " + endereco.substring(tag + li, 32));
                    int auxc = (Integer.parseInt(endereco.substring(tag + li, 32), 2) + 2); // Variavel auxiliar para guaradar a posicao do bloco que a palavra se encontra
                    if ((cache[Integer.parseInt(endereco.substring(tag, tag + li), 2)][auxc]).equalsIgnoreCase(String.valueOf(ender[i]))) {// ve no bloco se ele está
                        System.out.println("HIT EFETIVO");
                        printCache(cache, l, nPalavras+2);
                        //System.out.println("[Linha] " + Integer.parseInt(endereco.substring(tag, tag + li), 2) + " [Posição] " + Integer.parseInt(endereco.substring(tag + li, 32), 2));
                        hit++;
                    } else {
                        int enderAux = ender[i] - Integer.parseInt(endereco.substring(tag + li, 32), 2);
                        cache = copiaBloco(Integer.parseInt(endereco.substring(tag, tag + li), 2), enderAux, cache, endereco.substring(0, tag), nPalavras);
                        printCache(cache, l, nPalavras+2); // Metodo que printa a cache no final DEBUG
                    }
                } else {
                    int enderAux = ender[i] - Integer.parseInt(endereco.substring(tag + li, 32), 2);
                    cache = copiaBloco(Integer.parseInt(endereco.substring(tag, tag + li), 2), enderAux, cache, endereco.substring(0, tag), nPalavras);
                    printCache(cache, l, nPalavras+2); // Metodo que printa a cache no final DEBUG
                }
            } else {
                System.out.println("MISS");
                miss++;
                int enderAux = ender[i] - Integer.parseInt(endereco.substring(tag + li, 32), 2); //Anota a primeira palavra que deve estar presente no bloco
                cache = copiaBloco(Integer.parseInt(endereco.substring(tag, tag + li), 2), enderAux, cache, endereco.substring(0, tag), nPalavras);
                printCache(cache, l, nPalavras+2); // Metodo que printa a cache no final DEBUG
            }

            //System.out.println("------------------------------------------------ próximo endereço -----------------------------");
        }
        System.out.println("--------------------------------------------Informações Finais----------------------------------------------------------------");
        System.out.println("Foram ["+miss+"] MISSs, e ["+hit+"] Hits.");
        System.out.println("---------------------------------------------------FIM----------------------------------------------------------------");
    }

    //Método auxiliar onde realiza a cópia do bloco onde a palavra se encontra
    // Recebe como paramento a linha que ele vai copiar o bloco, o endereço que ele deve iniciar a copia do bloco
    // cache para ser atualizada, a tag para ser atualizada na cache, e a quantidade de palavras por bloco
    private String[][] copiaBloco(int linha, int enderAux, String[][] cache, String tag, int nPalavras) {
        cache[linha][0] = "1"; //Coloca o BV = 1
        cache[linha][1] = tag; //Colocas a TAG
        for (int j = 2; j < nPalavras + 2; j++) {
            cache[linha][j] = String.valueOf(enderAux);
            enderAux++;
        }
        
        //DEBUG
        //[][][10][]
        //1  2  3 4
        /*String debug = "|";
        for (int j = 0; j < nPalavras + 2; j++) {
            debug = debug + cache[linha][j] + "|";
        }*/
        //System.out.println("[LINHA] - " + linha + " [BV] - " + cache[linha][0]
        //        + " [TAG] - " + cache[linha][1] + " [PALAVRAS] - " + debug);
        return cache;
    }

    //Método auxiliar para iniciar a cache sempre com zero em todas as posições
    private String[][] zeraCache(String[][] cache, int l, int c) {
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                cache[i][j] = "0";
            }
        }
        return cache;
    }

    
    //Método auxiliar onde converte um número inteiro para número binario com 32bits
    private String bits32(int endereco) {
        String bit = Integer.toBinaryString(endereco);
        System.out.println("[Endereço Solicitado] = " + endereco + " [ele em bits] = " + bit);
        //System.out.println("[Tamanho do Endereço em bits]" + bit.length());
        int tam = 32 - bit.length();
        for (int i = 0; i < tam; i++) {
            bit = "0" + bit;
        }
        return bit;
    }

    public void printCache(String[][] cache, int l, int b) {
        System.out.println("              BV |TAG|Bloco ");
        for (int i = 0; i < l; i++) {
            String aux = "";
            for (int j = 0; j < b; j++) {
                aux = aux + " | " + cache[i][j];
            }
            System.out.println("Linha [" + i + "] ->" + aux);
        }
    }

    public void conjAssociativo() {

    }
}
