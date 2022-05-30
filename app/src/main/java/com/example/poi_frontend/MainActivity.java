    package com.example.poi_frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements iListener {

    private String ticketCode;
    private LoginResult loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onActivateTicket(View v) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);

        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }

    public void onNFCScan(View v) {
        if (this.loginResult == null || !this.loginResult.getResult()) {
            Toast.makeText(this, "Devi prima attivare un biglietto!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, NFCMedia.class);
        startActivity(intent);

        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        ticketCode = result.getContents();

        Downloader downloader = new Downloader(ticketCode);
        downloader.setListener(this);

        (new Thread(downloader)).start();
    }

    @Override
    public void onTaskComplete(LoginResult loginResult) {
        this.loginResult = loginResult;

        TextView view = ((TextView)findViewById(R.id.txt_title));
        if (loginResult.getResult())
            view.setText("Scade il " + loginResult.getExpiringTime());
        else
            view.setText("Biglietto non valido");
    }

    @Override
    public void onTaskComplete(String poi) {

        Intent intent = new Intent(this, MediaActivity.class);
        intent.putExtra("poi", poi);

        startActivity(intent);
    }

    public void onClick(View v) {
        if (this.loginResult == null || !this.loginResult.getResult()) {
            Toast.makeText(this, "Devi prima attivare un biglietto!", Toast.LENGTH_LONG).show();
            return;
        }

        POIDownloader poiDownloader = new POIDownloader("0000000000000002");
        poiDownloader.setListener(this);

        (new Thread(poiDownloader)).start();
    }

}

