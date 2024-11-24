package fin.domaci1;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AktivnaNaplatnaRampa
{
    private String naziv;
    private int brojStanica;
    private double srednjeVreme;
    private boolean otvorena;
    private int ukupnoNaplaceno;
    private List<NaplatnaStanica> stanice;
    private Cenovnik cenovnik;

    public AktivnaNaplatnaRampa(String naziv, int brojStanica, double srednjeVreme, NaplatnaStanica stanica)
    {
        this.naziv = naziv;
        this.brojStanica = brojStanica;
        this.srednjeVreme = srednjeVreme;
        this.otvorena = false;
        this.ukupnoNaplaceno = 0;
        this.stanice = new ArrayList<>();

        for(int i=0; i<brojStanica; i++)
        {
            this.stanice.add(stanica.napraviKopiju());
        }
    }

    public void otvori(Cenovnik cenovnik)
    {
        if(otvorena)
        {
            System.out.println("Rampa je vec otvorena.");
            return;
        }
        this.cenovnik = cenovnik;
        for(NaplatnaStanica stanica : stanice)
        {
            stanica.dodajCenovnik(cenovnik);
        }

        this.otvorena = true;
        System.out.println("Rampa: " + naziv + " je otvorena.");
    }

    public void zatvori()
    {
        if(!otvorena)
        {
            System.out.println("Rampa nije otvorena.");
            return;
        }
        this.otvorena = false;
        System.out.println("Rampa: " + naziv + " je zatvorena.");
    }

    public void unisti()
    {
        if(otvorena)
            zatvori();
        this.stanice.clear();
        this.cenovnik = null;
        System.out.println("Rampa: " + naziv + " je unistena.");
    }

    public void generisanjeDolaska()
    {
        if(!otvorena)
        {
            System.out.println("Rampa nije otvorena.");
            return;
        }
        Random random = new Random();
        int stanicaIndex = random.nextInt(brojStanica);
        int kategorija = random.nextInt(cenovnik.getBrojKategorija());

        try
        {
            NaplatnaStanica stanica = stanice.get(stanicaIndex);
            stanica.naplatiPutarinu(kategorija);
            ukupnoNaplaceno += stanica.getUkupnoNaplaceno();
            System.out.println("Vozilo je stiglo na stanicu " + stanicaIndex + " i naplacena je putarina: " + stanica.getUkupnoNaplaceno() + " RSD.");
        }
        catch(Exception e)
        {
            System.out.println("Greska prilikom naplate: " + e.getMessage());
        }

        double vremeDolaska = srednjeVreme * (1 + (random.nextDouble() * 0.6 - 0.3)); // 1±0,3
        System.out.println("Vozilo je stiglo za " + String.format("%.2f", vremeDolaska) + " sekundi.");
    }

    public int getUkupnoNaplaceno()
    {
        return ukupnoNaplaceno;
    }

    public String getOpis()
    {
        StringBuilder opis = new StringBuilder();
        opis.append(naziv).append("(").append(ukupnoNaplaceno).append(" RSD):");
        for (NaplatnaStanica stanica : stanice)
        {
            opis.append("Stanica ").append(stanica.getOpis()).append(", ");
        }
        return opis.toString().substring(0, opis.length() - 2);
    }
}
