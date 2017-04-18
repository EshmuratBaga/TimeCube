package cube.time.com.timecube.screen.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cube.time.com.timecube.R;
import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.model.Time;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Andrey on 4/15/2017.
 */

public class CubeSideAdapter extends RecyclerView.Adapter<CubeSideAdapter.CubeHolder>{

    private List<CubeSide> cubeSides = new ArrayList<>();
    private Context context;
    private long spendtime = 0;

    public CubeSideAdapter() {
    }

    public CubeSideAdapter(List<CubeSide> cubeSides, Context context) {
        this.cubeSides = cubeSides;
        this.context = context;
    }

    public class CubeHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTitle;
        private TextView txtTime;

        public CubeHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img_cube_side_item);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_cube_side_title);
            txtTime = (TextView) itemView.findViewById(R.id.txt_cube_side_time);
        }
    }

    @Override
    public CubeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_cube_side,parent,false);
        return new CubeHolder(view);
    }

    @Override
    public void onBindViewHolder(CubeHolder holder, int position) {
        Realm realm = Realm.getDefaultInstance();
        TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(cubeSides.get(position).getName().substring(0,1),randomColor());
        holder.imageView.setImageDrawable(myDrawable);

        if (realm.where(Time.class).equalTo("side",cubeSides.get(position).getSide()).findAll().size() != 0){
            RealmResults<Time> realmResults  = realm.where(Time.class).equalTo("side",cubeSides.get(position).getSide()).findAll();
            for (int i = 0; i < realmResults.size(); i++) {
                if (realmResults.get(i).getEndTime() != 0){
                    spendtime = spendtime + (realmResults.get(i).getEndTime() - realmResults.get(i).getStartTime());
                }
            }
            int spend = (int) (spendtime / 1000);
            int hours = (spend / 3600);
            int minutes = ((spend % 3600) / 60);
            int seconds = (spend % 60);

            String timeString = String.format("%02dч:%02dмин:%02dсек", hours, minutes, seconds);

            holder.txtTime.setText(timeString);
            spendtime = 0;
        }
        holder.txtTitle.setText(cubeSides.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cubeSides.size();
    }

    public void refresh(List<CubeSide> datas){
        cubeSides.clear();
        notifyDataSetChanged();
    }

    private int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
