public class PilhaInt {
    NoPilhaInt pilha;

    public PilhaInt() {}

    public void init(){
        pilha = null;
    }

    public boolean isEmpty() {
        return pilha == null;
    }
    public void push(int info){
        if(isEmpty())
            pilha = new NoPilhaInt(info,null);
        else
            pilha = new NoPilhaInt(info,pilha);
    }
    public int pop(){
        if(!isEmpty()){
            int info = pilha.getInfo();
            pilha = pilha.getProx();
            return info;
        }
        return -1;
    }
    public void print(){
        if (!isEmpty()){
            NoPilhaInt aux = pilha;
            while (aux != null){
                System.out.print(aux.getInfo()+", ");
                aux = aux.getProx();
            }
        }
    }
}
