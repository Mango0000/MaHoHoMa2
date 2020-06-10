package at.htlkaindorf.mahohoma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
