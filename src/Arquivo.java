import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

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

    public Arquivo() {}

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
        reg.gravaNoArq(arquivo); mov++;
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
            comp++;
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
            comp++;
            if (reg.getCodigo() < min)
                min = reg.getCodigo();
        }
        return min;
    }

    public void insercaoDireta() {
        int i, pos, tl = filesize();
        Registro reg = new Registro();
        Registro regAnt = new Registro();

        for (i = 1; i < tl;) {
            pos = i;
            seekArq(pos - 1);
            regAnt.leDoArq(arquivo);
            reg.leDoArq(arquivo);
            comp++;
            while (pos > 0 && reg.getCodigo() < regAnt.getCodigo()) {
                seekArq(pos);
                regAnt.gravaNoArq(arquivo); mov++;
                pos--;
                if (pos > 0) {
                    seekArq(pos - 1);
                    regAnt.leDoArq(arquivo);
                }
                comp++;
            }
            seekArq(pos);
            reg.gravaNoArq(arquivo); mov++;
            seekArq(++i);
            reg.leDoArq(arquivo);
        }
    }

    private int buscaBinaria(int chave, int f) {
        int i = 0, m = f / 2;
        Registro reg = new Registro();

        seekArq(m);
        reg.leDoArq(arquivo);
        comp++;
        while (i < f && chave != reg.getCodigo()) {
            comp++;
            if (chave > reg.getCodigo())
                i = m + 1;
            else f = m - 1;
            m = (i + f) / 2;
            seekArq(m);
            reg.leDoArq(arquivo);
            comp++;
        }
        comp++;
        if (chave > reg.getCodigo())
            return m + 1;
        return m;
    }
    public void insercaoBinaria() {
        int i, j, pos, tl = filesize();
        Registro reg = new Registro();
        Registro aux = new Registro();

        seekArq(1);
        reg.leDoArq(arquivo);
        for (i = 1; i < tl; ) {
            pos = buscaBinaria(reg.getCodigo(), i);
            for (j = i; j > pos; j--) {
                seekArq(j - 1);
                aux.leDoArq(arquivo);
                aux.gravaNoArq(arquivo); mov++;
            }
            if(pos != i) {
                seekArq(pos);
                reg.gravaNoArq(arquivo); mov++;
            }
            seekArq(++i);
            reg.leDoArq(arquivo);
        }
    }

    public void selecaoDireta() {
        int i, j, pos_menor, tl = filesize();
        Registro reg = new Registro();
        Registro aux = new Registro();
        Registro reg_menor = new Registro();


        for (i = 0; i < tl-1; ) {
            seekArq(i);
            reg.leDoArq(arquivo);
            pos_menor = i;
            reg_menor.setCodigo(reg.getCodigo());
            for (j = i + 1; j < tl; j++) {
                aux.leDoArq(arquivo);
                comp++;
                if (aux.getCodigo() < reg_menor.getCodigo()) {
                    reg_menor.setCodigo(aux.getCodigo());
                    pos_menor = j;
                }
            }
            if (pos_menor != i) {
                seekArq(i);
                reg_menor.gravaNoArq(arquivo);
                seekArq(pos_menor);
                reg.gravaNoArq(arquivo); mov+=2;
            }
            seekArq(++i);
            reg.leDoArq(arquivo);
        }
    }

    public void bubleSort() {
        int i, tl = filesize();
        boolean flag = true;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();

        while (tl > 1 && flag) {
            flag = false;
            for (i = 0; i < tl - 1; i++) {
                seekArq(i);
                reg1.leDoArq(arquivo);
                reg2.leDoArq(arquivo);
                comp++;
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    flag = true;
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    reg1.gravaNoArq(arquivo); mov+=2;
                }
            }
            tl--;
        }
    }

    public void shakeSort() {
        int i, ini = 0, fim = filesize() - 1;
        boolean flag = true;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();

        while (fim > ini && flag) {
            flag = false;
            for (i = ini; i < fim; i++) {
                seekArq(i);
                reg1.leDoArq(arquivo);
                reg2.leDoArq(arquivo);
                comp++;
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    flag = true;
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    reg1.gravaNoArq(arquivo); mov+=2;
                }
            }
            fim--;
            if (flag) {
                flag = false;
                for (i = fim; i > ini; i--) {
                    seekArq(i - 1);
                    reg1.leDoArq(arquivo);
                    reg2.leDoArq(arquivo);
                    comp++;
                    if (reg1.getCodigo() > reg2.getCodigo()) {
                        flag = true;
                        seekArq(i - 1);
                        reg2.gravaNoArq(arquivo);
                        reg1.gravaNoArq(arquivo); mov+=2;
                    }
                }
                ini++;
            }
        }
    }

    public void shellSort() {
        int i, pos, dist = 1, tl = filesize();
        Registro regAux = new Registro();
        Registro regDist = new Registro();

        while (dist < tl)
            dist = dist * 3 + 1;

        for (dist /= 3; dist > 0; dist /= 3)
            for (i = dist; i < tl; i++) {
                seekArq(i);
                regAux.leDoArq(arquivo);
                pos = i;
                seekArq(pos - dist);
                regDist.leDoArq(arquivo);
                comp++;
                while (pos >= dist && regAux.getCodigo() < regDist.getCodigo()) {
                    seekArq(pos);
                    regDist.gravaNoArq(arquivo); mov++;
                    pos -= dist;
                    if (pos >= dist) {
                        seekArq(pos - dist);
                        regDist.leDoArq(arquivo);
                    }
                    comp++;
                }
                seekArq(pos);
                regAux.gravaNoArq(arquivo); mov++;
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
                    comp++;
                    if (reg2.getCodigo() > reg1.getCodigo())
                        maiorF = FD;
                }
                seekArq(pai);
                reg1.leDoArq(arquivo);
                seekArq(maiorF);
                reg2.leDoArq(arquivo);
                comp++;
                if (reg2.getCodigo() > reg1.getCodigo()) {
                    seekArq(maiorF);
                    reg1.gravaNoArq(arquivo);
                    seekArq(pai);
                    reg2.gravaNoArq(arquivo); mov+=2;
                }
            }
            seekArq(0);
            reg1.leDoArq(arquivo);
            seekArq(tl - 1);
            reg2.leDoArq(arquivo);

            seekArq(0);
            reg2.gravaNoArq(arquivo);
            seekArq(tl - 1);
            reg1.gravaNoArq(arquivo); mov+=2;
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
            if (flag) {
                comp++;
                while (i < j && regi.getCodigo() <= regj.getCodigo()) {
                    regi.leDoArq(arquivo);
                    i++;
                    comp++;
                }
            }
            else {
                comp++;
                while (i < j && regj.getCodigo() >= regi.getCodigo()) {
                    seekArq(--j);
                    regj.leDoArq(arquivo);
                    comp++;
                }
            }
            if (i != j) {
                seekArq(i);
                regj.gravaNoArq(arquivo);
                seekArq(j);
                regi.gravaNoArq(arquivo); mov+=2;
            }
            flag = !flag;
        }
        if (ini < i - 1)
            quickSP(ini, i - 1);
        if (j + 1 < fim)
            quickSP(j + 1, fim);
    }

    public void quickSortSemPivoIterativo(){
        PilhaInt p = new PilhaInt();
        int i, j, ini, fim;
        Registro regi = new Registro();
        Registro regj = new Registro();
        boolean flag = true;

        p.push(0);
        p.push(filesize() - 1);
        while (!p.isEmpty()) {
            fim = j = p.pop();
            ini = i = p.pop();
            flag = true;
            while (i < j) {
                seekArq(j);regj.leDoArq(arquivo);
                seekArq(i);regi.leDoArq(arquivo);
                if (flag) {
                    comp++;
                    while (i < j && regi.getCodigo() <= regj.getCodigo()) {
                        regi.leDoArq(arquivo);
                        i++;
                        comp++;
                    }
                }
                else {
                    comp++;
                    while (i < j && regj.getCodigo() >= regi.getCodigo()) {
                        seekArq(--j);
                        regj.leDoArq(arquivo);
                        comp++;
                    }
                }
                if (i != j) {
                    seekArq(i);
                    regj.gravaNoArq(arquivo);
                    seekArq(j);
                    regi.gravaNoArq(arquivo); mov+=2;
                }
                flag = !flag;
            }
            if(fim > j + 1){
                p.push(j + 1);
                p.push(fim);
            }
            if(ini < i - 1){
                p.push(ini);
                p.push(i - 1);
            }
        }
    }

    public void quickComPivo() {
        quickCP(0, filesize() - 1);
    }
    private void quickCP(int ini, int fim) {
        int i = ini, j = fim;
        Registro pivo = new Registro();
        Registro regi = new Registro();
        Registro regj = new Registro();

        seekArq((ini + fim) / 2);
        pivo.leDoArq(arquivo);
        while (i < j) {
            seekArq(i);
            regi.leDoArq(arquivo);
            comp++;
            while (regi.getCodigo() < pivo.getCodigo()) {
                regi.leDoArq(arquivo);
                i++;
                comp++;
            }
            seekArq(j);
            regj.leDoArq(arquivo);
            comp++;
            while (regj.getCodigo() > pivo.getCodigo()) {
                seekArq(--j);
                regj.leDoArq(arquivo);
                comp++;
            }

            if (i <= j) {
                seekArq(i);
                regj.gravaNoArq(arquivo);
                seekArq(j);
                regi.gravaNoArq(arquivo); mov+=2;
                i++;
                j--;
            }
        }
        if (ini < j)
            quickCP(ini, j);
        if(i < fim)
            quickCP(i, fim);
    }

    public void quickSortComPivoIterativo() {
        PilhaInt p = new PilhaInt();
        Registro regi = new Registro();
        Registro regj = new Registro();
        int i, j, ini, fim, pivo;

        p.push(0);
        p.push(filesize() - 1);
        while (!p.isEmpty()){
            fim = j = p.pop();
            ini = i = p.pop();
            seekArq((ini + fim) / 2);
            regi.leDoArq(arquivo);
            pivo = regi.getCodigo();
            while (i < j) {
                seekArq(i);
                regi.leDoArq(arquivo);
                comp++;
                while (pivo > regi.getCodigo()) {
                    regi.leDoArq(arquivo);
                    i++;
                    comp++;
                }
                seekArq(j);
                regj.leDoArq(arquivo);
                comp++;
                while (pivo < regj.getCodigo()) {
                    seekArq(--j);
                    regj.leDoArq(arquivo);
                    comp++;
                }

                if (i < j) {
                    seekArq(i++);
                    regj.gravaNoArq(arquivo);
                    seekArq(j--);
                    regi.gravaNoArq(arquivo); mov+=2;
                } else if (i == j) { i++; j--;}
            }
            if(fim > i){
                p.push(i);
                p.push(fim);
            }
            if(ini < j){
                p.push(ini);
                p.push(j);
            }
        }
    }

    public void particao(Arquivo arq1, Arquivo arq2, int tl) {
        int i, meio = tl/2;
        Registro reg = new Registro();

        seekArq(0);
        for(i = 0; i < meio; i++){
            reg.leDoArq(arquivo);
            arq1.inserirRegNoFinal(reg);
        }
        for(i = meio; i < tl; i++){
            reg.leDoArq(arquivo);
            arq2.inserirRegNoFinal(reg);
        }
    }
    public void fusao_1(Arquivo arq1, Arquivo arq2, int tl, int seq) {
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
                comp++;
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
    public void mergeSort_1() throws IOException {
        int seq = 1, tl = filesize();
        Arquivo arq1 = new Arquivo("arq1.dat");
        Arquivo arq2 = new Arquivo("arq2.dat");
        while (seq < tl) {
            arq1.truncate(0);
            arq2.truncate(0);
            particao(arq1, arq2, tl);
            fusao_1(arq1, arq2, tl, seq);
            seq *= 2;
        }
        arq1.close();
        arq2.close();
        File arquivo1 = new File("arq1.dat");
        arquivo1.delete();
        File arquivo2 = new File("arq2.dat");
        arquivo2.delete();
    }

    private void fusao_2(Arquivo arq, int ini1, int fim1, int ini2, int fim2){
        int i = ini1, j = ini2, k = 0;
        Registro regi = new Registro();
        Registro regj = new Registro();

        arq.seekArq(k);
        seekArq(i);
        regi.leDoArq(arquivo);
        seekArq(j);
        regj.leDoArq(arquivo);

        while(i <= fim1 && j <= fim2){
            comp++;
           if(regi.getCodigo() < regj.getCodigo()){
               regi.gravaNoArq(arq.arquivo); mov++;
               seekArq(++i);
               regi.leDoArq(arquivo);
           }
           else {
               regj.gravaNoArq(arq.arquivo); mov++;
               seekArq(++j);
               regj.leDoArq(arquivo);
           }
           k++;
        }
        seekArq(i);
        regi.leDoArq(arquivo);
        while (i <= fim1){
            regi.gravaNoArq(arq.arquivo); mov++;
            regi.leDoArq(arquivo);
            i++;
            k++;
        }
        seekArq(j);
        regi.leDoArq(arquivo);
        while (j <= fim2){
            regj.gravaNoArq(arq.arquivo); mov++;
            regj.leDoArq(arquivo);
            j++;
            k++;
        }
        seekArq(ini1);
        arq.seekArq(0);
        for (i = 0; i < k; i++) {
            regi.leDoArq(arq.arquivo);
            regi.gravaNoArq(arquivo); mov++;
        }
    }
    private void merge(Arquivo arq, int esq, int dir){
        if (esq < dir){
            int meio = (esq + dir)/2;
            merge(arq, esq, meio);
            merge(arq, meio+1, dir);
            fusao_2(arq, esq, meio, meio + 1, dir);
        }
    }
    public void mergeSort_2() throws IOException {
        int tl = filesize();
        Arquivo arq = new Arquivo("arq.dat");
        merge(arq, 0, tl-1);
        arq.close();
        File arquivo1 = new File("arq.dat");
        arquivo1.delete();
    }

    public void mergeSort2Iterativo() throws IOException {
        Arquivo arq = new Arquivo("arq.dat");
        PilhaInt p1 = new PilhaInt();
        PilhaInt p2 = new PilhaInt();
        int esq, meio, dir, ini1, ini2, fim1, fim2;

        p1.push(0);p1.push(filesize()-1);
        while (!p1.isEmpty()){
            dir = p1.pop();
            esq = p1.pop();
            if(esq < dir){
                meio = (esq + dir)/2;
                p1.push(meio + 1);p1.push(dir);p1.push(esq);p1.push(meio);
                p2.push(meio + 1);p2.push(dir);p2.push(esq);p2.push(meio);
            }
        }
        while (!p2.isEmpty()){
            fim1 = p2.pop();ini1 = p2.pop();
            fim2 = p2.pop();ini2 = p2.pop();
            fusao_2(arq,ini1,fim1,ini2,fim2);
        }

        arq.close();
        File arquivo1 = new File("arq.dat");
        arquivo1.delete();
    }

    public void coutingSort() throws IOException {
        int i, M = max();
        Registro reg = new Registro();

        int[] vet = new int[M + 1];
        int tl = filesize();
        seekArq(0);
        for (i = 0; i < tl; i++) {
            reg.leDoArq(arquivo);
            vet[reg.getCodigo()]++;
        }

        for (i = 1; i < vet.length; i++)
            vet[i] += vet[i - 1];

        Arquivo newArq = new Arquivo("inOrdem.dat");
        i = filesize() - 1;
        while (i >= 0) {
            seekArq(i--);
            reg.leDoArq(arquivo);
            newArq.seekArq(vet[reg.getCodigo()] - 1);
            reg.gravaNoArq(newArq.arquivo); mov++;
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
        int i, j, pos, tam, tl, min = min(), max = max();
        int baldes = (int) Math.sqrt(max - min + 1);
        int intervalo = (max - min + 1) / baldes;
        Arquivo[] bucket = new Arquivo[baldes + 1];
        Registro reg = new Registro();

        for (i = 0; i < bucket.length; i++)
            bucket[i] = new Arquivo("balde" + i + ".dat");

        tl = filesize();
        seekArq(0);
        for (i = 0; i < tl; i++) {
            reg.leDoArq(arquivo);
            pos = (reg.getCodigo() - min) / intervalo;
            bucket[pos].inserirRegNoFinal(reg);
        }

        for (i = 0; i < bucket.length; i++)
            bucket[i].insercaoDireta();

        truncate(0);
        for (i = 0; i < bucket.length; i++){
            tam = bucket[i].filesize();
            bucket[i].seekArq(0);
            for (j = 0; j < tam; j++) {
                reg.leDoArq(bucket[i].arquivo);
                reg.gravaNoArq(arquivo); mov++;
            }
        }

        for (i = 0; i < bucket.length; i++)
            bucket[i].arquivo.close();
        for (i = 0; i < bucket.length; i++) {
            File balde = new File("balde" + i + ".dat");
            balde.delete();
        }
    }

    public void radixSort() throws IOException {
        int i, max = max(), tl = filesize();
        Registro reg = new Registro();

        for (int dgt = 1; dgt <= max; dgt *= 10) {
            int[] couting = new int[10];

            seekArq(0);
            for (i = 0; i < tl; i++){
                reg.leDoArq(arquivo);
                couting[(reg.getCodigo() / dgt) % 10]++;
            }

            for (i = 1; i < couting.length; i++)
                couting[i] += couting[i - 1];

            Arquivo newArq = new Arquivo("inOrdem.dat");
            i = tl - 1;
            while (i >= 0) {
                seekArq(i--);
                reg.leDoArq(arquivo);
                newArq.seekArq(couting[(reg.getCodigo() / dgt) % 10] - 1);
                reg.gravaNoArq(newArq.arquivo); mov++;
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
        int tl = filesize(), intervalo = (int) (tl / 1.3), i;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean flag = true;

        while (intervalo > 1 || flag) {
            flag = false;
            i = 0;
            while (i + intervalo < tl){
                seekArq(i);
                reg1.leDoArq(arquivo);
                seekArq(i + intervalo);
                reg2.leDoArq(arquivo);
                comp++;
                if (reg1.getCodigo() > reg2.getCodigo()) {
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);
                    seekArq(i + intervalo);
                    reg1.gravaNoArq(arquivo); mov+=2;
                    flag = true;
                }
                i++;
            }
            if(intervalo > 1) {
                flag = true;
                intervalo = (int) (intervalo / 1.3);
            }
        }
    }

    public void gnomeSort() throws IOException {
        int i, tl;
        Registro reg = new Registro();
        Registro regAnt = new Registro();

        for (i = 0, tl = filesize(); i < tl; ) {
            if (i == 0) {
                seekArq(++i);
                reg.leDoArq(arquivo);
            }
            seekArq(i - 1);
            regAnt.leDoArq(arquivo);
            comp++;
            if (reg.getCodigo() >= regAnt.getCodigo()) {
                seekArq(++i);
                reg.leDoArq(arquivo);
            } else {
                seekArq(i - 1);
                reg.gravaNoArq(arquivo);
                regAnt.gravaNoArq(arquivo); mov+=2;
                seekArq(--i);
                reg.leDoArq(arquivo);
            }
        }
    }

    private void insercaoDireta_ForTim(int ini, int fim) {
        int i, pos;
        Registro reg = new Registro();
        Registro regAnt = new Registro();
        for (i = ini + 1; i <= fim; ) {
            pos = i;
            seekArq(pos - 1);
            regAnt.leDoArq(arquivo);
            reg.leDoArq(arquivo);
            comp++;
            while (pos > ini && reg.getCodigo() < regAnt.getCodigo()) {
                seekArq(pos);
                regAnt.gravaNoArq(arquivo); mov++;
                pos--;
                if (pos > ini) {
                    seekArq(pos - 1);
                    regAnt.leDoArq(arquivo);
                }
                comp++;
            }
            seekArq(pos);
            reg.gravaNoArq(arquivo); mov++;
            seekArq(++i);
            reg.leDoArq(arquivo);
        }
    }
    private void fusao(Arquivo arq, int ini1, int fim1, int ini2, int fim2){
        int i = ini1, j = ini2, k = 0;
        Registro regi = new Registro();
        Registro regj = new Registro();

        arq.seekArq(k);
        seekArq(i);
        regi.leDoArq(arquivo);
        seekArq(j);
        regj.leDoArq(arquivo);

        while(i <= fim1 && j <= fim2){
            comp++;
            if(regi.getCodigo() < regj.getCodigo()){
                regi.gravaNoArq(arq.arquivo); mov++;
                seekArq(++i);
                regi.leDoArq(arquivo);
            }
            else {
                regj.gravaNoArq(arq.arquivo); mov++;
                seekArq(++j);
                regj.leDoArq(arquivo);
            }
            k++;
        }
        seekArq(i);
        regi.leDoArq(arquivo);
        while (i <= fim1){
            regi.gravaNoArq(arq.arquivo); mov++;
            regi.leDoArq(arquivo);
            i++;
            k++;
        }
        seekArq(j);
        regi.leDoArq(arquivo);
        while (j <= fim2){
            regj.gravaNoArq(arq.arquivo); mov++;
            regj.leDoArq(arquivo);
            j++;
            k++;
        }
        seekArq(ini1);
        arq.seekArq(0);
        for (i = 0; i < k; i++) {
            regi.leDoArq(arq.arquivo);
            regi.gravaNoArq(arquivo); mov++;
        }
    }
    public void timSort() throws IOException {
        int i,esq, meio, dir, tam,tl = filesize(), run = 32;

        for (i = 0; i < tl; i += run)
            insercaoDireta_ForTim(i, Math.min(i + run, tl-1));

        Arquivo arq = new Arquivo("arq.dat");

        for (tam = run; tam < tl; tam = 2 * tam){
            for (esq = 0; esq < tl; esq += 2 * tam){
                meio = esq + tam - 1;
                dir = Math.min((esq + 2 * tam - 1), (tl - 1));
                if (meio < dir)
                    fusao(arq, esq, meio, meio + 1, dir);
            }
        }

        arq.close();
        File arquivo = new File("arq.dat");
        arquivo.delete();
    }

    public void geraArquivoRandomico() {
        arquivo = new Arquivo("random.dat").getFile();
        Registro reg = new Registro();
        truncate(0);
        Random rand = new Random();
        for (int i = 0; i < 1024; i++) {
            reg.setCodigo(rand.nextInt(2000) + 1);
            inserirRegNoFinal(reg);
        }
    }

    public void geraArquivoOrdenado() {
        arquivo = new Arquivo("ordenado.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        for (int i = 0; i < 1024; i++)
            inserirRegNoFinal(new Registro(i + 1));
    }

    public void geraArquivoReverso() {
        arquivo = new Arquivo("reverso.dat").getFile();
        if (filesize() > 0)
            truncate(0);
        for (int i = 1024; i > 0; i--)
            inserirRegNoFinal(new Registro(i));
    }

    public void copiaArquivo(RandomAccessFile arquivoOrigem) throws IOException {
        arquivo = new Arquivo("copia.dat").getFile();
        truncate(0);
        arquivoOrigem.seek(0);
        while (arquivoOrigem.getFilePointer() != arquivoOrigem.length()){
            arquivo.writeInt(arquivoOrigem.readInt());
            for(int i=0 ; i < 1022; i++)
                arquivo.writeChar(arquivoOrigem.readChar());
        }
    }

    public void validaOrdenacao(String msg){
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean erro = false;
        for (int i = 0; i <= 1024; i++) {
            seekArq(i);
            reg1.leDoArq(arquivo);
            reg2.leDoArq(arquivo);
            if (reg1.getCodigo() > reg2.getCodigo()) {
                System.out.println(msg + " erro");
                erro = true;
                i = filesize() + 1;
            }
        }
        if(!erro)
            System.out.println(msg + " ok");
    }
}