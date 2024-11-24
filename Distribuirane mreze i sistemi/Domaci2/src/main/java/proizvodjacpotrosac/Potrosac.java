package proizvodjacpotrosac;

public class Potrosac extends Thread
{
    private static int statId = 0;
    private int id=++statId;

    private Skladiste skladiste;
    private int brojac = 0;
    private int minTime ;
    private int maxTime ;

    public void run()
    {
        System.out.println("Potrosac "+id+" je krenuo");
        try
        {
            while(!interrupted())
            {
                System.out.println("Potrosac je proizvod");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Potrosac "+id+ " je zavrsio sa radom");

        }
    }
}
