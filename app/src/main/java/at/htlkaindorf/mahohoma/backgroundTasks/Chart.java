package at.htlkaindorf.mahohoma.backgroundTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;

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
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import at.htlkaindorf.mahohoma.MainActivity;

public class Chart extends AsyncTask<String, String, DataPoint[]>
{
    @Override
    protected DataPoint[] doInBackground(String... strings)
    {
        try
        {
            URL url = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
            String apiKey = prefs.getString("apikey","");
            int x = prefs.getInt("chart",5);
            url = new URL("https://financialmodelingprep.com/api/v3/historical-chart/1hour/"+strings[0]+"?apikey="+apiKey);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = "";
            String line;
            int limit=0;
            while((line = br.readLine())!=null){
                limit++;
                if(limit>=x*7){
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setLenient(false);
                Date birthDate = sdf.parse(dateStr);
                list.add(new DataPoint(birthDate.getTime(), obj.getJSONObject(i).getInt("open")));
            }
            /*if(obj.has("profile")){
            JSONObject profile = obj.getJSONObject("profile");*/
           // }
            DataPoint[] dpsArray = new DataPoint[list.size()];
            Collections.sort(list, new Comparator<DataPoint>() {
                @Override
                public int compare(DataPoint o1, DataPoint o2) {
                    if (o1.getX() < o2.getX()) return -1;
                    if (o1.getX() > o2.getX()) return 1;
                    return 0;
                }
            });
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
