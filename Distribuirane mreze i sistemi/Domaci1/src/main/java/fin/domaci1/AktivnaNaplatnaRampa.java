package fin.domaci1;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AktivnaNaplatnaRampa extends Thread
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
        this.cenovnik = stanica.getCenovnik();

        for(int i=0; i<brojStanica; i++)
        {
            this.stanice.add(stanica.napraviKopiju());
        }
        //System.out.println(stanica.getCenovnik().putarine[2]);
    }

    public void otvori(Cenovnik cenovnik)
    {
        if(otvorena)
        {
            System.out.println("Rampa je vec otvorena.");
            return;
        }
        //this.cenovnik = cenovnik;
        for(NaplatnaStanica stanica : stanice)
        {
            stanica.dodajCenovnik(cenovnik);
            //System.out.println(stanica.getCenovnik().putarine[2]);
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
        System.out.println("Rampa: " + naziv + " je unistena.");
        interrupt();
    }

    @Override
    public void run()
    {
        try
        {
            while (!interrupted())
            {
                sleep((long)(srednjeVreme*(0.7+0.6*Math.random())));

                int kat = (int)(Math.random() * this.cenovnik.getBrojKategorija());
                Vozilo v = new Vozilo (kat);
                try
                {
                    stanice.get((int) (Math.random() * stanice.size())).naplatiPutarinu(v);
                }
                catch (Exception ex)
                {}
            }
        }
        catch (InterruptedException ex)
        {}
    }

    public int getUkupnoNaplaceno()
    {
        return ukupnoNaplaceno;
    }

    public String getOpis()
    {
        StringBuilder opis = new StringBuilder();
        opis.append(naziv).append("(").append(getUkupnoNaplaceno()).append(" RSD):");
        for (NaplatnaStanica stanica : stanice)
        {
            opis.append("Stanica ").append(stanica.getOpis()).append(", ");
        }
        return opis.toString().substring(0, opis.length() - 2);
    }
}
