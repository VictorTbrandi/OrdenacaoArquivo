import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Arquivo {
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;


    public Arquivo(String nomearquivo) {
        try {
            this.nomearquivo = nomearquivo;
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException ignored) {}
    }

    public Arquivo() {

    }

    public void setArquivo(String nomearquivo) {
        arquivo = new Arquivo(nomearquivo).getFile();
    }

    public void close() throws IOException {
        arquivo.close();
    }

    public void initComp() {
        this.comp = 0;
    }

    public void initMov() {
        this.mov = 0;
    }

    public int getComp() {
        return comp;
    }

    public int getMov() {
        return mov;
    }

    public RandomAccessFile getFile() {
        return arquivo;
    }


    public void truncate(long pos) {
        try {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException ignored) {
        }
    }

    public boolean eof() {
        boolean retorno = false;
        try {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException ignored) {
        }
        return (retorno);
    }

    public void inserirRegNoFinal(Registro reg) {
        seekArq(filesize());
        reg.gravaNoArq(arquivo);
    }

    public int filesize() {
        try {
            if (arquivo.length() > 0)
                return (int) (arquivo.length() / Registro.length());
            return 0;
        } catch (IOException ignored) {
        }
        return -1;
    }

    public void exibirArq() {
        Registro aux = new Registro();
        seekArq(0);
        while (!this.eof()) {
            aux.leDoArq(arquivo);
            aux.exibirReg();
        }
    }

    public void exibirUmRegistro(int pos) {
        Registro aux = new Registro();
        seekArq(pos);
        System.out.println("Posicao " + pos);
        aux.leDoArq(arquivo);
        aux.exibirReg();
    }

    public void seekArq(int pos) {
        try {
            arquivo.seek((long) pos * Registro.length());
        } catch (IOException ignored) {
        }
    }

    private int max() {
        int tl = filesize();
        Registro reg = new Registro();
        seekArq(0);
        reg.leDoArq(arquivo);
        int max = reg.getCodigo();
        for (int i = 1; i < tl; i++) {
            reg.leDoArq(arquivo);
            if (reg.getCodigo() > max)
                max = reg.getCodigo();
        }
        return max;
    }

    private int min() {
        int tl = filesize();
        Registro reg = new Registro();
        seekArq(0);
        reg.leDoArq(arquivo);
        int min = reg.getCodigo();
        for (int i = 1; i < tl; i++) {
            reg.leDoArq(arquivo);
            if (reg.getCodigo() < min)
                min = reg.getCodigo();
        }
        return min;
    }

    public void insercaoDireta() {
        int tl = filesize();

        if (tl > 1) {
            Registro reg = new Registro();
            seekArq(1);
            reg.leDoArq(arquivo);
            for (int i = 1; i < tl; ) {
                Registro regAnt = new Registro();
                int pos = i;
                seekArq(pos - 1);
                regAnt.leDoArq(arquivo);
                while (pos > 0 && reg.getCodigo() < regAnt.getCodigo()) {
                    seekArq(pos);
                    regAnt.gravaNoArq(arquivo);
                    pos--;
                    if (pos > 0) {
                        seekArq(pos - 1);
                        regAnt.leDoArq(arquivo);
                    }
                }
                seekArq(pos);
                reg.gravaNoArq(arquivo);
                seekArq(++i);
                reg.leDoArq(arquivo);

            }
        }
    }

    private int buscaBinaria(int chave, int f) {
        int i = 0, m = f / 2;
        Registro reg = new Registro();
        seekArq(m);
        reg.leDoArq(arquivo);
        while (i < f && chave != reg.getCodigo()) {
            if (chave > reg.getCodigo())
                i = m + 1;
            else f = m - 1;
            m = (i + f) / 2;
            seekArq(m);
            reg.leDoArq(arquivo);
        }
        if (chave > reg.getCodigo())
            return m + 1;
        return m;
    }

    public void insercaoBinaria() {
        int tl = filesize();

        if (tl > 1) {
            Registro reg = new Registro();
            seekArq(1);
            reg.leDoArq(arquivo);
            for (int i = 1; i < tl; ) {
                int pos = buscaBinaria(reg.getCodigo(), i);
                for (int j = i; j > pos; j--) {
                    seekArq(j - 1);
                    Registro aux = new Registro();
                    aux.leDoArq(arquivo);
                    aux.gravaNoArq(arquivo);
                }
                seekArq(pos);
                reg.gravaNoArq(arquivo);
                seekArq(++i);
                reg.leDoArq(arquivo);
            }
        }
    }

    public void selecaoDireta() {
        int tl = filesize();

        for (int i = 0; i < tl - 1; ) {
            Registro reg = new Registro();
            seekArq(i);
            reg.leDoArq(arquivo);
            int pos_menor = i;
            Registro reg_menor = reg;
            for (int j = i + 1; j < tl; j++) {
                Registro aux = new Registro();
                aux.leDoArq(arquivo);
                if (aux.getCodigo() < reg_menor.getCodigo()) {
                    pos_menor = j;
                    reg_menor = aux;
                }
            }
            if (pos_menor != i) {
                seekArq(i);
                reg_menor.gravaNoArq(arquivo);
                seekArq(pos_menor);
                reg.gravaNoArq(arquivo);
            }
            seekArq(++i);
            reg.leDoArq(arquivo);
        }
    }

    public void bubleSort() {
        int tl = filesize();
        boolean flag = true;
        while (tl > 1 && flag) {
            flag = false;
            for (int i = 0; i < tl - 1; i++) {
                Registro reg1 = new Registro();
                Registro reg2 = new Registro();
                seekArq(i);
                reg1.leDoArq(arquivo);
                reg2.leDoArq(arquivo);
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    flag = true;
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    reg1.gravaNoArq(arquivo);
                }
            }
            tl--;
        }
    }

    public void shakeSort() {
        int ini = 0, fim = filesize() - 1;
        boolean flag = true;

        while (fim > ini && flag) {
            flag = false;
            for (int i = ini; i < fim; i++) {
                Registro reg1 = new Registro();
                Registro reg2 = new Registro();
                seekArq(i);
                reg1.leDoArq(arquivo);
                reg2.leDoArq(arquivo);
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    flag = true;
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    reg1.gravaNoArq(arquivo);
                }
            }
            fim--;
            if (flag) {
                flag = false;
                for (int i = fim; i > ini; i--) {
                    Registro reg1 = new Registro();
                    Registro reg2 = new Registro();
                    seekArq(i - 1);
                    reg1.leDoArq(arquivo);
                    reg2.leDoArq(arquivo);
                    if (reg1.getCodigo() > reg2.getCodigo()) {
                        flag = true;
                        seekArq(i - 1);
                        reg2.gravaNoArq(arquivo);
                        reg1.gravaNoArq(arquivo);
                    }
                }
                ini++;
            }
        }
    }

    public void shellSort() {
        int dist = 1, tl = filesize();
        Registro regAux = new Registro();
        Registro regDist = new Registro();

        while (dist < tl)
            dist = dist * 3 + 1;

        for (dist /= 3; dist > 0; dist /= 3)
            for (int i = dist; i < tl; ) {
                seekArq(i);
                regAux.leDoArq(arquivo);
                int pos = i;
                seekArq(pos - dist);
                regDist.leDoArq(arquivo);
                while (pos >= dist && regAux.getCodigo() < regDist.getCodigo()) {
                    seekArq(pos);
                    regDist.gravaNoArq(arquivo);
                    pos -= dist;
                    if (pos >= dist) {
                        seekArq(pos - dist);
                        regDist.leDoArq(arquivo);
                    }
                }
                seekArq(pos);
                regAux.gravaNoArq(arquivo);
                seekArq(++i);
                regAux.leDoArq(arquivo);
            }
    }

    public void heapSort() {
        int tl = filesize(), FE, FD, pai, maiorF;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();

        while (tl > 1) {
            for (pai = tl / 2 - 1; pai >= 0; pai--) {
                FE = pai * 2 + 1;
                FD = pai * 2 + 2;
                maiorF = FE;
                if (FD < tl) {
                    seekArq(FE);
                    reg1.leDoArq(arquivo);
                    reg2.leDoArq(arquivo);
                    if (reg2.getCodigo() > reg1.getCodigo())
                        maiorF = FD;
                }
                seekArq(pai);
                reg1.leDoArq(arquivo);
                seekArq(maiorF);
                reg2.leDoArq(arquivo);
                if (reg2.getCodigo() > reg1.getCodigo()) {
                    seekArq(maiorF);
                    reg1.gravaNoArq(arquivo);
                    seekArq(pai);
                    reg2.gravaNoArq(arquivo);
                }
            }
            seekArq(0);
            reg1.leDoArq(arquivo);
            seekArq(tl - 1);
            reg2.leDoArq(arquivo);

            seekArq(0);
            reg2.gravaNoArq(arquivo);
            seekArq(tl - 1);
            reg1.gravaNoArq(arquivo);
            tl--;
        }
    }

    public void quickSemPivo() {
        quickSP(0, filesize() - 1);
    }
    private void quickSP(int ini, int fim) {
        int i = ini, j = fim;
        boolean flag = true;
        Registro regi = new Registro();
        Registro regj = new Registro();

        while (i < j) {
            seekArq(j);
            regj.leDoArq(arquivo);
            seekArq(i);
            regi.leDoArq(arquivo);
            if (flag)
                while (i < j && regi.getCodigo() <= regj.getCodigo()) {
                    regi.leDoArq(arquivo);
                    i++;
                }
            else
                while (i < j && regj.getCodigo() >= regi.getCodigo()) {
                    seekArq(--j);
                    regj.leDoArq(arquivo);
                }
            if (i != j) {
                seekArq(i);
                regj.gravaNoArq(arquivo);
                seekArq(j);
                regi.gravaNoArq(arquivo);
                flag = !flag;
            }
        }
        if (ini < i - 1)
            quickSP(ini, i - 1);
        if (j + 1 < fim)
            quickSP(j + 1, fim);
    }

    public void quickComPivo() {
        quickCP(0, filesize() - 1);
    }
    private void quickCP(int ini, int fim) {
        int i = ini, j = fim;
        Registro pivo = new Registro();

        seekArq((ini + fim) / 2);
        pivo.leDoArq(arquivo);
        while (i < j) {
            seekArq(i);
            Registro regi = new Registro();
            regi.leDoArq(arquivo);
            while (regi.getCodigo() < pivo.getCodigo()) {
                regi.leDoArq(arquivo);
                i++;
            }

            seekArq(j);
            Registro regj = new Registro();
            regj.leDoArq(arquivo);
            while (regj.getCodigo() > pivo.getCodigo()) {
                seekArq(--j);
                regj.leDoArq(arquivo);
            }

            if (i <= j) {
                seekArq(i);
                regj.gravaNoArq(arquivo);
                seekArq(j);
                regi.gravaNoArq(arquivo);
                i++;
                j--;
            }
        }
        if (ini < j)
            quickCP(ini, j);
        if(i < fim)
            quickCP(i, fim);
    }

    public void particao(Arquivo arq1, Arquivo arq2, int tl) {
        int meio = tl/2;
        seekArq(0);
        Registro reg = new Registro();
        for(int i = 0; i < meio; i++){
            reg.leDoArq(arquivo);
            arq1.inserirRegNoFinal(reg);
        }
        for(int i = meio; i < tl; i++){
            reg.leDoArq(arquivo);
            arq2.inserirRegNoFinal(reg);
        }
    }
    public void fusao(Arquivo arq1, Arquivo arq2, int tl, int seq) {
        int i, j, k, sum = seq;
        Registro regi = new Registro();
        Registro regj = new Registro();

        truncate(0);
        for(i = j = k = 0; k < tl; ){
            arq1.seekArq(i);
            regi.leDoArq(arq1.arquivo);
            arq2.seekArq(j);
            regj.leDoArq(arq2.arquivo);
            while(i < seq && j < seq) {
                if (regi.getCodigo() < regj.getCodigo()) {
                    inserirRegNoFinal(regi);
                    i++;
                    regi.leDoArq(arq1.arquivo);
                }
                else {
                    inserirRegNoFinal(regj);
                    j++;
                    regj.leDoArq(arq2.arquivo);
                }
                k++;
            }
            arq1.seekArq(i);
            while (i < seq) {
                regi.leDoArq(arq1.arquivo);
                inserirRegNoFinal(regi);
                i++;
                k++;
            }
            arq2.seekArq(j);
            while (j < seq) {
                regj.leDoArq(arq2.arquivo);
                inserirRegNoFinal(regj);
                j++;
                k++;
            }
            seq += sum;
        }
    }
    public void mergeSort1() throws IOException {
        int seq = 1, tl = filesize();
        Arquivo arq1 = new Arquivo("arq1.dat");
        Arquivo arq2 = new Arquivo("arq2.dat");

        while (seq < tl) {
            arq1.truncate(0);
            arq2.truncate(0);
            particao(arq1, arq2, tl);
            fusao(arq1, arq2, tl, seq);
            seq *= 2;
        }
        arq1.close();
        arq2.close();
        File arquivo1 = new File("arq1.dat");
        arquivo1.delete();
        File arquivo2 = new File("arq2.dat");
        arquivo2.delete();
    }

    public void coutingSort() throws IOException {
        int M = max();
        Registro reg = new Registro();

        int[] vet = new int[M + 1];
        int tl = filesize();
        seekArq(0);
        for (int i = 0; i < tl; i++) {
            reg.leDoArq(arquivo);
            vet[reg.getCodigo()]++;
        }

        for (int i = 1; i < vet.length; i++)
            vet[i] += vet[i - 1];

        Arquivo newArq = new Arquivo("inOrdem.dat");
        int i = filesize() - 1;
        while (i >= 0) {
            reg = new Registro();
            seekArq(i--);
            reg.leDoArq(arquivo);
            newArq.seekArq(vet[reg.getCodigo()] - 1);
            reg.gravaNoArq(newArq.arquivo);
            vet[reg.getCodigo()]--;
        }
        arquivo.close();
        newArq.arquivo.close();
        File original = new File("copia.dat");
        File ordenado = new File("inOrdem.dat");
        original.delete();
        ordenado.renameTo(original);
        arquivo = new Arquivo("copia.dat").getFile();
    }

    public void bucketSort() throws IOException {
        int min = min();
        int max = max();
        int baldes = (int) Math.ceil(Math.sqrt(max - min + 1));
        int intervalo = (max - min + 1) / baldes;
        Arquivo[] arqs = new Arquivo[baldes];

        for (int i = 0; i < baldes; i++)
            arqs[i] = new Arquivo("balde" + i + ".dat");

        int tl = filesize();
        seekArq(0);
        for (int i = 0; i < tl; i++) {
            Registro reg = new Registro();
            reg.leDoArq(arquivo);
            int pos = Math.min((reg.getCodigo() - min) / intervalo, baldes - 1);
            arqs[pos].inserirRegNoFinal(reg);
        }

        for (Arquivo arq : arqs) arq.insercaoDireta();

        truncate(0);
        for (int i = 0; i < arqs.length; i++) {
            int tam = arqs[i].filesize();
            arqs[i].seekArq(0);
            for (int j = 0; j < tam; j++) {
                Registro reg = new Registro();
                reg.leDoArq(arqs[i].arquivo);
                reg.gravaNoArq(arquivo);
            }
        }
        for (int i = 0; i < baldes; i++)
            arqs[i].arquivo.close();
        for (int i = 0; i < baldes; i++) {
            File balde = new File("balde" + i + ".dat");
            balde.delete();
        }
    }

    public void radixSort() throws IOException {
        int max = max(), tl = filesize();

        for (int dgt = 1; dgt <= max; dgt *= 10) {
            int[] couting = new int[10];

            seekArq(0);
            for (int i = 0; i < tl; i++){
                Registro reg = new Registro();
                reg.leDoArq(arquivo);
                couting[(reg.getCodigo() / dgt) % 10]++;
            }

            for (int i = 1; i < couting.length; i++)
                couting[i] += couting[i - 1];

            Arquivo newArq = new Arquivo("inOrdem.dat");
            int i = tl - 1;
            while (i >= 0) {
                Registro reg = new Registro();
                seekArq(i--);
                reg.leDoArq(arquivo);
                newArq.seekArq(couting[(reg.getCodigo() / dgt) % 10] - 1);
                reg.gravaNoArq(newArq.arquivo);
                couting[(reg.getCodigo() / dgt) % 10]--;
            }

            close();
            newArq.close();
            File original = new File("copia.dat");
            File ordenado = new File("inOrdem.dat");
            original.delete();
            ordenado.renameTo(original);
            arquivo = new Arquivo("copia.dat").getFile();
        }
    }

    public void combSort() {
        int tl = filesize(), intervalo = (int) (tl / 1.3), i = 0;

        while (intervalo > 0 && i < tl) {
            Registro reg1 = new Registro();
            Registro reg2 = new Registro();
            i = 0;
            seekArq(0);
            reg1.leDoArq(arquivo);
            seekArq(intervalo);
            reg2.leDoArq(arquivo);
            while (i + intervalo < tl) {
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    seekArq(i + intervalo);
                    reg1.gravaNoArq(arquivo);
                }
                seekArq(++i);
                reg1.leDoArq(arquivo);
                seekArq(i + intervalo);
                reg2.leDoArq(arquivo);
            }
            intervalo = (int) (intervalo / 1.3);
        }
    }

    public void gnomeSort() throws IOException {
        int i, tl;
        Registro reg = new Registro();

        for (i = 0, tl = filesize(); i < tl; ) {
            if (i == 0) {
                seekArq(++i);
                reg.leDoArq(arquivo);
            }
            Registro regAnt = new Registro();
            seekArq(i - 1);
            regAnt.leDoArq(arquivo);
            if (reg.getCodigo() >= regAnt.getCodigo()) {
                seekArq(++i);
                reg.leDoArq(arquivo);
            } else {
                seekArq(i-- - 1);
                reg.gravaNoArq(arquivo);
                regAnt.gravaNoArq(arquivo);
                seekArq(i);
                reg.leDoArq(arquivo);
            }
        }
    }

    public void geraArquivoRandomico() {
        arquivo = new Arquivo("random.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        Random rand = new Random();
        HashSet<Integer> valores = new HashSet<>();
        Set<Integer> sorteados = new HashSet<>();
        while (valores.size() < 64) {
            int cod = rand.nextInt(64) + 1;
            Registro reg = new Registro(cod);
            if (valores.add(cod))
                inserirRegNoFinal(reg);
        }
    }

    public void geraArquivoOrdenado() {
        arquivo = new Arquivo("ordenado.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        for (int i = 0; i < 64; i++)
            inserirRegNoFinal(new Registro(i + 1));
    }

    public void geraArquivoReverso() {
        arquivo = new Arquivo("reverso.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        for (int i = 64; i > 0; i--)
            inserirRegNoFinal(new Registro(i));
    }

    public void copiaArquivo(RandomAccessFile arquivoOrigem) throws IOException {
        arquivo = new Arquivo("copia.dat").getFile();
        if(filesize() > 0)
            truncate(0);
        arquivoOrigem.seek(0);
        while (arquivoOrigem.getFilePointer() != arquivoOrigem.length()){
            arquivo.writeInt(arquivoOrigem.readInt());
            for(int i=0 ; i<1022 ; i++)
                arquivo.writeChar(arquivoOrigem.readChar());
        }

    }
}