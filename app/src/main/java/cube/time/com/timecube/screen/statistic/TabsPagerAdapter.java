package cube.time.com.timecube.screen.statistic;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cube.time.com.timecube.screen.statistic.fragment.MonthFragment;
import cube.time.com.timecube.screen.statistic.fragment.TodayFragment;
import cube.time.com.timecube.screen.statistic.fragment.WeekFragment;


/**
 * Created by Andrey on 4/5/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private Context context;

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        tabs = new String[]{
                "Сегодня",
                "Неделя",
                "Месяц",
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TodayFragment.getInstance();
            case 1:
                return WeekFragment.getInstance();
            case 2:
                return MonthFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
