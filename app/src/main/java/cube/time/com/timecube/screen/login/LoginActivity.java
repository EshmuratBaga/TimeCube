package cube.time.com.timecube.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cube.time.com.timecube.R;
import cube.time.com.timecube.screen.reg.SignUpActivity;
import cube.time.com.timecube.screen.scan.ScanActivity;
import cube.time.com.timecube.screen.settings_cube.CubeSettingsActivity;
import cube.time.com.timecube.utils.dialog.LoadingDialog;
import cube.time.com.timecube.utils.dialog.LoadingView;

public class LoginActivity extends AppCompatActivity implements LoginView,View.OnClickListener{
    private Button btnLogin;
    private EditText etxtLogin;
    private EditText etxtPass;
    private TextView txtSignUp;
    private LoadingView loadingView;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        presenter = new LoginPresenter(this,this);

        btnLogin = (Button) findViewById(R.id.btn_sign_in);
        txtSignUp = (TextView) findViewById(R.id.txt_login_sign_up);
        etxtLogin = (EditText) findViewById(R.id.e_txt_login_email);
        etxtPass = (EditText) findViewById(R.id.e_txt_login_password);

        btnLogin.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
    }

    @Override
    public void openScanActivity() {
        startActivity(new Intent(this, ScanActivity.class));
    }

    @Override
    public void showLoading() {
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        loadingView.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_login_sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.btn_sign_in:
                presenter.checkUser(etxtLogin.getText().toString(),etxtPass.getText().toString());
                break;
        }
    }
}
