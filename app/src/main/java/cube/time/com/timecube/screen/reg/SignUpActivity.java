package cube.time.com.timecube.screen.reg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cube.time.com.timecube.R;
import cube.time.com.timecube.screen.login.LoginActivity;

public class SignUpActivity extends AppCompatActivity implements SignUpView,View.OnClickListener{
    private Button btnSignUp;
    private TextView txtSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initWidget();
    }

    @Override
    public void initWidget() {
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        txtSignIn = (TextView) findViewById(R.id.txt_sign_up_signin);

        txtSignIn.setOnClickListener(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_sign_up_signin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
