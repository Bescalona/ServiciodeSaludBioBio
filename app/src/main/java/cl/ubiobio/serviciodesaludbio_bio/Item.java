package cl.ubiobio.serviciodesaludbio_bio;

/**
 * Created by Bastian on 10-06-2018.
 */

public class Item {

    private String titulo;
    private String ciudad;
    private String direccion;
    private String horario;

    public Item() {
    }

    public Item(String titulo, String ciudad, String direccion, String horario) {
        this.titulo = titulo;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.horario = horario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
