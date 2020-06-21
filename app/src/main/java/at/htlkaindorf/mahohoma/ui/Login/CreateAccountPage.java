package at.htlkaindorf.mahohoma.ui.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.htlkaindorf.mahohoma.MainActivity;
import at.htlkaindorf.mahohoma.R;

public class CreateAccountPage extends AppCompatActivity
{
    private Button createAccountBt;
    private TextView username;
    private TextView emailAddress;
    private TextView password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_page);
        createAccountBt = findViewById(R.id.createAccountButton);
        username = findViewById(R.id.TfcreateUsername);
        emailAddress = findViewById(R.id.TfCreateMail);
        password = findViewById(R.id.TfCreatePassword);

        createAccountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       // createAccountBt.setBackgroundColor(Color.BLUE);
    }
}
