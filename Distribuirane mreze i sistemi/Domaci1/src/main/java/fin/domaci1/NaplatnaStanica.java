package fin.domaci1;

public class NaplatnaStanica
{
   private static int globalId = 1;
   private int id;
   private Cenovnik cenovnik;
   private int ukupnoNaplaceno;
   private int naplacenaPutarina;

    public NaplatnaStanica()
    {
        this.id = id;
        this.cenovnik = cenovnik;
        this.ukupnoNaplaceno = ukupnoNaplaceno;
        this.naplacenaPutarina = naplacenaPutarina;
    }

    public void dodajCenovnik(Cenovnik cenovnik)
    {
        this.cenovnik = cenovnik;
    }
    public NaplatnaStanica napraviKopiju()
    {
        NaplatnaStanica kopija = new NaplatnaStanica();
        kopija.dodajCenovnik(this.cenovnik);
        kopija.ukupnoNaplaceno = 0;
        kopija.naplacenaPutarina = 0;
        return kopija;
    }

    public void naplatiPutarinu(int kategorija)
    {
        int putarina = cenovnik.getPutarine(kategorija);
        this.ukupnoNaplaceno += putarina;
        this.naplacenaPutarina = putarina;
    }

    public int getUkupnoNaplaceno()
    {
        return ukupnoNaplaceno;
    }

    public String getOpis()
    {
        return "ID: " + id + " (" + ukupnoNaplaceno + " RSD)";
    }
}
