package com.example.repro_130397466;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.os.strictmode.Violation;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.enableStrictMode();

        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void enableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectNonSdkApiUsage()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyListener(LISTENER_EXECUTOR, GREY_LISTENER)
                .build());
    }

    private static final ExecutorService LISTENER_EXECUTOR= Executors.newSingleThreadExecutor();
    private final static StrictMode.OnVmViolationListener GREY_LISTENER =
        new StrictMode.OnVmViolationListener() {
            @Override
            public void onVmViolation(Violation violation)
            {
                boolean suppressViolation = false;
                String[] androidInternalUsage = new String[]{
                    "android/view/View;->computeFitSystemWindows", // androidx.appcompat.app.AppCompatActivity.findViewById
                    "android/view/ViewGroup;->makeOptionalFitsSystemWindows()", //androidx.appcompat.app.AppCompatActivity.findViewById
                };

                for (String value : androidInternalUsage)
                {
                    if (violation.getMessage().contains(value))
                    {
                        suppressViolation = true;
                        break;
                    }
                }

                if (!suppressViolation)
                {
                    throw new RuntimeException(violation.fillInStackTrace());
                }
            }
        };
}

