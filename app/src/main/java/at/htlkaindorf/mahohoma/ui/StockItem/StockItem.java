package at.htlkaindorf.mahohoma.ui.StockItem;

import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;

import at.htlkaindorf.mahohoma.R;

public class StockItem extends Fragment {
    ImageView iv;
    TextView name, value, change;

    String Name, Image, Value, Change;

    public StockItem(String name, String image, String value, String change) {
        Name = name;
        Image = image;
        Value = value;
        Change = change;
    }

    private StockItemViewModel mViewModel;

    public static StockItem newInstance() {
        return new StockItem(null, null, null, null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.stock_item_fragment, container, false);
        iv = root.findViewById(R.id.ivImage);
        Picasso.get().load(Image).into(iv);
        name= root.findViewById(R.id.tvCompanyName);
        name.setText(Name);
        value = root.findViewById(R.id.tvValue);
        value.setText(Value);
        change = root.findViewById(R.id.tvChange);
        change.setText(Change);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StockItemViewModel.class);
        // TODO: Use the ViewModel
    }

}
