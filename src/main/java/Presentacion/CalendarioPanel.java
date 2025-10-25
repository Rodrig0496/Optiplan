package Presentacion;
import Entidades.Tarea;
import Negocio.TareaNegocio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
/**
 *
 * @author Mi Equipo
 */
public class CalendarioPanel extends javax.swing.JPanel {
    private JPanel headerPanel;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private Calendar currentCalendar;
    private TareaNegocio tareaNegocio;
    private int usuarioID;
    private Map<String, List<Tarea>> tareasPorFecha;

    public CalendarioPanel(int usuarioID) {
        this.usuarioID = usuarioID;
        this.tareaNegocio = new TareaNegocio();
        this.currentCalendar = Calendar.getInstance();
        this.tareasPorFecha = new HashMap<>();
        
        initComponents2();
        cargarTareasDelMes();
        actualizarCalendario();
    }

    private void initComponents2() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        // Panel de cabecera con mes y año
        headerPanel = new JPanel(new BorderLayout());
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        monthLabel.setForeground(new Color(52, 152, 219));

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        
        // Estilos de botones
        prevButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        prevButton.setBackground(new Color(52, 152, 219));
        nextButton.setBackground(new Color(52, 152, 219));
        prevButton.setForeground(Color.WHITE);
        nextButton.setForeground(Color.WHITE);
        
        prevButton.addActionListener(e -> {
            currentCalendar.add(Calendar.MONTH, -1);
            cargarTareasDelMes();
            actualizarCalendario();
        });
        
        nextButton.addActionListener(e -> {
            currentCalendar.add(Calendar.MONTH, 1);
            cargarTareasDelMes();
            actualizarCalendario();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Panel del calendario
        calendarPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
    }

    private void cargarTareasDelMes() {
        // Obtener primer y último día del mes actual
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        // Convertir explícitamente a java.sql.Date
        java.sql.Date startDate = new java.sql.Date(cal.getTime().getTime());

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        java.sql.Date endDate = new java.sql.Date(cal.getTime().getTime());

        // Cargar tareas del mes
        List<Tarea> tareasMes = tareaNegocio.obtenerTareasPorRangoFechas(usuarioID, startDate, endDate);
        
        // Organizar tareas por fecha
        tareasPorFecha.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Tarea tarea : tareasMes) {
            String fechaKey = dateFormat.format(tarea.getFecha());
            tareasPorFecha.computeIfAbsent(fechaKey, k -> new java.util.ArrayList<>()).add(tarea);
        }
    }

    public void actualizarCalendario() {
        calendarPanel.removeAll();
        
        // Actualizar label del mes
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy");
        monthLabel.setText(monthFormat.format(currentCalendar.getTime()).toUpperCase());

        // Días de la semana
        String[] diasSemana = {"DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB"};
        for (String dia : diasSemana) {
            JLabel dayLabel = new JLabel(dia, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            dayLabel.setBackground(new Color(241, 241, 241));
            dayLabel.setOpaque(true);
            dayLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
            calendarPanel.add(dayLabel);
        }

        // Obtener primer día del mes y día de la semana
        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Ajustar para que la semana empiece en domingo
        int startCell = firstDayOfWeek - Calendar.SUNDAY;

        // Celdas vacías antes del primer día
        for (int i = 0; i < startCell; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Días del mes
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int day = 1; day <= daysInMonth; day++) {
            JPanel dayPanel = crearPanelDia(day, cal, dateFormat);
            calendarPanel.add(dayPanel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JPanel crearPanelDia(int day, Calendar cal, SimpleDateFormat dateFormat) {
        JPanel dayPanel = new JPanel(new BorderLayout());
        dayPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        dayPanel.setBackground(Color.WHITE);

        // Label del día
        JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER);
        dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Verificar si es hoy
        Calendar today = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
            day == today.get(Calendar.DAY_OF_MONTH)) {
            dayLabel.setBackground(new Color(52, 152, 219));
            dayLabel.setForeground(Color.WHITE);
            dayLabel.setOpaque(true);
        }

        dayPanel.add(dayLabel, BorderLayout.NORTH);

        // Panel para tareas
        JPanel tasksPanel = new JPanel();
        tasksPanel.setLayout(new BorderLayout());
        tasksPanel.setBackground(Color.WHITE);

        // Verificar si hay tareas para este día
        cal.set(Calendar.DAY_OF_MONTH, day);
        String fechaKey = dateFormat.format(cal.getTime());
        List<Tarea> tareasDia = tareasPorFecha.get(fechaKey);

        if (tareasDia != null && !tareasDia.isEmpty()) {
            JTextArea tasksText = new JTextArea();
            tasksText.setEditable(false);
            tasksText.setBackground(new Color(248, 248, 248));
            tasksText.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            tasksText.setMargin(new java.awt.Insets(2, 2, 2, 2));
            
            StringBuilder sb = new StringBuilder();
            for (Tarea tarea : tareasDia) {
                String hora = tarea.getHoraFin() != null ? 
                    new SimpleDateFormat("HH:mm").format(tarea.getHoraFin()) : "Sin hora";
                sb.append("• ").append(tarea.getTitulo())
                  .append(" (").append(hora).append(")\n");
            }
            tasksText.setText(sb.toString());
            
            tasksPanel.add(tasksText, BorderLayout.CENTER);
            
            // Cambiar color de fondo si hay tareas
            dayPanel.setBackground(new Color(230, 245, 255));
        }

        dayPanel.add(tasksPanel, BorderLayout.CENTER);
        return dayPanel;
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
