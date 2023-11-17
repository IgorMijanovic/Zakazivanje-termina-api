import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
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

    public void sacuvaj(String tip,String putanja) throws IOException {

        FileWriter fileWriter = new FileWriter("Desktop.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

       //for (Termin termin : raspored){
        //    csvPrinter.printRecord(termin.getPocetak(),termin.getKraj(),termin.getProstor(),termin.getDodaci());
        //}
        csvPrinter.printRecord("Aleksa","Nikolic");

        csvPrinter.close();
        fileWriter.close();


    }

    public List<Termin> getRaspored() {
        if(raspored == null) raspored = new ArrayList<>();
        return raspored;
    }

    public void setRaspored(List<Termin> terminiList) {
        this.raspored = terminiList;
    }
}
