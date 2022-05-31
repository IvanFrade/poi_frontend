    package com.example.poi_frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements iListener {

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        String ticketCode = result.getContents();

        Downloader downloader = new Downloader(ticketCode);
        downloader.setListener(this);

        (new Thread(downloader)).start();
    }

    @Override
    public void onTaskComplete(LoginResult loginResult) {
        this.loginResult = loginResult;

        TextView view = ((TextView)findViewById(R.id.txt_title));

        if (!loginResult.getResult()) {
            view.setText("Biglietto non valido");
            return;
        }

        view.setText("Biglietto attivo! Scade il " + loginResult.getExpiringTime());
        ((Button)findViewById(R.id.btn_scan_nfc)).setClickable(true);
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

/*
    appunti cookie e gdpr
    cookie informazioni necessarie per il sito per funzionare. ad es. la sessione
    in php crea un cookie finche il browser rimane aperto. inoltre salvano le
    informazioni perche il protocollo http e stateless. se il server non tiene
    traccia, lo fa il client coi cookie.
    in europa e stato applicato il gdpr per tutelare gli utenti. noi informatici
    non ci occupiamo di gdpr, e una questione legale che c'entra poco con l'informatica.
    il gdpr e un regolamento europeo. possibilita ad ogni individuo di sapere cosa se ne fa
    chi raccoglie i dati di quei dati. permettere di decidere per ogni singolo dato se
    autorizzare il trattamento o no. ovvero un macello. il titolare del trattamento
    dei dati deve monitorare e registrare tutte le attivita, sia interne sia fatte
    da terzi. se mi appoggio tipo a google per raccogliere dati, loro diventano
    responsabili dei dati e va fatto un contratto. nei cookie deve apparire che
    la ditta google e responsabile dei dati ecc. per conto del titolare, cioe io.
    di solito gestito dal DPO, data protection officer.
    inoltre tutti i consensi vanno registrati singolarmente come prova. i conseni
    devono poter essere revocati in ogni momento.
    cose importanti da prevedere
    - consenso informato, specifico, libero, inequivocabile. il vecchio tacito
    consenso non va bene e le caselle non possono essere preselezionate. finche uno
    non preme autorizza la navigazione deve essere bloccata.
    ogni individuo deve poter accedere ai propri dati. deve poter ritirare il
    consenso. deve poter far richiesta di cancellazione dati.
    in caso di violazione dei dati, bisogna informare le autorita e le persone
    interessate entro 72 ore. per far cio devo gioco forza avere un backup per
    sapere chi devo avvertire. se le aziende hanno piu di 250 dipendenti o
    trattano dati sensibili o sono aziende pubbliche, devono avere un DPO
    che e assieme al titolare responsabile delle violazioni del GDPR.

    cosa dev'esserci nei cookie
    integrazione di terze parti (google, facebook, instagram ecc.)
    cookie che permettono identificazione e profilazione dell'utente
    indicare finalita e entita. chiarimenti chiari e specifici
    possibilita degli utenti di accedere alle scelte fatte per revocarle
 */