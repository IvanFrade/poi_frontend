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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        ticketCode = result.getContents();

        Downloader downloader = new Downloader(ticketCode);
        downloader.setListener(this);

        (new Thread(downloader)).start();
    }

    public void onGetTicket(View v) {
        Downloader downloader = new Downloader(ticketCode);
        downloader.setListener(this);

        (new Thread(downloader)).start();
    }

    @Override
    public void onTaskComplete(Boolean isValid) {
        if (isValid) Toast.makeText(this, "biglietto valido", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "biglietto non valido", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskComplete(LoginResult loginResult) {
        TextView view = ((TextView)findViewById(R.id.txt_title));
        if (loginResult.getResult())
            view.setText("Scade il " + loginResult.getExpiringTime());
        else
            view.setText("Biglietto non valido");
    }
}

