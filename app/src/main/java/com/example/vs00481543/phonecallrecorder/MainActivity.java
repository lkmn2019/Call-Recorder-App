package com.example.vs00481543.phonecallrecorder;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler db=new DatabaseHandler(this);
    final static String TAGMA="Main Activity";
    RecordAdapter rAdapter;
    RecyclerView recycler;
    List<CallDetails> callDetailsList;
    boolean checkResume=false;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        pref.edit().putInt("numOfCalls",0).apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Check", "onResume: ");
        if(checkPermission()) {
            //Toast.makeText(getApplicationContext(), "Permission already granted", Toast.LENGTH_LONG).show();
            if(checkResume==false) {
                setUi();
                // this.callDetailsList=new DatabaseManager(this).getAllDetails();
                rAdapter.notifyDataSetChanged();
            }
        }
    }

    protected void onPause()
    {
        super.onPause();
        SharedPreferences pref3=PreferenceManager.getDefaultSharedPreferences(this);
        if(pref3.getBoolean("SonuShaikh",false)) {
            checkResume = true;
            pref3.edit().putBoolean("SonuShaikh",false).apply();
        }
        else
            checkResume=false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        // search view
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rAdapter.getFilter().filter(newText);
                return false;
            }
        });
//switch click
        MenuItem item=menu.findItem(R.id.mySwitch);

        View view = getLayoutInflater().inflate(R.layout.switch_layout,null,false) ;

        final SharedPreferences pref1= PreferenceManager.getDefaultSharedPreferences(this);

        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.switchCheck);
        switchCompat.setChecked(pref1.getBoolean("switchOn",true));
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.d("Switch", "onCheckedChanged: " +isChecked);
                    Toast.makeText(getApplicationContext(), "Call Recorder ON", Toast.LENGTH_LONG).show();
                    pref1.edit().putBoolean("switchOn",isChecked).apply();
                }else{
                    Log.d("Switch", "onCheckedChanged: " +isChecked);
                    Toast.makeText(getApplicationContext(), "Call Recorder OFF", Toast.LENGTH_LONG).show();
                    pref1.edit().putBoolean("switchOn",isChecked).apply();
                }
            }
        });
        item.setActionView(view);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_search){
            return true;
        }else if(id== R.id.mySwitch){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUi()
    {
        recycler=(RecyclerView) findViewById(R.id.recyclerView);
        callDetailsList=new DatabaseManager(this).getAllDetails();

        for(CallDetails cd:callDetailsList)
        {
            String log="Phone num : "+cd.getNum()+" | Time : "+cd.getTime1()+" | Date : "+cd.getDate1();
            Log.d("Database ", log);
        }

        Collections.reverse(callDetailsList);
        rAdapter=new RecordAdapter(callDetailsList,this);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(rAdapter);

    }



    private boolean checkPermission()
    {
        int i=0;
        String[] perm={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS};
        List<String> reqPerm=new ArrayList<>();

        for(String permis:perm) {
            int resultPhone = ContextCompat.checkSelfPermission(MainActivity.this,permis);
            if(resultPhone== PackageManager.PERMISSION_GRANTED)
                i++;
            else {
                reqPerm.add(permis);
            }
        }

        if(i==5)
            return true;
        else
            return requestPermission(reqPerm);
    }



    private boolean requestPermission(List<String> perm)
    {
        // String[] permissions={Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        String[] listReq=new String[perm.size()];
        listReq=perm.toArray(listReq);
        for(String permissions:listReq) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,permissions)) {
                Toast.makeText(getApplicationContext(), "Phone Permissions needed for " + permissions, Toast.LENGTH_LONG);
            }
        }

        ActivityCompat.requestPermissions(MainActivity.this, listReq, 1);


        return false;
    }


    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults)
    {
        switch(requestCode)
        {
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getApplicationContext(),"Permission Granted to access Phone calls",Toast.LENGTH_LONG);
                else
                    Toast.makeText(getApplicationContext(),"You can't access Phone calls",Toast.LENGTH_LONG);
                break;
        }

    }



}
