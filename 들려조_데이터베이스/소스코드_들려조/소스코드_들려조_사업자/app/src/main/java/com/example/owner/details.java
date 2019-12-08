package com.example.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class details extends AppCompatActivity {
// 사업자 번호로 연결되어 있음.
    String from;
    DatabaseReference mDatabase;
    SQLiteDatabase sampleDB = null;
    int owner_integer;
    String owner_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_fragment_details);

        Intent intent = getIntent(); /*데이터 수신*/

        //owner_integer = intent.getExtras().getInt("owner_number"); /*String형*/
        owner_string = intent.getExtras().getString("owner_number"); /*String형*/
        owner_integer = Integer.parseInt(owner_string);

        final EditText menu_name = (EditText) findViewById(R.id.menu_name); // 메뉴이름
        final EditText price = (EditText) findViewById(R.id.price); // 가격
        final EditText from = (EditText)findViewById(R.id.from_menu); //원산지
        final EditText details = (EditText)findViewById(R.id.details); // 상세설명
        Button add = (Button)findViewById(R.id.add);
        Button save = (Button)findViewById(R.id.savebtn);


        //온클릭리스너
        try {

            sampleDB = this.openOrCreateDatabase("Owner", MODE_PRIVATE, null);

            //테이블이 존재하지 않으면 새로 생성합니다.
            sampleDB.execSQL("CREATE TABLE IF NOT EXISTS MenuDetail(mName String, price INTEGER, origin String, details String);");
            sampleDB.execSQL(
                    "CREATE TRIGGER if not exists N1   " +
                    "   AFTER INSERT   " +
                    " ON[MenuDetail]  " +
                    "  for each row  " +
                    " WHEN New.price > 100000 and NEW.price < 0" +
                    "    BEGIN  " +
                    "        select raise(ignore);" +
                    "    END;  ");

            Log.d("good", "ok");

        } catch (SQLiteException se) {
            Toast.makeText(getApplicationContext(),  se.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("error", se.getMessage());


        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // realtime database
                //edittext에 저장된 텍스트 Strig에 저장

                String get_menu_name = menu_name.getText().toString(); // 메뉴이름
                Integer get_price = Integer.parseInt( price.getText().toString() ); //가격
                String get_details = details.getText().toString(); // 세부 설명
                String get_origin = from.getText().toString();



                sampleDB.execSQL("INSERT INTO " + "MenuDetail"
                        + " (mName, price, origin, details)  Values ('" + get_menu_name+"', '" + get_price+"','"+ get_origin +"','"+ get_details +"');");

                sampleDB.close();
                Toast.makeText(getApplicationContext(), "입력 완료 되었습니다.", Toast.LENGTH_LONG).show();



                Intent intent = new Intent(getApplicationContext(),details.class);

                intent.putExtra("owner_number", owner_string); /*송신*/


                startActivity(intent);


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // realtime database
                //edittext에 저장된 텍스트 Strig에 저장

                //sampleDB.execSQL("INSERT INTO menuDetail(sID, mName, price, origin, details) Values ('" + "12345" + "', '" + "name" + "', '" + "12345" +"', '" + "origin"+ "', '" + "details" +"');");



                String get_menu_name = menu_name.getText().toString(); // 메뉴이름
                Integer get_price = Integer.parseInt( price.getText().toString() ); //가격
                String get_details = details.getText().toString(); // 세부 설명
                String get_origin = from.getText().toString();
                Log.d("test1", get_menu_name + get_origin + get_details + owner_integer + get_price);

//                //새로운 데이터를 테이블에 집어넣습니다..
                sampleDB.execSQL("INSERT INTO " + "MenuDetail"
                        + " (mName, price, origin, details)  Values ('" + get_menu_name+"', '" + get_price+"','"+ get_origin +"','"+ get_details +"');");



                sampleDB.close();

                Toast.makeText(getApplicationContext(), "입력 완료 되었습니다.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);

                startActivity(intent);
            }
        });



    }
}
