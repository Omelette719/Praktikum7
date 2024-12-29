package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class CRUDController {

    @FXML
    private TextField Namafld, Emailfld, Teleponfld, judulfield, penulisfld, hargafld, stokfld, jumlahfld, totalhargafld, tanggalfld, penjualanidpelangganfld, penjualanidbukufld;

    @FXML
    private TableView<Pelanggan> Pelanggantable;
    @FXML
    private TableView<Buku> tablebuku;
    @FXML
    private TableView<Penjualan> tabelpenjualan;

    @FXML
    private TableColumn<Pelanggan, Integer> idpelangganclmn;
    @FXML
    private TableColumn<Pelanggan, String> pelanggannamaclmn, pelangganemailclmn, pelangganteleponclmn;

    @FXML
    private TableColumn<Buku, Integer> idbukuclmn, stokclmn;
    @FXML
    private TableColumn<Buku, String> judulclmn, penulisclmn;
    @FXML
    private TableColumn<Buku, Double> hargaclmn;

    @FXML
    private TableColumn<Penjualan, Integer> idpenjualanclmn, jumlahclmn, penjualanidpelangganclmn, penjualanidbukuclmn;
    @FXML
    private TableColumn<Penjualan, Double> totalhargaclmn;
    @FXML
    private TableColumn<Penjualan, String> tanggalclmn;

    private Connection connection;

    public void initialize() {
        connectToDatabase();
        setupTableColumns();
        loadData();
        
        Pelanggantable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { 
                Pelanggan selectedPelanggan = Pelanggantable.getSelectionModel().getSelectedItem();
                if (selectedPelanggan != null) {
                	Namafld.setText(selectedPelanggan.getNama());
                	Emailfld.setText(selectedPelanggan.getEmail());
                	Teleponfld.setText(selectedPelanggan.getTelepon());
                }
            }
        });
        
        tablebuku.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { 
                Buku selectedBuku = tablebuku.getSelectionModel().getSelectedItem();
                if (selectedBuku != null) {
                	judulfield.setText(selectedBuku.getJudul());
                	penulisfld.setText(selectedBuku.getPenulis());
                	hargafld.setText(String.valueOf(selectedBuku.getHarga()));
                	stokfld.setText(String.valueOf(selectedBuku.getStok()));
                }
            }
        });
        
        tabelpenjualan.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Penjualan selectedPenjualan = tabelpenjualan.getSelectionModel().getSelectedItem();
                if (selectedPenjualan != null) {
                	jumlahfld.setText(String.valueOf(selectedPenjualan.getJumlah()));
                	totalhargafld.setText(String.valueOf(selectedPenjualan.getTotalHarga()));
                	tanggalfld.setText(selectedPenjualan.getTanggal());
                	penjualanidpelangganfld.setText(String.valueOf(selectedPenjualan.getIdPelanggan()));
                	penjualanidbukufld.setText(String.valueOf(selectedPenjualan.getIdBuku()));
                }
            }
        });
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toko_buku", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        idpelangganclmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        pelanggannamaclmn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        pelangganemailclmn.setCellValueFactory(new PropertyValueFactory<>("email"));
        pelangganteleponclmn.setCellValueFactory(new PropertyValueFactory<>("telepon"));

        idbukuclmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        judulclmn.setCellValueFactory(new PropertyValueFactory<>("judul"));
        penulisclmn.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        hargaclmn.setCellValueFactory(new PropertyValueFactory<>("harga"));
        stokclmn.setCellValueFactory(new PropertyValueFactory<>("stok"));

        idpenjualanclmn.setCellValueFactory(new PropertyValueFactory<>("id"));
        jumlahclmn.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        totalhargaclmn.setCellValueFactory(new PropertyValueFactory<>("totalHarga"));
        tanggalclmn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        penjualanidpelangganclmn.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        penjualanidbukuclmn.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
    }

    private void loadData() {
        loadPelangganData();
        loadBukuData();
        loadPenjualanData();
    }

    private void loadPelangganData() {
        ObservableList<Pelanggan> pelangganList = FXCollections.observableArrayList();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM pelanggan");
            while (rs.next()) {
                pelangganList.add(new Pelanggan(rs.getInt("pelanggan_id"), rs.getString("nama"), rs.getString("email"), rs.getString("telepon")));
            }
            Pelanggantable.setItems(pelangganList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBukuData() {
        ObservableList<Buku> bukuList = FXCollections.observableArrayList();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM buku");
            while (rs.next()) {
                bukuList.add(new Buku(rs.getInt("buku_id"), rs.getString("judul"), rs.getString("penulis"), rs.getDouble("harga"), rs.getInt("stok")));
            }
            tablebuku.setItems(bukuList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPenjualanData() {
        ObservableList<Penjualan> penjualanList = FXCollections.observableArrayList();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM penjualan");
            while (rs.next()) {
                penjualanList.add(new Penjualan(rs.getInt("penjualan_id"), rs.getInt("jumlah"), rs.getDouble("total_harga"), rs.getString("tanggal"), rs.getInt("pelanggan_id"), rs.getInt("buku_id")));
            }
            tabelpenjualan.setItems(penjualanList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addPelanggan(ActionEvent event) {
        try {
            String query = "INSERT INTO pelanggan (nama, email, telepon) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, Namafld.getText());
            stmt.setString(2, Emailfld.getText());
            stmt.setString(3, Teleponfld.getText());
            stmt.executeUpdate();
            loadPelangganData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editPelanggan(ActionEvent event) {
        Pelanggan selected = Pelanggantable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String query = "UPDATE pelanggan SET nama = ?, email = ?, telepon = ? WHERE pelanggan_id = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, Namafld.getText());
                stmt.setString(2, Emailfld.getText());
                stmt.setString(3, Teleponfld.getText());
                stmt.setInt(4, selected.getId());
                stmt.executeUpdate();
                loadPelangganData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deletePelanggan(ActionEvent event) {
        Pelanggan selected = Pelanggantable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this record?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirmation");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.toFront();

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.YES) {
                try {
                    String query = "DELETE FROM pelanggan WHERE pelanggan_id = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();
                    loadPelangganData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void addBuku(ActionEvent event) {
        try {
            String query = "INSERT INTO buku (judul, penulis, harga, stok) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, judulfield.getText());
            stmt.setString(2, penulisfld.getText());
            stmt.setDouble(3, Double.parseDouble(hargafld.getText()));
            stmt.setInt(4, Integer.parseInt(stokfld.getText()));
            stmt.executeUpdate();
            loadBukuData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editBuku(ActionEvent event) {
        Buku selected = tablebuku.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String query = "UPDATE buku SET judul = ?, penulis = ?, harga = ?, stok = ? WHERE buku_id = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, judulfield.getText());
                stmt.setString(2, penulisfld.getText());
                stmt.setDouble(3, Double.parseDouble(hargafld.getText()));
                stmt.setInt(4, Integer.parseInt(stokfld.getText()));
                stmt.setInt(5, selected.getId());
                stmt.executeUpdate();
                loadBukuData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deleteBuku(ActionEvent event) {
        Buku selected = tablebuku.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this book?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirmation");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.toFront();

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.YES) {
                try {
                    String query = "DELETE FROM buku WHERE buku_id = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();
                    loadBukuData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void addPenjualan(ActionEvent event) {
        try {
            String query = "INSERT INTO penjualan (jumlah, total_harga, tanggal, pelanggan_id, buku_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(jumlahfld.getText()));
            stmt.setDouble(2, Double.parseDouble(totalhargafld.getText()));
            stmt.setString(3, tanggalfld.getText());
            stmt.setInt(4, Integer.parseInt(penjualanidpelangganfld.getText()));
            stmt.setInt(5, Integer.parseInt(penjualanidbukufld.getText()));
            stmt.executeUpdate();
            loadPenjualanData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editPenjualan(ActionEvent event) {
        Penjualan selected = tabelpenjualan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String query = "UPDATE penjualan SET jumlah = ?, total_harga = ?, tanggal = ?, pelanggan_id = ?, buku_id = ? WHERE penjualan_id = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(jumlahfld.getText()));
                stmt.setDouble(2, Double.parseDouble(totalhargafld.getText()));
                stmt.setString(3, tanggalfld.getText());
                stmt.setInt(4, Integer.parseInt(penjualanidpelangganfld.getText()));
                stmt.setInt(5, Integer.parseInt(penjualanidbukufld.getText()));
                stmt.setInt(6, selected.getId());
                stmt.executeUpdate();
                loadPenjualanData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void deletePenjualan(ActionEvent event) {
        Penjualan selected = tabelpenjualan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this sale?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirmation");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.toFront();

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.YES) {
                try {
                    String query = "DELETE FROM penjualan WHERE penjualan_id = ?";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();
                    loadPenjualanData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
