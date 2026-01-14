import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
public class TanamanGUI extends JFrame {
    private InventoryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public TanamanGUI() {
        manager = new InventoryManager();
        initComponents();
        loadTableData();
    }
    private void initComponents() {
        setTitle("🌸 Sistem Pengelolaan Inventaris Tanaman Hias");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(createDashboardPanel(), "Dashboard");
        mainPanel.add(createTambahPanel(), "Tambah");
        mainPanel.add(createUpdatePanel(), "Update");

        add(mainPanel);
    }
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        JLabel titleLabel = new JLabel("🌸 INVENTARIS TANAMAN HIAS 🌸");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        // Table
        String[] columns = {"ID", "Nama Tanaman", "Jenis", "Stok", "Kondisi", "Tipe", "Info Tambahan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(200, 230, 201));

        // Custom renderer for kondisi column
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String kondisi = value.toString();
                if (kondisi.equals("Sehat")) {
                    c.setBackground(new Color(200, 230, 201));
                    c.setForeground(new Color(46, 125, 50));
                } else if (kondisi.equals("Perlu Perawatan")) {
                    c.setBackground(new Color(255, 249, 196));
                    c.setForeground(new Color(245, 127, 23));
                } else if (kondisi.equals("Layu")) {
                    c.setBackground(new Color(255, 205, 210));
                    c.setForeground(new Color(198, 40, 40));
                }
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                }
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnTambah = createStyledButton("➕ Tambah", new Color(76, 175, 80));
        JButton btnUpdate = createStyledButton("🔄 Update", new Color(33, 150, 243));
        JButton btnHapus = createStyledButton("🗑️ Hapus", new Color(244, 67, 54));
        JButton btnLaporan = createStyledButton("📊 Laporan", new Color(156, 39, 176));
        JButton btnRefresh = createStyledButton("🔄 Refresh", new Color(96, 125, 139));

        btnTambah.addActionListener(e -> cardLayout.show(mainPanel, "Tambah"));
        btnUpdate.addActionListener(e -> cardLayout.show(mainPanel, "Update"));
        btnHapus.addActionListener(e -> hapusTanaman());
        btnLaporan.addActionListener(e -> showLaporan());
        btnRefresh.addActionListener(e -> loadTableData());

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnLaporan);
        buttonPanel.add(btnRefresh);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createTambahPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(76, 175, 80));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel titleLabel = new JLabel("➕ TAMBAH DATA TANAMAN HIAS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtId = new JTextField(25);
        JTextField txtNama = new JTextField(25);
        JTextField txtJenis = new JTextField(25);
        JTextField txtStok = new JTextField(25);
        JComboBox<String> cmbKondisi = new JComboBox<>(new String[]{"Sehat", "Perlu Perawatan", "Layu"});
        JComboBox<String> cmbTipe = new JComboBox<>(new String[]{"Tanaman Biasa", "Tanaman Indoor", "Tanaman Outdoor"});
        JComboBox<String> cmbCahaya = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        JCheckBox chkTahanHujan = new JCheckBox("Tahan Hujan");
        JPanel extraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        extraPanel.setBackground(new Color(250, 250, 250));

        addFormRow(formPanel, gbc, 0, "ID Tanaman:", txtId);
        addFormRow(formPanel, gbc, 1, "Nama Tanaman:", txtNama);
        addFormRow(formPanel, gbc, 2, "Jenis:", txtJenis);
        addFormRow(formPanel, gbc, 3, "Stok:", txtStok);
        addFormRow(formPanel, gbc, 4, "Kondisi:", cmbKondisi);
        addFormRow(formPanel, gbc, 5, "Tipe:", cmbTipe);
        addFormRow(formPanel, gbc, 6, "Info Tambahan:", extraPanel);

        cmbTipe.addActionListener(e -> {
            extraPanel.removeAll();
            String tipe = (String) cmbTipe.getSelectedItem();
            if (tipe.equals("Tanaman Indoor")) {
                extraPanel.add(new JLabel("Kebutuhan Cahaya:"));
                extraPanel.add(cmbCahaya);
            } else if (tipe.equals("Tanaman Outdoor")) {
                extraPanel.add(chkTahanHujan);
            }
            extraPanel.revalidate();
            extraPanel.repaint();
        });

        panel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnSimpan = createStyledButton("💾 Simpan", new Color(76, 175, 80));
        JButton btnBatal = createStyledButton("❌ Batal", new Color(244, 67, 54));

        btnSimpan.addActionListener(e -> {
            try {
                String id = txtId.getText().trim();
                String nama = txtNama.getText().trim();
                String jenis = txtJenis.getText().trim();
                int stok = Integer.parseInt(txtStok.getText().trim());
                String kondisi = (String) cmbKondisi.getSelectedItem();
                String tipe = (String) cmbTipe.getSelectedItem();

                if (id.isEmpty() || nama.isEmpty() || jenis.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua field harus diisi!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Tanaman tanaman = null;
                if (tipe.equals("Tanaman Indoor")) {
                    String cahaya = (String) cmbCahaya.getSelectedItem();
                    tanaman = new TanamanHiasIndoor(id, nama, jenis, stok, kondisi, cahaya);
                } else if (tipe.equals("Tanaman Outdoor")) {
                    boolean tahan = chkTahanHujan.isSelected();
                    tanaman = new TanamanHiasOutdoor(id, nama, jenis, stok, kondisi, tahan);
                } else {
                    tanaman = new Tanaman(id, nama, jenis, stok, kondisi);
                }

                manager.tambahTanaman(tanaman);
                loadTableData();
                clearForm(txtId, txtNama, txtJenis, txtStok);
                cmbKondisi.setSelectedIndex(0);
                cmbTipe.setSelectedIndex(0);
                cardLayout.show(mainPanel, "Dashboard");
                JOptionPane.showMessageDialog(this, "✓ Tanaman berhasil ditambahkan!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stok harus berupa angka!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBatal.addActionListener(e -> {
            clearForm(txtId, txtNama, txtJenis, txtStok);
            cardLayout.show(mainPanel, "Dashboard");
        });

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel titleLabel = new JLabel("🔄 UPDATE DATA TANAMAN HIAS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(250, 250, 250));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtId = new JTextField(20);
        JTextField txtNama = new JTextField(25);
        JTextField txtJenis = new JTextField(25);
        JTextField txtStok = new JTextField(25);
        JComboBox<String> cmbKondisi = new JComboBox<>(new String[]{"Sehat", "Perlu Perawatan", "Layu"});
        JButton btnCari = createStyledButton("🔍 Cari", new Color(33, 150, 243));

        addFormRow(formPanel, gbc, 0, "ID Tanaman:", txtId);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(btnCari, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 1, "Nama Baru:", txtNama);
        addFormRow(formPanel, gbc, 2, "Jenis Baru:", txtJenis);
        addFormRow(formPanel, gbc, 3, "Stok Baru:", txtStok);
        addFormRow(formPanel, gbc, 4, "Kondisi Baru:", cmbKondisi);

        btnCari.addActionListener(e -> {
            String id = txtId.getText().trim();
            Tanaman tanaman = manager.cariTanaman(id);
            if (tanaman != null) {
                txtNama.setText(tanaman.getNama());
                txtJenis.setText(tanaman.getJenis());
                txtStok.setText(String.valueOf(tanaman.getStok()));
                cmbKondisi.setSelectedItem(tanaman.getKondisi());
                JOptionPane.showMessageDialog(this, "✓ Tanaman ditemukan!",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "✗ Tanaman tidak ditemukan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnUpdate = createStyledButton("💾 Update", new Color(33, 150, 243));
        JButton btnBatal = createStyledButton("❌ Batal", new Color(244, 67, 54));

        btnUpdate.addActionListener(e -> {
            String id = txtId.getText().trim();
            String nama = txtNama.getText().trim();
            String jenis = txtJenis.getText().trim();
            String stokStr = txtStok.getText().trim();
            String kondisi = (String) cmbKondisi.getSelectedItem();

            Integer stok = null;
            if (!stokStr.isEmpty()) {
                try {
                    stok = Integer.parseInt(stokStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Stok harus berupa angka!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (manager.updateTanaman(id, nama, jenis, stok, kondisi)) {
                loadTableData();
                clearForm(txtId, txtNama, txtJenis, txtStok);
                cardLayout.show(mainPanel, "Dashboard");
                JOptionPane.showMessageDialog(this, "✓ Tanaman berhasil diupdate!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnBatal.addActionListener(e -> {
            clearForm(txtId, txtNama, txtJenis, txtStok);
            cardLayout.show(mainPanel, "Dashboard");
        });

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnBatal);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(140, 40));
        return button;
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Tanaman> daftarTanaman = manager.getDaftarTanaman();
        for (Tanaman t : daftarTanaman) {
            String infoTambahan = "-";
            if (t instanceof TanamanHiasIndoor) {
                infoTambahan = "Cahaya: " + ((TanamanHiasIndoor) t).getKebutuhanCahaya();
            } else if (t instanceof TanamanHiasOutdoor) {
                infoTambahan = ((TanamanHiasOutdoor) t).isTahanHujan() ? "Tahan Hujan" : "Tidak Tahan Hujan";
            }
            tableModel.addRow(new Object[]{
                    t.getIdTanaman(),
                    t.getNama(),
                    t.getJenis(),
                    t.getStok(),
                    t.getKondisi(),
                    t.getTipe(),
                    infoTambahan
            });
        }
    }

    private void hapusTanaman() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih tanaman yang akan dihapus!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus tanaman ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            manager.hapusTanaman(id);
            loadTableData();
            JOptionPane.showMessageDialog(this, "✓ Tanaman berhasil dihapus!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showLaporan() {
        List<Tanaman> daftarTanaman = manager.getDaftarTanaman();
        if (daftarTanaman.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Belum ada data untuk laporan!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int totalStok = 0, sehat = 0, perluPerawatan = 0, layu = 0;
        for (Tanaman t : daftarTanaman) {
            totalStok += t.getStok();
            switch (t.getKondisi()) {
                case "Sehat": sehat++; break;
                case "Perlu Perawatan": perluPerawatan++; break;
                case "Layu": layu++; break;
            }
        }

        String laporan = String.format(
                "📊 LAPORAN INVENTARIS TANAMAN HIAS\n\n" +
                        "════════════════════════════════════════\n\n" +
                        "Total Jenis Tanaman    : %d\n" +
                        "Total Stok Keseluruhan : %d\n\n" +
                        "Kondisi Tanaman:\n" +
                        "  🌿 Sehat              : %d tanaman\n" +
                        "  🌱 Perlu Perawatan    : %d tanaman\n" +
                        "  🥀 Layu               : %d tanaman\n\n" +
                        "════════════════════════════════════════",
                daftarTanaman.size(), totalStok, sehat, perluPerawatan, layu
        );

        JOptionPane.showMessageDialog(this, laporan, "Laporan Inventaris",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm(JTextField... fields) {
        for (JTextField field : fields) {
            if (field != null) field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TanamanGUI().setVisible(true);
        });
    }
}