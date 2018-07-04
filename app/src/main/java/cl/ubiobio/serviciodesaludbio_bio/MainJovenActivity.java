package cl.ubiobio.serviciodesaludbio_bio;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainJovenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HoraVisitaFragment.OnFragmentInteractionListener,
        CarteraServicioFragment.OnFragmentInteractionListener, CentrosDeSaludFragment.OnFragmentInteractionListener,
        ConsultaHoraMedFragment.OnFragmentInteractionListener, DonaSangreFragment.OnFragmentInteractionListener,
        FarmaciaTurnoFragment.OnFragmentInteractionListener, SaludRespondeFragment.OnFragmentInteractionListener,
        QSomosFragment.OnFragmentInteractionListener{

    //Declaro la variable sharedPre para guardar la preferencia por el modo viejo en caso de que elija el boton cambiar a modo mayor
    private SharedPreferences sharedPre;
    private SharedPreferences.Editor editorSP;
    private int MODO_VIEJO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joven);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPre = getSharedPreferences(getString(R.string.sharedPreID), MODE_PRIVATE);
        editorSP = sharedPre.edit();
        Fragment Fragment = null;
        Fragment = new QSomosFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_joven, Fragment).commit();

        //Boton flotante que descartamos, pero podria servir en el futuro

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.joven, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /*Cada if y else if contiene una accion a realizar al apretar un boton por ej: al presionar el boton con id=nav_visita, se llama al fragment
     "HoraVisitaFragment", el cual esta asociado al layout "activity_hvisita", este layout se muestra al interior del "content_joven", que corresponde
     a la pantalla principal del Modo Joven
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        Fragment miFragment = null;
        boolean fragmentSeleccionado = false;

        if (id == R.id.nav_visita) {
           miFragment = new HoraVisitaFragment();
           fragmentSeleccionado = true;
           //al seleccionar un boton de la barra vertical el ActionBar cambia su nombre predeterminado por el texto entre comillas
           getSupportActionBar().setTitle("Horario de visita");
        } else if (id == R.id.nav_consulta) {
            miFragment = new ConsultaHoraMedFragment();
            fragmentSeleccionado = true;
            getSupportActionBar().setTitle("Consulta hora médica");
        } else if (id == R.id.nav_centrossalud) {
            miFragment = new CentrosDeSaludFragment();
            fragmentSeleccionado = true;
            getSupportActionBar().setTitle("Consulta estado del paciente");
        } else if (id == R.id.nav_donacion) {
            miFragment = new DonaSangreFragment();
            fragmentSeleccionado = true;
            getSupportActionBar().setTitle("Dona sangre");
        } else if (id == R.id.nav_farmacia) {
            miFragment = new FarmaciaTurnoFragment();
            fragmentSeleccionado = true;
            getSupportActionBar().setTitle("Farmacia de turno");
        } else if (id == R.id.nav_servicio) {
            miFragment = new CarteraServicioFragment();
            fragmentSeleccionado = true;
            getSupportActionBar().setTitle("Cartera de servicio");
        }else if (id == R.id.nav_llame) {
            miFragment = new SaludRespondeFragment();
            fragmentSeleccionado = true;
            getSupportActionBar().setTitle("Salud responde");
        }else if (id == R.id.nav_cambiar_a_mayor){
            //al presionar el boton Cambiar a Modo Mayor guardamos el cambio en las preferencias de usuario, por lo tanto la proxima vez que el usuario abra la aplicacion, aparecera la pantalla del modo mayor
            editorSP.putInt("MODO",MODO_VIEJO);
            editorSP.commit();
            Intent cambiarm = new Intent( MainJovenActivity.this, MainMayorActivity.class);
            startActivity(cambiarm);
            finish();
        }

        /*Si se selecciona un boton, la pantalla principal del modo joven (content_joven) cambia por el layout
        del fragment asociado al boton que se presiono (miFragment)*/
        if (fragmentSeleccionado==true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_joven, miFragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Se crea al agregar los fragment en el implement
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
