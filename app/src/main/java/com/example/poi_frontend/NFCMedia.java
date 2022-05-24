package com.example.poi_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaExtractor;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;

import java.io.IOException;

public class NFCMedia extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcmedia);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this,
                        0,
                        new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        nfcAdapter.enableForegroundDispatch(this,
                pendingIntent,
                null,
                null);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) ||
            action.equals(NfcAdapter.ACTION_TECH_DISCOVERED) ||
            action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            String codicePOI = readContentTag(tag);

            Intent media = new Intent(this, MediaActivity.class);
            media.putExtra("codicePOI", codicePOI);

            startActivity(media);
            finish();
        }

    }

    private String readContentTag(Tag tag) {
        String toReturn = "";
        String[] tagTech = tag.getTechList();

        for (String tagString:
             tagTech) {
            if (tagString.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUltralight = MifareUltralight.get(tag);

                try {
                    mifareUltralight.connect();
                    byte[] buffer;

                    for (int offset = 4; offset <= 36; offset += 4) {
                        buffer = mifareUltralight.readPages(offset);

                        int i = 0;
                        while (i < buffer.length && buffer[i] != -2) i++;
                        toReturn = toReturn.concat(new String(buffer, 0, i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return toReturn.substring(9);
    }
}