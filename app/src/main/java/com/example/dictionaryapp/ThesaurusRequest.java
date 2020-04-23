package com.example.dictionaryapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


//in android calling network requests on the main thread forbidden by default
//create class to do async job
public class ThesaurusRequest extends AsyncTask<String, Integer, String> {
    Context context2;
    TextView synonym;

    ThesaurusRequest(Context context2, TextView tV)
    {
        this.context2 = context2;
        synonym = tV;
    }
    @Override
    protected String doInBackground(String... params) {

        //TODO: replace with your own app id and app key
        final String app_id = "c2e7b1fe";
        final String app_key = "c36dcd311d964bfd7041f090522ec733";
        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setRequestProperty("app_id",app_id);
            urlConnection.setRequestProperty("app_key",app_key);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            String Syn = "";
            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");
            JSONObject lEntries = results.getJSONObject(0);
            JSONArray laArray = lEntries.getJSONArray("lexicalEntries");
            JSONObject entries = laArray.getJSONObject(0);
            JSONArray e = entries.getJSONArray("entries");
            JSONObject jsonObject = e.getJSONObject(0);
            JSONArray sensesArray = jsonObject.getJSONArray("senses");

            JSONObject de = sensesArray.getJSONObject(0);

            JSONArray d = de.getJSONArray("subsenses");

            JSONObject sub = sensesArray.getJSONObject(0);

            JSONArray synArr = de.getJSONArray("synonyms");
            String[] synonyms = new String[100];
            List<String> allNames = new ArrayList<String>();
            for (int i = 0, size = 5; i < size; i++) {
                JSONObject objectInArray = synArr.getJSONObject(i);
                String s = objectInArray.getString("text");
                allNames.add(s);


                synonyms[i] = s;
            }
            synonym.setText(allNames.toString());
            // synonym.setText(synonyms[0].toString()+", "+synonyms[1].toString());

            Log.d("synonyms are ","hi" +synonyms.toString());
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Result of Dictionary", "onPostExecute" +result);

        System.out.println(result);


    }
}