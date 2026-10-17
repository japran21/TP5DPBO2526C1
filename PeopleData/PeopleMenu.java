import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.ArrayList;


public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JRadioButton lakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;
    private JScrollPane scrollPane;

    private ButtonGroup genderGroup;
    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    // Constructor
    public PeopleMenu() {
        // Fallback inisialisasi UI jika dikompilasi manual tanpa bytecode-instrumenter IntelliJ
        if (mainPanel == null) {
            initUI();
        }

        // Atur judul jendela
        setTitle("Data Member & Staf Gym - PeopleData");

        // 1. Inisialisasi listPerson
        listPerson = new ArrayList<>();

        // 2. Isi listPerson dengan data awal (dummy)
        populateList();

        // 3. Setup Group Radio Button untuk atribut Jenis Kelamin (BONUS +20)
        genderGroup = new ButtonGroup();
        genderGroup.add(lakiRadioButton);
        genderGroup.add(perempuanRadioButton);
        lakiRadioButton.setSelected(true);

        // 4. Atur isi combo box Kategori
        String[] kategoriData = { "--- Pilih Kategori ---", "Member Reguler", "Member VIP", "Personal Trainer", "Nutrisionis" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // 5. Isi tabel orang & styling
        peopleTable.setModel(setTable());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // 6. Sembunyikan tombol delete secara default
        deleteButton.setVisible(false);

        // 7. Event Listener saat tombol Add/Update ditekan
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

        // 8. Event Listener saat tombol Delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        // 9. Event Listener saat tombol Cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // 10. Event Listener saat baris tabel diklik
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex < 0) return;

                // Ambil nilai dari tabel berdasarkan baris terpilih
                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahunLahir = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curGender = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                // Tampilkan nilai ke form input
                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahunLahir);
                kategoriComboBox.setSelectedItem(curKategori);

                if ("Laki-laki".equalsIgnoreCase(curGender)) {
                    lakiRadioButton.setSelected(true);
                } else {
                    perempuanRadioButton.setSelected(true);
                }

                // Ubah teks tombol menjadi "Update" dan munculkan tombol "Delete"
                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    // Method untuk mengatur dan memuat data tabel
    public final DefaultTableModel setTable() {
        // Tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin" };

        // Buat objek tabel model
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel bersifat read-only melalui klik cell
            }
        };

        // Isi tabel dengan data dari listPerson
        for (int i = 0; i < listPerson.size(); i++) {
            Person p = listPerson.get(i);
            Object[] row = {
                i + 1,
                p.getId(),
                p.getNama(),
                p.getTahunLahir(),
                p.getKategori(),
                p.getJenisKelamin()
            };
            tmp.addRow(row);
        }

        return tmp;
    }

    // Method untuk menambah data baru
    public void insertData() {
        try {
            // Ambil value dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String strTahun = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem() != null ? kategoriComboBox.getSelectedItem().toString() : "";
            String gender = lakiRadioButton.isSelected() ? "Laki-laki" : "Perempuan";

            // Validasi input tidak boleh kosong
            if (id.isEmpty() || nama.isEmpty() || strTahun.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom form wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (kategori.equals("--- Pilih Kategori ---") || kategori.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Silakan pilih Kategori yang valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validasi format angka tahun lahir
            int tahunLahir = Integer.parseInt(strTahun);
            int currentYear = Year.now().getValue();
            if (tahunLahir < 1920 || tahunLahir > currentYear) {
                JOptionPane.showMessageDialog(this, "Tahun lahir harus antara tahun 1920 hingga " + currentYear + "!", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Tambahkan data ke dalam list
            listPerson.add(new Person(id, nama, tahunLahir, kategori, gender));

            // Perbarui tampilan tabel
            peopleTable.setModel(setTable());

            // Bersihkan form input
            clearForm();

            // Feedback sukses
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk memperbarui data yang dipilih
    public void updateData() {
        if (selectedIndex < 0 || selectedIndex >= listPerson.size()) {
            JOptionPane.showMessageDialog(this, "Pilih baris data yang ingin diubah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            String strTahun = tahunLahirField.getText().trim();
            String kategori = kategoriComboBox.getSelectedItem() != null ? kategoriComboBox.getSelectedItem().toString() : "";
            String gender = lakiRadioButton.isSelected() ? "Laki-laki" : "Perempuan";

            // Validasi data
            if (id.isEmpty() || nama.isEmpty() || strTahun.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom form wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (kategori.equals("--- Pilih Kategori ---") || kategori.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Silakan pilih Kategori yang valid!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int tahunLahir = Integer.parseInt(strTahun);
            int currentYear = Year.now().getValue();
            if (tahunLahir < 1920 || tahunLahir > currentYear) {
                JOptionPane.showMessageDialog(this, "Tahun lahir harus antara tahun 1920 hingga " + currentYear + "!", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Ubah data person di list
            Person p = listPerson.get(selectedIndex);
            p.setId(id);
            p.setNama(nama);
            p.setTahunLahir(tahunLahir);
            p.setKategori(kategori);
            p.setJenisKelamin(gender);

            // Perbarui tampilan tabel
            peopleTable.setModel(setTable());

            // Bersihkan form input
            clearForm();

            // Feedback sukses
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk menghapus data dengan Confirmation Prompt
    public void deleteData() {
        if (selectedIndex < 0 || selectedIndex >= listPerson.size()) {
            JOptionPane.showMessageDialog(this, "Pilih baris data yang ingin dihapus terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tampilkan konfirmasi dialog sebelum menghapus data (Syarat Wajib TP)
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        // Jika user memilih YES
        if (confirmation == JOptionPane.YES_OPTION) {
            // Hapus data dari list
            listPerson.remove(selectedIndex);

            // Perbarui tampilan tabel
            peopleTable.setModel(setTable());

            // Bersihkan form
            clearForm();

            // Feedback
            System.out.println("Delete berhasil");
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Method untuk mengosongkan form input
    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        lakiRadioButton.setSelected(true);

        // Kembalikan tombol ke kondisi awal
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
        peopleTable.clearSelection();
    }

    // Method untuk mengisi data hardcoded awal (dummy data)
    public void populateList() {
        listPerson.add(new Person("GYM-001", "Budi Hartono", 1995, "Member VIP", "Laki-laki"));
        listPerson.add(new Person("GYM-002", "Siti Aminah", 1998, "Member Reguler", "Perempuan"));
        listPerson.add(new Person("GYM-003", "Rian Pratama", 1992, "Personal Trainer", "Laki-laki"));
        listPerson.add(new Person("GYM-004", "Dewi Lestari", 1996, "Nutrisionis", "Perempuan"));
        listPerson.add(new Person("GYM-005", "Hendra Wijaya", 2001, "Member Reguler", "Laki-laki"));
    }

    /**
     * Fallback inisialisasi UI jika project dikompilasi langsung tanpa runtime IntelliJ GUI byte-code instrumenter.
     * Memastikan aplikasi dapat di-run langsung di mana saja.
     */
    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(10, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);

        // Title
        titleLabel = new JLabel("Data Member & Staf Gym", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center: Form and Table
        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        centerPanel.setBackground(Color.WHITE);

        // Form Grid
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inisialisasi komponen
        idField = new JTextField(18);
        namaField = new JTextField(18);
        tahunLahirField = new JTextField(18);
        kategoriComboBox = new JComboBox<>();
        lakiRadioButton = new JRadioButton("Laki-laki");
        lakiRadioButton.setBackground(Color.WHITE);
        perempuanRadioButton = new JRadioButton("Perempuan");
        perempuanRadioButton.setBackground(Color.WHITE);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(lakiRadioButton);
        genderPanel.add(perempuanRadioButton);

        addUpdateButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        deleteButton = new JButton("Delete");

        // Styling tombol
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

        // Row 4: Jenis Kelamin (BONUS +20)
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        formGrid.add(new JLabel("Jenis Kelamin"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        formGrid.add(genderPanel, gbc);

        centerPanel.add(formGrid, BorderLayout.NORTH);

        // Table & ScrollPane
        peopleTable = new JTable();
        peopleTable.setRowHeight(22);
        scrollPane = new JScrollPane(peopleTable);
        scrollPane.setPreferredSize(new Dimension(650, 260));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    // Main Method
    public static void main(String[] args) {
        // Buat object window
        PeopleMenu menu = new PeopleMenu();

        // Atur ukuran window
        menu.setSize(750, 650);

        // Letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // Isi window
        menu.setContentPane(menu.mainPanel);

        // Ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // Tampilkan window
        menu.setVisible(true);

        // Agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
