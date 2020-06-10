package at.htlkaindorf.mahohoma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.htlkaindorf.mahohoma.database.DB_Access;

public class LoginPage extends AppCompatActivity
{
    private DB_Access db_access = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button enterButton = findViewById(R.id.enterButton);
        TextView username = findViewById(R.id.tvUsername);
        TextView password = findViewById(R.id.tvPassword);
        Button createAccountButton = findViewById(R.id.createButton);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(username.getText().length()!=0 && password.getText().length()!=0)
                {
                    enterButton.setEnabled(true);
                }
                else
                {
                    enterButton.setEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(username.getText().length()!=0 && password.getText().length()!=0)
                {
                    enterButton.setEnabled(true);
                }
                else
                {
                    enterButton.setEnabled(false);
                }
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onClickCreateAccount();
                //db_access = DB_Access.getInstance();
            }
        });
    }

    private void onClickLogin()
    {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(myIntent);
    }

    private void onClickCreateAccount() {
        Intent myIntent = new Intent(getApplicationContext(), CreateAccountPage.class);
        startActivity(myIntent);
    }
}
