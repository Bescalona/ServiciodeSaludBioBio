package cl.ubiobio.serviciodesaludbio_bio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DSangreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsangre);
        //cambio el titulo del ActionBar por uno correspondiente a la clase
        getSupportActionBar().setTitle("Donar Sangre");
    }
}
