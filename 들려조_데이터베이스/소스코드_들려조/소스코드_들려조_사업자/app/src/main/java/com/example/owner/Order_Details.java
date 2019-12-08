package com.example.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order_Details extends AppCompatActivity {

    DatabaseReference Database;
    //데이터 베이스
    FirebaseDatabase database;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    //데이터베이스의 정보
    DatabaseReference ref;

    //정보 담을 객체
    List<User> userList = new ArrayList<>();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<Object>();

    ArrayList<HashMap<String,String>> menulist = new ArrayList<HashMap<String,String>>();


    User user;
    TextView test;
    String separate;
    private static ArrayList<String> mArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__details);

        listView = (ListView) findViewById(R.id.order_list);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        listView.setAdapter(adapter);
        final HashMap result = new HashMap<>();


        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("users/");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
//                Object user = dataSnapshot.getValue(Object.class);
//                test.setText(user.toString());
//                separate = user.toString();
//                Log.d("sep", separate);
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {

                    // child 내에 있는 데이터만큼 반복합니다.
                    Object user = messageData.getValue(Object.class);
                    //test.setText(user.toString());
                    Array.add(user.toString());
                    adapter.add(user.toString());

                }
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

//        String[] array = separate.split(" = ");
//
////출력
//        for(int i=0;i<array.length;i++) {
//            Log.d("sep", array[i]);
//        }




//        // 4. 리스트뷰에 목록 세팅
//        //새로운 apapter를 생성하여 데이터를 넣은 후..
//        adapter = new SimpleAdapter(
//                this, menulist, R.layout.order_list_firebase,
//                new String[]{"menu"},
//                new int[]{R.id.person_menu}
//        );
//
//
//        //화면에 보여주기 위해 Listview에 연결합니다.
//        listView.setAdapter(adapter);

    }


}
