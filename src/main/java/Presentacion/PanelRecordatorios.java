/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Presentacion;
import Entidades.Tarea;
import Negocio.TareaNegocio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Mi Equipo
 */
public class PanelRecordatorios extends javax.swing.JPanel {
    private TareaNegocio tareaNegocio;
    private int usuarioID;
    private JTable tablaTareas;
    private DefaultTableModel modeloTabla;
    /**
     * Creates new form PanelRecordatorios
     */
    public PanelRecordatorios(int usuarioID) {
        this.usuarioID = usuarioID;
        this.tareaNegocio = new TareaNegocio();
        
        initComponents2();
        cargarTareasPendientes();
    }
    
    private void initComponents2() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 400));

        // Título
        JLabel titulo = new JLabel("GESTOR DE RECORDATORIOS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(52, 152, 219));
        titulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        // Modelo de tabla
        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Título", "Fecha", "Hora Fin", "Recordatorio", "Acciones"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Solo editable recordatorio y acciones
            }
        };

        tablaTareas = new JTable(modeloTabla);
        tablaTareas.setRowHeight(30);
        tablaTareas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Renderer para columna de recordatorio
        tablaTareas.getColumnModel().getColumn(4).setCellRenderer(new RecordatorioRenderer());
        tablaTareas.getColumnModel().getColumn(4).setCellEditor(new RecordatorioEditor());
        
        // Renderer para columna de acciones
        tablaTareas.getColumnModel().getColumn(5).setCellRenderer(new AccionesRenderer());
        tablaTareas.getColumnModel().getColumn(5).setCellEditor(new AccionesEditor());

        JScrollPane scrollPane = new JScrollPane(tablaTareas);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de información
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel infoLabel = new JLabel("Selecciona una hora de recordatorio y haz clic en 'Activar'");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(Color.DARK_GRAY);
        infoPanel.add(infoLabel);
        
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void cargarTareasPendientes() {
        modeloTabla.setRowCount(0); // Limpiar tabla

        List<Tarea> tareasPendientes = tareaNegocio.obtenerTareasPorEstado(usuarioID, "Pendiente");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        System.out.println("Cargando " + tareasPendientes.size() + " tareas pendientes");

        for (Tarea tarea : tareasPendientes) {
            String fecha = tarea.getFecha() != null ? dateFormat.format(tarea.getFecha()) : "Sin fecha";
            String horaFin = tarea.getHoraFin() != null ? timeFormat.format(tarea.getHoraFin()) : "Sin hora";
            String recordatorio = tarea.isRecordatorioActivo() ? 
                "Activado (" + (tarea.getHoraRecordatorio() != null ? 
                    timeFormat.format(tarea.getHoraRecordatorio()) : "Sin hora") + ")" : "Desactivado";

            System.out.println("Tarea: " + tarea.getTitulo() + 
                             " - Recordatorio: " + recordatorio +
                             " - Hora Recordatorio: " + tarea.getHoraRecordatorio());

            modeloTabla.addRow(new Object[]{
                tarea.getTareaID(),
                tarea.getTitulo(),
                fecha,
                horaFin,
                recordatorio,
                tarea.isRecordatorioActivo() ? "Desactivar" : "Activar"
            });
        }
    }

    // Renderer para columna de recordatorio
    class RecordatorioRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        public RecordatorioRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.PLAIN, 11));
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            
            if (value.toString().contains("Activado")) {
                setBackground(new Color(220, 255, 220));
                setForeground(new Color(0, 128, 0));
            } else {
                setBackground(new Color(255, 220, 220));
                setForeground(new Color(128, 0, 0));
            }
            
            if (isSelected) {
                setBackground(new Color(180, 180, 255));
            }
            
            return this;
        }
    }

    // Editor para columna de recordatorio
    class RecordatorioEditor extends javax.swing.AbstractCellEditor 
        implements javax.swing.table.TableCellEditor {
    
        private JLabel label;

        public RecordatorioEditor() {
            label = new JLabel();
            label.setOpaque(true);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label.setText(value.toString());
            return label;
        }

        @Override
        public Object getCellEditorValue() {
            return label.getText();
        }
    }

    // Renderer para columna de acciones
    class AccionesRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public AccionesRenderer() {
            setOpaque(true);
        }
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            
            if (value.toString().equals("Activar")) {
                setBackground(new Color(76, 175, 80));
                setForeground(Color.WHITE);
            } else {
                setBackground(new Color(244, 67, 54));
                setForeground(Color.WHITE);
            }
            
            return this;
        }
    }

    // Editor para columna de acciones
    class AccionesEditor extends javax.swing.AbstractCellEditor 
        implements javax.swing.table.TableCellEditor {
    private JButton button;
    private int tareaID;
    private String accion;

    public AccionesEditor() {
        button = new JButton();
        button.addActionListener(e -> {
            ejecutarAccion();
            fireEditingStopped();
        });
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        tareaID = (int) table.getValueAt(row, 0);
        accion = value.toString();
        button.setText(accion);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return accion;
    }

    private void ejecutarAccion() {
        if (accion.equals("Activar")) {
            mostrarDialogoHoraPersonalizado();
        } else if (accion.equals("Desactivar")) {
            desactivarRecordatorio(tareaID);
        }
    }

    // Diálogo personalizado con JSpinner para escribir y seleccionar hora
    private void mostrarDialogoHoraPersonalizado() {
        // Crear panel personalizado
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Etiqueta informativa
        JLabel lblInfo = new JLabel("Ingrese o seleccione la hora para el recordatorio:");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(lblInfo, BorderLayout.NORTH);
        
        // Panel para controles de hora
        JPanel panelHora = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        // JSpinner para permitir escribir y seleccionar
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        JSpinner spinnerHora = new JSpinner(spinnerModel);
        
        // Configurar el editor para mostrar solo hora y minutos
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(editor);
        spinnerHora.setPreferredSize(new Dimension(80, 25));
        
        // Establecer hora actual como valor por defecto
        Calendar ahora = Calendar.getInstance();
        // Agregar 30 minutos a la hora actual para evitar horas pasadas
        ahora.add(Calendar.MINUTE, 30);
        // Redondear a los próximos 30 minutos
        int minutos = ahora.get(Calendar.MINUTE);
        if (minutos < 30) {
            ahora.set(Calendar.MINUTE, 30);
        } else {
            ahora.set(Calendar.MINUTE, 0);
            ahora.add(Calendar.HOUR, 1);
        }
        spinnerHora.setValue(ahora.getTime());
        
        panelHora.add(new JLabel("Hora:"));
        panelHora.add(spinnerHora);
        
        panel.add(panelHora, BorderLayout.CENTER);
        
        // Panel para mostrar hora actual
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel lblHoraActual = new JLabel("Hora actual: " + sdf.format(new Date()));
        lblHoraActual.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHoraActual.setForeground(Color.GRAY);
        panelInfo.add(lblHoraActual);
        panel.add(panelInfo, BorderLayout.SOUTH);
        
        // Mostrar diálogo
        int resultado = JOptionPane.showConfirmDialog(
            PanelRecordatorios.this,
            panel,
            "Configurar Recordatorio",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (resultado == JOptionPane.OK_OPTION) {
            procesarHoraSeleccionada(spinnerHora);
        }
    }
    
    // Procesar la hora seleccionada/escrita con validación
    private void procesarHoraSeleccionada(JSpinner spinnerHora) {
    try {
        Date horaSeleccionada = (Date) spinnerHora.getValue();
        
        // Crear objetos Calendar para comparar solo la hora
        Calendar calSeleccionada = Calendar.getInstance();
        calSeleccionada.setTime(horaSeleccionada);
        
        Calendar calActual = Calendar.getInstance();
        
        // Obtener solo hora y minutos para comparar
        int horaSel = calSeleccionada.get(Calendar.HOUR_OF_DAY);
        int minutoSel = calSeleccionada.get(Calendar.MINUTE);
        
        int horaActual = calActual.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calActual.get(Calendar.MINUTE);
        
        // Convertir a minutos totales para comparación fácil
        int minutosTotalesSel = horaSel * 60 + minutoSel;
        int minutosTotalesActual = horaActual * 60 + minutoActual;
        
        // VALIDAR: No permitir horas anteriores a la actual (solo hoy)
        if (minutosTotalesSel < minutosTotalesActual) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                "No puede seleccionar una hora anterior a la actual.\n" +
                "Hora seleccionada: " + sdf.format(horaSeleccionada) + "\n" +
                "Hora actual: " + sdf.format(calActual.getTime()),
                "Hora Inválida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // VALIDAR: No permitir horas más de 24 horas en el futuro
        if (minutosTotalesSel > minutosTotalesActual + (24 * 60)) {
            JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                "El recordatorio no puede programarse para más de 24 horas en el futuro.",
                "Hora Inválida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convertir a Time y guardar
        Time horaRecordatorio = new Time(horaSeleccionada.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String horaFormateada = sdf.format(horaSeleccionada);
        
        activarRecordatorio(tareaID, horaRecordatorio, horaFormateada);
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(PanelRecordatorios.this, 
            "Error al procesar la hora: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
    
    // MODIFICADO: Método para activar recordatorio
        private void activarRecordatorio(int tareaID, Time horaRecordatorio, String horaFormateada) {
            try {
                boolean exito = tareaNegocio.activarRecordatorio(tareaID, horaRecordatorio);

                if (exito) {
                    JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                        "Recordatorio activado para las " + horaFormateada,
                        "Recordatorio Configurado",
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarTareasPendientes();
                } else {
                    JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                        "Error al activar recordatorio", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    
    // Método para desactivar recordatorio
        private void desactivarRecordatorio(int tareaID) {
            boolean exito = tareaNegocio.desactivarRecordatorio(tareaID);

            if (exito) {
                JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                    "Recordatorio desactivado",
                    "Recordatorio Desactivado",
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTareasPendientes();
            } else {
                JOptionPane.showMessageDialog(PanelRecordatorios.this, 
                    "Error al desactivar recordatorio", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refrescarPanel() {
        cargarTareasPendientes();
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
