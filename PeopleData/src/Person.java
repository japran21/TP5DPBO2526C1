
public class Person {
    private String id;
    private String nama;
    private int thnLahir;
    private String kategori;
    private String email;


    public Person() {
    }


    public Person(String id, String nama, int thnLahir, String kategori, String email) {
        this.id = id;
        this.nama = nama;
        this.thnLahir = thnLahir;
        this.kategori = kategori;
        this.email = email;
    }


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

    public int getThnLahir() {
        return thnLahir;
    }

    public void setThnLahir(int thnLahir) {
        this.thnLahir = thnLahir;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
