package fin.domaci1;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("Domaci 1");
        Cenovnik cenovnik = new Cenovnik(new int[] {100, 200, 500});
        AktivnaNaplatnaRampa rampa = new AktivnaNaplatnaRampa("Batocina", 3, 211, new NaplatnaStanica(cenovnik));
        rampa.start();
        rampa.otvori(cenovnik);

        Thread.sleep(3000);
        System.out.println(rampa.getOpis());

        Thread.sleep(3000);
        rampa.zatvori();

        System.out.println(rampa.getOpis());
        rampa.unisti();
    }
}