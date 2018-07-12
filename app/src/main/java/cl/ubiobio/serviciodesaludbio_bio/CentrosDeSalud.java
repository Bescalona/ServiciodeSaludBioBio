/*INTEGRANTES
-Ivan Martinez
-Basthian Henriquez
-Bastian Escalona*/
package cl.ubiobio.serviciodesaludbio_bio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CentrosDeSalud extends AppCompatActivity implements LocationSource, OnMapReadyCallback,
       GoogleMap.OnMarkerClickListener,GoogleMap.OnMyLocationClickListener{

    /**arreglo donde almaceno la ubicación, nombre y tipo de los centros de salud**/
    private CentroSalud[] centro = {
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

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private LocationSource.OnLocationChangedListener listener;
    private LocationManager mLocationManager;
    private static long UPDATE_INTERVAL_IN_MILLISECONDS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centrosdesalud);
        //cambio el titulo del ActionBar por uno correspondiente a la clase
        getSupportActionBar().setTitle("Centros de salud");


        /** mLocationManager gestiona las peticiones de posicion **/
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /** Verificamos si el usuario tiene encendido el GPS, si no,
         * lo enviamos al menú para que lo encienda **/
        boolean enabledGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabledGPS) {
            Toast.makeText(this, "No hay señal de GPS", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        /** Establecemos el intervalo de actualización de la posicion **/
        UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

        /** Se busca el fragmento del mapa e iniciamos el mapa.
         * cuando el mapa se encuentre listo, se llamará a onMapReady() **/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /** gestionamos la respuesta de la petición de permisos **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            /** si el permiso fue aceptado, iniciamos el proceso de captura de posiciones **/
            enableMyLocation();
        } else {
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /** Si realizamos un click sobre su posicion (punto azul), mostraremos información acerca de ese punto **/
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Esta es mi posición actual", Toast.LENGTH_LONG).show();
    }

    /** Dialogo de error para cuando no se acepte el permiso **/
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    /** el mapa se encuentra listo, podemos modificar algunas configuraciones **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**Hagp un for para mostrar todos los centros de salud del arreglo en el mapa**/
        for(int i=0; i<centro.length-1;i++) {
            LatLng posicion = new LatLng(centro[i].getLatitud(), centro[i].getLongitud());
            Log.d("´POSICION:", " "+posicion);

            /**dependiendo del tipo de centro le asigno un icono de diferente color**/
            if(centro[i].getTipo()==0){
                mMap.addMarker(new MarkerOptions().position(posicion).title(centro[i].getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital_icon)));
            }

            if(centro[i].getTipo()==1){
                mMap.addMarker(new MarkerOptions().position(posicion).title(centro[i].getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.sar_icon)));
            }

            if(centro[i].getTipo()==2){
                mMap.addMarker(new MarkerOptions().position(posicion).title(centro[i].getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.sapu_icon)));
            }
        }

        /** Activacion de controles en el mapa **/
        mMap.getUiSettings().setZoomControlsEnabled(true); //control de zoom
        mMap.getUiSettings().setMyLocationButtonEnabled(true); //habilitamos boton para regresar a su posicion
        mMap.getUiSettings().setCompassEnabled(true); //el mapa busca el norte

        /** Tipo de terreno que se muestra en el mapa **/
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

        /** Gestión de algunos eventos**/
        mMap.setOnMyLocationClickListener(this); //click sobre posicion
        mMap.setOnMarkerClickListener(this); //click sobre marcador


        /** iniciamos el proceso de captura de posiciones **/
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            /** Si el permiso no fue concedido o no ha sido solicitado, se solicita **/
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            /** Cuando ya tenemos los permisos
             * le decimos al mapa que capture la posicion y
             * modificamos de donde se obtiene la posición,
             * con el objetivo de contralar como y cuando se actualiza **/
            mMap.setMyLocationEnabled(true);
            mMap.setLocationSource(this);

            /** Se le dice de donde se captura la posicion, en este caso el GPS(puede ser desde internet),
             * el intervalo de actualizacion,
             * la distancia minima que debe modificar la posicion para ser requerida una actualizacion
             * y el evento que capura la posicion**/
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL_IN_MILLISECONDS, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    /** cuando se captura una nueva posicion, se le entrega al evento que fue seteado en el mapa
                     * para que sea consiente se su posicion.
                     * En caso de necesitar tracking de posicion, en este punto se debe el iniciar el SW de trackeo **/
                    CentrosDeSalud.this.listener.onLocationChanged(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

            /** el mapa es consciente de la posción, pero necesitamos entregarle
             * la primera posicion al mapa para que cambie la vista entregada, siempre
             * y cuando, el telefono haya registrado su posicion antes. **/
            //mCurrentLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //LatLng angeles = new LatLng (-37.463433, -72.361637);
            LatLng angeles = new LatLng (-37.352676, -72.310730);
            /** movemos el mapa a la posicion obtenida **/
            mMap.moveCamera(CameraUpdateFactory.newLatLng(angeles));
            /** y le indicamos que establezca un zoom 16, entre mayor sea mas cerca se mostrará **/
            mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
            /*mCurrentLocation = (angeles);
            if (mCurrentLocation != null) {
                this.listener.onLocationChanged(mCurrentLocation);
                /** el objeto Location no es compatible con el mapa, por lo cual debemos crear un objeto
                 * compatible con este (LatLng) **/
               // LatLng thisLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                /** movemos el mapa a la posicion obtenida **/
              //  mMap.moveCamera(CameraUpdateFactory.newLatLng(thisLocation));
                /** y le indicamos que establezca un zoom 16, entre mayor sea mas cerca se mostrará **/
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            //}

        }

    }

    /** se inicializa el evento que captura las posiciones para el mapa **/
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.listener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        this.listener = null;
    }


    /** Evento que se activa al realizar click sobre un marcador **/
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("click", "click en marker");
        return false;
    }

}
