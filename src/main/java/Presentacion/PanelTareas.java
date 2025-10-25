/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Presentacion;
import Negocio.TareaNegocio;
import Entidades.Tarea;
import Entidades.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.AbstractCellEditor;
/**
 *
 * @author Mi Equipo
 */
public class PanelTareas extends javax.swing.JPanel {
    private Usuario usuario;
    private TareaNegocio tareaNegocio = new TareaNegocio();
    private JTable tbTareaPendiente;
    private JTable tbTareaCompletada;
    private JButton btnAgregar;
    public PanelTareas(Usuario usuario) {
        this.usuario = usuario;
        initComponents2();
        cargarTareas();
        aplicarEstilosTablas();
    }

    
    private void initComponents2() {
        setLayout(new BorderLayout());
        
        // PANEL SUPERIOR CON BOTÓN AGREGAR
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(new Color(240, 240, 240));
        panelSuperior.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnAgregar = new JButton("+ Agregar Nueva Tarea");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.setBackground(new Color(52, 152, 219));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setPreferredSize(new Dimension(200, 35));
        btnAgregar.addActionListener(this::btnAgregarActionPerformed);
        
        panelSuperior.add(btnAgregar);
        
        // Crear tablas
        tbTareaPendiente = new JTable();
        tbTareaCompletada = new JTable();
        
        // Panel principal con pestañas (usando JTabbedPane interno)
        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        
        // Panel de tareas pendientes
        JPanel panelPendientes = new JPanel(new BorderLayout());
        panelPendientes.add(new JScrollPane(tbTareaPendiente), BorderLayout.CENTER);
        
        // Panel de tareas completadas
        JPanel panelCompletadas = new JPanel(new BorderLayout());
        panelCompletadas.add(new JScrollPane(tbTareaCompletada), BorderLayout.CENTER);
        
        // Agregar pestañas
        tabbedPane.addTab("Tareas Pendientes", panelPendientes);
        tabbedPane.addTab("Tareas Completadas", panelCompletadas);
        add(panelSuperior, BorderLayout.SOUTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        AgregarTareaForm form = new AgregarTareaForm(usuario);
        form.setLocationRelativeTo(this);

        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Refrescar este panel y notificar al dashboard
                refrescar();
                DashboardForm.actualizarPaneles();
            }
        });

        form.setVisible(true);
    }
    
    private void cargarTareas() {   
        DefaultTableModel modeloPendientes = new DefaultTableModel(
            new String[]{"ID", "Título", "Categoría", "Descripción", "Prioridad", "FechaFin", "HoraFin", "Completar", "Eliminar"}, 0
        );

        DefaultTableModel modeloCompletadas = new DefaultTableModel(
            new String[]{"ID", "Titulo", "Categoria", "Descripcion", "Prioridad", "FechaFin", "HoraFin"}, 0
        );

        // Obtener tareas pendientes
        List<Tarea> pendientes = tareaNegocio.obtenerTareasPorEstado(usuario.getUsuarioID(), "Pendiente");
        for (Tarea t : pendientes) {
            modeloPendientes.addRow(new Object[]{
                t.getTareaID(),
                t.getTitulo(),
                tareaNegocio.obtenerNombreCategoria(t.getCategoriaID()), 
                t.getDescripcion(),
                t.getPrioridad(),
                t.getFecha(),
                t.getHoraFin()
            });
        }

        // Obtener tareas completadas
        List<Tarea> completadas = tareaNegocio.obtenerTareasPorEstado(usuario.getUsuarioID(), "Completado");
        for (Tarea t : completadas) {
            modeloCompletadas.addRow(new Object[]{
                t.getTareaID(),
                t.getTitulo(),
                tareaNegocio.obtenerNombreCategoria(t.getCategoriaID()), 
                t.getDescripcion(),
                t.getPrioridad(),
                t.getFecha(),
                t.getHoraFin()
            });
        }

        tbTareaPendiente.setModel(modeloPendientes);
        tbTareaCompletada.setModel(modeloCompletadas);
        
        // Configurar botones
        tbTareaPendiente.getColumn("Completar").setCellRenderer(new ButtonRenderer());
        tbTareaPendiente.getColumn("Completar").setCellEditor(new ButtonEditor());
        tbTareaPendiente.getColumn("Eliminar").setCellRenderer(new ButtonRenderer2("Eliminar"));
        tbTareaPendiente.getColumn("Eliminar").setCellEditor(new ButtonEditor2("Eliminar"));
    }
    
    private void aplicarEstilosTablas() {
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        Font cellFont = new Font("Segoe UI", Font.PLAIN, 13);

        // Encabezados
        tbTareaPendiente.getTableHeader().setFont(headerFont);
        tbTareaPendiente.getTableHeader().setBackground(new Color(52, 152, 219));
        tbTareaPendiente.getTableHeader().setForeground(Color.BLACK);

        tbTareaCompletada.getTableHeader().setFont(headerFont);
        tbTareaCompletada.getTableHeader().setBackground(new Color(46, 204, 113));
        tbTareaCompletada.getTableHeader().setForeground(Color.BLACK);

        // Filas
        tbTareaPendiente.setFont(cellFont);
        tbTareaCompletada.setFont(cellFont);

        // Altura de filas
        tbTareaPendiente.setRowHeight(28);
        tbTareaCompletada.setRowHeight(28);

        // Grid
        tbTareaPendiente.setShowGrid(true);
        tbTareaPendiente.setGridColor(new Color(189, 195, 199));
        tbTareaCompletada.setShowGrid(true);
        tbTareaCompletada.setGridColor(new Color(189, 195, 199));

        // Selección
        tbTareaPendiente.setSelectionBackground(new Color(241, 148, 138));
        tbTareaCompletada.setSelectionBackground(new Color(244, 208, 63));

        // Ajuste de columnas
        tbTareaPendiente.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tbTareaPendiente.getColumnModel().getColumn(1).setPreferredWidth(150);  // Título
        tbTareaPendiente.getColumnModel().getColumn(3).setPreferredWidth(200);  // Descripción
        tbTareaPendiente.getColumnModel().getColumn(7).setPreferredWidth(120);   // Completar
        tbTareaPendiente.getColumnModel().getColumn(8).setPreferredWidth(120);   // Eliminar

        tbTareaCompletada.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tbTareaCompletada.getColumnModel().getColumn(1).setPreferredWidth(150);  // Título
        tbTareaCompletada.getColumnModel().getColumn(3).setPreferredWidth(200);  // Descripción
    }
    
    public void refrescar() {
        cargarTareas();
    }
    
    // Renderer para mostrar el botón
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("✔ Completar");
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }
    
    class ButtonRenderer2 extends JButton implements TableCellRenderer {
        public ButtonRenderer2(String text) {
            setText(text);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }
    
    class ButtonEditor2 extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private int tareaID;
        private String action;

        public ButtonEditor2(String action) {
            this.action = action;
            button = new JButton(action);

            button.addActionListener(e -> {
                if ("Completar".equals(action)) {
                    marcarComoCompletado(tareaID);
                } else if ("Eliminar".equals(action)) {
                    eliminarTarea(tareaID);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            tareaID = (int) table.getValueAt(row, 0); // ID en primera columna
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return action;
        }
    }

    // Editor para manejar el click del botón
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private int tareaID;

        public ButtonEditor() {
            button = new JButton("✔ Completar");
            button.addActionListener(e -> {
                marcarComoCompletado(tareaID);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            tareaID = (int) table.getValueAt(row, 0); // ID está en la primera columna
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "✔ Completar";
        }   
    }
    
    private void marcarComoCompletado(int tareaID) {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Desea marcar esta tarea como completada?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean actualizado = tareaNegocio.cambiarEstadoTarea(tareaID, "Completado");
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Tarea marcada como completada");
                refrescar();
                // Notificar al DashboardForm para actualizar otros paneles
                DashboardForm.actualizarPaneles();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la tarea", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarTarea(int tareaID) {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea eliminar esta tarea?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = tareaNegocio.eliminarTarea(tareaID);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Tarea eliminada con éxito");
                refrescar();
                // Notificar al DashboardForm para actualizar otros paneles
                DashboardForm.actualizarPaneles();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la tarea", "Error", JOptionPane.ERROR_MESSAGE);
            }
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
