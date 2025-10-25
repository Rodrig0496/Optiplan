package Presentacion;
import Entidades.Tarea;
import Negocio.TareaNegocio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
/**
 *
 * @author Mi Equipo
 */
public class PanelSemanal extends javax.swing.JPanel {
    private JPanel headerPanel;
    private JPanel weekPanel;
    private JLabel weekLabel;
    private Calendar currentCalendar;
    private TareaNegocio tareaNegocio;
    private int usuarioID;
    private Map<String, List<Tarea>> tareasPorFecha;
    /**
     * Creates new form PanelSemanal
     */
    public PanelSemanal(int usuarioID ) {
        this.usuarioID = usuarioID;
        this.tareaNegocio = new TareaNegocio();
        this.currentCalendar = Calendar.getInstance();
        this.tareasPorFecha = new HashMap<>();
        
        initComponents2();
        cargarTareasDeLaSemana();
        actualizarSemana();
    }
    private void initComponents2() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 500));

        // Panel de cabecera con semana
        headerPanel = new JPanel(new BorderLayout());
        weekLabel = new JLabel("", SwingConstants.CENTER);
        weekLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        weekLabel.setForeground(new Color(52, 152, 219));

        JButton prevButton = new JButton("< Semana Anterior");
        JButton nextButton = new JButton("Semana Siguiente >");
        JButton hoyButton = new JButton("Hoy");
        
        // Estilos de botones
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 12);
        prevButton.setFont(buttonFont);
        nextButton.setFont(buttonFont);
        hoyButton.setFont(buttonFont);
        
        prevButton.setBackground(new Color(52, 152, 219));
        nextButton.setBackground(new Color(52, 152, 219));
        hoyButton.setBackground(new Color(46, 204, 113));
        
        prevButton.setForeground(Color.WHITE);
        nextButton.setForeground(Color.WHITE);
        hoyButton.setForeground(Color.WHITE);
        
        prevButton.addActionListener(e -> {
            currentCalendar.add(Calendar.WEEK_OF_YEAR, -1);
            cargarTareasDeLaSemana();
            actualizarSemana();
        });
        
        nextButton.addActionListener(e -> {
            currentCalendar.add(Calendar.WEEK_OF_YEAR, 1);
            cargarTareasDeLaSemana();
            actualizarSemana();
        });
        
        hoyButton.addActionListener(e -> {
            currentCalendar = Calendar.getInstance();
            cargarTareasDeLaSemana();
            actualizarSemana();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(prevButton);
        buttonPanel.add(hoyButton);
        buttonPanel.add(nextButton);
        
        headerPanel.add(weekLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Panel de la semana (7 días)
        weekPanel = new JPanel(new GridLayout(1, 7, 5, 5));
        weekPanel.setBackground(new Color(240, 240, 240));
        add(new JScrollPane(weekPanel), BorderLayout.CENTER);
    }
    
    
    
    private void cargarTareasDeLaSemana() {
        // Obtener lunes y domingo de la semana actual
        Calendar cal = (Calendar) currentCalendar.clone();

        // Ir al lunes de la semana
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        // Convertir a java.sql.Date
        java.sql.Date startDate = new java.sql.Date(cal.getTime().getTime());

        // Ir al domingo de la semana
        cal.add(Calendar.DAY_OF_WEEK, 6);
        java.sql.Date endDate = new java.sql.Date(cal.getTime().getTime());

        // Cargar tareas de la semana
        List<Tarea> tareasSemana = tareaNegocio.obtenerTareasPorRangoFechas(usuarioID, startDate, endDate);

        // Organizar tareas por fecha
        tareasPorFecha.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Tarea tarea : tareasSemana) {
            String fechaKey = dateFormat.format(tarea.getFecha());
            tareasPorFecha.computeIfAbsent(fechaKey, k -> new java.util.ArrayList<>()).add(tarea);
        }
    }

    public void actualizarSemana() {
        weekPanel.removeAll();
        
        // Actualizar label de la semana
        SimpleDateFormat weekFormat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String inicioSemana = weekFormat.format(cal.getTime());
        
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String finSemana = weekFormat.format(cal.getTime());
        
        weekLabel.setText("Semana: " + inicioSemana + " - " + finSemana);

        // Configurar para empezar desde el lunes
        cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Crear panel para cada día de la semana
        String[] diasSemana = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO"};
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM");
        
        for (int i = 0; i < 7; i++) {
            JPanel dayPanel = crearPanelDia(cal, diasSemana[i], dateFormat, dayFormat);
            weekPanel.add(dayPanel);
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }

        weekPanel.revalidate();
        weekPanel.repaint();
    }

    private JPanel crearPanelDia(Calendar cal, String nombreDia, SimpleDateFormat dateFormat, SimpleDateFormat dayFormat) {
        JPanel dayPanel = new JPanel(new BorderLayout());
        dayPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(180, 180, 180), 1), 
            nombreDia + " (" + dayFormat.format(cal.getTime()) + ")", 
            TitledBorder.CENTER, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 12), 
            new Color(52, 152, 219)
        ));
        
        // Verificar si es hoy
        Calendar today = Calendar.getInstance();
        if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            dayPanel.setBackground(new Color(230, 245, 255));
        } else {
            dayPanel.setBackground(Color.WHITE);
        }

        // Panel para tareas
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new GridLayout(0, 1, 2, 2));
        tasksPanel.setBackground(dayPanel.getBackground());

        // Verificar si hay tareas para este día
        String fechaKey = dateFormat.format(cal.getTime());
        List<Tarea> tareasDia = tareasPorFecha.get(fechaKey);

        if (tareasDia != null && !tareasDia.isEmpty()) {
            for (Tarea tarea : tareasDia) {
                JPanel taskPanel = crearPanelTarea(tarea);
                tasksPanel.add(taskPanel);
            }
        } else {
            JLabel noTasksLabel = new JLabel("Sin tareas", SwingConstants.CENTER);
            noTasksLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            noTasksLabel.setForeground(Color.GRAY);
            tasksPanel.add(noTasksLabel);
        }

        JScrollPane scrollPane = new JScrollPane(tasksPanel);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        scrollPane.setBorder(null);
        
        dayPanel.add(scrollPane, BorderLayout.CENTER);
        return dayPanel;
    }

    private JPanel crearPanelTarea(Tarea tarea) {
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        taskPanel.setBackground(new Color(245, 245, 245));
        taskPanel.setToolTipText("Haz doble clic para editar");

        // Panel superior con título editable
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(getColorByPriority(tarea.getPrioridad()));
        
        JTextField titleField = new JTextField(tarea.getTitulo());
        titleField.setBorder(null);
        titleField.setBackground(titlePanel.getBackground());
        titleField.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleField.setEditable(false); // Inicialmente no editable
        
        // Hacer editable al hacer doble clic
        titleField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarTituloTarea(tarea, titleField);
                }
            }
        });

        titlePanel.add(titleField, BorderLayout.CENTER);

        // Panel inferior con hora y botones
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(250, 250, 250));
        
        String hora = tarea.getHoraFin() != null ? 
            new SimpleDateFormat("HH:mm").format(tarea.getHoraFin()) : "Sin hora";
        
        JLabel timeLabel = new JLabel(hora, SwingConstants.LEFT);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timeLabel.setForeground(Color.DARK_GRAY);
        
        // Botón para eliminar
        JButton deleteButton = new JButton("X");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 8));
        deleteButton.setPreferredSize(new Dimension(20, 15));
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> eliminarTarea(tarea));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(infoPanel.getBackground());
        buttonPanel.add(deleteButton);
        
        infoPanel.add(timeLabel, BorderLayout.WEST);
        infoPanel.add(buttonPanel, BorderLayout.EAST);

        taskPanel.add(titlePanel, BorderLayout.NORTH);
        taskPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Ajustar altura
        taskPanel.setPreferredSize(new Dimension(0, 60));

        return taskPanel;
    }

    private Color getColorByPriority(String prioridad) {
        if (prioridad == null) return new Color(189, 195, 199);
        
        switch (prioridad.toUpperCase()) {
            case "ALTA":
                return new Color(231, 76, 60); // Rojo
            case "MEDIA":
                return new Color(241, 196, 15); // Amarillo
            case "BAJA":
                return new Color(46, 204, 113); // Verde
            default:
                return new Color(189, 195, 199); // Gris
        }
    }

    private void editarTituloTarea(Tarea tarea, JTextField titleField) {
        String nuevoTitulo = JOptionPane.showInputDialog(
            this, 
            "Editar título de la tarea:", 
            tarea.getTitulo()
        );
        
        if (nuevoTitulo != null && !nuevoTitulo.trim().isEmpty()) {
            boolean actualizado = tareaNegocio.actualizarTituloTarea(tarea.getTareaID(), nuevoTitulo.trim());
            if (actualizado) {
                titleField.setText(nuevoTitulo.trim());
                tarea.setTitulo(nuevoTitulo.trim());
                JOptionPane.showMessageDialog(this, "Título actualizado");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar título", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarTarea(Tarea tarea) {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar la tarea: '" + tarea.getTitulo() + "'?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = tareaNegocio.eliminarTarea(tarea.getTareaID());
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Tarea eliminada");
                cargarTareasDeLaSemana();
                actualizarSemana();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar tarea", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refrescarSemana() {
        cargarTareasDeLaSemana();
        actualizarSemana();
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
