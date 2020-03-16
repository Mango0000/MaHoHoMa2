package at.htlkaindorf.mahohoma.ui.browse;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import at.htlkaindorf.mahohoma.R;
import at.htlkaindorf.mahohoma.backgroundTasks.APIConnection;

public class BrowseFragment extends Fragment {
    EditText etsearch;
    TextView tv1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_browse, container, false);
        etsearch = root.findViewById(R.id.searchbar);
        tv1 = root.findViewById(R.id.tv1);
        etsearch.setOnClickListener(this::onSearch);
        return root;
    }

    private void onSearch(View view) {
        try {
            APIConnection apicon = new APIConnection();
            String output = apicon.execute(etsearch.getText().toString()).get();
            tv1.setText("\n\n\n\n"+output);
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}
