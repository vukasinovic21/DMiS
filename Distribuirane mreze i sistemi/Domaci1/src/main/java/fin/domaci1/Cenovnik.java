package fin.domaci1;

public class Cenovnik
{
    public int[] putarine;

    public Cenovnik(int[] putarine)
    {
        this.putarine = putarine;
    }

    public int getBrojKategorija()
    {
        return putarine.length;
    }

    public int getPutarine(int kategorija)
    {
            return putarine[kategorija];
    }
}
