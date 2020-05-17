package at.htlkaindorf.mahohoma.ui.top_types;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String name;
    private TextView tvTypeName;
    private LinearLayout llStocks;
    private String searchurl="";

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
        if(name.equals("Most Active")){
            searchurl="https://financialmodelingprep.com/api/v3/stock/actives";
        }else if(name.equals("Most Gainer")){
            searchurl="https://financialmodelingprep.com/api/v3/stock/gainers";
        }else if(name.equals("Most Loser")){
            searchurl="https://financialmodelingprep.com/api/v3/stock/losers";
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SearchAPITops stops = new SearchAPITops();
        try {
            List<String> output = stops.execute(searchurl).get();
            FragmentManager mFragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            llStocks.removeAllViews();
            if(output.isEmpty()){
                TextView empty = new TextView(getContext());
                empty.setText("No Results");
                llStocks.addView(empty);
            }else{
                for (String string:output) {
                    List<String> res = new CompanyResolver().execute(string).get();
                    Log.e(TAG, res.get(0));
                    if(res.isEmpty()){
                        //fragmentTransaction.add(R.id.llCompanies,new StockItem(string, null, null,null));
                    }else{
                        if(res.get(0).equals("")||res.get(0).equals("null")){
                            fragmentTransaction.add(R.id.llStocks,new StockItem(string, res.get(3), res.get(1),res.get(2),300));
                        }else{
                            fragmentTransaction.add(R.id.llStocks,new StockItem(res.get(0), res.get(3), res.get(1),res.get(2), 300));
                        }
                    }
                }
            }
            fragmentTransaction.commit();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
