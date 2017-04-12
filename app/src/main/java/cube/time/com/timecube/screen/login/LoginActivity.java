package cube.time.com.timecube.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cube.time.com.timecube.R;
import cube.time.com.timecube.screen.reg.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginView,View.OnClickListener{
    private Button btnLogin;
    private TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
    }

    @Override
    public void initWidget() {
        btnLogin = (Button) findViewById(R.id.btn_sign_in);
        txtSignUp = (TextView) findViewById(R.id.txt_login_sign_up);

        txtSignUp.setOnClickListener(this);
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
            case R.id.txt_login_sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}
