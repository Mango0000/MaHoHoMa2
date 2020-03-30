package at.htlkaindorf.mahohoma.backgroundTasks;

import android.os.AsyncTask;

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
import java.util.Arrays;
import java.util.List;

public class SearchCompanies extends AsyncTask<String, String, List<String>> {
    int i;
    @Override
    protected List<String> doInBackground(String... strings) {
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
            JSONArray obj = new JSONArray(result);
            List<String> resultset = new ArrayList<>();
            for (int i = 0; i<obj.length(); i++) {
                resultset.add(obj.getJSONObject(i).getString("symbol"));
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
