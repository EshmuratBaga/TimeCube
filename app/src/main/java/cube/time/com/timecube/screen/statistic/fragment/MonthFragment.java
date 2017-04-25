package cube.time.com.timecube.screen.statistic.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cube.time.com.timecube.R;
import cube.time.com.timecube.model.CubeSide;
import cube.time.com.timecube.model.Time;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment {
    private BarChart barChart;
    private Realm realm;
    private long spendtime = 0;
    private long starttime;
    private long endtime;
    private long weekMillisecond;
    private long today;
    private Calendar calendar;
    private int side;
    private List<BarEntry> entries = new ArrayList<>();
    private List<LegendEntry> entryList = new ArrayList<>();

    public static MonthFragment getInstance() {
        Bundle args = new Bundle();
        MonthFragment fragment = new MonthFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public MonthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_month, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Integer> colors = getIntegers();
        barChart = (BarChart) view.findViewById(R.id.bar_chart_month);

        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> cubeSides = realm.where(CubeSide.class).findAll();
        RealmResults<Time> times = realm.where(Time.class).findAll();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -30);
        weekMillisecond = calendar.getTimeInMillis();
        today = System.currentTimeMillis();
        Log.d("dddd","date" + calendar.getTime());

        for (int i = 0; i < cubeSides.size(); i++) {
            String name = cubeSides.get(i).getName();
            side = cubeSides.get(i).getSide();
            for (int j = 0; j < times.size(); j++) {
                if (times.get(j).getSide() == side){
                    starttime = times.get(j).getStartTime();
                    endtime = times.get(j).getEndTime();
                    if (weekMillisecond <= starttime && starttime < today && endtime != 0){
                        spendtime = spendtime + (times.get(j).getEndTime() - times.get(j).getStartTime());
                    }
                }
            }
            int min = (int) ((spendtime / 1000) / 60);
            entries.add(new BarEntry(i, min));
            entryList.add(new LegendEntry(name, Legend.LegendForm.SQUARE,10f,10f,null,colors.get(i)));
            spendtime = 0;
        }

        initBarChart(colors);
    }

    private void initBarChart(List<Integer> colors) {
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColors(colors);
        set.setValueFormatter(new LargeValueFormatter());

        Legend l = barChart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setCustom(entryList);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }

    @NonNull
    private List<Integer> getIntegers() {
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));
        colors.add(getResources().getColor(R.color.royal_blue));
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.portage));
        colors.add(getResources().getColor(R.color.blue_er));
        return colors;
    }
}
