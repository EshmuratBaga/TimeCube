package cube.time.com.timecube.screen.settings_cube;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cube.time.com.timecube.R;
import cube.time.com.timecube.utils.dialog.LoadingDialog;
import cube.time.com.timecube.utils.dialog.LoadingView;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;

public class CubeSettingsActivity extends AppCompatActivity implements CubeView,View.OnClickListener{
    private CubeSettingsPresenter presenter;
    private LifecycleHandler lifecycleHandler;
    private LoadingView loadingView;

    private RadioGroup rgrType;
    private RadioButton rBtnCube;
    private RadioButton rBtnOkta;
    private RadioButton rBtnDode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initWidget();

    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        lifecycleHandler = LoaderLifecycleHandler.create(this,getSupportLoaderManager());
        presenter = new CubeSettingsPresenter(lifecycleHandler,this);

        rgrType = (RadioGroup) findViewById(R.id.r_btn_group_type);
        rBtnCube = (RadioButton) findViewById(R.id.rbtn_cube);
        rBtnOkta = (RadioButton) findViewById(R.id.rbtn_okta);
        rBtnDode = (RadioButton) findViewById(R.id.rbtn_dode);

        rBtnCube.setOnClickListener(this);
        rBtnOkta.setOnClickListener(this);
        rBtnDode.setOnClickListener(this);
    }

    @Override
    public void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        rBtnCube.setChecked(false);
                        rBtnOkta.setChecked(false);
                        rBtnDode.setChecked(false);
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
            case R.id.rbtn_cube:
                rBtnCube.setChecked(true);
                rBtnOkta.setChecked(false);
                rBtnDode.setChecked(false);
                presenter.chooseType();
                break;
            case R.id.rbtn_okta:
                rBtnCube.setChecked(false);
                rBtnOkta.setChecked(true);
                rBtnDode.setChecked(false);
                presenter.chooseType();
                break;
            case R.id.rbtn_dode:
                rBtnCube.setChecked(false);
                rBtnOkta.setChecked(false);
                rBtnDode.setChecked(true);
                presenter.chooseType();
                break;
        }
    }
}
