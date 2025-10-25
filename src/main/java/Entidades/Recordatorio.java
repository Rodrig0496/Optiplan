/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;
import java.util.Date;
/**
 *
 * @author Mi Equipo
 */
public class Recordatorio {
    private int recordatorioID;
    private int usuarioID;
    private int tareaID;
    private Date fechaEnvio;
    private String medio;

    public Recordatorio() {}

    public Recordatorio(int recordatorioID, int usuarioID, int tareaID, Date fechaEnvio, String medio) {
        this.recordatorioID = recordatorioID;
        this.usuarioID = usuarioID;
        this.tareaID = tareaID;
        this.fechaEnvio = fechaEnvio;
        this.medio = medio;
    }

    public int getRecordatorioID() { return recordatorioID; }
    public void setRecordatorioID(int recordatorioID) { this.recordatorioID = recordatorioID; }

    public int getUsuarioID() { return usuarioID; }
    public void setUsuarioID(int usuarioID) { this.usuarioID = usuarioID; }

    public int getTareaID() { return tareaID; }
    public void setTareaID(int tareaID) { this.tareaID = tareaID; }

    public Date getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getMedio() { return medio; }
    public void setMedio(String medio) { this.medio = medio; }
}
