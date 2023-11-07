public class ZakazaniTermin {
    private Prostor prostor;
    private Termin termin;

    public ZakazaniTermin(Prostor prostor, Termin termin) {
        this.prostor = prostor;
        this.termin = termin;
    }

    public Prostor getProstor() {
        return prostor;
    }

    public void setProstor(Prostor prostor) {
        this.prostor = prostor;
    }

    public Termin getTermin() {
        return termin;
    }

    public void setTermin(Termin termin) {
        this.termin = termin;
    }
}
