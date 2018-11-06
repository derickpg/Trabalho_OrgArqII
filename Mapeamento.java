
import java.util.Random;


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

        int[][] cache = new int[l][nPalavras + 2]; // [Numero de Linhas][Tamanho da linha]
        cache = zeraCache(cache, l, nPalavras + 2); //Inicia a cache toda com Zeros
        int miss = 0; // Variavel que anota os miss's
        int hit = 0; // Variavel que anota os hit´s
        for (int i = 0; i < ender.length; i++) {
            String endereco = bits32(ender[i]); // Converto o endereço para o formato binario em 32bits
            int li = Integer.toBinaryString(l).length() - 1; // Tamnho da linha em bits
            int tagEnder = Integer.parseInt(endereco.substring(0, tag), 2);
            int linhaEnder = Integer.parseInt(endereco.substring(tag, tag + li), 2);

            if ((cache[linhaEnder][0]) == 1) { // SE o BV == 1
                System.out.println("HIT - TAG = " + cache[linhaEnder][1] + "| tagEnder = " + tagEnder + "| linha = " + linhaEnder);
                if ((cache[linhaEnder][1]) == tagEnder) {//Se a tag for igual
                    //System.out.println("HIT TAG " + "  - " + (Integer.parseInt(endereco.substring(tag + li, 32), 2) + 2) + " sub " + endereco.substring(tag + li, 32));
                    int auxc = (Integer.parseInt(endereco.substring(tag + li, 32), 2) + 2); // Variavel auxiliar para guaradar a posicao do bloco que a palavra se encontra
                    if ((cache[linhaEnder][auxc]) == ender[i]) {// ve no bloco se ele está
                        System.out.println("HIT EFETIVO");
                        printCache(cache, l, nPalavras+2);
                        //System.out.println("[Linha] " + Integer.parseInt(endereco.substring(tag, tag + li), 2) + " [Posição] " + Integer.parseInt(endereco.substring(tag + li, 32), 2));
                        hit++;
                    } else {
                        System.out.println("HITMISS");
                        miss++;
                        int enderAux = ender[i] - Integer.parseInt(endereco.substring(tag + li, 32), 2);
                        cache = copiaBloco(Integer.parseInt(endereco.substring(tag, tag + li), 2), enderAux, cache, tagEnder, nPalavras);
                        printCache(cache, l, nPalavras+2); // Metodo que printa a cache no final DEBUG
                    }
                } else {
                    System.out.println("HITMISS-2");
                    miss++;
                    int enderAux = ender[i] - Integer.parseInt(endereco.substring(tag + li, 32), 2);
                    cache = copiaBloco(Integer.parseInt(endereco.substring(tag, tag + li), 2), enderAux, cache, tagEnder, nPalavras);
                    printCache(cache, l, nPalavras+2); // Metodo que printa a cache no final DEBUG
                }
            } else {
                System.out.println("HIT - TAG = " + cache[linhaEnder][1] + "| tagEnder = " + tagEnder + "| linha = " + linhaEnder);
                System.out.println("MISS");
                miss++;
                int enderAux = ender[i] - Integer.parseInt(endereco.substring(tag + li, 32), 2); //Anota a primeira palavra que deve estar presente no bloco
                cache = copiaBloco(Integer.parseInt(endereco.substring(tag, tag + li), 2), enderAux, cache, tagEnder, nPalavras);
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
    private int[][] copiaBloco(int linha, int enderAux, int[][] cache,int tag, int nPalavras) {
        cache[linha][0] = 1; //Coloca o BV = 1
        cache[linha][1] = tag; //Colocas a TAG
        for (int j = 2; j < nPalavras + 2; j++) {
            cache[linha][j] = enderAux;
            enderAux++;
        }
        return cache;
    }

    //Método auxiliar para iniciar a cache sempre com zero em todas as posições
    private int[][] zeraCache(int[][] cache, int l, int c) {
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                cache[i][j] = 0;
            }
        }
        return cache;
    }


    //Método auxiliar onde converte um número inteiro para número binario com 32bits
    private String bits32(int endereco) {
        String bit = Integer.toBinaryString(endereco);
        //System.out.println("[Tamanho do Endereço em bits]" + bit.length());
        int tam = 32 - bit.length();
        for (int i = 0; i < tam; i++) {
            bit = "0" + bit;
        }
        System.out.println("[Endereço Solicitado] = " + endereco + " [ele em bits] = " + bit);
        return bit;
    }

    public void printCache(int[][] cache, int l, int b) {
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
  public void associativo(int tamCache, int tamPalavra, int nPalavras, int nConjuntos, boolean tipoSubs, int[] entrada){
    // endereco = [  tag  |  bitC  |  bitP  ]
    // - Tamanho = num bits necessarios para representar o tamCache
    //      ex: 1 GigaByte  = 30 bits, 8KB = 13 bits, 256MB = 28 bits
    int endereco = Integer.toBinaryString(tamCache).length()-1;

    // Numero de linhas = tamCache (em Bytes) / (tamPalavra*nPalavras / 8)
    int nLinhas = tamCache / ((tamPalavra*nPalavras) / 8);
    // int nConjuntos = nLinhas / nWays; //conjuntos = nWays linhas por conjunto

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

    int controleLRU = 0;
    int[][] lru = new int[nConjuntos][tamConjunto];

    // variaveis auxiliares
    int miss = 0, hit = 0;
    int aux, indice;
    int entT, entC, entP; // valor de entrada Tag, Conj, Palavra
    int conjToCache; // calcula a linha da cache a partir do conjunto
    // Deverá ser disponibilizado ao menos dois algoritmos a serem escolhidos como
    // política de substituição (LRU, LFU, Relógio, randômico, sequencial, ...).
    boolean substitui = true;
    // entrada 3149 = 110001001101 (ex: 8 palavras, 4 conjuntos)
    // | 1100010 |   01 |  101 |
    // |    entT | entC | entP |
    for (int i = 0; i < entrada.length; i++) {
      controleLRU++;
      substitui = true;
      entP = entrada[i] & nPalavras-1;
      entC = (entrada[i] >> bitP) & nConjuntos-1;
      entT = (entrada[i] >> (bitC + bitP) & 0xFFFFFFFF);
System.out.println("DEBUG> entrada (" + entrada[i] + ") " + Integer.toBinaryString(entrada[i]) + " |" + Integer.toBinaryString(entT) + "|" + Integer.toBinaryString(entC) + "|" + Integer.toBinaryString(entP) + "| -> |" +entT + "|" + entC + "|" + entP + "|");
      for (int j = 0; j < tamConjunto; j++) {
        //calcula a linha na memoria cache, EX: cache de 1024 linhas, 4 conj
        //  no conjunto entC=2 (0-3), indice j=3 (0-255)
        //  na memoria cache sera na linha 3 + 256*(2+1) = 3+768 = 771
        conjToCache = j + tamConjunto*(entC);

        //verifica se a tag j, presente no conjAssociativo entC é igual a entT
// System.out.println("DEBUG> conjAssociativo[entC][j]=" + conjAssociativo[entC][j] + " == " + entT);
        if(conjAssociativo[entC][j] == entT){
          substitui = false; //achou entao coloca substitui como false
// System.out.println("DEBUG> .conj = " + entC + " , indice = " + j + " , conjToCache = " + conjToCache);
          // se achou a tag mas na cache o bloco ainda nao foi copiado
          // (pode acontecer caso a tag da entrada seja = 0)
          // vou sinalizar miss e copiar o bloco de forma simples
          if(cache[conjToCache][0] == 0) {
            miss++;
            lru[entC][j] = controleLRU; //marca uso
            aux = entrada[i] - entP;
            conjAssociativo[entC][j] = entT;
            cache[conjToCache][0] = 1;
  System.out.print("MISS> nova linha[" + conjToCache + "] > ");
            for (int k = 0; k < nPalavras; k++) {
              cache[conjToCache][k+1] = aux+k;
System.out.print(cache[conjToCache][k+1] + " | ");
            }
System.out.println(" (existe tag mas nao tem DV)");
          } else /*hit?*/ {
            // achou a informacao sinaliza hit
            hit++;
            lru[entC][j] = controleLRU; //marca uso
            if(entrada[i] == cache[conjToCache][entP+1]) {
// System.out.println("DEBUG> acerto mizeravi");
System.out.println("HIT> Encontrei o valor " + entrada[i] + " na linha " + conjToCache + " posicao " + entP + " do bloco da cache. Cheguei aqui a partir da linha " + j + " do conjunto " + entC);

            }
          }
          break; //deu hit, para o for do conjunto
        } // end if == entT
        //se estou lendo valores zerados do conjunto, entao vou sinalizar miss
        // copiar o bloco de forma simples e cair fora do for do conj
        if(conjAssociativo[entC][j] == 0 && cache[conjToCache][0] == 0) {
          substitui = false; //achou entao coloca substitui como false
// System.out.println("j " + j);
// System.out.println("entC " + entC);
          miss++;
          lru[entC][j] = controleLRU; //marca uso
          aux = entrada[i] - entP;
          conjAssociativo[entC][j] = entT;
          cache[conjToCache][0] = 1;
System.out.print("MISS> nova linha[" + conjToCache + "] > ");
          for (int k = 0; k < nPalavras; k++) {
            cache[conjToCache][k+1] = aux+k;
System.out.print(cache[conjToCache][k+1] + " | ");
          }
System.out.println();
          break;
        }
      } //end for do conj associativo
      //percorri todo FOR, verifico se precisa aplicar metodo de substituicao
      if(substitui == true) {
        //chama o metodo de substituição
        // true random, false LRU?
System.out.print("DEBUG> SUBSTITUICAO, CHAMA O BRESSAN :-)~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
        if(tipoSubs) /* TRUE, politica randomico */ {
          indice = new Random().nextInt(tamConjunto);
System.out.println("RAND >>>>> " + indice + " + " + (entC*tamConjunto) + " = " + (indice + (entC*tamConjunto)));
        } else /* FALSE, politica = LRU */ {
          //percorrer o array LRU (igual ao conjunto, porem esse armazena o ultimo usado)
          //comparando o valor armazenado (busca do menor), guarda o indice
          aux = lru[entC][0];
          indice = 0;
System.out.print(lru[entC][0] + " | ");
          for (int k = 1; k < tamConjunto; k++)
            if (aux > lru[entC][k]) {
System.out.print(lru[entC][k] + " | ");
              aux = lru[entC][k];
              indice = k;
            } else
System.out.print(lru[entC][k] + " | ");

System.out.println(" --- LRU  >>>>> " + indice + " + " + (entC*tamConjunto) + " = " + (indice + (entC*tamConjunto)));
        } //end else

        conjToCache = indice + tamConjunto*(entC);

        miss++;
        lru[entC][indice] = controleLRU; //marca uso
        aux = entrada[i] - entP;
        conjAssociativo[entC][indice] = entT;
        cache[conjToCache][0] = 1;

System.out.print("MISS> nova linha[" + conjToCache + "] > ");
        for (int k = 0; k < nPalavras; k++) {
          cache[conjToCache][k+1] = aux+k;
System.out.print(cache[conjToCache][k+1] + " | ");
        }
System.out.println();

      }
    } //end for da entrada

    // Como resultado desta escolha, deve-se informar o número de conjuntos
    // associativos e o formato de interpretação do endereço (tag, conjunto, bloco).

    System.out.println("---------------------------");
    System.out.println("Numero de linhas      : " + nLinhas);
    // System.out.println("Numero de vias        : " + nWays);
    System.out.println("Conjuntos associativos: " + nConjuntos);
    System.out.println("Tamanho do conjunto   : " + tamConjunto);
    System.out.println("Total de Hits         : " + hit);
    System.out.println("Total de Miss         : " + miss);
    System.out.println("---------------------------");

for (int i = 0; i < nLinhas; i++) {
  for (int j = 0; j < nPalavras+1; j++) {
    System.out.print(cache[i][j] + " | ");
  }
System.out.println();
}

  }
}
