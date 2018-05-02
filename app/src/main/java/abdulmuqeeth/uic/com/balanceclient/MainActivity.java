package abdulmuqeeth.uic.com.balanceclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import abdulmuqeeth.uic.com.balancecommon.Balance;
import abdulmuqeeth.uic.com.balancecommon.DailyCash;

public class MainActivity extends AppCompatActivity {

    private Balance mBalanceService;
    private Boolean isBound = false;

    private String mMonth;
    private String mDay;
    private String mYear;
    private String mRange;

    private boolean isDbCreated = false;

    private Button query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button create = (Button) findViewById(R.id.create_db);

        query = (Button) findViewById(R.id.query_db);

        query.setEnabled(false);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(isBound){
                        try{
                            isDbCreated = mBalanceService.createDatabase();
                            if(isDbCreated){
                                query.setEnabled(true);
                            }
                            Log.i(getClass().toString(), "Database Created");
                        }catch (RemoteException e) {
                            Log.i(getClass().toString(), "Remote Exception");
                        }
                    }
                    else {
                        Log.i(getClass().toString(), "Service Not Bound");
                    }

            }
        });


        final EditText month = (EditText) findViewById(R.id.month);
        final EditText day = (EditText) findViewById(R.id.day);
        final EditText year = (EditText) findViewById(R.id.year);
        final EditText range = (EditText) findViewById(R.id.range);

        /*if(Integer.parseInt(mRange) > 30 || Integer.parseInt(mRange) <1){
            Toast.makeText(getApplicationContext(), "Range should be between 1 and 30", Toast.LENGTH_SHORT).show();
        }*/




        if(isDbCreated == false){
            query.setEnabled(false);
        }

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound){

                    mMonth = month.getText().toString();
                    mDay = day.getText().toString();
                    mYear = year.getText().toString();
                    mRange = range.getText().toString();
                    System.out.println(mYear);

                    if(!(mYear.contentEquals("2017") || mYear.contentEquals("2018"))) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid year 2017 or 2018"+mYear, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        if(Integer.parseInt(mRange) < 1 || Integer.parseInt(mRange) >30){
                            Toast.makeText(getApplicationContext(), "Please enter a valid range between 1 and 30", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid range between 1 and 30", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        if(Integer.parseInt(mMonth) < 1 || Integer.parseInt(mMonth) >12){
                            Toast.makeText(getApplicationContext(), "Please enter a valid month between 1 and 12", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid range between 1 and 12", Toast.LENGTH_SHORT).show();
                        return;
                    }



                    if(Integer.parseInt(mYear) == 2018) {
                        if(Integer.parseInt(mMonth)==3){
                            if(Integer.parseInt(mDay)>2){
                                Toast.makeText(getApplicationContext(), "Date out of bounds", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if(Integer.parseInt(mMonth)>3){
                            Toast.makeText(getApplicationContext(), "Date out of bounds", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if(Integer.parseInt(mYear) == 2017) {
                        if(Integer.parseInt(mMonth)==1){
                            if(Integer.parseInt(mDay)<3){
                                Toast.makeText(getApplicationContext(), "Date out of bounds", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }

                    try {
                        if(Integer.parseInt(mDay) < 1 || Integer.parseInt(mDay) >31){
                            Toast.makeText(getApplicationContext(), "Please enter a valid day between 1 and 31", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid day between 1 and 31", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try{
                        DailyCash[] mList = mBalanceService.dailyCash(Integer.parseInt(day.getText().toString()),Integer.parseInt(month.getText().toString()),Integer.parseInt(year.getText().toString()),Integer.parseInt(range.getText().toString()));

                        System.out.println("Length of mList = "+mList.length);

                        Log.i("checking int conversion", Integer.parseInt(day.getText().toString())+" "+Integer.parseInt(month.getText().toString())+" "+Integer.parseInt(year.getText().toString())+" "+Integer.parseInt(range.getText().toString()));

                        //Log.i(getClass().toString(), "Query Successful"+mList[1].getmDayOfWeek());

                        Intent i = new Intent(MainActivity.this, ListActivity.class);

                        //DailyCash[] list2 = new DailyCash[100];

                        Bundle mBundle = new Bundle();
                        mBundle.putParcelableArray("MyList", mList);
                        i.putExtras(mBundle);

                        startActivity(i);

                    }catch (RemoteException e) {
                        Log.i(getClass().toString(), "Remote Exception");
                    }
                }
                else {
                    Log.i(getClass().toString(), "Service Not Bound");
                }
            }
        });



    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mBalanceService = Balance.Stub.asInterface(iservice);

            if(mBalanceService == null) {
                Log.i(getClass().toString(), "null");
            }

            Log.i(getClass().toString(), "isBound true");

            isBound = true;

        }

        public void onServiceDisconnected(ComponentName className) {

            mBalanceService = null;

            isBound = false;

        }
    };

    // Bind to KeyGenerator Service
    @Override
    protected void onResume() {

        super.onResume();
        //isBound = false;
        System.out.println("In on resume");

        query.setEnabled(false);
        if (!isBound) {

            boolean b = false;
            Intent i = new Intent(Balance.class.getName());

            // UB:  Stoooopid Android API-20 no longer supports implicit intents
            // to bind to a service #@%^!@..&**!@
            // Must make intent explicit or lower target API level to 19.
            ResolveInfo info = getPackageManager().resolveService(i, 0);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(getClass().toString(), "Ugo says bindService() succeeded!");
            } else {
                Log.i(getClass().toString(), "Ugo says bindService() failed!");
            }

        }
    }

    // Unbind from KeyGenerator Service
    @Override
    protected void onPause() {

            //System.out.println("Reached here"+isBound);
            if (isBound) {
                unbindService(this.mConnection);
                //Set isBound to false
                isBound = false;
            }

        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
