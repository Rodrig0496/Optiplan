package Entidades;
import java.util.Date;
import java.sql.Time;
/**
 *
 * @author Mi Equipo
 */
public class Tarea {
    private int tareaID;
    private int usuarioID;
    private String titulo;
    private String descripcion;
    private int categoriaID;
    private String prioridad;
    private String estado;
    private Date fecha;
    private Time horaInicio;
    private Time horaFin;
    private Date fechaCreacion;
    private boolean recordatorioActivo;
    private Time horaRecordatorio;
    public Tarea() {}

    public Tarea(int tareaID, int usuarioID, String titulo, String descripcion, int categoriaID,
                 String prioridad, String estado, Date fecha, Time horaInicio, Time horaFin,
                 Date fechaCreacion, boolean recordatorioActivo, Time horaRecordatorio) {
        this.tareaID = tareaID;
        this.usuarioID = usuarioID;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoriaID = categoriaID;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fechaCreacion = fechaCreacion;
        this.recordatorioActivo = recordatorioActivo;
        this.horaRecordatorio = horaRecordatorio;
    }

    // Getters y setters
    public int getTareaID() { return tareaID; }
    public void setTareaID(int tareaID) { this.tareaID = tareaID; }

    public int getUsuarioID() { return usuarioID; }
    public void setUsuarioID(int usuarioID) { this.usuarioID = usuarioID; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCategoriaID() { return categoriaID; }
    public void setCategoriaID(int categoriaID) { this.categoriaID = categoriaID; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }

    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }

    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public boolean isRecordatorioActivo() { return recordatorioActivo; }
    public void setRecordatorioActivo(boolean recordatorioActivo) { this.recordatorioActivo = recordatorioActivo; }

    public Time getHoraRecordatorio() { return horaRecordatorio; }
    public void setHoraRecordatorio(Time horaRecordatorio) { this.horaRecordatorio = horaRecordatorio; }
}
