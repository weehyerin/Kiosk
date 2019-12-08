package com.example.order_app_client;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuListActivity extends AppCompatActivity {
    private SQLiteDatabase clientDB;

    public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {
        private ArrayList<String> mMenuName = null;
        private ArrayList<Integer> mPrice = null;
        private ArrayList<String> mOrigin = null;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView listitem_menu_name;
            TextView listitem_menu_price;
            TextView listitem_menu_origin;
            Button listitem_btn_add;

            ViewHolder(View itemView) {
                super(itemView);
                listitem_menu_name = itemView.findViewById(R.id.listitem_menu_name);
                listitem_menu_price = itemView.findViewById(R.id.listitem_menu_price);
                listitem_menu_origin = itemView.findViewById(R.id.listitem_menu_origin);
                listitem_btn_add = itemView.findViewById(R.id.listitem_btn_add);

                listitem_menu_origin.setVisibility(View.GONE);
            }
        }

        MenuListAdapter(ArrayList<String> list1, ArrayList<Integer> list2, ArrayList<String> list3) {
            mMenuName = list1;
            mPrice = list2;
            mOrigin = list3;
        }

        @NonNull
        @Override
        public MenuListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.listitem_menu_list, viewGroup, false);
            MenuListAdapter.ViewHolder vh = new MenuListAdapter.ViewHolder(view);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            final String menu_name    = mMenuName.get(i);
            final String menu_price   = Integer.toString(mPrice.get(i));
            final String menu_origin  = mOrigin.get(i);

            viewHolder.listitem_menu_name.setText(menu_name);
            viewHolder.listitem_menu_price.setText(menu_price);
            viewHolder.listitem_menu_origin.setText(menu_origin);

            viewHolder.listitem_menu_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.listitem_menu_origin.getVisibility() == View.GONE) {
                        viewHolder.listitem_menu_origin.setVisibility(View.VISIBLE);
                    }
                    else {
                        viewHolder.listitem_menu_origin.setVisibility(View.GONE);
                    }
                }
            });

            viewHolder.listitem_btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap result = new HashMap<>();
                    result.put("customer", "김민정");
                    result.put("ordered menu", menu_name);
                    result.put("payment", menu_price);

                    /*DatabaseReference mDatabase;
    FirebaseFirestore db = FirebaseFirestore.getInstance();*/

                    /*
                     *mDatabase = FirebaseDatabase.getInstance().getReference();
//                //firebase에 저장
//                mDatabase.child("article").push().setValue(result);
                     */

                    DatabaseReference mDB;
                    mDB = FirebaseDatabase.getInstance().getReference();
                    mDB.child("users").push().setValue(result);

                    Toast.makeText(getApplicationContext(), "선택하신 메뉴가 주문되었습니다", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMenuName.size();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        Intent intent = getIntent();
        String selected_sID = intent.getExtras().getString("Selected Store");
        Log.d("TAG", "sID = " + selected_sID);

        clientDB = openOrCreateDatabase("clientDB.db",MODE_PRIVATE, null);
        clientDB.execSQL("CREATE TABLE IF NOT EXISTS Menu(sID INTEGER NOT NULL, mName String NOT NULL," +
                "price INTEGER NOT NULL, origin String, PRIMARY KEY(sID, mName));");

        ArrayList<String> menu = new ArrayList<String>();
        ArrayList<Integer> price = new ArrayList<Integer>();
        ArrayList<String> origin = new ArrayList<String>();

        Cursor cursor = clientDB.rawQuery("SELECT mName, price, origin FROM Menu Where sID = '"+selected_sID+"'", null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            while (cursor.moveToNext()) {
                String m = cursor.getString(cursor.getColumnIndex("mName"));
                int p = cursor.getInt(cursor.getColumnIndex("price"));
                String o = cursor.getString(cursor.getColumnIndex("origin"));

                menu.add(m);
                price.add(p);
                origin.add(o);
            }
        }

        cursor.close();

        RecyclerView list_menu_list = findViewById(R.id.list_menu_list);
        list_menu_list.setLayoutManager(new LinearLayoutManager(this));

        MenuListAdapter adapter = new MenuListAdapter(menu, price, origin);
        list_menu_list.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
