import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class InventoryManager {
    private List<Tanaman> daftarTanaman;
    private String filename;
    public InventoryManager(String filename) {
        this.daftarTanaman = new ArrayList<>();
        this.filename = filename;
        muatData();
    }
    public InventoryManager() {
        this("data_tanaman.txt");
    }
    public List<Tanaman> getDaftarTanaman() {
        return daftarTanaman;
    }
    public void tambahTanaman(Tanaman tanaman) {
        daftarTanaman.add(tanaman);
        simpanData();
        System.out.println("\n✓ Tanaman '" + tanaman.getNama() + "' berhasil ditambahkan!");
    }
    public void lihatSemuaTanaman() {
        if (daftarTanaman.isEmpty()) {
            System.out.println("\n⚠ Belum ada data tanaman!");
            return;
        }

        System.out.println("\n" + "=".repeat(110));
        System.out.println("                           📋 DAFTAR INVENTARIS TANAMAN HIAS 📋");
        System.out.println("=".repeat(110));
        System.out.printf("%-6s | %-20s | %-15s | %-5s | %-17s | %s%n",
                "ID", "Nama Tanaman", "Jenis", "Stok", "Kondisi", "Info Tambahan");
        System.out.println("-".repeat(110));

        for (Tanaman tanaman : daftarTanaman) {
            System.out.println(tanaman.tampilkanInfo());
        }

        System.out.println("=".repeat(110));
        System.out.println("Total Tanaman: " + daftarTanaman.size());
    }

    public Tanaman cariTanaman(String idTanaman) {
        for (Tanaman tanaman : daftarTanaman) {
            if (tanaman.getIdTanaman().equals(idTanaman)) {
                return tanaman;
            }
        }
        return null;
    }

    public boolean updateTanaman(String idTanaman, String nama, String jenis,
                                 Integer stok, String kondisi) {
        Tanaman tanaman = cariTanaman(idTanaman);
        if (tanaman != null) {
            if (nama != null && !nama.isEmpty()) {
                tanaman.setNama(nama);
            }
            if (jenis != null && !jenis.isEmpty()) {
                tanaman.setJenis(jenis);
            }
            if (stok != null) {
                tanaman.setStok(stok);
            }
            if (kondisi != null && !kondisi.isEmpty()) {
                tanaman.setKondisi(kondisi);
            }
            simpanData();
            System.out.println("\n✓ Data tanaman ID " + idTanaman + " berhasil diupdate!");
            return true;
        } else {
            System.out.println("\n✗ Tanaman dengan ID " + idTanaman + " tidak ditemukan!");
            return false;
        }
    }

    public boolean hapusTanaman(String idTanaman) {
        Tanaman tanaman = cariTanaman(idTanaman);
        if (tanaman != null) {
            daftarTanaman.remove(tanaman);
            simpanData();
            System.out.println("\n✓ Tanaman '" + tanaman.getNama() + "' berhasil dihapus!");
            return true;
        } else {
            System.out.println("\n✗ Tanaman dengan ID " + idTanaman + " tidak ditemukan!");
            return false;
        }
    }

    public void simpanData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Tanaman tanaman : daftarTanaman) {
                writer.write(tanaman.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saat menyimpan data: " + e.getMessage());
        }
    }
    public void muatData() {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File data belum ada. Akan dibuat saat menyimpan data pertama kali.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String id = data[0];
                    String nama = data[1];
                    String jenis = data[2];
                    int stok = Integer.parseInt(data[3]);
                    String kondisi = data[4];
                    Tanaman tanaman;
                    if (data.length == 7) {
                        if (data[6].equals("Indoor")) {
                            String cahaya = data[5];
                            tanaman = new TanamanHiasIndoor(id, nama, jenis, stok, kondisi, cahaya);
                        } else if (data[6].equals("Outdoor")) {
                            boolean tahan = Boolean.parseBoolean(data[5]);
                            tanaman = new TanamanHiasOutdoor(id, nama, jenis, stok, kondisi, tahan);
                        } else {
                            tanaman = new Tanaman(id, nama, jenis, stok, kondisi);
                        }
                    } else {
                        tanaman = new Tanaman(id, nama, jenis, stok, kondisi);
                    }

                    daftarTanaman.add(tanaman);
                }
            }
        } catch (IOException e) {
            System.out.println("Error saat memuat data: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error format data: " + e.getMessage());
        }
    }

    public void laporanInventaris() {
        if (daftarTanaman.isEmpty()) {
            System.out.println("\n⚠ Belum ada data untuk laporan!");
            return;
        }

        int totalStok = 0;
        int sehat = 0;
        int perluPerawatan = 0;
        int layu = 0;

        for (Tanaman t : daftarTanaman) {
            totalStok += t.getStok();
            String kondisi = t.getKondisi();
            if (kondisi.equals("Sehat")) sehat++;
            else if (kondisi.equals("Perlu Perawatan")) perluPerawatan++;
            else if (kondisi.equals("Layu")) layu++;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("              📊 LAPORAN INVENTARIS TANAMAN HIAS 📊");
        System.out.println("=".repeat(60));
        System.out.println("Total Jenis Tanaman    : " + daftarTanaman.size());
        System.out.println("Total Stok Keseluruhan : " + totalStok);
        System.out.println("\nKondisi Tanaman:");
        System.out.println("  🌿 Sehat              : " + sehat + " tanaman");
        System.out.println("  🌱 Perlu Perawatan    : " + perluPerawatan + " tanaman");
        System.out.println("  🥀 Layu               : " + layu + " tanaman");
        System.out.println("=".repeat(60));
    }
}