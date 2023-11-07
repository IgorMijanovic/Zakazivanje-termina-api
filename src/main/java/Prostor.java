import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prostor {
    /**
     * ime ucionice
     */
    private String ime;
    /**
     * koliko slobodnih mesta ima ucionica
     */
    private int brojMesta;
    /**
     * koju dodatnu opremu moze da ima prostor:
     * da li ima videobim, projektor
     */
    private Map<String, String> dodaci;

    public Prostor() {
        dodaci = new HashMap<>();
    }

    public Prostor(String ime, int brojMesta ) {
        this.ime = ime;
        this.brojMesta = brojMesta;
        dodaci = new HashMap<>();
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getBrojMesta() {
        return brojMesta;
    }

    public void setBrojMesta(int brojMesta) {
        this.brojMesta = brojMesta;
    }

    public Map<String, String> getDodaci() {
        return dodaci;
    }

    public void setDodaci(Map<String, String> dodaci) {
        this.dodaci = dodaci;
    }
}
