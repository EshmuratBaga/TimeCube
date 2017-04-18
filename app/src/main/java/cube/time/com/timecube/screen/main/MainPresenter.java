package cube.time.com.timecube.screen.main;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.st.BlueSTSDK.Feature;
import com.st.BlueSTSDK.Node;

import java.util.ArrayList;
import java.util.List;

import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.model.Time;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/15/2017.
 */

public class MainPresenter {

    private final LifecycleHandler mLifecycleHandler;
    private final MainView view;
    private Realm realm;
    private Time time;
    private CubeSideAdapter adapter;
    private RealmResults<CubeSide> cubeSides;

    private Boolean exit = false;

    public MainPresenter(LifecycleHandler mLifecycleHandler, MainView view) {
        this.mLifecycleHandler = mLifecycleHandler;
        this.view = view;
    }

    public void backPressed(){
        if (exit) {
            view.finishBackPress();
        } else {
            exit = true;
            view.showToast();
            new Handler().postDelayed(() -> exit = false, 3 * 1000);
        }
    }

    public void initAdapter(){
        realm = Realm.getDefaultInstance();
        cubeSides = realm.where(CubeSide.class).findAll();
        view.initAdapter(cubeSides);
    }

    public void disableNeedNotification(Node mNode){
        List<Feature> features = mNode.getFeatures();
        for (Feature f : features) {
            if (mNode.isEnableNotification(f))
                mNode.disableNotification(f);
        }
    }

    public void setStratTimeModel(int sum){
        time = new Time();
        time.setId((int) (1 + System.currentTimeMillis()));
        time.setSide(sum);
        time.setStartTime(System.currentTimeMillis());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(time);
        realm.commitTransaction();
    }

    public void setEndTimeModel(int side, RecyclerView recyclerView){
        if (realm.where(Time.class).equalTo("side", side).findAll().size() != 0) {
            RealmResults<Time> realmResults = realm.where(Time.class).equalTo("side", side).findAll();
            time = realmResults.last();
            if (time.getEndTime() == 0) {
                realm.beginTransaction();
                time.setEndTime(System.currentTimeMillis());
                realm.copyToRealmOrUpdate(time);
                realm.commitTransaction();
            }
            adapter = new CubeSideAdapter(cubeSides, (Context) view);
            recyclerView.setAdapter(adapter);
        }
    }

    public void openSettingsActivity() {
        view.openSettingsActivity();
    }
}
