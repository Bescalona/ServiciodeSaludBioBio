package cl.ubiobio.serviciodesaludbio_bio;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class SaludRespondeActivity extends AppCompatActivity implements View.OnClickListener {


    private Button llamada;
    private Button sin_saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salud_responde);
        //cambio el titulo del ActionBar por uno correspondiente a la clase
        getSupportActionBar().setTitle("Salud Responde");

        //inicializo el boton llamada y le asigno la id del boton llamar del activity_salud_responde.xml
        llamada = findViewById(R.id.botonLlamar);
        //inicializo el boton sin_saldo, ocupado para dirigirme al layout con el webview
        sin_saldo = findViewById(R.id.sin_saldo);
        //le asigno una accion al presionar el boton
        sin_saldo.setOnClickListener(this);
        llamada.setOnClickListener(this);

    }

   @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonLlamar:
                //si presiono el boton marco automaticamente el numero de salud responde en el telefono
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:600-360-77777"));
                if (ActivityCompat.checkSelfPermission(SaludRespondeActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }
                startActivity(intent);
                break;
            case R.id.sin_saldo:
                Intent sin_saldo = new Intent(SaludRespondeActivity.this, SinSaldoActivity.class);
                startActivity(sin_saldo);
                break;
        }
    }
}
