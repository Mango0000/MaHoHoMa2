package at.htlkaindorf.mahohoma.backgroundTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Debug;

import androidx.preference.PreferenceManager;

import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import at.htlkaindorf.mahohoma.MainActivity;

public class IncomeStatement extends AsyncTask<String, String, DataPoint[]>
{
    @Override
    protected DataPoint[] doInBackground(String... strings)
    {
        try
        {
            URL url = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
            String apiKey = prefs.getString("apikey","");
            url = new URL("https://financialmodelingprep.com/api/v3/income-statement/"+strings[0]+"?period=quarter&apikey="+apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            int limit=0;
            while((line = br.readLine())!=null){
                limit++;
                if(limit>=600){
                    if(line.contains("}")){
                        result+="}]";
                        break;
                    }
                }
                result+=line+"\n";
            }
            br.close();
            urlConnection.disconnect();
            List<DataPoint> list = new ArrayList<>();
            JSONArray obj = new JSONArray(result);

            String dateStr;
            for (int i = 0; i<obj.length(); i++) {
                dateStr = obj.getJSONObject(i).getString("date");
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                Date birthDate = sdf.parse(dateStr);
                list.add(new DataPoint(birthDate.getTime(), obj.getJSONObject(i).getInt("revenue")));
            }
            /*if(obj.has("profile")){
            JSONObject profile = obj.getJSONObject("profile");*/
           // }
            DataPoint[] dpsArray = new DataPoint[list.size()];
            Collections.reverse(list);
            list.toArray(dpsArray);
            return dpsArray;
        }catch(MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            if(e.toString().contains("Error Message")){
                List<List<String>> resultset = new ArrayList<>();
                resultset.add(Arrays.asList("keyerror"));
                e.printStackTrace();
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
