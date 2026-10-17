import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

/**
 * DatabaseHelper - Mengelola semua operasi CRUD ke database MySQL.
 * Database : db_people
 * Tabel    : person (id, nama, thn_lahir, kategori, email)
 */
public class DatabaseHelper {

    // ===== Konfigurasi Koneksi =====
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/db_people?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // Ganti sesuai password MySQL kamu

    /** Membuka dan mengembalikan koneksi ke database. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // =====================================================================
    //  CREATE
    // =====================================================================

    /**
     * Menyimpan data Person baru ke database.
     *
     * @return true jika INSERT berhasil.
     *         false jika ID sudah ada (duplicate primary key).
     * @throws SQLException untuk error koneksi atau query lainnya.
     */
    public static boolean insertPerson(Person p) throws SQLException {
        String sql = "INSERT INTO person (id, nama, thn_lahir, kategori, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNama());
            ps.setInt   (3, p.getThnLahir());
            ps.setString(4, p.getKategori());
            ps.setString(5, p.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate PRIMARY KEY — kembalikan false agar pemanggil bisa tampilkan dialog
            return false;
        }
    }

    // =====================================================================
    //  READ
    // =====================================================================

    /**
     * Mengambil semua data dari tabel person, diurutkan berdasarkan id.
     * Digunakan oleh getTableModel() — tidak perlu ArrayList di layer UI.
     */
    private static ArrayList<Person> getAllPersons() throws SQLException {
        ArrayList<Person> list = new ArrayList<>();
        String sql = "SELECT id, nama, thn_lahir, kategori, email FROM person ORDER BY id";
        try (Connection con = getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Person(
                    rs.getString("id"),
                    rs.getString("nama"),
                    rs.getInt   ("thn_lahir"),
                    rs.getString("kategori"),
                    rs.getString("email")
                ));
            }
        }
        return list;
    }

    /**
     * Membangun DefaultTableModel langsung dari database.
     * PeopleMenu cukup panggil ini — tidak perlu menyimpan ArrayList sendiri.
     */
    public static DefaultTableModel getTableModel() throws SQLException {
        Object[] cols = { "No", "ID", "Nama", "Thn Lahir", "Kategori", "Email" };
        DefaultTableModel model = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // semua sel tidak bisa diedit langsung
            }
        };

        ArrayList<Person> list = getAllPersons();
        for (int i = 0; i < list.size(); i++) {
            Person p = list.get(i);
            model.addRow(new Object[]{
                i + 1,
                p.getId(),
                p.getNama(),
                p.getThnLahir(),
                p.getKategori(),
                p.getEmail()
            });
        }
        return model;
    }

    /**
     * Mengecek apakah suatu ID sudah ada di database.
     * Dipakai sebelum INSERT untuk mencegah duplikat.
     */
    public static boolean isIdExist(String id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM person WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // =====================================================================
    //  UPDATE
    // =====================================================================

    /**
     * Memperbarui data Person di database berdasarkan ID (PK tidak diubah).
     */
    public static void updatePerson(Person p) throws SQLException {
        String sql = "UPDATE person SET nama = ?, thn_lahir = ?, kategori = ?, email = ? WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNama());
            ps.setInt   (2, p.getThnLahir());
            ps.setString(3, p.getKategori());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getId());
            ps.executeUpdate();
        }
    }

    // =====================================================================
    //  DELETE
    // =====================================================================

    /**
     * Menghapus data Person dari database berdasarkan ID.
     */
    public static void deletePerson(String id) throws SQLException {
        String sql = "DELETE FROM person WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
