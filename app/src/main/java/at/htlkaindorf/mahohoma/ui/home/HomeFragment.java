package at.htlkaindorf.mahohoma.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import at.htlkaindorf.mahohoma.LoginPage;
import at.htlkaindorf.mahohoma.MainActivity;
import at.htlkaindorf.mahohoma.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btLog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        /*btLog = root.findViewById(R.id.btOpen);
        btLog.setOnClickListener(e -> click());*/
        return root;
    }

    private void click() {
        Intent myIntent = new Intent(getContext(), LoginPage.class);
        startActivity(myIntent);
    }
}
