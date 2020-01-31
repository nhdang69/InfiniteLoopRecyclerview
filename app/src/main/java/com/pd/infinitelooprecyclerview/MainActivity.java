package com.pd.infinitelooprecyclerview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    RecyclerView rcvBanner;
    List<String> urls;
    Timer timer;
    TimerTask timerTask;
    int position;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getListurl();

        rcvBanner = findViewById(R.id.rcvBanner);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvBanner.setLayoutManager(layoutManager);

        BannerAdapter bannerAdapter = new BannerAdapter(this, urls);
        rcvBanner.setAdapter(bannerAdapter);

        if (urls != null) {
            position = Integer.MAX_VALUE / 2;
            rcvBanner.scrollToPosition(position);
        }

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcvBanner);
        rcvBanner.smoothScrollBy(5, 0);

        rcvBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 1) {
                    stopAutoScrollBanner();
                } else if (newState == 0) {
                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        runAutoScrollBanner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScrollBanner();
    }

    private void stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
            position = layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    private void runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (position == Integer.MAX_VALUE) {
                        position = Integer.MAX_VALUE / 2;
                        rcvBanner.scrollToPosition(position);
                        rcvBanner.smoothScrollBy(5, 0);
                    } else {
                        position++;
                        rcvBanner.smoothScrollToPosition(position);
                    }
                }
            };
            timer.schedule(timerTask, 4000, 4000);
        }
    }

    private void getListurl() {
        urls = new ArrayList<>();
        urls.add("https://cdn.luxstay.com/home/apartment/apartment_1_1578970876.jpg");
        urls.add("https://cdn.luxstay.com/home/apartment/apartment_2_1578970932.jpg");
        urls.add("https://cdn.luxstay.com/home/apartment/apartment_3_1578970988.jpg");
        urls.add("https://cdn.luxstay.com/home/apartment/apartment_4_1578971024.jpg");
    }
}
