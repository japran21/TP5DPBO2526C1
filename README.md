# TP5DPBO2526C1

# Janji
Saya Muhammad Ilal Zhafran dengan NIM 2502623 mengerjakan Tugas Praktikum 5 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

# Struktur

<img width="282" height="685" alt="image" src="https://github.com/user-attachments/assets/0a8508e7-6b99-4f11-9bf3-b808cc0c5d3c" />

# Desain
mengelola data `Person` dengan atribut berikut:
- ID
- Nama
- Tahun Lahir
- Kategori
- Email

Data yang ditampilkan dalam tabel mengikuti format:
- No
- ID
- Nama
- Thn Lahir
- Kategori
- Email

# Alur dari aplikasi dibuka sampai data tersimpan
1. Pengguna menyiapkan dan menjalankan server MySQL, lalu memastikan database `db_people` dan tabel `person` sudah dibuat menggunakan `db_people.sql`.
2. Pengguna menjalankan `PeopleMenu`. Aplikasi menyiapkan tampilan, mengisi pilihan kategori, lalu meminta data awal dari database.
3. `DatabaseHelper` membuka koneksi JDBC ke MySQL. Data pada tabel `person` dibaca dan disusun menjadi `DefaultTableModel`, kemudian ditampilkan di `JTable`.
4. Pengguna mengisi form. Saat tombol Add ditekan, aplikasi memeriksa kolom wajib, pilihan kategori, tahun lahir, dan keunikan ID.
5. Jika validasi lolos, data dikirim ke MySQL melalui query `INSERT` dengan `PreparedStatement`. Setelah berhasil, tabel dimuat ulang dari database dan form dibersihkan.
6. Jika validasi gagal atau database tidak dapat diakses, aplikasi menampilkan dialog peringatan atau error; data tidak dianggap berhasil disimpan.

### Alur operasi CRUD

#### Create (Tambah)
- Isi ID, Nama, Tahun Lahir, Kategori, dan Email, lalu tekan `Add`.
- ID harus belum digunakan. Tahun lahir harus berupa angka antara 1920 dan tahun berjalan.
- Data baru disimpan ke database, tabel diperbarui, dan form dikosongkan.

#### Read (Tampilkan dan pilih)
- Data dibaca dari database saat aplikasi dibuka dan setiap kali tabel perlu diperbarui.
- Klik baris untuk menyalin nilainya ke form. Tombol `Add` berubah menjadi `Update` dan tombol `Delete` ditampilkan.

#### Update (Ubah)
- Pilih baris, ubah nilai pada form, lalu tekan `Update`.
- Aplikasi memvalidasi ulang form dan menjalankan query `UPDATE` berdasarkan ID.
- ID adalah kunci pencarian record saat update; jangan mengubah ID di form ketika mengubah data lainnya.
- Setelah berhasil, tabel dimuat ulang dan form dibersihkan.

#### Delete (Hapus)
- Pilih baris lalu tekan `Delete`.
- Aplikasi meminta konfirmasi. Pilih `Yes` untuk menghapus record berdasarkan ID; pilih `No` untuk membatalkan.
- Setelah penghapusan berhasil, tabel dimuat ulang dan form dibersihkan.

#### Cancel (Reset)
- Tekan `Cancel` untuk mengosongkan form, membatalkan pilihan baris, dan mengembalikan tombol ke mode `Add`.

## Eror

| Gejala / pesan | Penyebab umum | Yang perlu diperiksa |
|---|---|---|
| Kategori belum dipilih | Dropdown masih menunjukkan `--- Pilih Kategori ---` | Pilih salah satu kategori yang tersedia. |
| Tahun lahir harus berupa angka | Tahun lahir berisi huruf, simbol, atau format lain selain angka bulat | Masukkan tahun dalam angka, misalnya `1998`. |
| Tahun lahir di luar rentang | Nilai tahun kurang dari 1920 atau lebih besar dari tahun berjalan | Masukkan tahun antara 1920 dan tahun saat ini. |

# Dokumentasi
| AKSI | FOTO |
|---|---|
| ADD | <img width="957" height="710" alt="Add" src="https://github.com/user-attachments/assets/916d4d00-8f55-43c7-b7e4-16c2485f1a21" /> <img width="957" height="710" alt="Add2" src="https://github.com/user-attachments/assets/fa591ef7-0f0a-4d0f-a212-c539fff210e4" /> <img width="1172" height="896" alt="Add3" src="https://github.com/user-attachments/assets/93998657-71e9-4b0f-ad67-2d859cce449e" /> |
| DELETE | <img width="975" height="715" alt="Delete" src="https://github.com/user-attachments/assets/044451b6-36ce-4e83-8d01-550dbc5d8d65" /><img width="957" height="607" alt="Delete2" src="https://github.com/user-attachments/assets/7938c8f4-c072-4560-bb69-36b89a07e463" /><img width="1071" height="772" alt="Delete3" src="https://github.com/user-attachments/assets/10eca331-e7f6-4e6f-8b3c-b94a7cccf1f2" /> |
| UPDATE | <img width="970" height="611" alt="Update" src="https://github.com/user-attachments/assets/3e865dfc-80f5-4893-9669-a606efb4fff5" /><img width="960" height="692" alt="Update2" src="https://github.com/user-attachments/assets/e179ac1c-7eb2-4bde-9a6b-4c72a4def9f9" /><img width="957" height="621" alt="Update3" src="https://github.com/user-attachments/assets/bb0a3be9-ce4e-4925-a4d3-e575ef195356" /><img width="1086" height="857" alt="Update4" src="https://github.com/user-attachments/assets/4664dec9-a652-4620-b9ff-bbb92940a89c" /> |
| EROR | <img width="957" height="637" alt="Eror" src="https://github.com/user-attachments/assets/2f0d39f2-3143-4e97-b89c-e54c0b674f0a" /><img width="952" height="647" alt="Eror2" src="https://github.com/user-attachments/assets/a3c30e70-80df-453d-8508-5af4cdc4ae33" /> |














