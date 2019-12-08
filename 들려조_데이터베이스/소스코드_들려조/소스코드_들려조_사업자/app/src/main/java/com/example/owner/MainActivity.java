package com.example.owner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.meta.When;


public class MainActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    double d1; // 위도
    double d2; // 경도
    String spin_var; // 한식
    String spin_var3; // 전화번호

    String bathroom = "NO";
    String stairs = "NO";
    String takeout = "NO";
    String menu = "NO";
    String elevator = "NO";

    int open_hour = 0;
    int open_minute = 0;
    int close_hour = 0;
    int close_minute = 0;
    int floor = 1;

    ArrayList<HashMap<String, String>> personList;



    SQLiteDatabase sampleDB = null;
    String lllocation;
    EditText floor_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // tm
        Spinner spinner1 = findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spin_var = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //주소검색 버튼 클릭
        Button location = (Button) findViewById(R.id.add_savebtn2);
        final Geocoder geocoder = new Geocoder(this);
        final EditText et1 = (EditText)findViewById(R.id.location1);
        final EditText et2 = (EditText)findViewById(R.id.location2);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Address> list = null;



                String str = et1.getText().toString();
                try {
                    list = geocoder.getFromLocationName(
                            str, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        et2.setText("해당되는 주소 정보는 없습니다");
                    } else {
                        //et2.setText(list.get(0).toString());
                        //          list.get(0).getCountryName();  // 국가명
                        //          list.get(0).getLatitude();        // 위도
                        //          list.get(0).getLongitude();    // 경도
                        List<Address> list2 = null;
                        try {
                            d1 = list.get(0).getLatitude();
                            d2 = list.get(0).getLongitude();

                            list2 = geocoder.getFromLocation(
                                    d1, // 위도
                                    d2, // 경도
                                    10); // 얻어올 값의 개수
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                        }
                        if (list2 != null) {
                            if (list.size()==0) {
                                et2.setText("해당되는 주소 정보는 없습니다");
                            } else {
                                et2.setText(list2.get(0).getAddressLine(0));
                                lllocation = list2.get(0).getAddressLine(0);
                            }
                        }
                    }
                }
            }
        });

        //전화번호
        Spinner spinner3 = findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spin_var3 = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //시간
        Spinner OPENHOUR = findViewById(R.id.open_hour);
        OPENHOUR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str_open_hour = adapterView.getItemAtPosition(i).toString();
                open_hour = Integer.parseInt(str_open_hour);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Spinner OPENMINUTE = findViewById(R.id.open_minute);
        OPENMINUTE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str_open_minute = adapterView.getItemAtPosition(i).toString();
                open_minute = Integer.parseInt(str_open_minute);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Spinner CLOSEHOUR = findViewById(R.id.close_hour);
        CLOSEHOUR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str_close_hour = adapterView.getItemAtPosition(i).toString();
                close_hour = Integer.parseInt(str_close_hour);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Spinner CLOSEMINUTE = findViewById(R.id.close_minute);
        CLOSEMINUTE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str_cloase_minute = adapterView.getItemAtPosition(i).toString();
                close_minute = Integer.parseInt(str_cloase_minute);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //토글 버튼들 사용

        final ToggleButton tb1 = (ToggleButton)findViewById(R.id.toggleButton);
        final ToggleButton tb2 = (ToggleButton)findViewById(R.id.toggleButton2);
        final ToggleButton tb3 = (ToggleButton)findViewById(R.id.toggleButton3);
        final ToggleButton tb4 = (ToggleButton)findViewById(R.id.toggleButton4);
        final ToggleButton tb5 = (ToggleButton)findViewById(R.id.toggleButton6);

        tb1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(tb1.isChecked()){
                    bathroom = "YES";
                    Log.d("push", "bath");
                }
            }
        });
        tb2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(tb2.isChecked()){
                    //선택됐을 때
                    stairs = "YES";
                    Log.d("push", "stairs");
                }
            }
        });
        tb3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(tb3.isChecked()){
                    //선택됐을 때
                    takeout = "YES";
                    Log.d("push", "takeout");

                }
            }
        });
        tb4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(tb4.isChecked()){
                    //선택됐을 때
                    menu = "YES";
                    Log.d("push", "menu");

                }
            }
        });
        tb5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(tb5.isChecked()){
                    //선택됐을 때
                    elevator = "YES";
                    Log.d("push", "elevator");

                }
            }
        });




        //아이디 정의
        Button add_savebtn = (Button) findViewById(R.id.add_savebtn);

        final EditText add_title = (EditText) findViewById(R.id.add_title); // 가게이름
        final EditText add_url = (EditText) findViewById(R.id.add_url); // 대표자명
        final EditText id = (EditText)findViewById(R.id.id_edit); // id
        final EditText passwd = (EditText)findViewById(R.id.passwd); // passwd
        final EditText owner_num = (EditText)findViewById(R.id.Phone); // 사업자 번호
        final EditText phone = (EditText)findViewById(R.id.id); // 전화번호
        final EditText dayoff = (EditText)findViewById(R.id.dayoff);
        floor_text = (EditText)findViewById(R.id.floor); // 층수

        //edittext에 저장된 텍스트 Strig에 저장


        final HashMap result = new HashMap<>();

        try {


            sampleDB = this.openOrCreateDatabase("Owner", MODE_PRIVATE, null);


            //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.


            //테이블이 존재하지 않으면 새로 생성합니다.
//            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + "BaseInformation"
//                    + " (name VARCHAR(20), seperate_large VARCHAR(20), seperate_small VARCHAR(20), location VARCHAR(100));");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS Store(sID INTEGER PRIMARY KEY NOT NULL, sName String NOT NULL," +
                    "category String NOT NULL, contact String NOT NULL);");

            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS StoreInfo(sID INTEGER PRIMARY KEY NOT NULL," +
                    "dayoff String, tOpenHour INTEGER, tOpenMinute INTEGER, tCloseHour INTEGER, tCloseMinute INTEGER)");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS CVS(sID INTEGER PRIMARY KEY NOT NULL, elevator String," +
                    "bMenu String, bToilet String, bStair String, bServing String);");
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS Address(addressID INTEGER PRIMARY KEY NOT NULL," +
                    "address String NOT NULL, floor INTEGER NOT NULL, latitude DOUBLE NOT NULL, longitude DOUBLE NOT NULL);");



            sampleDB.execSQL("DELETE FROM " + "Store"  );
            sampleDB.execSQL("DELETE FROM " + "StoreInfo"  );
            sampleDB.execSQL("DELETE FROM " + "CVS"  );
            sampleDB.execSQL("DELETE FROM " + "Address"  );
            sampleDB.execSQL("DELETE FROM " + "MenuDetail"  );




//            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS Menu(sID INTEGER PRIMARY KEY NOT NULL, mName String NOT NULL," +
//                    "subCategory String NOT NULL, price INTEGER NOT NULL, origin String);");



        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("", se.getMessage());


        }

        //온클릭리스너
        add_savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // realtime database
                final String get_title = add_title.getText().toString(); // 가게이름
                final String get_url = add_url.getText().toString(); //대표자명
                final String get_id = id.getText().toString(); // id
                final String get_passwd = id.getText().toString(); // passwd
                final String get_num = owner_num.getText().toString(); // 사업자 번호
                final String get_phone = phone.getText().toString();
                final String set_phone = spin_var3 + get_phone; // 전화번호
                String floor_text_string = floor_text.getText().toString();
                String dayoff_result = dayoff.getText().toString();

                floor = Integer.parseInt(floor_text_string);
                int get_num_int = Integer.parseInt(get_num);
//                //hashmap 만들기
//
//                result.put("title", get_title);
//                result.put("name", get_url);
//                result.put("id", get_id);
//                result.put("passwd", get_passwd);
//                result.put("number", get_num);
//                result.put("food1", spin_var); //한식
//                result.put("food2", spin_var2); //분류
//                result.put("Latitude", d1); //위도
//                result.put("Longitude", d2); //경도
//                result.put("Phone", set_phone);
//                result.put("bathroom",bathroom);
//                result.put("stairs",stairs);
//                result.put("takeout",takeout);
//                result.put("menu",menu);
//                result.put("elevator",elevator);
//
//                //firebase 정의
//                mDatabase = FirebaseDatabase.getInstance().getReference();
//                //firebase에 저장
//                mDatabase.child("article").push().setValue(result);
                //새로운 데이터를 테이블에 집어넣습니다..
                //테이블이 존재하는 경우 기존 데이터를 지우기 위해서 사용합니다.
                //sampleDB.execSQL("DELETE FROM " + "MenuDetail"  );
                sampleDB.execSQL("INSERT INTO " + "Store"
                        + " (sID, sName, category, contact)  Values ('" + get_num_int + "', '" + get_title+"', '" + spin_var+"', '" + set_phone+"');");
                Log.d("test1", get_num_int + get_title + spin_var + set_phone);

                sampleDB.execSQL("INSERT INTO " + "StoreInfo"
                        + " (sID, dayoff, tOpenHour, tOpenMinute, tCloseHour, tCloseMinute)  Values ('" + get_num_int + "', '" + dayoff_result + "', '" + open_hour+"', '" + open_minute+"', '"+ close_hour+"', '" + close_minute+"');");
                //Log.d("test2", get_num_int +  + open_minute + close_hour + close_minute);
                sampleDB.execSQL("INSERT INTO " + "CVS"
                        + " (sID, elevator, bMenu, bToilet, bStair, bServing)  Values ('" + get_num_int + "', '" + elevator+"', '" + menu+"', '" +bathroom+"', '"+ stairs+"', '" + takeout+"');");
                sampleDB.execSQL("INSERT INTO " + "Address"
                        + " (addressID, address, floor, latitude, longitude)  Values ('" + "20001" + "', '" + lllocation+"', '" + floor+"', '"+ d1+"', '" + d2+"');");
                Log.d("CVS", elevator + menu + bathroom + stairs + takeout);

                sampleDB.close();

                Toast.makeText(getApplicationContext(), "입력 완료 되었습니다.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),details.class);

                intent.putExtra("owner_number", get_num); /*송신*/


                startActivity(intent);

            }
        });




    }
}
