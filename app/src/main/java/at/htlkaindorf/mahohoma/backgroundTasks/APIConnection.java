package at.htlkaindorf.mahohoma.backgroundTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import at.htlkaindorf.mahohoma.MainActivity;

public class APIConnection extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
            String apiKey = prefs.getString("apikey","");
            url = new URL("https://financialmodelingprep.com/api/v3/search?query="+strings[0]+"&limit=20&apikey="+apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            while((line = br.readLine())!=null){
                result+=line+"\n";
            }
            return result;
        }catch(MalformedURLException e){
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }
}
