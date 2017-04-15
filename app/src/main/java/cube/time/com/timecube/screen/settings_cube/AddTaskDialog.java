package cube.time.com.timecube.screen.settings_cube;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import cube.time.com.timecube.R;
import cube.time.com.timecube.model.CubeSide;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Andrey on 4/14/2017.
 */

public class AddTaskDialog extends DialogFragment {

    private View form=null;
    private EditText etxtTitle;
    private Realm realm;
    private CubeSide cubeSide;
    private boolean isHave = false;
    int side;

    public static AddTaskDialog newInstance(int num) {
        AddTaskDialog f = new AddTaskDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("side", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        side = getArguments().getInt("side");
        Log.d("dddd","side" + side);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        etxtTitle = (EditText) form.findViewById(R.id.e_txt_title_task);

        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> sides = realm.where(CubeSide.class).findAll();
        if (sides.size() != 0){
            Log.d("ddddd","size" + 1000);
            cubeSide = sides.where().equalTo("side",side).findFirst();
            if (cubeSide != null){
                etxtTitle.setText(cubeSide.getName());
                Log.d("dddd","id" + cubeSide.getId());
                Log.d("dddd","side" + cubeSide.getSide());
                isHave = true;
            }
        }else {
            Log.d("ddddd","size" + 0);
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isHave){
                    realm.beginTransaction();
                    cubeSide.setName(etxtTitle.getText().toString());
                    realm.copyToRealmOrUpdate(cubeSide);
                    realm.commitTransaction();
                }else {
                    realm.beginTransaction();
                    cubeSide = new CubeSide();
                    cubeSide.setId((int) (1 + System.currentTimeMillis()));
                    cubeSide.setName(etxtTitle.getText().toString());
                    cubeSide.setSide(side);
                    cubeSide.setType("cube");
                    realm.insert(cubeSide);
                    realm.commitTransaction();
                }
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setView(form).create();

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(dialog -> {
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        return alert;
    }
}
