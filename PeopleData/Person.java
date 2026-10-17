/**
 * Model class Person untuk merepresentasikan data individu (Member & Staf) pada Gym & Fitness Center.
 * Mengandung 4 atribut dasar (ID, Nama, Tahun Lahir, Kategori)
 * dan 1 atribut baru untuk BONUS (+20): Jenis Kelamin.
 */
public class Person {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    private String jenisKelamin;

    // Default Constructor
    public Person() {
    }

    // Parameterized Constructor
    public Person(String id, String nama, int tahunLahir, String kategori, String jenisKelamin) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.jenisKelamin = jenisKelamin;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getTahunLahir() {
        return tahunLahir;
    }

    public void setTahunLahir(int tahunLahir) {
        this.tahunLahir = tahunLahir;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
}
