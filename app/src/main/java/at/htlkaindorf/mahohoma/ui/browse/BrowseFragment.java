package at.htlkaindorf.mahohoma.ui.browse;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import at.htlkaindorf.mahohoma.backgroundTasks.SearchAPITops;
import at.htlkaindorf.mahohoma.backgroundTasks.SearchCompanies;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;
import at.htlkaindorf.mahohoma.ui.top_types.top_types;

public class BrowseFragment extends Fragment {
    EditText etsearch;
    LinearLayout ll;
    private static Context mContext;
    ImageView iv;
    TextView tve;
    AnimationDrawable animation;

    private static final String TAG = "MyActivity";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_browse, container, false);
        etsearch = root.findViewById(R.id.searchbar);
        iv = root.findViewById(R.id.ivLoader);
        tve = root.findViewById(R.id.tverror);
        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onSearch();
                }
                return false;
            }
        });
        //etsearch.setOn(this::onSearch);
        ll = root.findViewById(R.id.llCompanies);
        //fragment = inflater.inflate(R.layout.stock_item_fragment, container, true);
        mContext = this.getContext();

        animation = (AnimationDrawable) iv.getDrawable();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        //animation.start();
        waitForCreation();
        //animation.stop();
        //iv.setVisibility(View.INVISIBLE);
        ll.removeAllViews();
    }

    public void waitForCreation() {
        top_types most_active = new top_types("Most Active");
        top_types most_gainer = new top_types("Most Gainer");
        top_types most_loser = new top_types("Most Loser");
        iv.setVisibility(View.VISIBLE);
        animation.start();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                do {
                    Log.w("active", most_active.is_done+"");
                } while (most_active.is_done == 0 || most_gainer.is_done == 0 || most_loser.is_done == 0);
                if(most_active.is_done==-1){
                    Log.e("Invalid API Key", most_active.is_done+"");
                }else if(most_active.is_done==2){

                }else{
                    FragmentManager mFragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    //Most Active
                    fragmentTransaction.add(R.id.llCompanies, most_active);
                    //Most Gainer
                    fragmentTransaction.add(R.id.llCompanies, most_gainer);
                    //Most Loser
                    fragmentTransaction.add(R.id.llCompanies, most_loser);
                    fragmentTransaction.commit();
                }
                animation.stop();
                iv.setVisibility(View.INVISIBLE);
            }
        };
        new Thread(runnable).start();
    }



    private void onSearch() {
        try {
            SearchCompanies search = new SearchCompanies();
            List<String> output = search.execute(etsearch.getText().toString()).get();
            FragmentManager mFragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            ll.removeAllViews();
            if(output == null){
                TextView empty = new TextView(getContext());
                empty.setText("No Results");
                ll.addView(empty);
            }else{
                for (String string:output) {
                    Log.e(TAG, string);
                    List<String> res = new CompanyResolver().execute(string).get();
                    if(res.isEmpty()){
                        //fragmentTransaction.add(R.id.llCompanies,new StockItem(string, null, null,null));
                    }else{
                        if(res.get(0).equals("")||res.get(0).equals("null")){
                            fragmentTransaction.add(R.id.llCompanies,new StockItem(string, res.get(3), res.get(1),res.get(2), 0));
                        }else{
                            fragmentTransaction.add(R.id.llCompanies,new StockItem(res.get(0), res.get(3), res.get(1),res.get(2), 0));
                        }
                    }
                }
            }
            fragmentTransaction.commit();
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }

    public static Context getContext2() {
        return mContext;
    }



}
