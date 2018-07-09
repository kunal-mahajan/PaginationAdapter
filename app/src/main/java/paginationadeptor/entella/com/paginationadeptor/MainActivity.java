package paginationadeptor.entella.com.paginationadeptor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import paginationadeptor.entella.com.paginationadeptor.component.DynamicLoadingListHelper;

public class MainActivity extends AppCompatActivity {

    private DynamicLoadingListHelper listHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadNewJob((LinearLayout) findViewById(R.id.ll));
    }

    private void loadNewJob(LinearLayout llNewJobs) {
        listHelper = new DynamicLoadingListHelper(this, llNewJobs, new NameDynamicLoadingListHealper(this), 3) {
            int k;


            @Override
            protected void loadData(final int offset, final int limit) {
                final int totalRecords = 500;

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int count = offset + limit < totalRecords ? limit : totalRecords - offset;
                        final String s[] = new String[count];
                        for (int i = 0; i < count; i++) {
                            s[i] = "Testing " + k++;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listHelper.dataLoaded(Arrays.asList(s), totalRecords);
                            }
                        });
                    }
                }, 1500);
            }
        };
    }
}
