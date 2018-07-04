package cl.ubiobio.serviciodesaludbio_bio;

public class CentroSalud {

    public static final CentroSalud[] CENTROS = {
            new CentroSalud(-37.26396517332799, -72.70894288978565, "Hospital de Laja", CentroSalud.TIPO_HOSPITAL),
            new CentroSalud(-37.72017747976133, -72.24048722738715, "Hospital de Mulchen", CentroSalud.TIPO_HOSPITAL),
            new CentroSalud(-37.507646090046, -72.67705064572718, "Hospital de Nacimiento", CentroSalud.TIPO_HOSPITAL),
            new CentroSalud(-37.24029753985731, -71.94249144372948, "Hospital de Huepil", CentroSalud.TIPO_HOSPITAL),
            new CentroSalud(-37.09646773819262, -72.55789529008226, "Hospital de Yumbel", CentroSalud.TIPO_HOSPITAL),
            new CentroSalud(-37.66771548155897, -72.01686339946335, "Hospital de Santa Barbara", CentroSalud.TIPO_HOSPITAL),
            new CentroSalud(-37.03863082798051, -72.39760816524732, "SAR Cabrero", CentroSalud.TIPO_SAR),
            new CentroSalud(-37.46391253623157, -72.36150034639502, "SAR Norte", CentroSalud.TIPO_SAR),
            new CentroSalud(-37.45717366542049, -72.34298031662847, "SAPU Nor Oriente", CentroSalud.TIPO_SAPU),
            new CentroSalud(-37.47711475738601, -72.36535129973514, "SAPU Dos De Septiembre", CentroSalud.TIPO_SAPU),
            new CentroSalud(-37.485556, -72.339167, "SAPU Cesfam Paillihue", CentroSalud.TIPO_SAPU),
            new CentroSalud(-37.46588415795363, -72.58176564117416, "SAPU Cesfam Santa Fe", CentroSalud.TIPO_SAPU),
            new CentroSalud(-37.46732655571086, -72.38350914312044, "SAPU Cesfam Nuevo Horizonte", CentroSalud.TIPO_SAPU)
    };

    public static final int TIPO_HOSPITAL = 0;
    public static final int TIPO_SAR = 1;
    public static final int TIPO_SAPU = 2;

    private double latitud;
    private double longitud;
    private String nombre;
    private int tipo;

    public CentroSalud(double latitud, double longitud, String nombre, int tipo) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
