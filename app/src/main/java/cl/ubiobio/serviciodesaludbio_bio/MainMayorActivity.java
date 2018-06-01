package cl.ubiobio.serviciodesaludbio_bio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMayorActivity extends AppCompatActivity implements View.OnClickListener {

    private Button cambiarj;
    private Button hvisita;
    private Button consultah;
    private Button consultap;
    private Button farmaciat;
    private Button saludresp;
    private Button dsangre;
    private Button carteraserv;
    private SharedPreferences sharedPre;
    private SharedPreferences.Editor editorSP;
    private int MODO_JOVEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor);
        sharedPre = getSharedPreferences(getString(R.string.sharedPreID), MODE_PRIVATE);
        editorSP = sharedPre.edit();
        //Asocio los id de los botones del layout activity_mayor a botones de la clase
        cambiarj = findViewById(R.id.cambiar_a_joven);
        hvisita = findViewById(R.id.hvisita);
        consultah = findViewById(R.id.consultah);
        consultap = findViewById(R.id.consultap);
        farmaciat = findViewById(R.id.farmaciaturno);
        saludresp = findViewById(R.id.saludresp);
        dsangre = findViewById(R.id.dsangre);
        carteraserv = findViewById(R.id.carteraserv);
        //llamo a la funcion setOnClickListener(), para realizar una accion al momento de presionar un boton determinado
        cambiarj.setOnClickListener(this);
        hvisita.setOnClickListener(this);
        consultah.setOnClickListener(this);
        consultap.setOnClickListener(this);
        farmaciat.setOnClickListener(this);
        saludresp.setOnClickListener(this);
        dsangre.setOnClickListener(this);
        carteraserv.setOnClickListener(this);
    }

    /*Esta funcion abre una pantalla diferente dependiendo del boton que seleccionamos, ej: si el boton que seleccionamos tiene
    id=hvisita, entonces cambiaremos a la clase HvisitaActivity y por consiguiente al layout activity_hvisita*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //al presionar el boton Cambiar a Modo Joven guardamos el cambio en las preferencias de usuario, por lo tanto la proxima vez que el usuario abra la aplicacion, aparecera la pantalla del modo joven
            case R.id.cambiar_a_joven:
                editorSP.putInt("MODO",MODO_JOVEN);
                editorSP.commit();
                Intent cambiarj = new Intent(MainMayorActivity.this, MainJovenActivity.class);
                startActivity(cambiarj);
                finish();
                break;
            case R.id.hvisita:
                Intent hvisita = new Intent(MainMayorActivity.this, HVisitaActivity.class);
                startActivity(hvisita);
                break;
            case R.id.consultah:
                Intent consultah = new Intent(MainMayorActivity.this, CHoraActivity.class);
                startActivity(consultah);
                break;
            case R.id.consultap:
                Intent consultap = new Intent(MainMayorActivity.this, CEstadoActivity.class);
                startActivity(consultap);
                break;
            case R.id.farmaciaturno:
                Intent farmaciat = new Intent(MainMayorActivity.this, FarmaciaActivity.class);
                startActivity(farmaciat);
                break;
            case R.id.saludresp:
                Intent saludresp = new Intent(MainMayorActivity.this, SaludRespondeActivity.class);
                startActivity(saludresp);
                break;
            case R.id.dsangre:
                Intent dsangre = new Intent(MainMayorActivity.this, DSangreActivity.class);
                startActivity(dsangre);
                break;
            case R.id.carteraserv:
                Intent carteraserv = new Intent(MainMayorActivity.this, CServiciosActivity.class);
                startActivity(carteraserv);
                break;
        }

    }
}
