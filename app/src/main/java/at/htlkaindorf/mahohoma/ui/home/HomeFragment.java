package at.htlkaindorf.mahohoma.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.CompanyResolver;
import at.htlkaindorf.mahohoma.favourite.favourite;
import at.htlkaindorf.mahohoma.ui.StockItem.StockItem;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private LinearLayout llFavourites;
    private favourite favourites;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /*btLog = root.findViewById(R.id.btOpen);
        btLog.setOnClickListener(e -> click());*/
        favourites = favourite.getTheInstance();
        llFavourites = root.findViewById(R.id.llFavourites);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadFavourites();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadFavourites() throws ExecutionException, InterruptedException {
        List<String> favouritesList = favourites.getFavourites();
        llFavourites.removeAllViews();
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if(favouritesList.isEmpty()){
            TextView tvNoFavourite = new TextView(this.getContext());
            tvNoFavourite.setText("No Favourites found");
            llFavourites.addView(tvNoFavourite);
        }
        for(String string: favouritesList){
            List<String> res = new CompanyResolver().execute(string).get();
            if(res.get(0).equals("keyerror")){
                showMyDialog();
                break;
            }else{
                fragmentTransaction.add(R.id.llFavourites,new StockItem(string, res.get(3), res.get(1), res.get(2), string,0));
            }
        }
        fragmentTransaction.commit();
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
}
