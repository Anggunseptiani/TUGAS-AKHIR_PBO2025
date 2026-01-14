public class TanamanHiasIndoor extends Tanaman {
    private String kebutuhanCahaya;
    public TanamanHiasIndoor(String idTanaman, String nama, String jenis,
                             int stok, String kondisi, String kebutuhanCahaya) {
        super(idTanaman, nama, jenis, stok, kondisi);
        this.kebutuhanCahaya = kebutuhanCahaya;
    }
    public String getKebutuhanCahaya() {
        return kebutuhanCahaya;
    }
    public void setKebutuhanCahaya(String kebutuhanCahaya) {
        this.kebutuhanCahaya = kebutuhanCahaya;
    }
    @Override
    public String tampilkanInfo() {
        String infoDasar = super.tampilkanInfo();
        return String.format("%s | Indoor (Cahaya: %s)", infoDasar, kebutuhanCahaya);
    }
    @Override
    public String toString() {
        return String.format("%s,%s,Indoor", super.toString(), kebutuhanCahaya);
    }
    @Override
    public String getTipe() {
        return "Indoor";
    }
}