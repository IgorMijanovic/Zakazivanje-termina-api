import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class ObradaTermina {

    private List<Termin> raspored;
    private List<Prostor> prostori;
    private int pocetakRadnogVremena;
    private int krajRadnogVremena;


    public abstract boolean dodajNoviTermin(String... args);
    public abstract boolean brisanjeTermina(String... args);
    public abstract boolean premestanjeTermina(String... args);

    public abstract boolean dodavanjeProstorija(String... args);

    public boolean pretragaTermina(String... args){

        return true;
    }

    public void export(String... args){}



    public List<Termin> getRaspored() {
        if(raspored == null) raspored = new ArrayList<>();
        return raspored;
    }

    public void setRaspored(List<Termin> terminiList) {
        this.raspored = terminiList;
    }

    public List<Prostor> getProstori() {
        if(prostori == null) prostori = new ArrayList<>();
        return prostori;
    }

    public void setProstori(List<Prostor> prostori) {
        this.prostori = prostori;
    }

    public int getPocetakRadnogVremena() {
        return pocetakRadnogVremena;
    }

    public void setPocetakRadnogVremena(int pocetakRadnogVremena) {
        this.pocetakRadnogVremena = pocetakRadnogVremena;
    }

    public int getKrajRadnogVremena() {
        return krajRadnogVremena;
    }

    public void setKrajRadnogVremena(int krajRadnogVremena) {
        this.krajRadnogVremena = krajRadnogVremena;
    }
}
