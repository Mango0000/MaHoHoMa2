package at.htlkaindorf.mahohoma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Button enterButton = findViewById(R.id.enterButton);
        TextView username = findViewById(R.id.tvUsername);
        TextView password = findViewById(R.id.tvPassword);

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
    }

    private void checkTextViews()
    {

    }
}
