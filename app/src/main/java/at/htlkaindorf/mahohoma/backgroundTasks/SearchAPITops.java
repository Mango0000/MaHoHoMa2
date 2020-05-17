package at.htlkaindorf.mahohoma.backgroundTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchAPITops extends AsyncTask<String, String, List<String>> {
    @Override
    protected List<String> doInBackground(String... strings) {
        try{
            URL url = null;
            url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            while((line = br.readLine())!=null){
                result+=line+"\n";
            }
            br.close();
            urlConnection.disconnect();
            JSONObject obj = new JSONObject(result);
            JSONArray objarr = obj.getJSONArray(obj.names().get(0).toString());
            List<String> resultset = new ArrayList<>();
            for (int i = 0; i<objarr.length(); i++) {
                resultset.add(objarr.getJSONObject(i).getString("ticker"));
            }
            return resultset;
        }catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
