public class Tanaman {
    private String idTanaman;
    private String nama;
    private String jenis;
    private int stok;
    private String kondisi;

    public Tanaman(String idTanaman, String nama, String jenis, int stok, String kondisi) {
        this.idTanaman = idTanaman;
        this.nama = nama;
        this.jenis = jenis;
        this.stok = stok;
        this.kondisi = kondisi;
    }

    // Getter methods
    public String getIdTanaman() { return idTanaman; }
    public String getNama() { return nama; }
    public String getJenis() { return jenis; }
    public int getStok() { return stok; }
    public String getKondisi() { return kondisi; }

    // Setter methods dengan validasi
    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public void setStok(int stok) {
        if (stok >= 0) {
            this.stok = stok;
        } else {
            System.out.println("⚠ Stok tidak boleh negatif!");
        }
    }

    public void setKondisi(String kondisi) {
        String[] kondisiValid = {"Sehat", "Perlu Perawatan", "Layu"};
        boolean valid = false;
        for (String k : kondisiValid) {
            if (k.equals(kondisi)) {
                valid = true;
                break;
            }
        }
        if (valid) {
            this.kondisi = kondisi;
        } else {
            System.out.println("⚠ Kondisi tidak valid!");
        }
    }

    public String tampilkanInfo() {
        return String.format("%-6s | %-20s | %-15s | %-5d | %-17s",
                idTanaman, nama, jenis, stok, kondisi);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%s",
                idTanaman, nama, jenis, stok, kondisi);
    }

    public String getTipe() {
        return "Biasa";
    }
}