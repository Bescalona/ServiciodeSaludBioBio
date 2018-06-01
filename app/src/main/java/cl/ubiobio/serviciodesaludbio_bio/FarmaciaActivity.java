package cl.ubiobio.serviciodesaludbio_bio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FarmaciaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmacia);
        //cambio el titulo del ActionBar por uno correspondiente a la clase
        getSupportActionBar().setTitle("Farmacia de turno");
    }
}
