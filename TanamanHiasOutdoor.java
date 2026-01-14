public class TanamanHiasOutdoor extends Tanaman {
    private boolean tahanHujan;
    public TanamanHiasOutdoor(String idTanaman, String nama, String jenis,
                              int stok, String kondisi, boolean tahanHujan) {
        super(idTanaman, nama, jenis, stok, kondisi);
        this.tahanHujan = tahanHujan;
    }
    public boolean isTahanHujan() {
        return tahanHujan;
    }
    public void setTahanHujan(boolean tahanHujan) {
        this.tahanHujan = tahanHujan;
    }
    @Override
    public String tampilkanInfo() {
        String infoDasar = super.tampilkanInfo();
        String tahan = tahanHujan ? "Tahan" : "Tidak Tahan";
        return String.format("%s | Outdoor (%s Hujan)", infoDasar, tahan);
    }
    @Override
    public String toString() {
        return String.format("%s,%s,Outdoor", super.toString(), tahanHujan);
    }
    @Override
    public String getTipe() {
        return "Outdoor";
    }
}