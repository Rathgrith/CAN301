package com.example.can301;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
public class ItemExchangeActivity extends AppCompatActivity {
    RecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemexchange);
        init();
        getData();
    }

    private void init(){

        RecyclerView recyclerView = findViewById(R.id.recyclerView) ;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)) ;
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void getData(){
        DataItem data = new DataItem(R.drawable.xjtlu_bear, "XJTLU Bear", 1000);
        adapter.addItem(data);
        data = new DataItem(R.drawable.xjtlu_bird, "XJTLU Bird", 1000);
        adapter.addItem(data);
        data = new DataItem(R.drawable.xjtlu_cap, "XJTLU Ball Cap", 2000);
        adapter.addItem(data);
        for (int i = 1; i <= 30; i++) {
            data = new DataItem(R.drawable.item_example, "Example", 1500);
            adapter.addItem(data);
        }

    }

}
