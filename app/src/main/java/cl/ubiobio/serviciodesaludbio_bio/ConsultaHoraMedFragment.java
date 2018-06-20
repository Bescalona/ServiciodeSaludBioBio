package cl.ubiobio.serviciodesaludbio_bio;
/*Fragment asociado al layout activity_chora, su funcion es ser llamado en el MainJovenActivity para luego mostrar su layout en la pantalla
  principal del modo joven (content_joven)*/
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultaHoraMedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultaHoraMedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultaHoraMedFragment extends Fragment implements View.OnClickListener {
    private Button brut;
    private EditText rutform;
    String rutFormateado;
    String antesGuion;
    String despuesGuion;
    int antesGuionInt;


    private TextView sinHora;
    //private EditText rut;

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
    //Array donde almaceno objetos de la clase ResponseXML que contiene datos de las horas medicas que mostrare en el layout
    ArrayList<ResponseXML> horas;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ConsultaHoraMedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultaHoraMedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultaHoraMedFragment newInstance(String param1, String param2) {
        ConsultaHoraMedFragment fragment = new ConsultaHoraMedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //inicializo mi arreglo con objetos item
        horas = new ArrayList<>();
        horas.clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chora, container, false);
        sinHora= view.findViewById(R.id.sinhora);
        sinHora.setText("");
        brut = view.findViewById(R.id.brut);
        brut.setOnClickListener(this);
        //obtengo el rut desde el EditText del layout activity_chora.xml
        rutform = view.findViewById(R.id.rut);
        //inicializo mi lista ubicada en activity_farmacia.xml
        listaHoras = view.findViewById(R.id.listaHoras);
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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

    //funcion que despliega un mensaje (toast)
    private void generateToast(String msg){
        Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
    }

    //metodo que consulta a una API las horas medicas de un rut que pasamos por parametro y las despliega en el ListView de activity_chora.xml
    private void getHoras(int antesGuionInt, String despuesGuion){

        String WS_URL = "http://10.8.117.115/ws/SAC/Servicios_Usuarios/server.php";
        final String requestBody = getXMLBody(antesGuionInt, despuesGuion);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                            //si el tamaño de lista es 1, significa que no pudo entregarme info de horas medicas o que solo tiene una unica hora medica
                            if(lista.size() == 1){
                                //si lista.get(0).getCodigo() es diferente de 1 significa que no pudo entregar info de horas medicas, por lo tanto despliego un mensaje con la descripcion del error
                                if(!lista.get(0).getCodigo().equals("1")){
                                    Log.e("ERROR", "DESCRIPCION: " + lista.get(0).getDescripcion_codigo());
                                    Log.e("ERROR", "CODIGO: " + lista.get(0).getCodigo());
                                    sinHora.setText(lista.get(0).getDescripcion_codigo());
                                }else{
                                    //en el caso que lista.get(0).getCodigo() sea 1, significa que exite una unica hora medica, almaceno los valores en el objeto respuesta y luego le paso objeto a la lista horas
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
                                //en el caso de que la lista se mayor a 1 significa que el rut ingresado tiene mas de 1 hora medica, en ese caso almaceno todas las horas medicas en horas
                            }else{
                                for (int i=0;i<lista.size();i++){
                                    respuesta.setCodigo(lista.get(i).getCodigo());
                                    respuesta.setDescripcion_codigo(lista.get(i).getDescripcion_codigo());
                                    respuesta.setFecha_asignada(lista.get(i).getFecha_asignada());
                                    respuesta.setPaciente(lista.get(i).getPaciente());
                                    respuesta.setPoliclinico(lista.get(i).getPoliclinico());
                                    respuesta.setProfesional(lista.get(i).getProfesional());
                                    respuesta.setTipo_hora(lista.get(i).getTipo_hora());
                                    respuesta.setUbicacion(lista.get(i).getUbicacion());
                                    horas.add(respuesta);
                                }
                            }
                            adaptador = new AdaptadorHora(getActivity(),horas);
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
                //limpio la pantalla de horas medicas mostradas anteriormente
                horas.clear();

                sinHora.setText("");
                //le paso el rut a la funcion validarRut que verifica que sea correcto
                if(validarRut(rutform.getText().toString())) {
                    //en el caso que devuelva true, le quito el guion al rut y lo separo entre la parte antes del guion y la despues del guion
                    rutFormateado = rutform.getText().toString().replace("-","");
                    antesGuion =rutFormateado.substring(0,rutFormateado.length()-1);
                    //transformo la parte antes del guion a int
                    antesGuionInt=Integer.parseInt(antesGuion);
                    despuesGuion= rutFormateado.substring(rutFormateado.length()-1,rutFormateado.length()).toUpperCase();

                    //invoco al metodo getHoras el cual busca las horas medicas dado un rut que paso por parametro y llena la lista con info de las horas
                    getHoras(antesGuionInt,despuesGuion);
                }else{
                    rutform.setError("El rut ingresado es invalido");
                }

                break;
        }
    }
}
