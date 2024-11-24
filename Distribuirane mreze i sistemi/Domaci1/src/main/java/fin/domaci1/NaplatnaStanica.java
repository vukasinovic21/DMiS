package fin.domaci1;

import lombok.Getter;
import lombok.Setter;

public class NaplatnaStanica
{
   private static int globalId = 0;
   @Getter
   private int id;
   @Getter
   private Cenovnik cenovnik;
   private int ukupnoNaplaceno;
   private int naplacenaPutarina;

    public NaplatnaStanica()
    {
        this.id = globalId++;
        this.ukupnoNaplaceno = ukupnoNaplaceno;
        this.naplacenaPutarina = naplacenaPutarina;
    }

    public NaplatnaStanica(Cenovnik cenovnik)
    {
        this.id = globalId++;
        this.cenovnik = cenovnik;
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

    public void naplatiPutarinu(Vozilo v) throws Exception
    {
        int putarina = this.cenovnik.getPutarine(v.getKategorija());
        this.ukupnoNaplaceno += putarina;
        this.naplacenaPutarina = putarina;
    }

    public int getUkupnoNaplaceno()
    {
        return ukupnoNaplaceno;
    }

    public String getOpis()
    {
        return "ID: " + id + " (" + getUkupnoNaplaceno() + " RSD)";
    }
}
