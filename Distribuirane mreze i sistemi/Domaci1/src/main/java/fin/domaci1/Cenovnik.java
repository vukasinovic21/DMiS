package fin.domaci1;

public class Cenovnik
{
    public int[] putarine;

    public Cenovnik(int[] putarine)
    {
        this.putarine = putarine;
        //System.out.println(this.putarine[2]);
    }

    public int getBrojKategorija()
    {
        return putarine.length;
    }

    public int getPutarine(int kategorija) throws Exception
    {
        try
        {
            return putarine[kategorija];
        }
        catch (Exception ex)
        {
            throw new Exception("Kategorija ne postoji.");
        }
    }

    public int[] getSvePutarine()
    {
        return putarine;
    }
}
