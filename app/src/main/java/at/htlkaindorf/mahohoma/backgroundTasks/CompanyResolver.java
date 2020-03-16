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
import java.util.List;

public class CompanyResolver extends AsyncTask<String, String, List<String>> {
    @Override
    protected List<String> doInBackground(String... strings) {
        try{
            URL url = null;
            url = new URL("https://financialmodelingprep.com/api/v3/company/profile/"+strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            while((line = br.readLine())!=null){
                result+=line+"\n";
            }
            JSONObject obj = new JSONObject(result);
            JSONObject profile = obj.getJSONObject("profile");
            List<String> list = new ArrayList<>();
            list.add(profile.getString("price"));
            list.add(profile.getString("changes"));
            list.add(profile.getString("image"));
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
