package cube.time.com.timecube.screen.reg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cube.time.com.timecube.R;
import cube.time.com.timecube.screen.login.LoginActivity;
import cube.time.com.timecube.utils.dialog.LoadingDialog;
import cube.time.com.timecube.utils.dialog.LoadingView;

public class SignUpActivity extends AppCompatActivity implements SignUpView,View.OnClickListener{

    private LoadingView loadingView;
    private SignUpPresenter presenter;

    private Button btnSignUp;
    private TextView txtSignIn;
    private EditText etxtName;
    private EditText etxtEmail;
    private EditText etxtPass;
    private EditText etxtPass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initWidget();
    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        presenter = new SignUpPresenter(this,this);

        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        txtSignIn = (TextView) findViewById(R.id.txt_sign_up_signin);
        etxtName = (EditText) findViewById(R.id.e_txt_sign_up_name);
        etxtEmail = (EditText) findViewById(R.id.e_txt_sign_up_email);
        etxtPass = (EditText) findViewById(R.id.e_txt_sign_up_password);
        etxtPass2 = (EditText) findViewById(R.id.e_txt_sign_up_password_repeat);

        txtSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
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
            case R.id.btn_sign_up:
                if (etxtPass.getText().toString().equals(etxtPass2.getText().toString())){
                    presenter.signUp();
                }else {
                    Toast.makeText(this,"Пароль не совпадает",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
