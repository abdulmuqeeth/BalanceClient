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

import java.util.List;

import abdulmuqeeth.uic.com.balancecommon.Balance;
import abdulmuqeeth.uic.com.balancecommon.DailyCash;

public class MainActivity extends AppCompatActivity {

    private Balance mBalanceService;
    private Boolean isBound = false;
    private List<DailyCash> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button create = (Button) findViewById(R.id.create_db);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(isBound){
                        try{
                            mBalanceService.createDatabase();
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

        final Button query = (Button) findViewById(R.id.query_db);

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound){
                    try{

                        mList = mBalanceService.dailyCash(1,1,1,1);
                        Log.i(getClass().toString(), "Query Successful"+mList.get(0).getmCash());
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
        final EditText range = (EditText) findViewById(R.id.range);
        final EditText year = (EditText) findViewById(R.id.year);



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

        if (isBound) {
            unbindService(this.mConnection);
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
