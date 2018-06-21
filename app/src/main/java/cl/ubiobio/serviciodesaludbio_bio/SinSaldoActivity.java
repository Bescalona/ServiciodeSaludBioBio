package cl.ubiobio.serviciodesaludbio_bio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SinSaldoActivity extends AppCompatActivity {

    String url ="http://eccnetserver.entelcallcenter.cl/minsalc2c/index.aspx";

    //en el onCreate se redirecciona a la pagina indicada en el url, mostrandola a traves de un webview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_saldo);

        WebView webView = (WebView) this.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
