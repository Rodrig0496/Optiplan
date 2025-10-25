/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import Entidades.Tarea;
import Negocio.TareaNegocio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
/**
 *
 * @author Mi Equipo
 */
public class Recordatorio {
    private Timer timer;
    private TareaNegocio tareaNegocio;
    
    public Recordatorio() {
        this.tareaNegocio = new TareaNegocio();
        this.timer = new Timer(true); // Timer como daemon
    }
    
    public void iniciarSistema() {
        // Verificar recordatorios cada minuto
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verificarRecordatorios();
            }
        }, 0, 60000); // Cada 60 segundos
    }
    
    public void detenerSistema() {
        if (timer != null) {
            timer.cancel();
        }
    }
    
    private void verificarRecordatorios() {
        try {
            List<Tarea> tareasConRecordatorios = tareaNegocio.obtenerTareasConRecordatoriosActivos();
            Calendar ahora = Calendar.getInstance();
            int horaActual = ahora.get(Calendar.HOUR_OF_DAY);
            int minutoActual = ahora.get(Calendar.MINUTE);

            System.out.println("Verificando recordatorios a las " + horaActual + ":" + minutoActual);
            System.out.println("Tareas con recordatorios activos: " + tareasConRecordatorios.size());

            for (Tarea tarea : tareasConRecordatorios) {
                if (tarea.getHoraRecordatorio() != null) {
                    Time horaRecordatorio = tarea.getHoraRecordatorio();
                    Calendar calRecordatorio = Calendar.getInstance();
                    calRecordatorio.setTime(horaRecordatorio);

                    int horaRecordatorioValor = calRecordatorio.get(Calendar.HOUR_OF_DAY);
                    int minutoRecordatorioValor = calRecordatorio.get(Calendar.MINUTE);

                    System.out.println("Tarea '" + tarea.getTitulo() + "' programada para: " + 
                                     horaRecordatorioValor + ":" + minutoRecordatorioValor);

                    // Verificar si es la hora del recordatorio (con margen de 1 minuto)
                    if (horaActual == horaRecordatorioValor && 
                        Math.abs(minutoActual - minutoRecordatorioValor) <= 1) {
                        System.out.println("MOSTRANDO NOTIFICACIÓN para: " + tarea.getTitulo());
                        mostrarNotificacion(tarea);

                        // Opcional: Desactivar el recordatorio después de mostrarlo
                        tareaNegocio.desactivarRecordatorio(tarea.getTareaID());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en sistema de recordatorios: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarNotificacion(Tarea tarea) {
        // Ejecutar en el hilo de EDT de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            crearVentanaNotificacion(tarea);
        });
    }
    
    private void crearVentanaNotificacion(Tarea tarea) {
        JDialog dialog = new JDialog();
        dialog.setTitle("🔔 Recordatorio de Tarea");
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.setResizable(false);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 255, 240));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Icono y título
        JLabel icono = new JLabel("", SwingConstants.CENTER);
        icono.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel titulo = new JLabel("¡Recordatorio!", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(52, 152, 219));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(icono, BorderLayout.WEST);
        headerPanel.add(titulo, BorderLayout.CENTER);
        headerPanel.setBackground(panel.getBackground());
        
        // Información de la tarea
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setBackground(panel.getBackground());
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setText(String.format(
            "Tarea: %s\n\nDescripción: %s\n\nPrioridad: %s\n\nHora programada: %s",
            tarea.getTitulo(),
            tarea.getDescripcion() != null ? tarea.getDescripcion() : "Sin descripción",
            tarea.getPrioridad() != null ? tarea.getPrioridad() : "No especificada",
            tarea.getHoraFin() != null ? tarea.getHoraFin().toString() : "Sin hora"
        ));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(infoArea, BorderLayout.CENTER);
        
        dialog.add(panel);
        dialog.pack();
        
        // Posicionar en la esquina inferior derecha
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.getWidth() - dialog.getWidth() - 20);
        int y = (int) (screenSize.getHeight() - dialog.getHeight() - 40);
        dialog.setLocation(x, y);
        
        // Auto-cerrar después de 10 segundos
        Timer timerCierre = new Timer();
        timerCierre.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dispose();
            }
        }, 10000);
        
        dialog.setVisible(true);
        
        // Reproducir sonido del sistema (opcional)
        Toolkit.getDefaultToolkit().beep();
    }
}
