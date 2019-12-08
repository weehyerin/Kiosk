package com.example.order_app_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StoreListActivity extends AppCompatActivity {
    private SQLiteDatabase clientDB;

    public static final String PREFS_FACILITY = "MyFacilityPrefs";
    String hasBrailleToilet ;
    String hasBrailleStair  ;
    String hasBrailleMenu   ;
    String hasElevator      ;
    String hasServing       ;

    public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
        private ArrayList<String> sID = null;
        private ArrayList<String> mStore = null;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView listitem_store_list;

            ViewHolder(View itemView) {
                super(itemView);
                listitem_store_list = itemView.findViewById(R.id.listitem_store_list);
            }
        }

        StoreListAdapter(ArrayList<String> list, ArrayList<String> list2) {
            mStore = list;
            sID = list2;
        }

        @NonNull
        @Override
        public StoreListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.listitem_store_list, viewGroup, false);
            StoreListAdapter.ViewHolder vh = new StoreListAdapter.ViewHolder(view);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
            String text = mStore.get(i);
            viewHolder.listitem_store_list.setText(text);

            viewHolder.listitem_store_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MenuListActivity.class);
                    intent.putExtra("Selected Store", sID.get(i));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mStore.size();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);

        Intent intent = getIntent();
        String before_activity = intent.getExtras().getString("BEFORE ACTIVITY");

        String searched_store_name;
        String selected_category;

        searched_store_name = intent.getExtras().getString("Searched Store Name");
        selected_category = intent.getExtras().getString("Selected Category");

        clientDB = openOrCreateDatabase("clientDB.db",MODE_PRIVATE, null);

        final SharedPreferences user_facility = getSharedPreferences(PREFS_FACILITY, 0);
        hasBrailleToilet    = user_facility.getString("hasBrailleToilet", "NO");
        hasBrailleStair     = user_facility.getString("hasBrailleStair", "NO");
        hasBrailleMenu      = user_facility.getString("hasBrailleMenu", "NO");
        hasElevator         = user_facility.getString("hasElevator", "NO");
        hasServing          = user_facility.getString("hasServing", "NO");

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<String> storeID = new ArrayList<String>();

        if (before_activity.equals("StoreCategoryActivity")) {
        // 수정되어야 할 쿼리
        /*Cursor cursor = clientDB.rawQuery("SELECT sID, sName FROM Store NATURAL JOIN CVS WHERE elevator = '"+hasElevator+"' and" +
                "bMenu = '"+hasBrailleMenu+"' and bToilet = '"+hasBrailleToilet+"' and bStair = '"+hasBrailleStair+"' and" +
                "bServing = '"+hasServing+"' and category = '"+selected_category+"' ", null);*/
        clientDB.execSQL("DROP VIEW IF EXISTS first_floor");
        clientDB.execSQL("DROP VIEW IF EXISTS StoreOfSelecedCategory");
        clientDB.execSQL("DROP VIEW IF EXISTS CVSUserSelected");
        clientDB.execSQL("DROP VIEW IF EXISTS CVSElevatorAndStair");

        clientDB.execSQL("CREATE TABLE IF NOT EXISTS Store(sID String PRIMARY KEY NOT NULL, sName String NOT NULL," +
                "category String NOT NULL, contact String NOT NULL);");
        clientDB.execSQL("CREATE TABLE IF NOT EXISTS Address(addressID INTEGER PRIMARY KEY NOT NULL," +
                    "address String NOT NULL, floor INTEGER NOT NULL, latitude DOUBLE NOT NULL, longitude DOUBLE NOT NULL);");
        clientDB.execSQL("CREATE TABLE IF NOT EXISTS Located(sID String NOT NULL, addressID INTEGER NOT NULL, PRIMARY KEY(sID, addressID)," +
                "FOREIGN KEY(sID) REFERENCES Store(sID), FOREIGN KEY(addressID) REFERENCES Address(addressID));");
        clientDB.execSQL("CREATE TABLE IF NOT EXISTS StoreInfo(sID String PRIMARY KEY NOT NULL, dayOff String," +
                "openHour INTEGER NOT NULL, openMin INTEGER NOT NULL, closeHour INTEGER NOT NULL," +
                "closeMin INTEGER NOT NULL, FOREIGN KEY(sID) REFERENCES Store(sID));");
        clientDB.execSQL("CREATE TABLE IF NOT EXISTS CVS(sID String PRIMARY KEY NOT NULL, elevator String," +
                "bMenu String, bToilet String, bStair String, bServing String, FOREIGN KEY(sID) REFERENCES Store(sID));");
        clientDB.execSQL("CREATE TABLE IF NOT EXISTS Menu(sID String PRIMARY KEY NOT NULL, mName String NOT NULL," +
                "price INTEGER NOT NULL, origin String, FOREIGN KEY(sID) REFERENCES Store(sID));");

        clientDB.execSQL("CREATE VIEW CVSElevatorAndStair AS SELECT sID FROM CVS WHERE elevator = '"+hasElevator+"'" +
                "OR bStair = '"+hasBrailleStair+"' UNION SELECT sID FROM Located NATURAL JOIN Address WHERE Address.floor = 1");
        clientDB.execSQL("CREATE VIEW CVSUserSelected AS SELECT sID FROM CVSElevatorAndStair NATURAL JOIN CVS " +
                "WHERE bMenu = '"+hasBrailleMenu+"' AND bToilet = '"+hasBrailleToilet+"' AND bServing = '"+hasServing+"'");
        clientDB.execSQL("CREATE VIEW StoreOfSelecedCategory AS SELECT sID, sName " +
                "FROM Store NATURAL JOIN CVSUserSelected WHERE Store.category = '"+selected_category+"'");

        Cursor cursor = clientDB.rawQuery("SELECT sID, sName FROM StoreOfSelecedCategory", null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            while(cursor.moveToNext()) {
                String store_id = cursor.getString(cursor.getColumnIndex("sID"));
                String store_name = cursor.getString(cursor.getColumnIndex("sName"));

                storeID.add(store_id);
                list.add(store_name);
            }
        }
        cursor.close();
        clientDB.execSQL("DROP VIEW IF EXISTS first_floor");
        clientDB.execSQL("DROP VIEW IF EXISTS StoreOfSelecedCategory");
        clientDB.execSQL("DROP VIEW IF EXISTS CVSUserSelected");
        clientDB.execSQL("DROP VIEW IF EXISTS CVSElevatorAndStair");
        }
        else { // if(before_activity.equals("StoreSearchActivity"))
            //...
        }

        clientDB.close();

        RecyclerView list_store_list = findViewById(R.id.list_store_list);
        list_store_list.setLayoutManager(new LinearLayoutManager(this));

        StoreListAdapter adapter = new StoreListAdapter(list, storeID);
        list_store_list.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
