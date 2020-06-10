package at.htlkaindorf.mahohoma.ui.StockItem;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.FragmentNavigator;

import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.favourite.favourite;
import at.htlkaindorf.mahohoma.ui.stock.stock;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class StockItem extends Fragment implements View.OnClickListener {
    ImageView iv;
    TextView name, value, change;

    String Name, Image, Value, Change, Symbol;
    int width;
    private View root;
    private favourite favourites;

    public StockItem(String name, String image, String value, String change, String symbol, int width) {
        Name = name;
        Image = image;
        Value = value;
        Change = change;
        Symbol = symbol;
        this.width = width;
        favourites = favourite.getTheInstance();
    }

    private StockItemViewModel mViewModel;

    /*public static StockItem newInstance() {
        return new StockItem(null, null, null, null);
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.stock_item_fragment, container, false);
        iv = root.findViewById(R.id.ivImage);
        Picasso.get().load(Image).transform(new RoundedCornersTransformation(40,0)).fit().centerInside().into(iv);
        name= root.findViewById(R.id.tvCompanyName);
        if(Name.length()>40){
            name.setText(Name.substring(0,40) +" ...");
        }else{
            name.setText(Name);
        }
        value = root.findViewById(R.id.tvValue);
        value.setText(Value);
        change = root.findViewById(R.id.tvChange);
        change.setText(Change);
        root.setOnClickListener(this);
        if(!favourites.isFavourite(Symbol)){
            setOnAddFavourite();
        }else{
            setOnRemoveFavourite();
        }
        LinearLayout ll1 = root.findViewById(R.id.ll1);
        ViewGroup.LayoutParams params = ll1.getLayoutParams();
        if(width!=0){
            final float scale = getContext().getResources().getDisplayMetrics().density;
            params.width =(int) (width * scale + 0.5f);
        }
        ll1.setLayoutParams(params);
        return root;
    }



    public void setOnRemoveFavourite(){
        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Remove Favourite")
                        .setMessage("Do you want to remove this Item from your favourites?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                favourites.removeFavourite(Symbol);
                                setOnAddFavourite();
                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                        .setIcon(R.drawable.ic_heart_svg_remove)
                        .show();
                return true;
            }
        });
    }

    public void setOnAddFavourite(){
        root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Add to Favourite")
                        .setMessage("Do you want to add this Item to your favourites?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        favourites.addFavourite(Symbol);
                        setOnRemoveFavourite();
                    }
                })
                        .setIcon(R.drawable.ic_heart_svg)
                        .show();
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StockItemViewModel.class);
    }

    @Override
    public void onClick(View v) {
        stock stock = new stock(Name, Image, Symbol);
        FragmentManager fragmentManager;
        if(width == 0){
            fragmentManager = getParentFragmentManager();
        }else{
            fragmentManager = getParentFragment().getParentFragmentManager();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, stock);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}
