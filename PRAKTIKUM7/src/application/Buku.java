package application;

public class Buku {
    private int id;
    private String judul;
    private String penulis;
    private int stok;
    private double harga;

    public Buku(int id, String judul, String penulis, double harga, int stok) {
        this.id = id;
        this.judul = judul;
        this.penulis = penulis;
        this.stok = stok;
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
