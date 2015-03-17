package net.androidsensei.pokedex;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import net.androidsensei.pokedex.data.PokedexContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class PokemonListApiTask extends AsyncTask<Void, Void, Void> {

    private final String LOG_TAG = PokemonListApiTask.class.getSimpleName();

    private ListView pokemonList;
    private ProgressBar progressBarLoading;
    private final Context mContext;

    public PokemonListApiTask(Context context, ListView pokemonList, ProgressBar progressBarLoading){
        this.mContext = context;
        this.pokemonList = pokemonList;
        this.progressBarLoading = progressBarLoading;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(LOG_TAG, "PreExec");
        this.pokemonList.setVisibility(View.INVISIBLE);
        this.progressBarLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.d(LOG_TAG, "PostExec");
        this.progressBarLoading.setVisibility(View.INVISIBLE);
        this.pokemonList.setVisibility(View.VISIBLE);
    }

    private void getPokemonFromJson(String pokemonJsonStr) throws JSONException {
        try {
            JSONArray pokemonArray = new JSONObject(pokemonJsonStr).getJSONArray("pokemon");
            Vector<ContentValues> cVVector = new Vector<ContentValues>(pokemonArray.length());
            for (int i = 0; i < pokemonArray.length(); i++) {
                JSONObject pokemonJson = pokemonArray.getJSONObject(i);
                String name = pokemonJson.getString("name");
                String description = pokemonJson.getString("resource_uri");
                ContentValues pokemonValues = new ContentValues();
                pokemonValues.put(PokedexContract.PokemonEntry.COLUMN_NAME, name);
                pokemonValues.put(PokedexContract.PokemonEntry.COLUMN_DESCRIPTION, description);
                cVVector.add(pokemonValues);
            }
            int inserted = 0;
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(PokedexContract.PokemonEntry.CONTENT_URI, cvArray);
            }
            Log.d(LOG_TAG, "ApiTask Complete. " + inserted + " Inserted");
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String pokemonJsonStr = null;

        try {
            Log.e(LOG_TAG, "Say hello!");
            URL url = new URL("http://pokeapi.co/api/v1/pokedex/1");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            pokemonJsonStr = buffer.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        try {
            getPokemonFromJson(pokemonJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
}
