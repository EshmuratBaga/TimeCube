package cube.time.com.timecube.screen.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cube.time.com.timecube.R;
import cube.time.com.timecube.model.Time;
import cube.time.com.timecube.screen.reg.SignUpActivity;
import cube.time.com.timecube.screen.scan.ScanActivity;
import cube.time.com.timecube.screen.settings_cube.CubeSettingsActivity;
import cube.time.com.timecube.utils.dialog.LoadingDialog;
import cube.time.com.timecube.utils.dialog.LoadingView;
import io.realm.Realm;
import io.realm.RealmResults;

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
//
//        RealmResults<Time> blackjackHand = realm.where(Time.class).findAll();
//        Gson gson = new Gson();
//        List<Time> times = realm.copyFromRealm(blackjackHand);
//        String json = gson.toJson(times);
//        System.out.println(json);
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
        Intent intent = new Intent(this, ScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
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
//                presenter.checkUser(etxtLogin.getText().toString(),etxtPass.getText().toString());
                openScanActivity();
                break;
        }
    }
}
