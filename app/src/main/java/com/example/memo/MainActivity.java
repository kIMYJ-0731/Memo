package com.example.memo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper dbHelper;

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    Button btnAdd;

    List<Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new SQLiteHelper(MainActivity.this);
        memoList = dbHelper.selectAll();

        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter(memoList);
        recyclerView.setAdapter(recyclerAdapter);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //새로운 메모 작성
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,0);
            }
        });


        dbHelper = new SQLiteHelper(MainActivity.this);
        memoList = dbHelper.selectAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            String strMain = data.getStringExtra("main");
            String strSub = data.getStringExtra("sub");

            Memo memo = new Memo(strMain,strSub,0);
            recyclerAdapter.addItem(memo);
            recyclerAdapter.notifyDataSetChanged();

            dbHelper.insertMemo(memo);

        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{
        private List<Memo> listdata;

        public RecyclerAdapter(List<Memo> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Memo memo = listdata.get(position);

            holder.maintext.setText(memo.getMaintext());
            holder.subtext.setText(memo.getSubtext());

            if(memo.getIsdone()==0){
                holder.img.setBackgroundColor(Color.LTGRAY);
            }
            else {
                holder.img.setBackgroundColor(Color.GREEN);
            }
        }

        void addItem(Memo memo){
            listdata.add(memo);
        }

        void removeItem(int position){
            listdata.remove(position);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView maintext;
            private TextView subtext;
            private ImageView img;

            public ItemViewHolder(@NonNull final View itemview){
                super(itemview);

                maintext = itemview.findViewById(R.id.item_maintext);
                subtext = itemview.findViewById(R.id.item_maintext);
                img = itemview.findViewById(R.id.item_image);
                }
            }

        }
    }
