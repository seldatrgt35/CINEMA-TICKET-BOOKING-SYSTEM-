public class Koltuk {
    private int koltukNumarasi;
    private boolean doluMu;

    public Koltuk(int koltukNumarasi) {
        this.koltukNumarasi = koltukNumarasi;
        this.doluMu = false; // Başlangıçta koltuklar boş
    }

    // Getter ve setter metodları

    public boolean isDoluMu() {
        return doluMu;
    }

    public void setDoluMu(boolean doluMu) {
        this.doluMu = doluMu;
    }
}
