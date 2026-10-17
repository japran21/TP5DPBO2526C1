import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.Year;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;  // nama field sesuai .form, dipetakan ke kolom thn_lahir di DB
    private JComboBox<String> kategoriComboBox;
    private JTextField emailField;       // menggantikan radio button jenis kelamin
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;
    private JScrollPane scrollPane;

    // ArrayList dihapus sesuai spesifikasi TP4 — semua data diambil langsung dari database
    private int selectedIndex = -1; // -1 = belum ada baris yang diklik
    public PeopleMenu() {
        if (mainPanel == null) {
            initUI();
        }

        setTitle("People Data");

        String[] kategoriData = {
            "--- Pilih Kategori ---",
            "Full-time", "Part-time", "Contract", "Internship"
        };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        deleteButton.setVisible(false);

        // Muat data awal dari database ke tabel
        refreshTable();

        //  tombol Add / Update 
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        //  tombol Delete 
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        // tombol Cancel 
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        //  klik baris tabel 
        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex < 0) return;

                // value textfield dan combo box (all input)
                // Ambil nilai langsung dari model tabel — tanpa ArrayList
                String curId       = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama     = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curThnLahir = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curEmail    = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curThnLahir);
                kategoriComboBox.setSelectedItem(curKategori);
                emailField.setText(curEmail);

                // ubah button 'add' jadi 'Update'
                addUpdateButton.setText("Update");

                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public void refreshTable() {
        try {
            peopleTable.setModel(DatabaseHelper.getTableModel());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat data dari database:\n" + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void insertData() {
        String id       = idField.getText().trim();
        String nama     = namaField.getText().trim();
        String strTahun = tahunLahirField.getText().trim();
        String kategori = kategoriComboBox.getSelectedItem() != null
                          ? kategoriComboBox.getSelectedItem().toString() : "";
        String email    = emailField.getText().trim();

        // Validasi: tidak boleh ada kolom yang kosong
        if (id.isEmpty() || nama.isEmpty() || strTahun.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Semua kolom wajib diisi!\nID, Nama, Tahun Lahir, dan Email tidak boleh kosong.",
                "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //  Validasi: kategori harus dipilih 
        if (kategori.equals("--- Pilih Kategori ---") || kategori.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Silakan pilih Kategori terlebih dahulu!",
                "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi: tahun lahir harus angka
        int thnLahir;
        try {
            thnLahir = Integer.parseInt(strTahun);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Tahun Lahir harus berupa angka!",
                "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int currentYear = Year.now().getValue();
        if (thnLahir < 1920 || thnLahir > currentYear) {
            JOptionPane.showMessageDialog(this,
                "Tahun Lahir harus antara 1920 hingga " + currentYear + "!",
                "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //  Validasi: cek duplikat ID di database 
        try {
            if (DatabaseHelper.isIdExist(id)) {
                JOptionPane.showMessageDialog(this,
                    "ID \"" + id + "\" sudah ada di database!\nGunakan ID yang berbeda.",
                    "ID Duplikat", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memeriksa ID:\n" + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //  Simpan ke database 
        try {
            boolean berhasil = DatabaseHelper.insertPerson(
                new Person(id, nama, thnLahir, kategori, email)
            );

            if (berhasil) {
                refreshTable();
                clearForm();
                System.out.println("Insert berhasil: " + id);
                JOptionPane.showMessageDialog(this,
                    "Data berhasil ditambahkan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Fallback: duplicate key dari sisi database
                JOptionPane.showMessageDialog(this,
                    "ID \"" + id + "\" sudah ada di database!",
                    "ID Duplikat", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data:\n" + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    public void updateData() {
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih baris data yang ingin diubah terlebih dahulu!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id       = idField.getText().trim();
        String nama     = namaField.getText().trim();
        String strTahun = tahunLahirField.getText().trim();
        String kategori = kategoriComboBox.getSelectedItem() != null
                          ? kategoriComboBox.getSelectedItem().toString() : "";
        String email    = emailField.getText().trim();

        //  Validasi: tidak boleh ada kolom yang kosong 
        if (id.isEmpty() || nama.isEmpty() || strTahun.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Semua kolom wajib diisi!\nID, Nama, Tahun Lahir, dan Email tidak boleh kosong.",
                "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //  Validasi: kategori harus dipilih
        if (kategori.equals("--- Pilih Kategori ---") || kategori.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Silakan pilih Kategori terlebih dahulu!",
                "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //  Validasi: tahun lahir harus angka 
        int thnLahir;
        try {
            thnLahir = Integer.parseInt(strTahun);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Tahun Lahir harus berupa angka!",
                "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int currentYear = Year.now().getValue();
        if (thnLahir < 1920 || thnLahir > currentYear) {
            JOptionPane.showMessageDialog(this,
                "Tahun Lahir harus antara 1920 hingga " + currentYear + "!",
                "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update ke database 
        try {
            DatabaseHelper.updatePerson(new Person(id, nama, thnLahir, kategori, email));
            refreshTable();
            clearForm();
            System.out.println("Update berhasil: " + id);
            JOptionPane.showMessageDialog(this,
                "Data berhasil diubah!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memperbarui data:\n" + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public void deleteData() {
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this,
                "Pilih baris data yang ingin dihapus terlebih dahulu!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil ID dari model tabel — tanpa ArrayList
        String idToDelete = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data dengan ID: " + idToDelete + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                DatabaseHelper.deletePerson(idToDelete);
                refreshTable();
                clearForm();
                System.out.println("Delete berhasil: " + idToDelete);
                JOptionPane.showMessageDialog(this,
                    "Data berhasil dihapus!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus data:\n" + e.getMessage(),
                    "Error Database", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   
    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        emailField.setText("");
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
        peopleTable.clearSelection();
    }


    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);

        titleLabel = new JLabel("People Data", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        centerPanel.setBackground(Color.WHITE);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField          = new JTextField(18);
        namaField        = new JTextField(18);
        tahunLahirField  = new JTextField(18);
        kategoriComboBox = new JComboBox<>();
        emailField       = new JTextField(18);

        addUpdateButton = new JButton("Add");
        cancelButton    = new JButton("Cancel");
        deleteButton    = new JButton("Delete");

        addUpdateButton.setPreferredSize(new Dimension(85, 28));
        cancelButton.setPreferredSize(new Dimension(85, 28));
        deleteButton.setPreferredSize(new Dimension(85, 28));

        // Row 0: ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formGrid.add(new JLabel("ID"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        formGrid.add(idField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0;
        formGrid.add(addUpdateButton, gbc);

        // Row 1: Nama
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formGrid.add(new JLabel("Nama"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        formGrid.add(namaField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.0;
        formGrid.add(cancelButton, gbc);

        // Row 2: Tahun Lahir
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formGrid.add(new JLabel("Tahun Lahir"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        formGrid.add(tahunLahirField, gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.0;
        formGrid.add(deleteButton, gbc);

        // Row 3: Kategori
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formGrid.add(new JLabel("Kategori"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        formGrid.add(kategoriComboBox, gbc);
        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0.0;
        formGrid.add(new JLabel(""), gbc);

        // Row 4: Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        formGrid.add(new JLabel("Email"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        formGrid.add(emailField, gbc);

        centerPanel.add(formGrid, BorderLayout.NORTH);

        peopleTable = new JTable();
        peopleTable.setRowHeight(22);
        scrollPane = new JScrollPane(peopleTable);
        scrollPane.setPreferredSize(new Dimension(700, 260));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setSize(800, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
