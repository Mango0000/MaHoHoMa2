package at.htlkaindorf.mahohoma.ui.top_types;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.MainActivity;
import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyResolver;
import at.htlkaindorf.mahohoma.backgroundTasks.SearchAPITops;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link top_types#newInstance} factory method to
 * create an instance of this fragment.
 */
public class top_types extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "MyActivity";
    public int is_done=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String name;
    private TextView tvTypeName;
    private LinearLayout llStocks;
    private String searchurl="";
    List<String> output;
    List<List<String>> res = new ArrayList<>();

    public top_types() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment top_types.
     */

    public top_types(String name){
        this.name = name;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String apiKey = prefs.getString("apikey","");
        if(name.equals("Most Active")){
            searchurl="https://financialmodelingprep.com/api/v3/stock/actives?apikey="+apiKey;
        }else if(name.equals("Most Gainer")){
            searchurl="https://financialmodelingprep.com/api/v3/stock/gainers?apikey="+apiKey;
        }else if(name.equals("Most Loser")){
            searchurl="https://financialmodelingprep.com/api/v3/stock/losers?apikey="+apiKey;
        }
        loadStocks();
    }

    private void loadStocks() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SearchAPITops stops = new SearchAPITops();
                try {
                    output = stops.execute(searchurl).get();
                    for (String string : output) {
                        res.add(new CompanyResolver().execute(string).get());
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(output==null){
                    is_done=-1;
                }else{
                    is_done=1;
                }
            }
        };
        //todo request saving mode
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        Boolean isSavingMode = prefs.getBoolean("sync", false);
        if(!isSavingMode){
            new Thread(runnable).start();
        }else{
            is_done=2;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_top_types, container, false);
        tvTypeName = root.findViewById(R.id.tvTypeName);
        tvTypeName.setText(name);
        llStocks = root.findViewById(R.id.llStocks);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentManager mFragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (output != null) {
            if (output.isEmpty()) {
                TextView empty = new TextView(getContext());
                empty.setText("No Results");
                llStocks.addView(empty);
            } else {
                List<String> item = null;
                int i = 0;
                for (String string : output) {
                    item = res.get(i);
                    /*List<String> res = null;
                    try {
                        //res = new CompanyResolver().execute(string).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    if (item.isEmpty()||item==null) {
                        //fragmentTransaction.add(R.id.llCompanies,new StockItem(string, null, null,null));
                    } else {
                        if (item.get(0).equals("") || item.get(0).equals("null")) {
                            fragmentTransaction.add(R.id.llStocks, new StockItem(string, item.get(3), item.get(1), item.get(2), string,300));
                        } else {
                            fragmentTransaction.add(R.id.llStocks, new StockItem(item.get(0), item.get(3), item.get(1), item.get(2), string,300));
                        }
                    }
                    i++;
                }
            }
        }else{
        }
        fragmentTransaction.commit();
        llStocks.removeAllViews();

    }
}
