import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Termin {
    /**
     * koji se prostor koristi
     */
    private Prostor prostor;
    /**
     * vremenski pocetak termina
     */
    private LocalDateTime pocetak;
    /**
     * vremenski karj tremina
     */
    private LocalDateTime kraj;
    /**
     * sta sve termin moze da ima dodatno:
     * naziv predmeta, koja grupa slusa, tip casa (vezbe ili predavanje)
     */
    private Map<String, String> dodaci;
    private int period;
    private PrvaDrugaImp tipZakazivanja;

    public Termin() {
        this.dodaci = new HashMap<>();
    }

    public Termin(Prostor prostor, LocalDateTime pocetak, LocalDateTime kraj) {
        this.prostor = prostor;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.dodaci = new HashMap<>();
    }

    public Termin(Prostor prostor, LocalDateTime pocetak, LocalDateTime kraj, Map<String, String> dodaci, int period) {
        this.prostor = prostor;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.period = period;
        this.dodaci = new HashMap<>();
    }

    public Prostor getProstor() {
        return prostor;
    }

    public void setProstor(Prostor prostor) {
        this.prostor = prostor;
    }

    public LocalDateTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDateTime pocetak) {
        this.pocetak = pocetak;
    }

    public LocalDateTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalDateTime kraj) {
        this.kraj = kraj;
    }

    public Map<String, String> getDodaci() {
        return dodaci;
    }

    public void setDodaci(Map<String, String> dodaci) {
        this.dodaci = dodaci;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public PrvaDrugaImp getTipZakazivanja() {
        return tipZakazivanja;
    }

    public void setTipZakazivanja(PrvaDrugaImp tipZakazivanja) {
        this.tipZakazivanja = tipZakazivanja;
    }

    @Override
    public String toString() {
        return this.pocetak + " " + this.kraj;
    }
}
