package at.htlkaindorf.mahohoma;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

public class CreateAccountPage extends AppCompatActivity
{
    private Button createAccountBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_page);
        createAccountBt = findViewById(R.id.createAccountButton);
       // createAccountBt.setBackgroundColor(Color.BLUE);
    }
}
