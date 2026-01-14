import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static InventoryManager manager = new InventoryManager();

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║  Pilih Mode Aplikasi:                          ║");
        System.out.println("║  1. Mode Console (CLI)                         ║");
        System.out.println("║  2. Mode GUI (Graphical Interface)             ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("Pilihan Anda (1/2): ");

        String pilihan = scanner.nextLine();

        if (pilihan.equals("2")) {
            // Jalankan GUI
            javax.swing.SwingUtilities.invokeLater(() -> {
                new TanamanGUI().setVisible(true);
            });
        } else {
            // Jalankan Console
            runConsoleMode();
        }
    }

    private static void runConsoleMode() {
        boolean running = true;

        while (running) {
            tampilkanMenu();
            System.out.print("\nPilih menu (0-5): ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tambahTanamanMenu();
                    break;
                case "2":
                    manager.lihatSemuaTanaman();
                    break;
                case "3":
                    updateTanamanMenu();
                    break;
                case "4":
                    hapusTanamanMenu();
                    break;
                case "5":
                    manager.laporanInventaris();
                    break;
                case "0":
                    System.out.println("\n" + "=".repeat(50));
                    System.out.println("   Terima kasih telah menggunakan SPIM! 🌸");
                    System.out.println("=".repeat(50));
                    running = false;
                    break;
                default:
                    System.out.println("\n⚠ Pilihan tidak valid! Silakan pilih 0-5");
            }

            if (running) {
                System.out.print("\nTekan Enter untuk melanjutkan...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void tampilkanMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        🌸 SISTEM PENGELOLAAN INVENTARIS 🌸");
        System.out.println("          TANAMAN HIAS (SPIM) v1.0");
        System.out.println("=".repeat(50));
        System.out.println("1. Tambah Data Tanaman");
        System.out.println("2. Lihat Data Tanaman");
        System.out.println("3. Update Data Tanaman");
        System.out.println("4. Hapus Data Tanaman");
        System.out.println("5. Laporan Inventaris");
        System.out.println("0. Keluar");
        System.out.println("=".repeat(50));
    }

    private static void tambahTanamanMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("         ➕ TAMBAH DATA TANAMAN HIAS");
        System.out.println("-".repeat(50));

        System.out.print("ID Tanaman (ex: T001): ");
        String id = scanner.nextLine();

        System.out.print("Nama Tanaman: ");
        String nama = scanner.nextLine();

        System.out.print("Jenis Tanaman: ");
        String jenis = scanner.nextLine();

        int stok = 0;
        boolean validStok = false;
        while (!validStok) {
            try {
                System.out.print("Jumlah Stok: ");
                stok = Integer.parseInt(scanner.nextLine());
                validStok = true;
            } catch (NumberFormatException e) {
                System.out.println("⚠ Stok harus berupa angka!");
            }
        }

        System.out.println("\nKondisi Tanaman:");
        System.out.println("1. Sehat");
        System.out.println("2. Perlu Perawatan");
        System.out.println("3. Layu");
        System.out.print("Pilih kondisi (1-3): ");
        String pilihanKondisi = scanner.nextLine();

        String kondisi = "Sehat";
        switch (pilihanKondisi) {
            case "1": kondisi = "Sehat"; break;
            case "2": kondisi = "Perlu Perawatan"; break;
            case "3": kondisi = "Layu"; break;
        }

        System.out.println("\nTipe Tanaman:");
        System.out.println("1. Tanaman Biasa");
        System.out.println("2. Tanaman Indoor (dengan kebutuhan cahaya)");
        System.out.println("3. Tanaman Outdoor (dengan ketahanan hujan)");
        System.out.print("Pilih tipe (1-3): ");
        String tipe = scanner.nextLine();

        Tanaman tanaman = null;

        if (tipe.equals("2")) {
            System.out.println("\nKebutuhan Cahaya:");
            System.out.println("1. Low (Rendah)");
            System.out.println("2. Medium (Sedang)");
            System.out.println("3. High (Tinggi)");
            System.out.print("Pilih (1-3): ");
            String cahayaPilih = scanner.nextLine();

            String cahaya = "Medium";
            switch (cahayaPilih) {
                case "1": cahaya = "Low"; break;
                case "2": cahaya = "Medium"; break;
                case "3": cahaya = "High"; break;
            }

            tanaman = new TanamanHiasIndoor(id, nama, jenis, stok, kondisi, cahaya);

        } else if (tipe.equals("3")) {
            System.out.print("Tahan hujan? (y/n): ");
            String tahanInput = scanner.nextLine();
            boolean tahan = tahanInput.equalsIgnoreCase("y");

            tanaman = new TanamanHiasOutdoor(id, nama, jenis, stok, kondisi, tahan);

        } else {
            tanaman = new Tanaman(id, nama, jenis, stok, kondisi);
        }

        if (tanaman != null) {
            manager.tambahTanaman(tanaman);
        }
    }

    private static void updateTanamanMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("         🔄 UPDATE DATA TANAMAN HIAS");
        System.out.println("-".repeat(50));

        System.out.print("Masukkan ID Tanaman yang akan diupdate: ");
        String id = scanner.nextLine();

        Tanaman tanaman = manager.cariTanaman(id);

        if (tanaman == null) {
            System.out.println("\n✗ Tanaman dengan ID " + id + " tidak ditemukan!");
            return;
        }

        System.out.println("\nData saat ini: " + tanaman.getNama() +
                " | Jenis: " + tanaman.getJenis() +
                " | Stok: " + tanaman.getStok() +
                " | Kondisi: " + tanaman.getKondisi());
        System.out.println("\nBiarkan kosong jika tidak ingin mengubah field tersebut");

        System.out.print("Nama Tanaman baru: ");
        String nama = scanner.nextLine();

        System.out.print("Jenis Tanaman baru: ");
        String jenis = scanner.nextLine();

        System.out.print("Jumlah Stok baru: ");
        String stokInput = scanner.nextLine();
        Integer stok = null;
        if (!stokInput.isEmpty()) {
            try {
                stok = Integer.parseInt(stokInput);
            } catch (NumberFormatException e) {
                System.out.println("⚠ Stok tidak valid, tidak diubah");
            }
        }
        System.out.println("\nKondisi Tanaman:");
        System.out.println("1. Sehat");
        System.out.println("2. Perlu Perawatan");
        System.out.println("3. Layu");
        System.out.println("0. Tidak ubah");
        System.out.print("Pilih kondisi: ");
        String pilihanKondisi = scanner.nextLine();

        String kondisi = null;
        switch (pilihanKondisi) {
            case "1": kondisi = "Sehat"; break;
            case "2": kondisi = "Perlu Perawatan"; break;
            case "3": kondisi = "Layu"; break;
        }

        manager.updateTanaman(id, nama, jenis, stok, kondisi);
    }

    private static void hapusTanamanMenu() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("         🗑️  HAPUS DATA TANAMAN HIAS");
        System.out.println("-".repeat(50));

        System.out.print("Masukkan ID Tanaman yang akan dihapus: ");
        String id = scanner.nextLine();

        Tanaman tanaman = manager.cariTanaman(id);

        if (tanaman != null) {
            System.out.println("\nTanaman yang akan dihapus: " + tanaman.getNama());
            System.out.print("Yakin ingin menghapus? (y/n): ");
            String konfirmasi = scanner.nextLine();

            if (konfirmasi.equalsIgnoreCase("y")) {
                manager.hapusTanaman(id);
            } else {
                System.out.println("\n✗ Penghapusan dibatalkan");
            }
        } else {
            System.out.println("\n✗ Tanaman dengan ID " + id + " tidak ditemukan!");
        }
    }
}