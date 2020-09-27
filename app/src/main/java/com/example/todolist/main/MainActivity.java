package com.example.todolist.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.todolist.R;
import com.example.todolist.addEdit.AddEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView main_rcv;
    private FloatingActionButton main_fab;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_rcv = findViewById(R.id.main_rcv);
        main_fab = findViewById(R.id.main_fab);

        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEditActivity.class);
                intent.putExtra("mode", 0);
                startActivity(intent);
                //숙제 : 플로팅 버튼을 클릭했을 때 다른 화면으로 넘어가기
                //힌트 : intent를 사용해서 activity 화면 바꾸기
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //menu에서 버튼을 클릭했을 때 어떻게 되는지
        switch (item.getItemId()){
            case R.id.menu_delete_all:
                //TODO:전체삭제
            case R.id.menu_delete_selected:
                //TODO:선택삭제
        }
        return super.onOptionsItemSelected(item);
    }
}