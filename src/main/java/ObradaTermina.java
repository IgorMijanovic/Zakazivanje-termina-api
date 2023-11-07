import java.util.ArrayList;
import java.util.List;

public abstract class ObradaTermina {

    private List<Termin> raspored;
    private List<Prostor> prostori;


    public abstract boolean dodajNoviTermin();
    public abstract boolean brisanjeTermina();
    public abstract boolean premestanjeTermina();

    public abstract boolean dodavanjeProstorija();

    public boolean pretragaTermina(){

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
