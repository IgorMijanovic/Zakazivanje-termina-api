import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ObradaTermina {
    /**
     * Lista raspoder predstavlja sve zakazane termine
     */
    private List<Termin> raspored;
    /**
     * Lista prostori predstavlja sve prostorije
     */
    private List<Prostor> prostori;
    /**
     * pocetak radnog vremena. Dobija se iz metadata.txt
     */
    private int pocetakRadnogVremena;
    /**
     * kraj radnog vremena. Dobija se iz metadata.txt
     */
    private int krajRadnogVremena;
    /**
     * Lista neradniDani predstavlja listu dana kada ne moze da se zakaze termin
     */
    private List<LocalDateTime> neradniDani;

    private String metadataPutanja = "src/main/resources/metadata.txt";

    /**
     *
     * Metoda ubacuje termine u raspored ako je dati termin slobodan i ne preklapa ni sa jednim drugim terminom.
     *
     *Zakazivanje za jedan termin:
     * 1.arg - ime sobe u kojoj treba da bude termin
     * 2.arg - pocetno vreme termina (u satima)
     * 3.arg - vreme zavrsetka termina (u satima)
     * 4.arg - datum kada termin treba da bude zakazan
     * 5.arg ... x. arg - dodatne informacije o terminu (profesor koji drzi, grupa koja slusa,...)
     *
     * Zakazivanje termina kroz period:
     * 1.arg -
     * 2.arg -
     * 3.arg -
     * 4.arg -
     * 5.arg -
     * 6.arg -
     *
     * @param args
     * @return
     */
    public abstract boolean dodajNoviTermin(String... args);

    /**
     *Metoda brise termin iz rasporeda. Argumenti su iformacije o terminu koji zelimo da obrisemo
     * @param args
     * @return
     */
    public abstract boolean brisanjeTermina(String... args);

    /**
     * Termin se premesta u neki drugi termin, sa proverama da li moze da se premesti(da li je slobodan taj period)
     * @param args
     * @return
     */
    public abstract boolean premestanjeTermina(String... args);

    /**
     * kreira raspored i uctiava sobe
     */
    public void initRaspored(){
        raspored = new ArrayList<>();
        prostori = new ArrayList<>();
        neradniDani = new ArrayList<>();
        dodavanjeProstorija();

    }

    /**
     * sobe se ucitavaju iz metadata.txt i ostale iformacije koje se ne menjaju(radno vreme, neradni dani, ...)
     */
    private void dodavanjeProstorija(){



        try {
            FileReader fr = new FileReader(metadataPutanja);
            BufferedReader br = new BufferedReader(fr);

            String line;
            int flag= 0;
            while ((line = br.readLine()) != null){

//                System.out.println(line);
                if(line.equals("PROSTOR") || line.equals("RADNOVREME") || line.equals("NERADNIDANI")){
                    if(line.equals("PROSTOR"))
                        flag = 1;

                    if(line.equals("RADNOVREME"))
                        flag = 2;

                    if(line.equals("NERADNIDANI"))
                        flag = 3;

                    continue;
                }
                if (flag == 1) {
                    String[] delovi = line.split("-");

                    String imeSobe = delovi[0];
//                    System.out.println(imeSobe);
                    int brojMesta = Integer.parseInt(delovi[1]);
//                    System.out.println(brojMesta);

                    Prostor prostor = new Prostor(imeSobe, brojMesta);
                    for(int i = 2; i< delovi.length; i++){
                        String[] dodatak = delovi[i].split(":");
                        prostor.getDodaci().put(dodatak[0], dodatak[1]);
                    }
//                    System.out.println(prostor);
                    getProstori().add(prostor);
                }
                if(flag == 2){
                    String[] delovi1 = line.split("-");
//                    System.out.println(delovi1[0] + "-" + delovi1[1]);
                    setPocetakRadnogVremena(Integer.parseInt(delovi1[0]));
                    setKrajRadnogVremena(Integer.parseInt(delovi1[1]));
                }
                if(flag == 3){
//                    System.out.println(line);
                    String[] datumSplit = line.split("\\.");
                    int d, m, g;
//        System.out.println(datumSplit[0] +":" + datumSplit[1] +":" + datumSplit[2]);
                    d = Integer.parseInt(datumSplit[0]);
                    m = Integer.parseInt(datumSplit[1]);
                    g = Integer.parseInt(datumSplit[2]);

                    LocalDateTime neradniDan = LocalDateTime.of(g, m, d, 0, 0);
                    getNeradniDani().add(neradniDan);
                }

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        for (Prostor p : getProstori()){
//            System.out.println(p);
//
//        }
//        System.out.println("neradni: " + getNeradniDani());
//        System.out.println(getNeradniDani());

    }

    /**
     * Metoda vrsi pretragu kroz raspored u odnosu na to koji su argumetni prosledjeni metodi aka. tipovi pretraga
     * @param args
     * @return
     */
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

    public List<LocalDateTime> getNeradniDani() {
        if(neradniDani == null) neradniDani = new ArrayList<>();
        return neradniDani;
    }

    public void setNeradniDani(List<LocalDateTime> neradniDani) {
        this.neradniDani = neradniDani;
    }
}
