
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


  // Para o mapeamento conjunto associativo, deve-se poder informar o tamanho em
  // bytes da cache, o número de palavras por bloco, o tamanho da o tamanho da palavra.
  public void associativo(int tamCache, int tamPalavra, int nPalavras, int nWays, int[] entrada){
    // endereco = [  tag  |  bitC  |  bitP  ]
    // - Tamanho = num bits necessarios para representar o tamCache
    //      ex: 1 GigaByte  = 30 bits, 8KB = 13 bits, 256MB = 28 bits
    int endereco = Integer.toBinaryString(tamCache).length()-1;

    // Numero de linhas = tamCache (em Bytes) / (tamPalavra*nPalavras / 8)
    int nLinhas = tamCache / ((tamPalavra*nPalavras) / 8);
    int nConjuntos = nLinhas / nWays; //conjuntos = nWays linhas por conjunto

    // - Conteudo = TAG + bitC + bitP (num bits necessarios para representar nPalavras e nConjuntos)
    int bitP = Integer.toBinaryString(nPalavras).length()-1; //ex palavras = 4 , bitP = 2 (00,01,10,11)
    int bitC = Integer.toBinaryString(nConjuntos).length()-1; //ex 32 conjuntos, bitC = 5
    int tag = endereco - bitP - bitC; // Numero de Bits da tag = endereco - bitPalavras - bitConjuntos

    // bloco (de palavras) = tamPalavra x nPalavras
    int bloco = tamPalavra * nPalavras; // Tamanho em Bits do bloco
    // linha = DV (1 bit) + bloco
    int linha = bloco + 1; // Tamanho da Linha em bits

    // int memAssociativa = nLinhas/nConjuntos * tag;
    int tamConjunto = nLinhas/nConjuntos;

    int[][] conjAssociativo = new int[nConjuntos][tamConjunto];
    int[][] cache = new int[nLinhas][1+nPalavras];
    // ex: 1024 linhas, 4 conjuntos, 8 palavras
    // conjAssociativo[4][256]
    // cache[1024][9]
    System.out.println("-------------------------");
    System.out.println("Numero de linhas      : " + nLinhas);
    System.out.println("Numero de vias        : " + nWays);
    System.out.println("Conjuntos associativos: " + nConjuntos);
    System.out.println("-------------------------");

    // variaveis auxiliares
    int miss = 0, hit = 0;
    int entT, entC, entP; // valor de entrada Tag, Conj, Palavra
    int conjToCache; // calcula a linha da cache a partir do conjunto

    // Deverá ser disponibilizado ao menos dois algoritmos a serem escolhidos como
    // política de substituição (LRU, LFU, Relógio, randômico, sequencial, ...).

    // entrada 3149 = 110001001101 (ex: 8 palavras, 4 conjuntos)
    // | 1100010 |   01 |  101 |
    // |    entT | entC | entP |
    for (int i = 0; i < entrada.length; i++) {
      entP = entrada[i] & nPalavras-1;
      entC = (entrada[i] >> bitP) & nConjuntos-1;
      entT = (entrada[i] >> (bitC + bitP) & 0xFFFFFFFF);
System.out.println("DEBUG> (" + entrada[i] + ") " + Integer.toBinaryString(entrada[i]) + " |" + Integer.toBinaryString(entT) + "|" + Integer.toBinaryString(entC) + "|" + Integer.toBinaryString(entP) + "|");
      for (int j = 0; j < tamConjunto; j++) {
        //verifica se a tag j, presente no conjAssociativo entC é igual a entT
        if(conjAssociativo[entC][j] == entT){
          //calcula a linha na memoria cache, EX: cache de 1024 linhas, 4 conj
          //  no conjunto entC=2 (0-3), indice j=3 (0-255)
          //  na memoria cache sera na linha 3 + 256*(2+1) = 3+768 = 771
          conjToCache = j + tamConjunto*(entC+1);
          //trabalha na memoria
          if(cache[conjToCache][0] == 0) {
System.out.println("DEBUG> deu merda, existe a tag mas nao tem DV");
          } else /*hit?*/ {
            // busca
            if(entrada[i] == cache[conjToCache][entP]) {
System.out.println("DEBUG> acerto mizeravi");
System.out.println("Encontrei o valor " + entrada[i] + " na linha " + conjToCache + " posicao " + entP + " do bloco da cache. Cheguei aqui a partir da linha " + j + " do conjunto " + entC);
            }
          }

          break; //deu hit para o for
        }
      } //end for
      //chama o metodo de substituição
    }

    // Como resultado desta escolha, deve-se informar o número de conjuntos
    // associativos e o formato de interpretação do endereço (tag, conjunto, bloco).

    System.out.println("-------------------------");
    System.out.println("Numero de linhas      : " + nLinhas);
    System.out.println("Numero de vias        : " + nWays);
    System.out.println("Conjuntos associativos: " + nConjuntos);
    System.out.println("-------------------------");

  }
}
