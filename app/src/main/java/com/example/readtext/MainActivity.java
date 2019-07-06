package com.example.readtext;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;

    private boolean ready;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.text);
        Button btn = findViewById(R.id.btn);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                Set<Locale> language = textToSpeech.getAvailableLanguages();
                if(language!= null) {
                    for (Locale lang : language) {
                        Log.e("TTS", "Supported Language: " + lang);
                    }
                }

                Locale lang = Locale.ENGLISH;
                int result = textToSpeech.setLanguage(lang);
                if (result == TextToSpeech.LANG_MISSING_DATA) {
                    ready = false;
                    Log.e("Msg","Missing language data");
                    return;
                } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    ready = false;
                    Log.e("Msg","Language not supported");
                    return;
                } else {
                    ready = true;
                    Locale currentLanguage = textToSpeech.getVoice().getLocale();
                    Log.e("Msg","Language: "+currentLanguage);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut();
            }
        });
    }

    private void speakOut() {
        if (!ready) {
            Toast.makeText(this, "Text to Speech not ready", Toast.LENGTH_LONG).show();
            return;
        }

        // Văn bản cần đọc.
        String toSpeak = et.getText().toString();
        Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show();

        // Một String ngẫu nhiên (ID duy nhất).
        String utteranceId = UUID.randomUUID().toString();
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
}
