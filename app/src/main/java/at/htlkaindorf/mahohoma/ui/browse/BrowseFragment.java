package at.htlkaindorf.mahohoma.ui.browse;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.APIConnection;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyResolver;
import at.htlkaindorf.mahohoma.backgroundTasks.SearchCompanies;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;

public class BrowseFragment extends Fragment {
    EditText etsearch;
    LinearLayout ll;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_browse, container, false);
        etsearch = root.findViewById(R.id.searchbar);
        etsearch.setOnClickListener(this::onSearch);
        ll = root.findViewById(R.id.llCompanies);
        //fragment = inflater.inflate(R.layout.stock_item_fragment, container, true);
        return root;
    }

    private void onSearch(View view) {
        try {
            SearchCompanies search = new SearchCompanies();
            List<String> output = search.execute(etsearch.getText().toString()).get();
            FragmentManager mFragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            ll.removeAllViews();
            for (String string:output) {
                Log.e(string, string);
                List<String> res = new CompanyResolver().execute(string).get();
                fragmentTransaction.add(R.id.llCompanies,new StockItem(string, res.get(2), res.get(0),res.get(1)));
            }
            fragmentTransaction.commit();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}
