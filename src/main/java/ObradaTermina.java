import java.util.ArrayList;
import java.util.List;

public abstract class ObradaTermina {

    private List<Termin> raspored;
    private List<Prostor> prostori;


    public abstract boolean dodajNoviTermin(String... args);
    public abstract boolean brisanjeTermina(String... args);
    public abstract boolean premestanjeTermina(String... args);

    public abstract boolean dodavanjeProstorija(String... args);

    public boolean pretragaTermina(String... args){

        return true;
    }

    public List<Termin> getRaspored() {
        if(raspored == null) raspored = new ArrayList<>();
        return raspored;
    }

    public void setRaspored(List<Termin> terminiList) {
        this.raspored = terminiList;
    }
}
