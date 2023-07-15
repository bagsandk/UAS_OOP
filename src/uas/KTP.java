/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uas;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author gasabag
 */
public class KTP extends javax.swing.JFrame {
    
    private final String url = "jdbc:mysql://localhost:3306/ktpdb"; // URL database
    private final String username = "root"; // Username database
    private final String password = ""; // Password database
    
    // Method untuk menghubungkan ke database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Method untuk menutup koneksi ke database
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Method untuk membersihkan form
    private void clearForm() {
        nik.setText("");
        nama.setText("");
        ttl.setText("");
        jk1.setSelected(false);
        jk2.setSelected(false);
        alamat.setText("");
        agama.setText("");
        pekerjaan.setText("");
        berlaku.setText("");
        kwn.setText("");
    }

    // Method untuk mengambil data dari database berdasarkan NIK
    private void searchData(String searchNIK) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String query = "SELECT * FROM ktpdata WHERE nik = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, searchNIK);
            rs = stmt.executeQuery();

            if (rs.next()) {
                nik.setText(rs.getString("nik"));
                nama.setText(rs.getString("nama"));
                ttl.setText(rs.getString("ttl"));
                String jk = rs.getString("jenis_kelamin");
                jk1.setSelected(jk.equals("Perempuan"));
                jk2.setSelected(jk.equals("Laki-laki"));
                alamat.setText(rs.getString("alamat"));
                agama.setText(rs.getString("agama"));
                pekerjaan.setText(rs.getString("pekerjaan"));
                berlaku.setText(rs.getString("berlaku_hingga"));
                kwn.setText(rs.getString("kewarganegaraan"));
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            closeConnection(conn);
        }
    }

    // Method untuk menyimpan data ke database
    private void saveData() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
         // Cek apakah NIK sudah digunakan sebelumnya
       
        
        try {
            conn = getConnection();
            
            String checkQuery = "SELECT COUNT(*) FROM ktpdata WHERE nik = ?";
            stmt = conn.prepareStatement(checkQuery);
            stmt.setString(1, nik.getText());
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "NIK sudah digunakan. Silakan gunakan NIK lain.");
                return; // Hentikan proses penyimpanan data
            }
            
            String query = "INSERT INTO ktpdata (nik, nama, ttl, jenis_kelamin, alamat, agama, pekerjaan, berlaku_hingga, kewarganegaraan) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nik.getText());
            stmt.setString(2, nama.getText());
            stmt.setString(3, ttl.getText());
            stmt.setString(4, jk1.isSelected() ? "Perempuan" : "Laki-laki");
            stmt.setString(5, alamat.getText());
            stmt.setString(6, agama.getText());
            stmt.setString(7, pekerjaan.getText());
            stmt.setString(8, berlaku.getText());
            stmt.setString(9, kwn.getText());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            closeConnection(conn);
        }
    }

    // Method untuk mengubah data di database
    private void updateData() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            
            String checkQuery = "SELECT COUNT(*) FROM ktpdata WHERE nik = ? AND nik <> ?";
            stmt = conn.prepareStatement(checkQuery);
            stmt.setString(1, nik.getText());
            stmt.setString(2, nik.getText());
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "NIK sudah digunakan oleh data lain. Silakan gunakan NIK lain.");
                return; // Hentikan proses pembaruan data
            }
            
            String query = "UPDATE ktpdata SET nama = ?, ttl = ?, jenis_kelamin = ?, alamat = ?, "
                    + "agama = ?, pekerjaan = ?, berlaku_hingga = ?, kewarganegaraan = ? WHERE nik = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nama.getText());
            stmt.setString(2, ttl.getText());
            stmt.setString(3, jk1.isSelected() ? "Perempuan" : "Laki-laki");
            stmt.setString(4, alamat.getText());
            stmt.setString(5, agama.getText());
            stmt.setString(6, pekerjaan.getText());
            stmt.setString(7, berlaku.getText());
            stmt.setString(8, kwn.getText());
            stmt.setString(9, nik.getText());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengubah data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            closeConnection(conn);
        }
    }

    // Method untuk menghapus data di database
    private void deleteData() {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            String query = "DELETE FROM ktpdata WHERE nik = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, nik.getText());

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            closeConnection(conn);
        }
    }
    /**
     * Creates new form KTP
     */
    public KTP() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lNIK = new javax.swing.JLabel();
        nik = new javax.swing.JTextField();
        lNama = new javax.swing.JLabel();
        nama = new javax.swing.JTextField();
        lTTL = new javax.swing.JLabel();
        ttl = new javax.swing.JTextField();
        lJK = new javax.swing.JLabel();
        alamat = new javax.swing.JTextField();
        lAlamat = new javax.swing.JLabel();
        lAgama = new javax.swing.JLabel();
        agama = new javax.swing.JTextField();
        lPekerjaan = new javax.swing.JLabel();
        pekerjaan = new javax.swing.JTextField();
        berlaku = new javax.swing.JTextField();
        lBerlaku = new javax.swing.JLabel();
        lKwn = new javax.swing.JLabel();
        kwn = new javax.swing.JTextField();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        cari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jk1 = new javax.swing.JRadioButton();
        jk2 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        title1.setText("DATA KTP");

        jLabel2.setText("DESA PEKRAMAN TAMAN SARI");

        lNIK.setText("NIK");

        nik.setToolTipText("Masukan NIK");
        nik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nikActionPerformed(evt);
            }
        });

        lNama.setText("Nama");

        nama.setToolTipText("Masukan NIK");
        nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaActionPerformed(evt);
            }
        });

        lTTL.setText("Tempat / Tgl Lahir");

        ttl.setToolTipText("Masukan NIK");
        ttl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ttlActionPerformed(evt);
            }
        });

        lJK.setText("Jenis Kelamin");

        alamat.setToolTipText("Masukan NIK");
        alamat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alamatActionPerformed(evt);
            }
        });

        lAlamat.setText("Alamat");

        lAgama.setText("Agama");

        agama.setToolTipText("Masukan NIK");
        agama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agamaActionPerformed(evt);
            }
        });

        lPekerjaan.setText("Pekerjaan");

        pekerjaan.setToolTipText("Masukan NIK");
        pekerjaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pekerjaanActionPerformed(evt);
            }
        });

        berlaku.setToolTipText("Masukan NIK");
        berlaku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                berlakuActionPerformed(evt);
            }
        });

        lBerlaku.setText("Berlaku Hingga");

        lKwn.setText("Kewarganegaraan");

        kwn.setToolTipText("Masukan NIK");
        kwn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kwnActionPerformed(evt);
            }
        });

        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        cari.setToolTipText("Cari");
        cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariActionPerformed(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jk1.setText("Perempuan");
        jk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jk1ActionPerformed(evt);
            }
        });

        jk2.setText("Laki-laki");
        jk2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jk2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(title1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lNIK)
                                    .addComponent(lNama)
                                    .addComponent(lTTL)
                                    .addComponent(lJK)
                                    .addComponent(lAlamat)
                                    .addComponent(lAgama)
                                    .addComponent(lPekerjaan)
                                    .addComponent(lBerlaku)
                                    .addComponent(lKwn))
                                .addGap(124, 124, 124)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nik, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ttl, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(agama, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(berlaku, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(kwn, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nama, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                    .addComponent(alamat)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jk1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jk2))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnSimpan)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnUbah)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnHapus)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnClear)
                                    .addGap(109, 109, 109)
                                    .addComponent(cari, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(btnCari, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(title1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lNIK)
                    .addComponent(nik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lNama)
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lTTL)
                    .addComponent(ttl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lJK)
                    .addComponent(jk1)
                    .addComponent(jk2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lAlamat)
                    .addComponent(alamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lAgama)
                    .addComponent(agama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lPekerjaan)
                    .addComponent(pekerjaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lBerlaku)
                    .addComponent(berlaku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lKwn)
                    .addComponent(kwn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnUbah)
                    .addComponent(btnHapus)
                    .addComponent(btnClear)
                    .addComponent(cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCari)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nikActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nikActionPerformed

    private void namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaActionPerformed

    private void ttlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ttlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ttlActionPerformed

    private void alamatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alamatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_alamatActionPerformed

    private void agamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_agamaActionPerformed

    private void pekerjaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pekerjaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pekerjaanActionPerformed

    private void berlakuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_berlakuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_berlakuActionPerformed

    private void kwnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kwnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kwnActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
       saveData();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
       updateData();
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        deleteData();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        String searchNIK = cari.getText();
        if (!searchNIK.isEmpty()) {
            searchData(searchNIK);
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan NIK untuk mencari data.");
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void jk2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jk2ActionPerformed
        if (jk2.isSelected()) {
        jk1.setSelected(false); // Menghilangkan pilihan pada jk1
        }
    }//GEN-LAST:event_jk2ActionPerformed

    private void jk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jk1ActionPerformed
        if (jk1.isSelected()) {
        jk2.setSelected(false); // Menghilangkan pilihan pada jk2
        }
    }//GEN-LAST:event_jk1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KTP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KTP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KTP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KTP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KTP().setVisible(true);
            }
        });
        System.exit(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField agama;
    private javax.swing.JTextField alamat;
    private javax.swing.JTextField berlaku;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JTextField cari;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jk1;
    private javax.swing.JRadioButton jk2;
    private javax.swing.JTextField kwn;
    private javax.swing.JLabel lAgama;
    private javax.swing.JLabel lAlamat;
    private javax.swing.JLabel lBerlaku;
    private javax.swing.JLabel lJK;
    private javax.swing.JLabel lKwn;
    private javax.swing.JLabel lNIK;
    private javax.swing.JLabel lNama;
    private javax.swing.JLabel lPekerjaan;
    private javax.swing.JLabel lTTL;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField nik;
    private javax.swing.JTextField pekerjaan;
    private javax.swing.JLabel title1;
    private javax.swing.JTextField ttl;
    // End of variables declaration//GEN-END:variables
}
