package fin.domaci1;

import lombok.Getter;

public class Vozilo extends Kategorizovano
{
    @Getter
    private int kategorija;
    public Vozilo(int kat)
    {
        this.kategorija = kat;
    }
}
