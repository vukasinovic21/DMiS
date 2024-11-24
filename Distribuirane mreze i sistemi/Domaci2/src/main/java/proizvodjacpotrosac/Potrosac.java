package proizvodjacpotrosac;
import java.util.Random;

public class Potrosac extends Thread
{
    private static int statId = 0;
    private int id=++statId;

    private Skladiste skladiste;
    private int brojac = 0;
    Random random;
    private int minTime ;
    private int maxTime ;

    public Potrosac(Skladiste skladiste, int minTime, int maxTime)
    {
        this.skladiste = skladiste;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    @Override
    public void run()
    {
        System.out.println("Potrosac " + id + " je krenuo");
        try
        {
            while(!interrupted())
            {
                int koliko = random.nextInt(maxTime - minTime) + minTime;
                int proizvod = skladiste.Uzmi();
                System.out.println("Potrosac " + id + " trosi proizvod "+ proizvod);
                Thread.sleep(koliko);
                System.out.println("Potrosac  " + id + " je potrosio proizvod "+ proizvod);
            }
        }
        catch (Exception ex)
        {
            System.out.println("Potrosac " + id + " je zavrsio sa radom");

        }
    }
}
