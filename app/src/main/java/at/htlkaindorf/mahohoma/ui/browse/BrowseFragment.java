package at.htlkaindorf.mahohoma.ui.browse;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyResolver;
import at.htlkaindorf.mahohoma.backgroundTasks.SearchCompanies;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;
import at.htlkaindorf.mahohoma.ui.top_types.top_types;

public class BrowseFragment extends Fragment
{
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
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            showMyDialog();
                            animation.stop();
                            iv.setVisibility(View.INVISIBLE);
                        }
                    });
                }else if(most_active.is_done==2){

                    animation.stop();
                    iv.setVisibility(View.INVISIBLE);
                }else if(most_active.is_done==-2){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            showNoConnectionDialog();
                            animation.stop();
                            iv.setVisibility(View.INVISIBLE);
                        }
                    });

                }else{
                    FragmentManager mFragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    //Most Active
                    fragmentTransaction.add(R.id.llCompanies, most_active);
                    //Most Gainer
                    fragmentTransaction.add(R.id.llCompanies, most_gainer);
                    //Most Loser
                    fragmentTransaction.add(R.id.llCompanies, most_loser);

                    animation.stop();
                    iv.setVisibility(View.INVISIBLE);
                    fragmentTransaction.commit();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void showMyDialog() {
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

    private void showNoConnectionDialog() {
        new AlertDialog.Builder(this.getContext())
                .setTitle("No connection")
                .setMessage("No internet conenction available")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    private void onSearch() {
        try {
            SearchCompanies search = new SearchCompanies();
            List<String> output = search.execute(etsearch.getText().toString()).get();
            FragmentManager mFragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            ll.removeAllViews();
            if(output == null || output.isEmpty()){
                TextView empty = new TextView(getContext());
                empty.setText("No Results");
                ll.addView(empty);
            }else if(output.get(0)=="keyerror"){
                showMyDialog();
            }else {
                for (String string:output) {
                    Log.e(TAG, string);
                    List<String> res = new CompanyResolver().execute(string).get();
                    if(res == null){
                        fragmentTransaction.add(R.id.llCompanies,new StockItem("test", null, null,null,null,0));
                    }else{
                        if(res.get(0).equals("")||res.get(0).equals("null")){
                            fragmentTransaction.add(R.id.llCompanies,new StockItem(string, res.get(3), res.get(1), res.get(2), string,0));
                        }else{
                            fragmentTransaction.add(R.id.llCompanies,new StockItem(res.get(0), res.get(3), res.get(1), res.get(2), string,0));
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
