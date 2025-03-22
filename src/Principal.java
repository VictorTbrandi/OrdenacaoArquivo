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


            StringBuilder line = new StringBuilder("| Inserção Direta       | ");
            //Inserção Direta
            arqOrd.initComp();
            arqOrd.initMov();
            long tini = System.currentTimeMillis();
            arqOrd.insercaoDireta();
            long tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,arqOrd.filesize()-1, 3*(arqOrd.filesize()-1));
            System.out.println("Inserção Direta");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.insercaoDireta();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 2)/4, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 9 - 10)/4 );
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.insercaoDireta();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,(int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() - 4)/4, (int) (Math.pow(arqOrd.filesize(), 2) + arqOrd.filesize() * 3 - 4)/4 );
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);


            //Inserção Binária
            line = new StringBuilder("| Inserção Binária      | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.insercaoBinaria();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Inserção Binária");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.insercaoBinaria();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.insercaoBinaria();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Seleção Direta
            line = new StringBuilder("| Seleção Direta        | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.selecaoDireta();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Seleção Direta");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.selecaoDireta();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.selecaoDireta();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Bolha
            line = new StringBuilder("| Bolha                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.bubleSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Bolha");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.bubleSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.bubleSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Shake
            line = new StringBuilder("| Shake                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.shakeSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Shake");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.shakeSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.shakeSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Shell
            line = new StringBuilder("| Shell                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.shellSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Shell");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.shellSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.shellSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Heap
            line = new StringBuilder("| Heap                  | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.heapSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Heap");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.heapSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.heapSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Quick sem pivo
            line = new StringBuilder("| Quick S/ pivô         | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.quickSemPivo();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Quick sem pivo");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.quickSemPivo();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.quickSemPivo();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Quick com pivo
            line = new StringBuilder("| Quick C/ pivô         | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.quickComPivo();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Quick com pivo");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.quickComPivo();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.quickComPivo();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Merge 1ª implementação
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Merge 1ª Implement    | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.mergeSort1();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("merge 1");
            arqOrd.exibirArq();
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.mergeSort1();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            System.out.println();
            auxRev.exibirArq();
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.mergeSort1();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Counting
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Counting               | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.coutingSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Counting");
            arqOrd.exibirArq();
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.coutingSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.coutingSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);
            arqOrd.setArquivo("ordenado.dat");

            //Bucket
            System.gc();
            line = new StringBuilder("| Bucket                | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.bucketSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Bucket");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.bucketSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.bucketSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Radix
            auxRev.close();
            auxRand.close();
            System.gc();
            line = new StringBuilder("| Radix                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.radixSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Radix");
            arqOrd.exibirArq();
            arqOrd.close();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.radixSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();
            auxRev.close();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.radixSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();
            auxRand.close();

            writer.write(line.toString());
            quebraLinhaTabela(writer);
            arqOrd.setArquivo("ordenado.dat");

            //Comb
            line = new StringBuilder("| Comb                  | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.combSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Comb");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.combSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.combSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

            writer.write(line.toString());
            quebraLinhaTabela(writer);

            //Gnome
            line = new StringBuilder("| Gnome                 | ");
            arqOrd.initComp();
            arqOrd.initMov();
            tini = System.currentTimeMillis();
            arqOrd.gnomeSort();
            tfim = System.currentTimeMillis();
            append1(line, tfim - tini, arqOrd,0, 0);
            System.out.println("Gnome");
            arqOrd.exibirArq();

            auxRev.initComp();
            auxRev.initMov();
            auxRev.copiaArquivo(arqRev.getFile());
            tini = System.currentTimeMillis();
            auxRev.gnomeSort();
            tfim = System.currentTimeMillis();
            append2(line, tfim - tini, auxRev, 0, 0);
            auxRev.exibirArq();

            auxRand.initComp();
            auxRand.initMov();
            auxRand.copiaArquivo(arqRand.getFile());
            tini = System.currentTimeMillis();
            auxRand.gnomeSort();
            tfim = System.currentTimeMillis();
            append3(line, tfim - tini, auxRand,0, 0);
            auxRand.exibirArq();

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
