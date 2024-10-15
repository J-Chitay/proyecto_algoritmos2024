

import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import javax.swing.JOptionPane;



public class login extends javax.swing.JFrame {
    public String nombreUsuario; // Variable global para almacenar el nombre del usuario

Conexion cx;
    public login() {
        initComponents();
        this.setTitle("LOGIN");
        this.setSize(400, 400);
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/iniciar-sesion2.png"));
        lblLogo.setIcon(new ImageIcon(img.getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(),Image.SCALE_SMOOTH)));
        this.setIconImage(img);
        this.setLocationRelativeTo(null);
        cx = new Conexion("dbproyecto_algoritmos");
        cx.conectar();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Usuario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, -1, -1));
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 144, -1));

        jLabel2.setText("Password");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, -1, -1));

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, 114, 42));

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 135, 42));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/iniciar-sesion2.png"))); // NOI18N
        lblLogo.setText("jLabel3");
        getContentPane().add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 140, 130));
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 230, 140, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
    try {
        String user=txtUsuario.getText();
        String password=String.valueOf(txtPassword.getPassword());
        String query = "SELECT * FROM usuario WHERE user='"+ user +"' AND password='"+ password +"'";
        Statement st = cx.conectar().createStatement();
        ResultSet rs = st.executeQuery(query);
        if(rs.next()){
            String nombreUsuario = rs.getString("user");  // Capturar el nombre del usuario
            JOptionPane.showMessageDialog(this, "EL USUARIO EXISTE EN LA BASE DE DATOS");
            dispose();
            menu ir = new menu();
            ir.setNombreUsuario(user); // Pasar el nombre de usuario
            ir.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(this, "EL USUARIO NO EXISTE EN LA BASE DE DATOS");
        }
        
    } catch (SQLException ex) {
        Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }//GEN-LAST:event_btnIniciarActionPerformed


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
