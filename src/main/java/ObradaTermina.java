import java.io.BufferedReader;
import java.io.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.itextpdf.kernel.pdf.PdfName.Document;



public abstract class ObradaTermina {

    private static ObradaTermina obj;



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

    //samo dodajem na string slobodno do pocetak termini, slobodno od kraja termina
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


   public void exportJson(String... args){
       List<String> unos = new ArrayList<>(Arrays.asList(args));
       try {
            String putanjaDoFajla = args[0];
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            if (args.length == 1) {
                String jsonString = objectMapper.writeValueAsString(getRaspored());
                Files.write(Paths.get(putanjaDoFajla), jsonString.getBytes(), StandardOpenOption.APPEND);
            }else if (args.length == 3 && unos.get(1).contains(".") && unos.get(2).contains(".")) {
                LocalDateTime pocetniDatum = datum(args[1]);
                LocalDateTime krajniDatum = datum(args[2]);
                List<Termin> zaIspis = new ArrayList<>();
                for (LocalDateTime datumm = pocetniDatum; datumm.isBefore(krajniDatum); datumm = datumm.plusDays(1))
                    for (Termin obj : getRaspored()) {
                        if (datumm.getYear() == obj.getPocetak().getYear() && datumm.getMonth() == obj.getPocetak().getMonth() && datumm.getDayOfMonth() == obj.getPocetak().getDayOfMonth()){
                            zaIspis.add(obj);
                        }
                    }
                String jsonString = objectMapper.writeValueAsString(zaIspis);
                Files.write(Paths.get(putanjaDoFajla), jsonString.getBytes(), StandardOpenOption.APPEND);
            }else if (args.length == 2 && DaLiJeDan(unos.get(1)) != null) {
                DayOfWeek dan = DaLiJeDan(unos.get(1));
                List<Termin> zaIspis = new ArrayList<>();
                for (Termin obj : getRaspored()){
                    if (obj.getPocetak().getDayOfWeek() == dan){
                        zaIspis.add(obj);
                    }
                }
                String jsonString = objectMapper.writeValueAsString(zaIspis);
                Files.write(Paths.get(putanjaDoFajla), jsonString.getBytes(), StandardOpenOption.APPEND);
            }
            int flag = 0;
           List<Termin> zaIspis3 = new ArrayList<>();
           for (String s : unos) {
               if (daLiJeUDodacima(s) != null) {
                   List<Termin> zaIspis2 = daLiJeUDodacima(s);
                   zaIspis3.addAll(zaIspis2);
                   flag = 1;
               }
           }
           if (flag == 1){
               String jsonString = objectMapper.writeValueAsString(zaIspis3);
               Files.write(Paths.get(putanjaDoFajla), jsonString.getBytes(), StandardOpenOption.APPEND);
           }

           int flag2 = 0;
           List<Termin> zaIspis4 = new ArrayList<>();
           for (String s : unos){
               for (Termin obj : getRaspored()){
                   if (obj.getProstor().getIme().equals(s)){
                       zaIspis4.add(obj);
                       flag2 = 1;
                   }
               }
           }
           if (flag2 == 1){
               String jsonString = objectMapper.writeValueAsString(zaIspis4);
               Files.write(Paths.get(putanjaDoFajla), jsonString.getBytes(), StandardOpenOption.APPEND);
           }

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }



    public void exportPDF(String... args){
      File file = new File(args[0]);
        List<String> unos = new ArrayList<>(Arrays.asList(args));
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Table table = new Table(5);
            if (args.length == 1){
                for (Termin t : getRaspored()){
                    table.addCell(new Cell().add(new Paragraph(t.getProstor().getIme())));
                    table.addCell(new Cell().add(new Paragraph(t.getPocetak().toString())));
                    table.addCell(new Cell().add(new Paragraph(t.getKraj().toString())));
                    table.addCell(new Cell().add(new Paragraph(t.getDodaci().toString())));
                    table.addCell(new Cell().add(new Paragraph(t.getTipZakazivanja().toString())));
                }
            }else if (args.length == 3 && unos.get(1).contains(".") && unos.get(2).contains(".")) {
                LocalDateTime pocetniDatum = datum(args[1]);
                LocalDateTime krajniDatum = datum(args[2]);
                for (LocalDateTime datumm = pocetniDatum; datumm.isBefore(krajniDatum); datumm = datumm.plusDays(1))
                    for (Termin obj : getRaspored()) {
                        if (datumm.getYear() == obj.getPocetak().getYear() && datumm.getMonth() == obj.getPocetak().getMonth() && datumm.getDayOfMonth() == obj.getPocetak().getDayOfMonth()){

                                table.addCell(new Cell().add(new Paragraph(obj.getProstor().getIme())));
                                table.addCell(new Cell().add(new Paragraph(obj.getPocetak().toString())));
                                table.addCell(new Cell().add(new Paragraph(obj.getKraj().toString())));
                                table.addCell(new Cell().add(new Paragraph(obj.getDodaci().toString())));
                                table.addCell(new Cell().add(new Paragraph(obj.getTipZakazivanja().toString())));

                        }
                    }
            }else if (args.length == 2 && DaLiJeDan(unos.get(1)) != null) {
                DayOfWeek dan = DaLiJeDan(unos.get(1));
                for (Termin obj : getRaspored()){
                    if (obj.getPocetak().getDayOfWeek() == dan){
                        table.addCell(new Cell().add(new Paragraph(obj.getProstor().getIme())));
                        table.addCell(new Cell().add(new Paragraph(obj.getPocetak().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getKraj().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getDodaci().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getTipZakazivanja().toString())));
                    }
                }

            }

            for (String s : unos) {
                if (daLiJeUDodacima(s) != null) {
                    List<Termin> zaIspis = daLiJeUDodacima(s);
                    for (Termin obj : zaIspis) {
                        table.addCell(new Cell().add(new Paragraph(obj.getProstor().getIme())));
                        table.addCell(new Cell().add(new Paragraph(obj.getPocetak().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getKraj().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getDodaci().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getTipZakazivanja().toString())));
                    }

                }
            }

            for (String s : unos){
                for (Termin obj : getRaspored()){
                    if (obj.getProstor().getIme().equals(s)){
                        table.addCell(new Cell().add(new Paragraph(obj.getProstor().getIme())));
                        table.addCell(new Cell().add(new Paragraph(obj.getPocetak().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getKraj().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getDodaci().toString())));
                        table.addCell(new Cell().add(new Paragraph(obj.getTipZakazivanja().toString())));
                    }
                }

            }

            document.add(table);
            document.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void exportCSV(String... args){
        List<String> unos = new ArrayList<>(Arrays.asList(args));

        try (Writer writer = new FileWriter(args[0]);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            if (args.length == 1) {
                for (Termin obj : getRaspored()) {
                    csvPrinter.printRecord(obj.getProstor().getIme(), obj.getPocetak(), obj.getKraj(), obj.getDodaci(), obj.getTipZakazivanja());
                }
            } else if (args.length == 3 && unos.get(1).contains(".") && unos.get(2).contains(".")) {
                LocalDateTime pocetniDatum = datum(args[1]);
                LocalDateTime krajniDatum = datum(args[2]);
                System.out.println(pocetniDatum);
                System.out.println(krajniDatum);
                for (LocalDateTime datumm = pocetniDatum; datumm.isBefore(krajniDatum); datumm = datumm.plusDays(1))
                for (Termin obj : getRaspored()) {
                    if (datumm.getYear() == obj.getPocetak().getYear() && datumm.getMonth() == obj.getPocetak().getMonth() && datumm.getDayOfMonth() == obj.getPocetak().getDayOfMonth()){
                        csvPrinter.printRecord(obj.getProstor().getIme(), obj.getPocetak(), obj.getKraj(), obj.getDodaci(), obj.getTipZakazivanja());
                        System.out.println("ispisan");
                }
                }
            } else if (args.length == 2 && DaLiJeDan(unos.get(1)) != null) {
                DayOfWeek dan = DaLiJeDan(unos.get(1));
                for (Termin obj : getRaspored()){
                    if (obj.getPocetak().getDayOfWeek() == dan){
                        csvPrinter.printRecord(obj.getProstor().getIme(), obj.getPocetak(), obj.getKraj(), obj.getDodaci(), obj.getTipZakazivanja());
                    }
                }

            }

            for (String s : unos) {
                if (daLiJeUDodacima(s) != null) {
                    List<Termin> zaIspis = daLiJeUDodacima(s);
                    for (Termin obj : zaIspis) {
                        csvPrinter.printRecord(obj.getProstor().getIme(), obj.getPocetak(), obj.getKraj(), obj.getDodaci(), obj.getTipZakazivanja());
                    }

                }
            }

            for (String s : unos){
                for (Termin obj : getRaspored()){
                    if (obj.getProstor().getIme().equals(s)){
                            csvPrinter.printRecord(obj.getProstor().getIme(), obj.getPocetak(), obj.getKraj(), obj.getDodaci(), obj.getTipZakazivanja());
                    }
                }

            }


            csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importovanjeJson(String putanja){

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String jsonContent = Files.readString(new File(putanja).toPath());
            List<Termin> zaDodavanje = objectMapper.readValue(jsonContent, new TypeReference<List<Termin>>() {});
            getRaspored().addAll(zaDodavanje);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void importovanjeCSV(String putanja){
       try {
           Reader reader = new FileReader(putanja);
           CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

           for (CSVRecord csvRecord : csvParser){
               String imeSobe = csvRecord.get(0);
               Termin t = new Termin();
               t.setProstor(nadjiSobu(imeSobe));

               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

               LocalDateTime pocetak = LocalDateTime.parse(csvRecord.get(1),formatter);
               LocalDateTime kraj = LocalDateTime.parse(csvRecord.get(2),formatter);
               t.setPocetak(pocetak);
               t.setKraj(kraj);

               String dodaci = csvRecord.get(3);

               Pattern pattern = Pattern.compile("\\b(\\w+)=(\\w+(?:\\s+\\w+)?)\\b");
               Matcher matcher = pattern.matcher(dodaci);
               Map<String,String> dodadaciZaSet = new HashMap<>();

               while (matcher.find()){
                   String kljuc = matcher.group(1);
                   String vrednost = matcher.group(2);

                   dodadaciZaSet.put(kljuc,vrednost);
               }
               t.setDodaci(dodadaciZaSet);
               //System.out.println(csvRecord.get(4));
               if (csvRecord.get(4).equals("DRIGA_IMP")){
                   t.setTipZakazivanja(PrvaDrugaImp.DRIGA_IMP);
                   System.out.println("drugaaaaaaaaa");
               } else if (csvRecord.get(4).equals("PRVA_IMP")) {
                   t.setTipZakazivanja(PrvaDrugaImp.PRVA_IMP);
               }

               getRaspored().add(t);
           }




       }catch (IOException e){

       }
    }

    private Prostor nadjiSobu(String ime){
        for (Prostor p : getProstori()){
            if (p.getIme() == ime){
                return p;
            }
        }
        return null;
    }

    private DayOfWeek DaLiJeDan(String dan){
        List<String> daniUNedelji = new ArrayList<>();
        daniUNedelji.add("ponedeljak");
        daniUNedelji.add("utorak");
        daniUNedelji.add("sreda");
        daniUNedelji.add("cetvrtak");
        daniUNedelji.add("petak");
        daniUNedelji.add("subota");
        daniUNedelji.add("nedelja");

        DayOfWeek danEng = null;
        if (daniUNedelji.contains(dan)){
            switch (dan){
                case "ponedeljak":
                    danEng = DayOfWeek.MONDAY;
                    break;
                case "utorak":
                    danEng = DayOfWeek.TUESDAY;
                    break;
                case "sreda":
                    danEng = DayOfWeek.WEDNESDAY;
                    break;
                case "cetvrtak":
                    danEng = DayOfWeek.THURSDAY;
                    break;
                case "petak":
                    danEng = DayOfWeek.FRIDAY;
                    break;
                case "subota":
                    danEng = DayOfWeek.SATURDAY;
                    break;
                case "nedelja":
                    danEng = DayOfWeek.SUNDAY;
                    break;
            }
            return danEng;
        }else {
            return null;
        }
    }

    private List<Termin> daLiJeUDodacima(String tekst){
        if (!tekst.contains(":")){
            return null;
        }
        String kljuc = tekst.split(":")[0];
        String vrednost = tekst.split(":")[1];

        List<Termin> imaju = new ArrayList<>();

        for (Termin t : getRaspored()){
            if (t.getDodaci().containsKey(kljuc) && t.getDodaci().containsValue(vrednost)){
                imaju.add(t);
            }
        }
        if (imaju.isEmpty()){
            return null;
        }else {
            return imaju;
        }



    }
    private LocalDateTime datum(String datumm){
        int dan,mesec,godina;

        dan = Integer.parseInt(String.valueOf(datumm.charAt(0)))*10 + Integer.parseInt(String.valueOf(datumm.charAt(1)));

        mesec = Integer.parseInt(String.valueOf(datumm.charAt(3)))*10 + Integer.parseInt(String.valueOf(datumm.charAt(4)));

        godina = Integer.parseInt(String.valueOf(datumm.charAt(6)))*1000 + Integer.parseInt(String.valueOf(datumm.charAt(7)))*100 + Integer.parseInt(String.valueOf(datumm.charAt(8)))*10 + Integer.parseInt(String.valueOf(datumm.charAt(9)));

        LocalDateTime vrati = LocalDateTime.of(godina,mesec,dan,0,0);

        return vrati;

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

    public static ObradaTermina getObj() {
//        System.out.println(obj);
        return obj;
    }

    public static void setObj(ObradaTermina newObj) {
        obj = newObj;
    }
}
