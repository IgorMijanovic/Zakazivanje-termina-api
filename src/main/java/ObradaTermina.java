import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ObradaTermina {

    private List<Termin> raspored;
    private List<Prostor> prostori;

    public abstract boolean dodajNoviTermin(Prostor prostor, LocalDateTime pocetak, LocalDateTime kraj, Map<String,String> dodaci);
    public abstract boolean brisanjeTermina();
    public abstract boolean premestanjeTermina();
    public abstract boolean pretragaTermina();


    public List<Termin> getRaspored() {
        if(raspored == null) raspored = new ArrayList<>();
        return raspored;
    }

    public void setRaspored(List<Termin> terminiList) {
        this.raspored = terminiList;
    }
}
