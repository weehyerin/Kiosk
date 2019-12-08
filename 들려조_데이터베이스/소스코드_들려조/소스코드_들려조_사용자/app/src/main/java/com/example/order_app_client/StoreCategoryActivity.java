package com.example.order_app_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StoreCategoryActivity extends AppCompatActivity {
    public class StoreCategoryAdapter extends RecyclerView.Adapter<StoreCategoryAdapter.ViewHolder> {
        private ArrayList<String> mStore = null;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView listitem_store_category;

            ViewHolder(View itemView) {
                super(itemView);
                listitem_store_category = itemView.findViewById(R.id.listitem_store_category);
            }
        }

        StoreCategoryAdapter(ArrayList<String> list) {
            mStore = list;
        }

        @NonNull
        @Override
        public StoreCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.listitem_store_category, viewGroup, false);
            StoreCategoryAdapter.ViewHolder vh = new StoreCategoryAdapter.ViewHolder(view);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
            String text = mStore.get(i);
            viewHolder.listitem_store_category.setText(text);

            viewHolder.listitem_store_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);
                    intent.putExtra("BEFORE ACTIVITY", "StoreCategoryActivity");
                    intent.putExtra("Selected Category", viewHolder.listitem_store_category.getText().toString());
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
        setContentView(R.layout.activity_store_category);

        ArrayList<String> list = new ArrayList<String>();
        list.add("한식");
        list.add("중식");
        list.add("일식");
        list.add("양식");
        list.add("패스트푸드");
        list.add("디저트");

        RecyclerView list_store_category = findViewById(R.id.list_store_category);
        list_store_category.setLayoutManager(new LinearLayoutManager(this));

        StoreCategoryAdapter adapter = new StoreCategoryAdapter(list);
        list_store_category.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
