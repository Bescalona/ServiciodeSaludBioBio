package cl.ubiobio.serviciodesaludbio_bio;

/*Fragment asociado al layout activity_farmacia, su funcion es ser llamado en el MainJovenActivity para luego mostrar su layout en la pantalla
  principal del modo joven (content_joven)*/
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FarmaciaTurnoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FarmaciaTurnoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FarmaciaTurnoFragment extends Fragment implements View.OnClickListener {
    private Button farmacia_cercana;
    private ProgressBar progressBarFarmacia;
    private ListView fragment_lvlItems;
    private Adaptador adaptador;
    //Array donde almaceno objetos de la clase item que contiene datos de las farmacias que mostrare en el layout
    ArrayList<Item> farmacias;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FarmaciaTurnoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FarmaciaTurnoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FarmaciaTurnoFragment newInstance(String param1, String param2) {
        FarmaciaTurnoFragment fragment = new FarmaciaTurnoFragment();
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
        farmacias = new ArrayList<>();
        farmacias.clear();
        //invoco al metodo getFarmacias el cual busca las farmacias de turno y llena la lista con info de ellas
        getFarmacias();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_farmacia, container, false);

        farmacia_cercana = view.findViewById(R.id.mapa_farmacia);
        farmacia_cercana.setOnClickListener(this);

        //inicializo el efecto de carga
        progressBarFarmacia = view.findViewById(R.id.progressBarFarmacia);

        //inicializo el editText donde ingresare texto para filtar por ciudad
        EditText editText = view.findViewById(R.id.fragment_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        fragment_lvlItems = view.findViewById(R.id.fragment_lvlItems);
        // Inflate the layout for this fragment
        return view;
    }

    //funcion que filtra los elementos  del arreglo de farmacia que se muestran en el listview
    private void filter(String text){
        ArrayList<Item> filteredList = new ArrayList<>();
        for(Item dat: farmacias){
            //filtro por ciudad
            if(dat.getCiudad().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(dat);
            }
        }
        adaptador.filterList(filteredList);
    }

    //Si existe un erro en el web service despliego el siguiente mensaje
    private void generateToast(String msg){
        Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
    }

    //metodo que consulta a una API las farmacias de turno y las despliega en el ListView de activity_farmacia.xml
    private void getFarmacias(){

        Log.d("LOG WS", "entre");
        String WS_URL = "http://farmanet.minsal.cl/index.php/ws/getLocalesTurnos";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                            adaptador = new Adaptador(getContext(),farmacias);
                            progressBarFarmacia.setVisibility(View.INVISIBLE);
                            fragment_lvlItems.setAdapter(adaptador);

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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.mapa_farmacia:
                Intent mapfarm = new Intent(getActivity(), MapaFarmacia.class);
                startActivity(mapfarm);
                break;
        }
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
}
