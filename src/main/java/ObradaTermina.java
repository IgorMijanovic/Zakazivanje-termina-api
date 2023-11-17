import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ObradaTermina {

    private List<Termin> raspored;
    private List<Prostor> prostori;
    private int pocetakRadnogVremena;
    private int krajRadnogVremena;
    private List<String> neradniDani;


    public abstract boolean dodajNoviTermin(String... args);
    public abstract boolean brisanjeTermina(String... args);
    public abstract boolean premestanjeTermina(String... args);

    public abstract boolean dodavanjeProstorija(String... args);

    public boolean pretragaTermina(String... args){
        List<String> unos = new ArrayList<>(Arrays.asList(args));
        if(unos.get(0).contains(".") && args.length == 1){
            System.out.println("po datumu samo");
            String[] datumSplit = unos.get(0).split("\\.");
            int d, m, g;

//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
            d = Integer.parseInt(datumSplit[0]);
            m = Integer.parseInt(datumSplit[1]);
            g = Integer.parseInt(datumSplit[2]);
            System.out.println(d +":"+m+":"+g);
            LocalDateTime datumZaProveru = LocalDateTime.of(g,m,d,0,0);
            for(Termin t: raspored){
                if(t.getPocetak().getYear()==datumZaProveru.getYear()
                        && t.getPocetak().getMonth()==datumZaProveru.getMonth()
                        && t.getPocetak().getDayOfMonth()==datumZaProveru.getDayOfMonth()){
                    System.out.println(t);
                }
            }
            return true;
        }
        if(unos.get(0).matches("s[1-10]+") && args.length == 1){
            System.out.println("pretraga po sobi");
            for (Termin t: raspored){
                if(t.getProstor().getIme().equals(unos.get(0))){
                    System.out.println(t);
                }
            }
            return true;
        }
        if(unos.get(0).contains(".") && unos.get(1).matches("s[1-10]+")  && args.length == 2){
            System.out.println("provera za prostor i datum");
            String[] datumSplit = unos.get(0).split("\\.");
            int d, m, g;

//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
            d = Integer.parseInt(datumSplit[0]);
            m = Integer.parseInt(datumSplit[1]);
            g = Integer.parseInt(datumSplit[2]);
//            System.out.println(d +":"+m+":"+g);
            LocalDateTime datumZaProveru = LocalDateTime.of(g,m,d,0,0);
            for (Termin t: raspored){
                if(t.getPocetak().getYear()==datumZaProveru.getYear()
                        && t.getPocetak().getMonth()==datumZaProveru.getMonth()
                        && t.getPocetak().getDayOfMonth()==datumZaProveru.getDayOfMonth()){
                    if(t.getProstor().getIme().equals(unos.get(1))){
                        System.out.println(t);
                    }
                }
            }
            return true;
        }
        if(unos.get(0).contains(".") && unos.get(1).matches("\\d+") && unos.get(2).matches("\\d+")
                && args.length == 3){
            System.out.println("pretraga za datum i odredjeni vremenski period");
            int satPocetka = Integer.parseInt(unos.get(1));
            int satKraj = Integer.parseInt(unos.get(2));
            String[] datumSplit = unos.get(0).split("\\.");
            int d, m, g;

//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
            d = Integer.parseInt(datumSplit[0]);
            m = Integer.parseInt(datumSplit[1]);
            g = Integer.parseInt(datumSplit[2]);
//            System.out.println(d +":"+m+":"+g);
            LocalDateTime datumZaProveru = LocalDateTime.of(g,m,d,0,0);
            for (Termin t: raspored){
                if(t.getPocetak().getYear()==datumZaProveru.getYear()
                        && t.getPocetak().getMonth()==datumZaProveru.getMonth()
                        && t.getPocetak().getDayOfMonth()==datumZaProveru.getDayOfMonth()){
                    if(satPocetka < t.getPocetak().getHour() && satKraj < t.getPocetak().getHour()
                            || (satPocetka > t.getKraj().getHour() && satKraj > t.getKraj().getHour())){
                        System.out.println("slobodno");
                        return true;
                    }else {
                        System.out.println("zauzeto");
                        return false;
                    }
                }
            }
        }
        if (unos.get(0).matches("[tp]") && unos.get(1).contains(":") && args.length == 2){
            System.out.println("pretraga za datum + dodatak");

            String[] tokeni = unos.get(1).split(":");
            String key = tokeni[0];
            String value = tokeni[1];
            if (unos.get(0).equals("t")){
                for (Termin t : raspored) {
                    if (t.getDodaci().containsKey(key)) {
                        if (t.getDodaci().get(key).equals(value)) {
                            System.out.println(t);
                        }
                    }
                }
            }else if(unos.get(0).equals("p")){
                for (Prostor p : prostori){
                    if (p.getDodaci().containsKey(key)) {
                        if (p.getDodaci().get(key).equals(value)) {
                            System.out.println(p);
                        }
                    }
                }
            }
        }


        return true;
    }

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

    public List<String> getNeradniDani() {
        if(neradniDani == null) neradniDani = new ArrayList<>();
        return neradniDani;
    }

    public void setNeradniDani(List<String> neradniDani) {
        this.neradniDani = neradniDani;
    }
}
