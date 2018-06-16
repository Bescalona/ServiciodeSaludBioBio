package cl.ubiobio.serviciodesaludbio_bio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CHoraActivity extends AppCompatActivity implements View.OnClickListener {


    private Button brut;
    private EditText rutform;
    String validaRut;


    private TextView sinHora;
    private EditText rut;

    private static final String ns = null;

    private static final String ETIQUETA_CODIGO = "codigo";
    private static final String ETIQUETA_DESCRIPCION = "descripcion";
    private static final String ETIQUETA_PACIENTE = "paciente";
    private static final String ETIQUETA_FECHA_ASIGNADA = "fecha_asignada";
    private static final String ETIQUETA_PROFESIONAL = "profesional";
    private static final String ETIQUETA_POLICLINICO = "policlinico";
    private static final String ETIQUETA_UBICACION = "ubicacion";
    private static final String ETIQUETA_TIPO_HORA = "tipo_hora";
    private static final String ETIQUETA_ITEM = "item";
    private static final String ETIQUETA_RETURN = "return";

    private ListView listaHoras;
    private AdaptadorHora adaptador;
    //Array donde almaceno objetos de la clase item que contiene datos de las farmacias que mostrare en el layout
    ArrayList<ResponseXML> horas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chora);
        //cambio el titulo del ActionBar por uno correspondiente a la clase
        getSupportActionBar().setTitle("Consulta hora médica");

        brut = findViewById(R.id.brut);
        brut.setOnClickListener(this);

        sinHora= findViewById(R.id.sinhora);
        //inicializo mi arreglo con objetos item
        horas = new ArrayList<>();
        //inicializo mi lista ubicada en activity_farmacia.xml
        listaHoras = findViewById(R.id.listaHoras);
        horas.clear();

    }

    //Metodo al cual le paso un rut y me devuelve true si el rut es valido y false si el rut es invalido
    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    //Si existe un error en el web service despliego el siguiente mensaje
    private void generateToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }

    //metodo que consulta a una API las farmacias de turno y las despliega en el ListView de activity_farmacia.xml
    private void getHoras(){

        String WS_URL = "http://10.8.117.115/ws/SAC/Servicios_Usuarios/server.php";
        final String requestBody = getXMLBody(18821266, "2");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(
                Request.Method.POST,
                WS_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //en respuesta almaceno lo que me entregue el xml en el caso de que existan horas medicas
                        ResponseXML respuesta = new ResponseXML();
                        Log.d("LOG WS", response);
                        XmlPullParser parser = Xml.newPullParser();
                        InputStream stream = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
                        try {
                            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                            parser.setInput(stream, null);
                            parser.nextTag();

                            while(!parser.getName().equals(ETIQUETA_RETURN)){
                                Log.d("LOG", "name: " + parser.getName());
                                parser.nextTag();
                            }
                            List<ResponseXML> lista = leerHoras(parser);
                            if(lista.size() == 1){
                                if(!lista.get(0).getCodigo().equals(1)){
                                    Log.e("ERROR", "DESCRIPCION: " + lista.get(0).getDescripcion_codigo());
                                    sinHora.setText(lista.get(0).getDescripcion_codigo());
                                }else{
                                    respuesta.setCodigo(lista.get(0).getCodigo());
                                    respuesta.setDescripcion_codigo(lista.get(0).getDescripcion_codigo());
                                    respuesta.setFecha_asignada(lista.get(0).getFecha_asignada());
                                    respuesta.setPaciente(lista.get(0).getPaciente());
                                    respuesta.setPoliclinico(lista.get(0).getPoliclinico());
                                    respuesta.setProfesional(lista.get(0).getProfesional());
                                    respuesta.setTipo_hora(lista.get(0).getTipo_hora());
                                    respuesta.setUbicacion(lista.get(0).getUbicacion());
                                    horas.add(respuesta);
                                }
                            }else{
                                for (int i=0;i<lista.size();i++){
                                    respuesta.setCodigo(lista.get(0).getCodigo());
                                    respuesta.setDescripcion_codigo(lista.get(0).getDescripcion_codigo());
                                    respuesta.setFecha_asignada(lista.get(0).getFecha_asignada());
                                    respuesta.setPaciente(lista.get(0).getPaciente());
                                    respuesta.setPoliclinico(lista.get(0).getPoliclinico());
                                    respuesta.setProfesional(lista.get(0).getProfesional());
                                    respuesta.setTipo_hora(lista.get(0).getTipo_hora());
                                    respuesta.setUbicacion(lista.get(0).getUbicacion());
                                    horas.add(respuesta);
                                }
                            }
                            adaptador = new AdaptadorHora(getApplicationContext(),horas);
                            listaHoras.setAdapter(adaptador);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG WS", error.toString());
            }
        }
        ){
            @Override
            public String getBodyContentType() {
                return "text/xml;charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("SOAPAction", "urn:WS#HorasAsignadasUsuarios");
                //params.put("Content-Type","text/xml;charset=UTF-8");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.d("LOG WSSS", "body: " + requestBody);
                try {
                    return requestBody == null ? null : requestBody.getBytes("UTF-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private List<ResponseXML> leerHoras(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<ResponseXML> listaHoteles = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_RETURN);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombreEtiqueta = parser.getName();
            if (nombreEtiqueta.equals(ETIQUETA_ITEM)) {
                listaHoteles.add(leerHora(parser));
            } else {
                saltarEtiqueta(parser);
            }
        }
        return listaHoteles;
    }
    private ResponseXML leerHora(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_ITEM);
        String codigo = null;
        String descripcion_codigo = null;
        String paciente = null;
        String fecha_asignada = null;
        String profesional = null;
        String policlinico = null;
        String ubicacion = null;
        String tipo_hora = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case ETIQUETA_CODIGO:
                    codigo = leerCodigo(parser);
                    break;
                case ETIQUETA_DESCRIPCION:
                    descripcion_codigo = leerDescripcion(parser);
                    break;
                case ETIQUETA_PACIENTE:
                    paciente = leerPaciente(parser);
                    break;
                case ETIQUETA_FECHA_ASIGNADA:
                    fecha_asignada = leerFecha(parser);
                    break;
                case ETIQUETA_PROFESIONAL:
                    profesional = leerProfesional(parser);
                    break;
                case ETIQUETA_POLICLINICO:
                    policlinico = leerPoliclinico(parser);
                    break;
                case ETIQUETA_UBICACION:
                    ubicacion = leerUbicacion(parser);
                    break;
                case ETIQUETA_TIPO_HORA:
                    tipo_hora = leerTipoHora(parser);
                    break;
                default:
                    saltarEtiqueta(parser);
                    break;
            }
        }
        return new ResponseXML(codigo,descripcion_codigo,paciente,fecha_asignada,profesional,policlinico,ubicacion,tipo_hora);
    }

    private String leerCodigo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_CODIGO);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_CODIGO);
        return nombre;
    }

    private String leerDescripcion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_DESCRIPCION);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_DESCRIPCION);
        return nombre;
    }

    private String leerPaciente(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PACIENTE);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_PACIENTE);
        return nombre;
    }

    private String leerFecha(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_FECHA_ASIGNADA);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_FECHA_ASIGNADA);
        return nombre;
    }

    private String leerProfesional(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_PROFESIONAL);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_PROFESIONAL);
        return nombre;
    }

    private String leerPoliclinico(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_POLICLINICO);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_POLICLINICO);
        return nombre;
    }

    private String leerUbicacion(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_UBICACION);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_UBICACION);
        return nombre;
    }

    private String leerTipoHora(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, ETIQUETA_TIPO_HORA);
        String nombre = obtenerTexto(parser);
        parser.require(XmlPullParser.END_TAG, ns, ETIQUETA_TIPO_HORA);
        return nombre;
    }

    private String obtenerTexto(XmlPullParser parser) throws IOException, XmlPullParserException {
        String resultado = "";
        if (parser.next() == XmlPullParser.TEXT) {
            resultado = parser.getText();
            parser.nextTag();
        }
        return resultado;
    }

    public String getXMLBody(int rut, String dv) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:WS\">");
        stringBuilder.append("<soapenv:Header/>");
        stringBuilder.append("<soapenv:Body>");
        stringBuilder.append("<urn:HorasAsignadasUsuarios soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
        stringBuilder.append("<proArray xsi:type=\"urn:ArrayReq\">");
        stringBuilder.append("<rut_paciente xsi:type=\"xsd:int\">" + rut +"</rut_paciente>");
        stringBuilder.append("<dv_paciente xsi:type=\"xsd:string\">" + dv +"</dv_paciente>");
        stringBuilder.append("<token xsi:type=\"xsd:string\">qmVq2x7Yxm</token>");
        stringBuilder.append("</proArray>");
        stringBuilder.append("</urn:HorasAsignadasUsuarios>");
        stringBuilder.append("</soapenv:Body>");
        stringBuilder.append("</soapenv:Envelope>");
        String result = stringBuilder.toString();
        Log.d("LOG", result);
        return result;
    }

    private void saltarEtiqueta(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brut:
                //obtengo el rut desde el EditText del layout activity_chora.xml
                rutform = findViewById(R.id.rut);
                validaRut = rutform.getText().toString();
                if(validarRut(validaRut)) {
                    //invoco al metodo getFarmacias el cual busca las farmacias de turno y llena la lista con info de ellas
                    getHoras();
                }else{
                    rutform.setError("El rut ingresado es invalido");
                }

                break;
        }
    }
}
