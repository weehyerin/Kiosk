package com.example.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firestore.v1.StructuredQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {
    ListView list;
    ListAdapter adapter;
    ArrayList<HashMap<String,String>> menulist = new ArrayList<HashMap<String,String>>();

    private static final String TAG_MENU = "menu";
    private static final String TAG_PRICE ="price";
    private static final String TAG_DETAILS ="details";
    String Name;
    String Category;
    String result = "";
    String dayoff;
    String address;
    String service = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        TextView store_title = (TextView) findViewById(R.id.title_store);
        TextView category = (TextView) findViewById(R.id.seperate);
        TextView llocation = (TextView) findViewById(R.id.llocation);
        TextView time = (TextView) findViewById(R.id.time);
        TextView off = (TextView) findViewById(R.id.off);
        TextView serviceTextview = (TextView) findViewById(R.id.service);

        list = (ListView) findViewById(R.id.listView);


        try {

            SQLiteDatabase ReadDB_owner = this.openOrCreateDatabase("Owner", MODE_PRIVATE, null);
            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c_owner_store = ReadDB_owner.rawQuery("SELECT sName, category FROM " + "Store", null);
            Cursor c_owner_Address = ReadDB_owner.rawQuery("SELECT address FROM " + "Address", null);
            Cursor c_owner_Time = ReadDB_owner.rawQuery("SELECT * FROM " + "StoreInfo", null);
            Cursor c_owner_Service = ReadDB_owner.rawQuery("SELECT * FROM " + "CVS", null);
            //제공서비스
            if (c_owner_store != null) {
                if (c_owner_store.moveToFirst()) {
                    //테이블에서 두개의 컬럼값을 가져와서
                    Name = c_owner_store.getString(c_owner_store.getColumnIndex("sName"));
                    Category = c_owner_store.getString(c_owner_store.getColumnIndex("category"));
                }
            }
            if (c_owner_Time != null) {
                if (c_owner_Time.moveToFirst()) {
                    //테이블에서 두개의 컬럼값을 가져와서
                    String openH = c_owner_Time.getString(c_owner_Time.getColumnIndex("tOpenHour"));
                    String openM = c_owner_Time.getString(c_owner_Time.getColumnIndex("tOpenMinute"));
                    String closeH = c_owner_Time.getString(c_owner_Time.getColumnIndex("tCloseHour"));
                    String closeM = c_owner_Time.getString(c_owner_Time.getColumnIndex("tCloseMinute"));
                    result = " " + openH + " : " + openM + " ~ " + closeH + " : " + closeM;
                    dayoff = c_owner_Time.getString(c_owner_Time.getColumnIndex("dayoff"));




                }
            }
            if (c_owner_Address != null) {
                if (c_owner_Address.moveToFirst()) {
                    //테이블에서 두개의 컬럼값을 가져와서
                    address = c_owner_Address.getString(c_owner_Address.getColumnIndex("address"));


                }
            }
            if (c_owner_Service != null) {
                if (c_owner_Service.moveToFirst()) {
                    //테이블에서 두개의 컬럼값을 가져와서
                    //elevator, bMenu, bToilet, bStair, bServing

                    String elevator = c_owner_Service.getString(c_owner_Service.getColumnIndex("elevator"));
                    String bMenu = c_owner_Service.getString(c_owner_Service.getColumnIndex("bMenu"));
                    String bToilet = c_owner_Service.getString(c_owner_Service.getColumnIndex("bToilet"));
                    String bStair = c_owner_Service.getString(c_owner_Service.getColumnIndex("bStair"));
                    String bServing = c_owner_Service.getString(c_owner_Service.getColumnIndex("bServing"));

                    if(elevator.equals("YES")){
                        service = service + " " + "엘리베이터";
                        Log.d("CSV", "ele");
                    }
                    if(bMenu.equals("YES")){
                        service = service + " " + "점자메뉴판";
                        Log.d("CSV", "menu");
                    }
                    if(bToilet.equals("YES")){
                        service = service + " " + "장애인 화장실";
                        Log.d("CSV", "toilet");
                    }
                    if(bStair.equals("YES")){
                        service = service + " " + "계단";
                        Log.d("CSV", "stairs");
                    }
                    if(bServing.equals("YES")){
                        service = service + " " + "서빙";
                        Log.d("CSV", "serving");
                    }



                }
            }

            ReadDB_owner.close();

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());
        }

        store_title.setText(Name);
        category.setText(Category);
        time.setText(result);
        off.setText(dayoff);
        llocation.setText(address);
        serviceTextview.setText(service);

        Button order = (Button)findViewById(R.id.button3);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Order_Details.class);

                startActivity(intent);
            }
        });

        showList();

    }

    protected void showList(){


        try {

            SQLiteDatabase ReadDB = this.openOrCreateDatabase("Owner", MODE_PRIVATE, null);


            //SELECT문을 사용하여 테이블에 있는 데이터를 가져옵니다..
            Cursor c = ReadDB.rawQuery("SELECT * FROM " + "MenuDetail", null);

            if (c != null) {


                if (c.moveToFirst()) {
                    do {

                        //테이블에서 두개의 컬럼값을 가져와서
                        String menu = c.getString(c.getColumnIndex("mName"));
                        String price = c.getString(c.getColumnIndex("price"));
                        String origin = c.getString(c.getColumnIndex("origin"));
                        String detail = c.getString(c.getColumnIndex("details"));


                        //HashMap에 넣습니다.
                        HashMap<String,String> MENU = new HashMap<String,String>();

                        MENU.put("menu",menu);
                        MENU.put("price",price);
                        MENU.put("origin", origin);
                        MENU.put("detail",detail);
//                        Log.d("이놈", menuList.);

                        //ArrayList에 추가합니다..
                        menulist.add(MENU);

                    } while (c.moveToNext());
                }
            }

            ReadDB.close();


            //새로운 apapter를 생성하여 데이터를 넣은 후..
            adapter = new SimpleAdapter(
                    this, menulist, R.layout.list_item,
                    new String[]{"menu","price", "origin", "detail"},
                    new int[]{ R.id.menu, R.id.price, R.id.origin, R.id.detail}
            );


            //화면에 보여주기 위해 Listview에 연결합니다.
            list.setAdapter(adapter);


        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("",  se.getMessage());
        }



    }

}
