package at.htlkaindorf.mahohoma.backgroundTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import at.htlkaindorf.mahohoma.MainActivity;

public class CompanyInformations extends AsyncTask<String, String, List<String>>
{
    @Override
    protected List<String> doInBackground(String... strings)
    {
        try
        {
            URL url = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
            String apiKey = prefs.getString("apikey","");
            url = new URL("https://financialmodelingprep.com/api/v3/profile/"+strings[0]+"?apikey="+apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            while((line = br.readLine())!=null){
                result+=line+"\n";
            }
            br.close();
            urlConnection.disconnect();
            List<String> list = new ArrayList<>();
            JSONArray obj = new JSONArray(result);
            /*if(obj.has("profile")){
            JSONObject profile = obj.getJSONObject("profile");*/
                list.add(obj.getJSONObject(0).getString("price"));
                list.add(obj.getJSONObject(0).getString("changes"));
                list.add(obj.getJSONObject(0).getString("ceo"));
                list.add(obj.getJSONObject(0).getString("sector"));
                list.add(obj.getJSONObject(0).getString("website"));
                list.add(obj.getJSONObject(0).getString("industry"));
                list.add(obj.getJSONObject(0).getString("exchange"));
                list.add(obj.getJSONObject(0).getString("exchangeShortName"));
            list.add(obj.getJSONObject(0).getString("description"));
            // }
            return list;
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
