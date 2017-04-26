package cube.time.com.timecube.screen.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Manager;
import com.st.BlueSTSDK.Node;

import java.util.List;

import cube.time.com.timecube.R;
import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.screen.scan.NodeContainerFragment;
import cube.time.com.timecube.screen.settings_cube.CubeSettingsActivity;
import cube.time.com.timecube.screen.statistic.StatisticActivity;
import cube.time.com.timecube.utils.dialog.LoadingDialog;
import cube.time.com.timecube.utils.dialog.LoadingView;
import io.realm.Realm;
import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.arturvasilov.rxloader.LoaderLifecycleHandler;

public class MainActivity extends AppCompatActivity implements MainView{

    private final static String NODE_FRAGMENT = MainActivity.class.getCanonicalName() + "" +
            ".NODE_FRAGMENT";

    private final static String NODE_TAG = MainActivity.class.getCanonicalName() + "" +
            ".NODE_TAG";

    private Node mNode;

    private NodeContainerFragment mNodeContainer;

    private Feature.FeatureListener mGenericUpdate;

    private Feature selectedFeature;

    private String nodeTag;

    private LifecycleHandler lifecycleHandler;
    private LoadingView loadingView;
    private MainPresenter presenter;
    private Realm realm;
    private CubeSide cubeSide;
    private boolean pStatus;

    private Chronometer chronometer;

    private TextView txtTitleTask;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private int curent = 0;
    private int side;

    private boolean px = false,py = false,pz = false;
    private double dx, dy, dz;
    private double ax = 2000, ay = 2000, az = 2000;
    private double dt = 1;

    private int sx = 0,sy = 0,sz = 0;

    private Node.NodeStateListener mNodeStatusListener = (node, newState, prevState) -> {
        if (newState == Node.State.Connected) {
            MainActivity.this.runOnUiThread(this::populateFeatureList);
        }
    };

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
        }//if
    }//populateFeatureList

    public static Intent getStartIntent(Context c, @NonNull Node node) {
        Intent i = new Intent(c, MainActivity.class);
        i.putExtra(NODE_TAG, node.getTag());
        i.putExtras(NodeContainerFragment.prepareArguments(node));
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return i;
    }//getStartIntent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initWidget();

        nodeTag = getIntent().getStringExtra(NODE_TAG);
        mNode = Manager.getSharedInstance().getNodeWithTag(nodeTag);

        //create or recover the NodeContainerFragment
        if (savedInstanceState == null) {
            Intent i = getIntent();
            mNodeContainer = new NodeContainerFragment();
            mNodeContainer.setArguments(i.getExtras());

            getSupportFragmentManager().beginTransaction().add(mNodeContainer, NODE_FRAGMENT).commit();

        } else {
            mNodeContainer = (NodeContainerFragment) getSupportFragmentManager()
                    .findFragmentByTag(NODE_FRAGMENT);
        }//if-else
    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
        if (mNode.isConnected()) {
            populateFeatureList();
        } else{
            mNode.addNodeStateListener(mNodeStatusListener);
        }

    }//onResume

    @Override
    protected void onPause() {
        //it is safe remove also if we didn't add it
        mNode.removeNodeStateListener(mNodeStatusListener);
//        if the node is already disconnected we don't care of disable the notification
        if (mNode.isConnected()) {
            presenter.disableNeedNotification(mNode);
        }//if
        super.onPause();
    }//stopDemo

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }

    @Override
    public void onBackPressed() {
        presenter.backPressed();
    }

    @Override
    public void initWidget() {
        loadingView = LoadingDialog.view(getFragmentManager());
        lifecycleHandler = LoaderLifecycleHandler.create(this,getSupportLoaderManager());
        presenter = new MainPresenter(lifecycleHandler,this);

        chronometer = (Chronometer) findViewById(R.id.chrono);
        txtTitleTask = (TextView) findViewById(R.id.txt_task_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_tasks);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        presenter.initAdapter();

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
    public void finishBackPress() {
        finish();
    }

    @Override
    public void showToast() {
        Toast.makeText(this, "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initAdapter(List<CubeSide> cubeSides) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CubeSideAdapter(cubeSides,this));
    }

    @Override
    public void openSettingsActivity() {
        Intent intent = CubeSettingsActivity.openCubeSetting(this,mNode);
        startActivity(intent);
    }

    @Override
    public void openStatisticActivity() {
        Intent intent = StatisticActivity.openStatisticActivity(this);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main_settings) {
            presenter.openSettingsActivity();
            return true;
        }
        if (id == R.id.menu_main_statistic) {
            presenter.openStatisticActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GenericFragmentUpdate implements Feature.FeatureListener {

        final private TextView mTextView;

        GenericFragmentUpdate(TextView text) {
            mTextView = text;
        }//GenericFragmentUpdate

        @Override
        public void onUpdate(Feature f, final Feature.Sample sample) {
            final Number[] numXYZ =  f.getSample().data;
            MainActivity.this.runOnUiThread(() -> {
                pStatus = true;
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
                    if (sum == -2 && curent != 1){
                        curent = 1;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",sum).findFirst();
                        presenter.setEndTimeModel(side,recyclerView);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        presenter.startProgresBar(pStatus,progressBar);
                        chronometer.start();
                        presenter.setStratTimeModel(sum);
                        txtTitleTask.setText(cubeSide.getName());
                        side = sum;
                    }
                    if (sum == -1  && curent != 2){
                        curent = 2;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",sum).findFirst();
                        presenter.setEndTimeModel(side, recyclerView);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        presenter.startProgresBar(pStatus,progressBar);
                        chronometer.start();
                        presenter.setStratTimeModel(sum);
                        txtTitleTask.setText(cubeSide.getName());
                        side = sum;
                    }
                    if (sum == 3 && curent != 3){
                        curent = 3;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",sum).findFirst();
                        presenter.setEndTimeModel(side, recyclerView);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        presenter.startProgresBar(pStatus,progressBar);
                        chronometer.start();
                        presenter.setStratTimeModel(sum);
                        txtTitleTask.setText(cubeSide.getName());
                        side = sum;
                    }
                    if (sum == -3 && curent != 4){
                        curent = 4;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",sum).findFirst();
                        presenter.setEndTimeModel(side, recyclerView);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        presenter.startProgresBar(pStatus,progressBar);
                        chronometer.start();
                        presenter.setStratTimeModel(sum);
                        txtTitleTask.setText(cubeSide.getName());
                        side = sum;
                    }
                    if (sum == 1 && curent != 5){
                        curent = 5;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",sum).findFirst();
                        presenter.setEndTimeModel(side, recyclerView);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        presenter.startProgresBar(pStatus,progressBar);
                        chronometer.start();
                        presenter.setStratTimeModel(sum);
                        txtTitleTask.setText(cubeSide.getName());
                        side = sum;
                    }
                    if (sum == 2 && curent != 6){
                        curent = 6;
                        cubeSide = realm.where(CubeSide.class).equalTo("side",sum).findFirst();
                        presenter.setEndTimeModel(side, recyclerView);
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        presenter.startProgresBar(pStatus,progressBar);
                        chronometer.start();
                        presenter.setStratTimeModel(sum);
                        txtTitleTask.setText(cubeSide.getName());
                        side = sum;
                    }

                }
            });
        }//onUpdate

    }//GenericFragmentUpdate
}
