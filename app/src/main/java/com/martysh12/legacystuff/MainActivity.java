package com.martysh12.legacystuff;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActivitySheet activitySheet = findViewById(R.id.activity_sheet);
        activitySheet.setDragStateListener(dragState -> {
            switch (dragState) {
                case IDLE_COLLAPSED:
                    activitySheet.setTitle("get pranked");
                    break;
                case IDLE_EXPANDED:
                    activitySheet.setTitle("my epic application");
                    break;
                case DRAGGING:
                    activitySheet.setTitle(":o");
                    break;
            }
        });
    }
}