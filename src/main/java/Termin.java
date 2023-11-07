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
}
