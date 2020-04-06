package com.example.dictionaryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String url;
    private TextView showDef;
    private EditText enterWord;
    Button btn_speechText;
    TextToSpeech toSpeech;
    ImageButton imgButton_speech;
    ImageButton pronounce;
    TextToSpeech pronounciation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showDef = findViewById(R.id.showDef);
        showDef.setVisibility(View.INVISIBLE);
        enterWord = findViewById(R.id.enterWord);
//        btn_speechText = (Button) findViewById(R.id.btn_speechText);
        imgButton_speech = (ImageButton) findViewById(R.id.imgButton_speech);
        pronounce = (ImageButton) findViewById(R.id.pronounce);
        toSpeech =new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                    if(i!=TextToSpeech.ERROR)
                        toSpeech.setLanguage(Locale.ENGLISH);
                    }
        });
        pronounciation = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                    pronounciation.setLanguage(Locale.ENGLISH);
            }
        });

        pronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pronounciation.speak(enterWord.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });

/*       btn_speechText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSpeech.speak(showDef.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        }); */
    }
    private String dictionaryEntries() {
        final String language = "en-gb";
        final String word = enterWord.getText().toString();
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

    public void sendRequestOnClick(View v)
    {
        DictionaryRequest dR = new DictionaryRequest(this, showDef);
        showDef.setVisibility(View.VISIBLE);
        url = dictionaryEntries();
        dR.execute(url);
    }

    public void btnSpeech(View view) {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Speak Something");
        try {
            startActivityForResult(intent,1);
        }catch(ActivityNotFoundException e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case 1:
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    enterWord.setText(result.get(0));
                }
                break;
        }
    }
}
