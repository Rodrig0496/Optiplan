package Entidades;
public class Usuario {
    private int usuarioID;
    private String nombre;
    private String correo;
    private String contrasena;
    private java.util.Date fechaRegistro;


    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(int usuarioID, String nombre, String correo, String contrasena, java.util.Date fechaRegistro) {
    this.usuarioID = usuarioID;
    this.nombre = nombre;
    this.correo = correo;
    this.contrasena = contrasena;
    this.fechaRegistro = fechaRegistro;
}

    // Getters y setters
    public int getUsuarioID() { return usuarioID; }
    public void setUsuarioID(int usuarioID) { this.usuarioID = usuarioID; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public java.util.Date getFechaRegistro() { return fechaRegistro; }
public void setFechaRegistro(java.util.Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
