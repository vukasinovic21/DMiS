import proizvodjacpotrosac.Izvestavac;
import proizvodjacpotrosac.Potrosac;
import proizvodjacpotrosac.Proizvodjac;
import proizvodjacpotrosac.Skladiste;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Drugi domaci.");
        Skladiste skladiste = new Skladiste(10);
        Proizvodjac[] threadsProizvodjac = new Proizvodjac[20];
        Potrosac[] threadsPotrosac = new Potrosac[30];
        Izvestavac[] threadsIzvestavac = new Izvestavac[3];

        List<Integer> raspored = raspored();
        AtomicInteger trenutni = new AtomicInteger(0);
        final int kraj = raspored.size();
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);
        for (int i = 0; i < 3; i++)
        {
            threadsIzvestavac[i] = new Izvestavac(skladiste);
            threadsIzvestavac[i].start();
        }

        threadPool.scheduleAtFixedRate(() -> {
            if (trenutni.get() < kraj)
            {
                threadPool.submit(threadsIzvestavac[raspored.get(trenutni.get()) - 1]);
                trenutni.getAndIncrement();
            }
            else
            {
                threadPool.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);

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
        threadPool.shutdown();
    }
    private static List<Integer> raspored()
    {
        List<Integer> raspored = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("raspored.txt")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 2)
                {
                    String part1 = parts[1];
                    char num = part1.charAt(part1.length() - 1);
                    if (Character.isDigit(num))
                    {
                        int id = Character.getNumericValue(num);
                        raspored.add(id);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }

        return raspored;
    }
}