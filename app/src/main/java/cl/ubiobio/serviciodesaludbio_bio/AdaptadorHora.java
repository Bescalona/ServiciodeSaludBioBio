package cl.ubiobio.serviciodesaludbio_bio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bastian on 15-06-2018.
 */

public class AdaptadorHora extends BaseAdapter {
    private Context context;
    private ArrayList<ResponseXML> listaHoras;

    public AdaptadorHora(Context context, ArrayList<ResponseXML> listaHoras) {
        this.context = context;
        this.listaHoras = listaHoras;
    }

    //retorna el largo de la vista
    @Override
    public int getCount() {
        return listaHoras.size();
    }

    //retorna un item segun su posicion en el arreglo
    @Override
    public Object getItem(int position) {
        return listaHoras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //en este metodo creamos cada item de consulta hora medica y le asiggnamos los elementos
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseXML hmed = (ResponseXML) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.hora,null);
        TextView paciente = (TextView) convertView.findViewById(R.id.paciente);
        TextView fecha_asignada = (TextView) convertView.findViewById(R.id.fecha_asignada);
        TextView profesional = (TextView) convertView.findViewById(R.id.profesional);
        TextView policlinico = (TextView) convertView.findViewById(R.id.policlinico);
        TextView ubicacion = (TextView) convertView.findViewById(R.id.ubicacion);

        paciente.setText(hmed.getPaciente());
        fecha_asignada.setText(hmed.getFecha_asignada());
        profesional.setText(hmed.getProfesional());
        policlinico.setText(hmed.getPoliclinico());
        ubicacion.setText(hmed.getUbicacion());
        return convertView;
    }
}
