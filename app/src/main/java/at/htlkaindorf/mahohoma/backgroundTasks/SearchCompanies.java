package at.htlkaindorf.mahohoma.backgroundTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

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

import at.htlkaindorf.mahohoma.MainActivity;

public class SearchCompanies extends AsyncTask<String, String, List<String>> {
    @Override
    protected List<String> doInBackground(String... strings) {
        try{
            URL url = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
            String apiKey = prefs.getString("apikey","");
            int limit = prefs.getInt("searchlimit",1);
            url = new URL("https://financialmodelingprep.com/api/v3/search?query="+strings[0]+"&limit="+limit+"&apikey="+apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            while((line = br.readLine())!=null){
                result+=line+"\n";
            }
            br.close();
            List<String> resultset = new ArrayList<>();
            JSONArray obj = new JSONArray(result);

            for (int i = 0; i<obj.length(); i++) {
                resultset.add(obj.getJSONObject(i).getString("symbol"));
            }
            return resultset;
        }catch(MalformedURLException e){
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (JSONException e) {
            if(e.toString().contains("Error Message")){
                List<String> resultset = new ArrayList<>();
                resultset.add("keyerror");
                return resultset;
            }
        }
        return null;
    }
}
