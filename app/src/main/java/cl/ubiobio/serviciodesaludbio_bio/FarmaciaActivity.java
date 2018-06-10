package cl.ubiobio.serviciodesaludbio_bio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FarmaciaActivity extends AppCompatActivity {

    private ListView lvlItems;
    private Adaptador adaptador;
    //Array donde almaceno objetos de la clase item que contiene datos de las farmacias que mostrare en el layout
    ArrayList<Item> farmacias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacia);
        //cambio el titulo del ActionBar por uno correspondiente a la clase
        getSupportActionBar().setTitle("Farmacias de turno");

        //inicializo mi arreglo con objetos item
        farmacias = new ArrayList<>();
        //inicializo mi lista ubicada en activity_farmacia.xml
        lvlItems = findViewById(R.id.lvlItems);
        farmacias.clear();
        //invoco al metodo getFarmacias el cual busca las farmacias de turno y llena la lista con info de ellas
        getFarmacias();

    }

    //Si existe un erro en el web service despliego el siguiente mensaje
    private void generateToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
    }

    //metodo que consulta a una API las farmacias de turno y las despliega en el ListView de activity_farmacia.xml
    private void getFarmacias(){

        Log.d("LOG WS", "entre");
        String WS_URL = "http://farmanet.minsal.cl/index.php/ws/getLocalesTurnos";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                WS_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray responseJson = new JSONArray(response);

                            for(int i = 0; i < responseJson.length(); i++){
                                JSONObject o = responseJson.getJSONObject(i);
                                Item far = new Item();

                                //solo capturo los datos de las comunas pertenecientes al servicio de salud bio bio
                                if(o.getString("comuna_nombre").equalsIgnoreCase("ALTO BIO BIO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("ALTO BIO BIO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("ANTUCO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("CABRERO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("LOS ANGELES") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("MULCHEN") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("NACIMIENTO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("NEGRETE") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("QUILACO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("QUILLECO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("SAN ROSENDO") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("SANTA BARBARA") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("TUCAPEL") ||
                                        o.getString("comuna_nombre").equalsIgnoreCase("YUMBEL")) {

                                    far.setTitulo(o.getString("local_nombre"));
                                    far.setCiudad(o.getString("comuna_nombre"));
                                    far.setDireccion(o.getString("local_direccion"));
                                    far.setHorario("Horario de cierre: "+o.getString("funcionamiento_hora_cierre"));

                                    farmacias.add(far);
                                }

                                try{

                                }catch (NumberFormatException e){

                                }


                            }
                            adaptador = new Adaptador(getApplicationContext(),farmacias);
                            lvlItems.setAdapter(adaptador);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG WS", error.toString());
                generateToast("Error en el WEB Service");
            }
        }
        );
        requestQueue.add(request);

    }
}
