package abdulmuqeeth.uic.com.balanceclient;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import abdulmuqeeth.uic.com.balancecommon.DailyCash;

public class ListActivity extends AppCompatActivity {

    private ListView mList;
    private ArrayAdapter mAdapter;
    private List<String> itemList = new ArrayList<String>();

    //Intent mIntent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();

        Parcelable[] pList =  extras.getParcelableArray("MyList");

        DailyCash [] dList = new DailyCash[pList.length];

        int i=0;
        for(Parcelable parcel : pList){
                dList[i] = (DailyCash) parcel;
                i++;
        }

        System.out.println("Length of pList= "+pList.length);

        for(int j=0; j< pList.length; j++){

            String s = dList[j].getmMonth()+"/"+dList[j].getmDay()+"/"+dList[j].getmYear()+"  Closing Balance : "+dList[j].getmCash();
            System.out.println("Here "+s);
            itemList.add(s);
        }


        mList = (ListView) findViewById(R.id.main_list);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);

        mList.setAdapter(mAdapter);


    }
}
