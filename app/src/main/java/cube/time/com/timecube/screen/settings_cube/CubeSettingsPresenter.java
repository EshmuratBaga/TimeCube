package cube.time.com.timecube.screen.settings_cube;

import android.support.annotation.NonNull;

import com.st.BlueSTSDK.Node;

import cube.time.com.timecube.model.CubeSide;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.arturvasilov.rxloader.LifecycleHandler;

/**
 * Created by Andrey on 4/12/2017.
 */

public class CubeSettingsPresenter {
    private final LifecycleHandler mLifecycleHandler;
    private final CubeView view;

    private Realm realm;

    public CubeSettingsPresenter(@NonNull LifecycleHandler mLifecycleHandler,@NonNull CubeView view) {
        this.mLifecycleHandler = mLifecycleHandler;
        this.view = view;
    }

    public void chooseType(){
        view.showDialog();
    }

    public void initAddTask(Node mNode, Node.NodeStateListener mNodeStatusListener){
        if (mNode.isConnected()) {
            view.populateFeatureList();
        } else {
            mNode.addNodeStateListener(mNodeStatusListener);
        }
    }

    public void addTaskDialog(int sum){
        view.showAddTaskDialog(sum);
    }

    public void checkDataCubeSide(){
        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> sides = realm.where(CubeSide.class).findAll();
        if (sides.size() != 0){
            view.openMainActivity();
        }
    }
}
