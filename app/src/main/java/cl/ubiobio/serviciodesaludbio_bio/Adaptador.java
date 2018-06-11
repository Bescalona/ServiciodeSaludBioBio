//clase con metodos necesarios para desplegar la lista de farmacias de turno

package cl.ubiobio.serviciodesaludbio_bio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bastian on 10-06-2018.
 */

public class Adaptador extends BaseAdapter {

    private Context context;
    private ArrayList<Item> listItems;

    public Adaptador(Context context, ArrayList<Item> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    //retorna el largo de la vista
    @Override
    public int getCount() {
        return listItems.size();
    }

    //retorna un item segun su posicion en el arreglo
    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //en este metodo creamos cada item de farmacias de turno y le asiggnamos los elementos
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item fturno = (Item) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
        TextView titulo = (TextView) convertView.findViewById(R.id.titulo);
        TextView ciudad = (TextView) convertView.findViewById(R.id.ciudad);
        TextView direccion = (TextView) convertView.findViewById(R.id.direcccion);
        TextView horario = (TextView) convertView.findViewById(R.id.horario);

        titulo.setText(fturno.getTitulo());
        ciudad.setText(fturno.getCiudad());
        direccion.setText(fturno.getDireccion());
        horario.setText(fturno.getHorario());
        return convertView;
    }

    //filtra los objetos del listView segun la variable indicada en FarmaciaActivity (en este caso filtro por ciudad)
    public void filterList(ArrayList<Item> filteredList){
        listItems = filteredList;
        notifyDataSetChanged();
    }
}
