import proizvodjacpotrosac.Izvestavac;
import proizvodjacpotrosac.Potrosac;
import proizvodjacpotrosac.Proizvodjac;
import proizvodjacpotrosac.Skladiste;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Drugi domaci.");
        Skladiste skladiste = new Skladiste(10);
        Proizvodjac[] threadsProizvodjac = new Proizvodjac[20];
        Potrosac[] threadsPotrosac = new Potrosac[30];
        Izvestavac[] threadsIzvestavac = new Izvestavac[3];

        for (int i = 0; i < 3; i++)
        {
            threadsIzvestavac[i] = new Izvestavac(skladiste);
            threadsIzvestavac[i].start();
        }

        for (int i = 0; i < 20; i++)
        {
            threadsProizvodjac[i] = new Proizvodjac(skladiste, 30, 50);
            threadsProizvodjac[i].start();
        }
        for (int i = 0; i < 30; i++)
        {
            threadsPotrosac[i] = new Potrosac(skladiste, 30, 50);
            threadsPotrosac[i].start();
        }

        for (int i = 0; i < 3; i++)
        {
            threadsIzvestavac[i].join();
        }
        for (int i = 0; i < 20; i++)
        {
            threadsProizvodjac[i].join();
        }
        for (int i = 0; i < 30; i++)
        {
            threadsPotrosac[i].join();
        }
    }
}