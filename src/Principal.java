import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Principal {
    Arquivo arqOrd, arqRev, arqRand, auxRev, auxRand;

    public Principal() {
        arqOrd = new Arquivo();
        arqRev = new Arquivo();
        arqRand = new Arquivo();
        auxRev = new Arquivo();
        auxRand = new Arquivo();
    }

    public void geraTabela(){
        arqOrd.geraArquivoOrdenado();
        arqRev.geraArquivoReverso();
        arqRand.geraArquivoRandomico();

        String nomeArquivo = "tabela_eficiencia.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            //Cabeçalho
            writer.write("|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|\n");
            writer.write("| Métodos de Ordenação  |                               Arquivo Ordenado                                |                             Arquivo em Ordem Reversa                           |                               Arquivo Randômico                               |\n");
            writer.write("| ----------------------|-------------------------------------------------------------------------------|--------------------------------------------------------------------------------|-------------------------------------------------------------------------------|\n");
            writer.write("| Nomes dos Métodos     | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo      | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo       | Comp. Prog. * | Comp. Equa. # | Mov. Prog. + | Mov. Equa. - |      Tempo      |\n");
            writer.write("|-----------------------|---------------|---------------|--------------|--------------|-----------------|---------------|---------------|--------------|--------------|------------------|---------------|---------------|--------------|--------------|-----------------|\n");

            //Inserção Direta
            System.out.println("1");
            StringBuilder line = new StringBuilder("| Inserção Direta       | ");
            arqOrd.initComp();
            arqOrd.initMov();
            long tini = System.currentTimeMillis();
            arqOrd.insercaoDireta();
            long tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,arqOrd.filesize()-1, 3*(arqOrd.filesize()-1));

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.insercaoDireta();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 2)/4, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 9 - 10)/4 );

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.insercaoDireta();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,(int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 4)/4, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4)/4 );

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Inserção Binária
            System.out.println("2");
            line = new StringBuilder("| Inserção Binária      | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.insercaoBinaria();
            tfim = System.currentTimeMillis();
            int comp = (int)(arqOrd.filesize() * (Math.log(arqOrd.filesize()) - Math.log(Math.E) + 0.5));
            append1(line, (tfim - tini)/1000, arqOrd,comp, 3*(arqOrd.filesize()-1));

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.insercaoBinaria();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, comp, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4)/4);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.insercaoBinaria();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,comp, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4)/4);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Seleção Direta
            System.out.println("3");
            line = new StringBuilder("| Seleção Direta        | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.selecaoDireta();
            tfim = System.currentTimeMillis();
            comp = (int) (Math.pow(arqOrd.filesize(),2)-arqOrd.filesize())/2;
            append1(line, (tfim - tini)/1000, arqOrd,comp, 3*(arqOrd.filesize()-1));

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.selecaoDireta();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, comp, (int) (arqRev.filesize()*(Math.log(arqRev.filesize()) * 0.577216)));

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.selecaoDireta();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,comp, (int) Math.pow(arqRand.filesize(),2)/4 + 3*(arqRand.filesize()-1));

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Bolha
            System.out.println("4");
            line = new StringBuilder("| Bolha                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.bubleSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,comp, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.bubleSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, comp, (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.bubleSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,comp, (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Shake
            System.out.println("5");
            line = new StringBuilder("| Shake                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.shakeSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,comp, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.shakeSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, comp, (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.shakeSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,comp, (int) (3 * (Math.pow(arqRev.filesize(), 2) - arqRev.filesize()) / 4));

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Shell
            System.out.println("6");
            line = new StringBuilder("| Shell                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.shellSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.shellSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.shellSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Heap
            System.out.println("7");
            line = new StringBuilder("| Heap                  | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.heapSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.heapSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.heapSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Quick sem pivo
            System.out.println("8");
            line = new StringBuilder("| Quick S/ pivô         | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.quickSemPivo();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.quickSemPivo();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.quickSemPivo();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Quick com pivo
            System.out.println("9");
            line = new StringBuilder("| Quick C/ pivô         | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.quickComPivo();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.quickComPivo();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.quickComPivo();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Merge 1ª implementação
            System.out.println("10");
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Merge 1ª Implement    | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.mergeSort_1();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.mergeSort_1();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.mergeSort_1();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);
            arqOrd.setArquivo("ordenado.dat");

            //Merge 2ª implementação
            System.out.println("11");
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Merge 2ª Implement    | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.mergeSort_2();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.mergeSort_2();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.mergeSort_2();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);
            arqOrd.setArquivo("ordenado.dat");

            //Counting
            System.out.println("12");
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Counting              | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.coutingSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.coutingSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.coutingSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);
            arqOrd.setArquivo("ordenado.dat");

            //Bucket
            System.out.println("13");
            System.gc();
            line = new StringBuilder("| Bucket                | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.bucketSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.bucketSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.bucketSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Radix
            System.out.println("14");
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Radix                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.radixSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.radixSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.radixSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);
            arqOrd.setArquivo("ordenado.dat");

            //Comb
            System.out.println("15");
            line = new StringBuilder("| Comb                  | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.combSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.combSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.combSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Gnome
            System.out.println("16");
            line = new StringBuilder("| Gnome                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.gnomeSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.gnomeSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.gnomeSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Tim
            System.out.println("17");
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Tim                   | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.timSort();
            tfim = System.currentTimeMillis();
            append1(line, (tfim - tini)/1000, arqOrd,0, 0);
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.timSort();
            tfim = System.currentTimeMillis();
            append2(line, (tfim - tini)/1000, auxRev, 0, 0);
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.timSort();
            tfim = System.currentTimeMillis();
            append3(line, (tfim - tini)/1000, auxRand,0, 0);

            writer.write(line.toString());
            writer.write("|_______________________|_______________|_______________|______________|______________|_________________|_______________|_______________|______________|______________|__________________|_______________|_______________|______________|______________|_________________|\n");
        } catch (IOException ignored) {}
    }

    public static void append1(StringBuilder line, long tempo, Arquivo arq, int compEqua, int movEqua){
        line.append(arq.getComp());
        line.append(" ".repeat(Math.max(0, 39 - (line.length() - 1))));
        line.append("| ");

        line.append(compEqua);
        line.append(" ".repeat(Math.max(0, 55 - (line.length() - 1))));
        line.append("| ");

        line.append(arq.getMov());
        line.append(" ".repeat(Math.max(0, 70 - (line.length() - 1))));
        line.append("| ");

        line.append(movEqua);
        line.append(" ".repeat(Math.max(0, 85 - (line.length() - 1))));
        line.append("| ");

        line.append(tempo);
        line.append(" ".repeat(Math.max(0, 103 - (line.length() - 1))));
        line.append("| ");
    }

    public static void append2(StringBuilder line, long tempo, Arquivo arq, int compEqua, int movEqua){
        line.append(arq.getComp());
        line.append(" ".repeat(Math.max(0, 119 - (line.length() - 1))));
        line.append("| ");

        line.append(compEqua);
        line.append(" ".repeat(Math.max(0, 135 - (line.length() - 1))));
        line.append("| ");

        line.append(arq.getMov());
        line.append(" ".repeat(Math.max(0, 150 - (line.length() - 1))));
        line.append("| ");

        line.append(movEqua);
        line.append(" ".repeat(Math.max(0, 165 - (line.length() - 1))));
        line.append("| ");

        line.append(tempo);
        line.append(" ".repeat(Math.max(0, 184 - (line.length() - 1))));
        line.append("| ");
    }

    public static void append3(StringBuilder line, long tempo, Arquivo arq, int compEqua, int movEqua){
        line.append(arq.getComp());
        line.append(" ".repeat(Math.max(0, 200 - (line.length() - 1))));
        line.append("| ");

        line.append(compEqua);
        line.append(" ".repeat(Math.max(0, 216 - (line.length() - 1))));
        line.append("| ");

        line.append(arq.getMov());
        line.append(" ".repeat(Math.max(0, 231 - (line.length() - 1))));
        line.append("| ");

        line.append(movEqua);
        line.append(" ".repeat(Math.max(0, 246 - (line.length() - 1))));
        line.append("| ");

        line.append(tempo);
        line.append(" ".repeat(Math.max(0, 264 - (line.length() - 1))));
        line.append("|\n");
    }

    public static void quebraLinhaTabela(BufferedWriter writer) throws IOException {
        writer.write("|_______________________|_______________________________________________________________________________|________________________________________________________________________________|_______________________________________________________________________________|\n");
    }

    public static void main(String[] args)
    {
        Principal p = new Principal();
        p.geraTabela();
    }
}