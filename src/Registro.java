import java.io.IOException;
import java.io.RandomAccessFile;

class Registro
{
    public final int tf=1022;
    private int codigo; //4 bytes
    private final char[] lixo = new char[tf]; //2044 bytes

    public Registro() {}

    public Registro(int codigo)
    {
        this.codigo = codigo;
        for (int i = 0 ; i<tf ; i++)
            lixo[i]='X';
    }

    public int getCodigo()
    {
        return (codigo);
    }

    public void gravaNoArq(RandomAccessFile arquivo)
    {
        try
        {
            arquivo.writeInt(codigo);
            for(int i=0 ; i<tf ; i++)
                arquivo.writeChar(lixo[i]);
        }catch(IOException ignored){}
    }

    public void leDoArq(RandomAccessFile arquivo)
    {
        try
        {
            codigo = arquivo.readInt();
            for(int i=0 ; i<tf ; i++)
                lixo[i]=arquivo.readChar();
        }catch(IOException ignored){}
    }
    public void exibirReg()
    {
        System.out.println("codigo: " + this.codigo);
    }

    static int length()
    {
        //int numero; 4 bytes
        //char lixo[] = new char[tf]; 2044 bytes
        //--------------------------------------
        // 2048 bytes
        return(2048);
    }
}
