package at.htlkaindorf.mahohoma.ui.commodities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.CommodityResolver;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyResolver;
import at.htlkaindorf.mahohoma.backgroundTasks.SearchCommodities;
import at.htlkaindorf.mahohoma.ui.CommodityItem.CommodityItem;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;


public class CommodityFragment extends Fragment
{
    LinearLayout ll;
    private static Context mContext;
    TextView tve;

    private static final String TAG = "MyActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_commodity, container, false);
        tve = root.findViewById(R.id.tverror);
        ll = root.findViewById(R.id.llCommodities);
        mContext = this.getContext();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        try
        {
            loadCommodities();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ll.removeAllViews();
    }

    private void loadCommodities() throws ExecutionException, InterruptedException
    {
        SearchCommodities search = new SearchCommodities();
        List<String> commoditiesList = search.execute().get();
        ll.removeAllViews();
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if(commoditiesList.isEmpty())
        {
            TextView tvNoFavourite = new TextView(this.getContext());
            tvNoFavourite.setText("No Commodities found");
            ll.addView(tvNoFavourite);
        }
        for(String string: commoditiesList)
        {
            List<String> res = new CommodityResolver().execute(string).get();
            if(res.get(0).equals("keyerror")){
                showMyDialog();
                break;
            }
            else {
                fragmentTransaction.add(R.id.llCommodities,new CommodityItem(res.get(0), res.get(1), res.get(2), string,0));
            }
        }
        fragmentTransaction.commit();
    }

    private void showMyDialog()
    {
        new AlertDialog.Builder(this.getContext())
                .setTitle("API Key Error")
                .setMessage("The API Key is invalid")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}