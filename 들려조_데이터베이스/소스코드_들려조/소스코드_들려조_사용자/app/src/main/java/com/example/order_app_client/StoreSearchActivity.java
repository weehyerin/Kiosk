package com.example.order_app_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StoreSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_search);

        final EditText text_searched_name = findViewById(R.id.text_searched_name);

        Button btn_search_cancel    = findViewById(R.id.btn_search_cancel);
        Button btn_search_confirm   = findViewById(R.id.btn_search_confirm);

        btn_search_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_search_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_searched_name.getText().toString().length() > 1) {
                    Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);
                    intent.putExtra("BEFORE ACTIVITY", "StoreSearchActivity");
                    intent.putExtra("Searched Store Name", text_searched_name.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "가게 이름을 입력하세요",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
