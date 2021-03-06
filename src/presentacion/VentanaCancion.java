/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentacion;

import controles.CancionesJpaController;
import entidades.Canciones;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Francisco
 */
public class VentanaCancion extends javax.swing.JFrame {

    private DefaultTableModel dtm;
    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("proyectoJPAPU");
    CancionesJpaController jpaCanciones = new CancionesJpaController(emf);
    List listaCanciones = jpaCanciones.findCancionesEntities();
    Canciones cancion = new Canciones();

    /**
     * Creates new form FramePrincipal
     */
    public VentanaCancion() {
        initComponents();
        this.setLocationRelativeTo(null);
        dtm = (DefaultTableModel) tablaCanciones.getModel();
        tablaCanciones.setModel(dtm);
        botonActualizar.setEnabled(false);
        botonEliminar.setEnabled(false);
    }

    private void mostrarCanciones() {
        setModel(tablaCanciones);
        DefaultTableModel modelo = (DefaultTableModel) tablaCanciones.getModel();
        try {
            jpaCanciones.insertarModelo(modelo);
        } catch (Exception e) {
            Logger.getLogger(VentanaActor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void setModel(JTable table) {
        while (table.getRowCount() > 0) {
            ((DefaultTableModel) table.getModel()).removeRow(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        campoClave = new javax.swing.JTextField();
        campoAutor = new javax.swing.JTextField();
        botonRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCanciones = new javax.swing.JTable();
        botonEliminar = new javax.swing.JButton();
        botonActualizar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        campoInterprete = new javax.swing.JTextField();
        botonMostrarTodos = new javax.swing.JButton();
        botonRegresar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ventana Cancion");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Clave:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Autor:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));
        getContentPane().add(campoClave, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 111, -1));
        getContentPane().add(campoAutor, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 236, -1));

        botonRegistrar.setText("Registrar");
        botonRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRegistrarActionPerformed(evt);
            }
        });
        getContentPane().add(botonRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(112, 171, -1, -1));

        tablaCanciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Autor", "Interprete"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCanciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaCancionesMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaCanciones);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 530, 190));

        botonEliminar.setText("Eliminar");
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(459, 171, -1, -1));

        botonActualizar.setText("Actualizar");
        botonActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizarActionPerformed(evt);
            }
        });
        getContentPane().add(botonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 171, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Interprete:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 123, -1, -1));
        getContentPane().add(campoInterprete, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 236, -1));

        botonMostrarTodos.setText("Mostrar Todos");
        botonMostrarTodos.setFocusable(false);
        botonMostrarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMostrarTodosActionPerformed(evt);
            }
        });
        getContentPane().add(botonMostrarTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, -1, 60));

        botonRegresar.setText("Regresar");
        botonRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonRegresarActionPerformed(evt);
            }
        });
        getContentPane().add(botonRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(287, 452, -1, 33));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Diapositiva2.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 670, 510));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegistrarActionPerformed
        String Clave = campoClave.getText();
        String Autor = campoAutor.getText();
        String Interprete = campoInterprete.getText();

        Canciones cancion = new Canciones();
        cancion.setClave(Clave);
        cancion.setAutor(Autor);
        cancion.setInterprete(Interprete);

        try {
            jpaCanciones.create(cancion);
            campoAutor.setText(null);
            campoClave.setText(null);
            campoInterprete.setText(null);
            setModel(tablaCanciones);
            mostrarCanciones();
            JOptionPane.showMessageDialog(null, "Cancion agregada correctamente");

        } catch (Exception e) {
            Logger.getLogger(VentanaCancion.class.getName()).log(Level.SEVERE, null, e);
        }

    }//GEN-LAST:event_botonRegistrarActionPerformed

    private void botonActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizarActionPerformed
          cancion.setClave(campoClave.getText());
        cancion.setAutor(campoAutor.getText());
        cancion.setInterprete(campoInterprete.getText());

        try {
            jpaCanciones.edit(cancion);

            setModel(tablaCanciones
            );
            mostrarCanciones();
            JOptionPane.showMessageDialog(null, "Actor actualizado correctamente");
        } catch (Exception e) {
            Logger.getLogger(VentanaActor.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_botonActualizarActionPerformed

    private void botonMostrarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMostrarTodosActionPerformed
        mostrarCanciones();
    }//GEN-LAST:event_botonMostrarTodosActionPerformed

    private void botonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarActionPerformed
        cancion.setClave(campoClave.getText());

        try {
            jpaCanciones.destroy(campoClave.getText());
            setModel(tablaCanciones);
            mostrarCanciones();
            campoAutor.setText(null);
            campoClave.setText(null);
            campoInterprete.setText(null);
            JOptionPane.showMessageDialog(null, "Cancion eliminada correctamente");
        } catch (Exception e) {
            Logger.getLogger(VentanaActor.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_botonEliminarActionPerformed

    private void tablaCancionesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaCancionesMousePressed
        int fila = tablaCanciones.getSelectedRow();
        if (fila >= 0) {
            campoClave.setText(tablaCanciones.getValueAt(fila, 0).toString());
            campoAutor.setText(tablaCanciones.getValueAt(fila, 1).toString());
            campoInterprete.setText(tablaCanciones.getValueAt(fila, 2).toString());
            botonEliminar.setEnabled(true);
            botonActualizar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "NO A SELECCIONADO NINGUNA CANCION");
        }
    }//GEN-LAST:event_tablaCancionesMousePressed

    private void botonRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonRegresarActionPerformed
        this.dispose();
        MenuPrincipal menu;
        menu = new MenuPrincipal();
        menu.setVisible(true);
    }//GEN-LAST:event_botonRegresarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonActualizar;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JButton botonMostrarTodos;
    private javax.swing.JButton botonRegistrar;
    private javax.swing.JButton botonRegresar;
    private javax.swing.JTextField campoAutor;
    private javax.swing.JTextField campoClave;
    private javax.swing.JTextField campoInterprete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaCanciones;
    // End of variables declaration//GEN-END:variables
}
