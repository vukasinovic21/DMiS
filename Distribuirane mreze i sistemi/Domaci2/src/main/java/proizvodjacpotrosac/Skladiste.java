package proizvodjacpotrosac;
public class Skladiste
{
    private static int statId = 0;
    private int id=++statId;
    private int [] niz;

    private int ulaz;
    private int izlaz;

    private int stanje;
    private final int kapacitet;
    public int getStanje()
    {
        return stanje;
    }


    public int getId()
    {
        return id;
    }
    public Skladiste( int kapacitet )
    {

        this.kapacitet = kapacitet;

        niz = new int[kapacitet];
    }

    public synchronized void Stavi (int element) throws InterruptedException
    {
        while(stanje == kapacitet) wait();
        niz[ulaz++] = element;
        stanje++;
        if(ulaz == kapacitet)ulaz = 0;
        notifyAll();
    }

    public synchronized int Uzmi() throws InterruptedException
    {
        while(stanje==0)wait();
        int element = niz[izlaz];
        niz[izlaz++]=0;
        stanje--;
        if(izlaz == kapacitet) izlaz=0;
        notifyAll();

        return element;
    }
}
