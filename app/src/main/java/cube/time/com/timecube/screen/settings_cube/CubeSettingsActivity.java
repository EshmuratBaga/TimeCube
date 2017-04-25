package cube.time.com.timecube.screen.settings_cube;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;

import cube.time.com.timecube.R;
import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.screen.bottom.TimeActivity;
import cube.time.com.timecube.screen.main.MainActivity;
import cube.time.com.timecube.screen.scan.NodeContainerFragment;
import cube.time.com.timecube.utils.dialog.LoadingDialog;
import cube.time.com.timecube.utils.dialog.LoadingView;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;

public class CubeSettingsActivity extends AppCompatActivity implements CubeView,View.OnClickListener{

    /**
     * tag used for retrieve the NodeContainerFragment
     */
    private final static String NODE_FRAGMENT = CubeSettingsActivity.class.getCanonicalName() + "" +
            ".NODE_FRAGMENT";

    private final static String NODE_TAG = CubeSettingsActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";

    private final static String MAIN_TO_SET = CubeSettingsActivity.class.getCanonicalName() + "" +
            ".MAIN_TO_SET";

    /**
     * node that will stream the data
     */
    private Node mNode;

    /**
     * fragment that manage the node connection and avoid a re connection each time the activity
     * is recreated
     */
    private NodeContainerFragment mNodeContainer;

    /**
     * listener that will update the displayed feature data
     */
    private Feature.FeatureListener mGenericUpdate;

    private Feature selectedFeature;

    private String nodeTag;
    private String isMain;
    private CubeSettingsPresenter presenter;
    private LifecycleHandler lifecycleHandler;

    private LoadingView loadingView;
    private RadioGroup rgrType;
    private RadioButton rBtnCube;
    private RadioButton rBtnOkta;

    private RadioButton rBtnDode;

    private int curent = 0;

    private boolean px = false,py = false,pz = false;
    private double dx, dy, dz;
    private double ax = 2000, ay = 2000, az = 2000;
    private double dt = 1;

    private int sx = 0,sy = 0,sz = 0;
    /**
            * listener that will be used for enable the notification when the node is connected
     */
    private Node.NodeStateListener mNodeStatusListener = (node, newState, prevState) -> {
        if (newState == Node.State.Connected) {
            CubeSettingsActivity.this.runOnUiThread(this::populateFeatureList);
        }
    };

    @Override
    public void populateFeatureList() {
        if (mNode != null) {
            List<Feature> features = mNode.getFeatures();
            for (Feature f : features) {
                if (f.getName().contains("Accelerometer")) {
                    selectedFeature = f;

                    if(selectedFeature==null)
                        return;

                    if (mNode.isEnableNotification(selectedFeature)) {

                        selectedFeature.removeFeatureListener(mGenericUpdate);
                        mNode.disableNotification(selectedFeature);
                    } else {
                        //create a listener that will update the selected view
                        View view = null;
                        mGenericUpdate = new GenericFragmentUpdate((TextView) view);
                        selectedFeature.addFeatureListener(mGenericUpdate);
                        mNode.enableNotification(selectedFeature);
                    }
                }//if
            }//for

            //set the adapter as data source for the adapter

        }//if
    }//populateFeatureList

    @Override
    public void showAddTaskDialog(int sum) {
        AddTaskDialog.newInstance(sum).show(getSupportFragmentManager(),"filterdate");
    }

    public static Intent getStartIntent(Context c, @NonNull Node node) {
        Intent i = new Intent(c, CubeSettingsActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        return i;
    }

    public static Intent openCubeSetting(Context c, @NonNull Node node) {
        Intent i = new Intent(c, CubeSettingsActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtra(MAIN_TO_SET,"isMain");
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cube_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nodeTag = getIntent().getStringExtra(NODE_TAG);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);
        isMain = getIntent().getStringExtra(MAIN_TO_SET);
        initWidget();
        //create or recover the NodeContainerFragment
        if (savedInstanceState == null) {
            Intent i = getIntent();
            mNodeContainer = new NodeContainerFragment();
            mNodeContainer.setArguments(i.getExtras());

            getSupportFragmentManager().beginTransaction().add(mNodeContainer, NODE_FRAGMENT).commit();

        } else {
            mNodeContainer = (NodeContainerFragment) getSupportFragmentManager().findFragmentByTag(NODE_FRAGMENT);
        }//if-else

    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        lifecycleHandler = LoaderLifecycleHandler.create(this,getSupportLoaderManager());
        presenter = new CubeSettingsPresenter(lifecycleHandler,this);

        if (isMain == null){
            Log.d("dddd","" + 123);
            presenter.checkDataCubeSide();
        }

        rgrType = (RadioGroup) findViewById(R.id.r_btn_group_type);
        rBtnCube = (RadioButton) findViewById(R.id.rbtn_cube);
        rBtnOkta = (RadioButton) findViewById(R.id.rbtn_okta);
        rBtnDode = (RadioButton) findViewById(R.id.rbtn_dode);

        rBtnCube.setOnClickListener(this);
        rBtnOkta.setOnClickListener(this);
        rBtnDode.setOnClickListener(this);
    }

    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(this,mNode);
        startActivity(intent);
        finish();
    }

    @Override
    public void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Добавьте задачи")
                .setMessage("Переверните кубик чтобы добавить задач")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        presenter.initAddTask(mNode,mNodeStatusListener);
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

    @Override
    protected void onPause() {

        //it is safe remove also if we didn't add it
        mNode.removeNodeStateListener(mNodeStatusListener);

        //if the node is already disconnected we don't care of disable the notification
        if (mNode.isConnected()) {
            disableNeedNotification();
        }//if

        super.onPause();
    }//stopDemo

    private void disableNeedNotification() {
        List<Feature> features = mNode.getFeatures();
        for (Feature f : features) {
            if (mNode.isEnableNotification(f))
                mNode.disableNotification(f);
        }//for sTestFeature
    }//disableNeedNotification

    private class GenericFragmentUpdate implements Feature.FeatureListener {

        /**
         * text view that will contain the data/name
         */
        final private TextView mTextView;

        /**
         * @param text text view that will show the name/values
         */
        GenericFragmentUpdate(TextView text) {
            mTextView = text;
        }//GenericFragmentUpdate

        /**
         * set the text view text with the feature toString value
         *
         * @param f      feature that has received an update
         * @param sample new data received from the feature
         */
        @Override
        public void onUpdate(Feature f, final Feature.Sample sample) {
            final Number[] numXYZ =  f.getSample().data;
            CubeSettingsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int sum = Math.round((numXYZ[0].floatValue()*1/988) + (numXYZ[1].floatValue()*2/988) + (numXYZ[2].floatValue()*3/988));

                    dx = (numXYZ[0].floatValue() - ax) / dt;
                    ax = numXYZ[0].floatValue();
                    if(Math.abs(dx)<50){
                        sx=sx + 1;
                    }else {
                        sx = 0;
                    }


                    dy = (numXYZ[1].floatValue() - ay) / dt;
                    ay = numXYZ[1].floatValue();
                    if(Math.abs(dy)<50){
                        sy=sy + 1;
                    }else {
                        sy = 0;
                    }


                    dz = (numXYZ[2].floatValue() - az) / dt;
                    az = numXYZ[2].floatValue();
                    if(Math.abs(dz)<50){
                        sz=sz + 1;
                    }else {
                        sz = 0;
                    }

                    if(sx>5){
                        px = true;
                    }else {
                        px = false;
                    }

                    if(sy>5){
                        py = true;
                    }else {
                        py = false;
                    }

                    if(sz>5){
                        pz = true;
                    }else {
                        pz = false;
                    }

                    //************************************


                    if (px && py && pz){
                        Log.d("dddd","sum" + sum);
                        if (sum == -2 && curent != 1){
                            curent = 1;
                            presenter.addTaskDialog(sum);
                            Toast.makeText(CubeSettingsActivity.this,"1",Toast.LENGTH_SHORT).show();
                        }
                        if (sum == -1  && curent != 2){
                            curent = 2;
                            presenter.addTaskDialog(sum);
                            Toast.makeText(CubeSettingsActivity.this,"2",Toast.LENGTH_SHORT).show();
                        }
                        if (sum == 3 && curent != 3){
                            curent = 3;
                            presenter.addTaskDialog(sum);
                            Toast.makeText(CubeSettingsActivity.this,"3",Toast.LENGTH_SHORT).show();
                        }
                        if (sum == -3 && curent != 4){
                            curent = 4;
                            presenter.addTaskDialog(sum);
                            Toast.makeText(CubeSettingsActivity.this,"4",Toast.LENGTH_SHORT).show();
                        }
                        if (sum == 1 && curent != 5){
                            curent = 5;
                            presenter.addTaskDialog(sum);
                            Toast.makeText(CubeSettingsActivity.this,"5",Toast.LENGTH_SHORT).show();
                        }
                        if (sum == 2 && curent != 6){
                            curent = 6;
                            presenter.addTaskDialog(sum);
                            Toast.makeText(CubeSettingsActivity.this,"6",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }//onUpdate

    }//GenericFragmentUpdate

}
