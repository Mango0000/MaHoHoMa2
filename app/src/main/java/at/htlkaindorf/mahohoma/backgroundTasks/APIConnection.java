package at.htlkaindorf.mahohoma.backgroundTasks;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class APIConnection extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = null;
            url = new URL("https://financialmodelingprep.com/api/v3/search?query="+strings[0]+"&limit=20");
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
