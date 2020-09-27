package com.example.todolist.addEdit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.room.MyDatabase;
import com.example.todolist.room.TodoItem;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddEditActivity extends AppCompatActivity {
    private TextInputLayout til_title, til_sDate, til_dDate, til_memo;
    private ImageButton ib_sDate, ib_dDate;
    private final int START_DATE = 0; //start date랑 due date 버튼을 클릭할 때 각각 달력이 나오는데, 그 둘을 구분하기 위함.
    private final int DUE_DATE = 1;
    private int mode; //add인지 edit인지 구분
    //edit
    private int id;
    private TodoItem selectItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        //과제 : mode를 가져오기
        mode = getIntent().getExtras().getInt("mode");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            if (mode == 0) {
                actionBar.setTitle("Add Todo");
            } else if (mode == 1) {
                actionBar.setTitle("Edit Todo");
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        til_title = findViewById(R.id.addEdit_til_title);
        til_sDate = findViewById(R.id.addEdit_til_start);
        til_dDate = findViewById(R.id.addEdit_til_due);
        til_memo = findViewById(R.id.addEdit_til_memo);

        ib_sDate = findViewById(R.id.addEdit_ibtn_start);
        ib_dDate = findViewById(R.id.addEdit_ibtn_due);

        if(mode == 1) {
            //TODO: edit

            //과제 : id 가져오기, default value = -1
            id = getIntent().getIntExtra("id", -1);

            if(id == -1) { //id 잘못 가져온 경우
                Log.d("todo_id", "item id wrong");
                Toast.makeText(AddEditActivity.this, "item id wrong", Toast.LENGTH_SHORT).show();
            } else { //잘 가져온 경우
                MyDatabase myDatabase = MyDatabase.getInstance(AddEditActivity.this);
                try {
                    selectItem = myDatabase.todoDao().getTodo(getIntent().getIntExtra("id", -1));
                    //getTodo : id를 받아서 찾아오는 역할을 하는 함수, id는 고쳐주기.
                } catch (Exception e) {
                    e.printStackTrace();
                }

                til_title.getEditText().setText(selectItem.getTitle());
                til_sDate.getEditText().setText(selectItem.getStart());
                til_dDate.getEditText().setText(selectItem.getDue());
                til_memo.getEditText().setText(selectItem.getMemo());
            }
        }


        //ibtn

        ib_sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(START_DATE);
            }
        });
        ib_dDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(DUE_DATE);
            }
        });


    }

    public void showCalender(final int mode) {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDate = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(AddEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) { //원래는 i, i1, i2로 적혀있었음.
                //달, 일은 한 자리 수일 때, 앞에 0을 붙여줘야 하기 때문에 고쳐줘야 함
                String s_month = new Integer(month+1).toString();
                String s_date = new Integer(dayOfMonth).toString();
                if(month+1 < 10)
                    s_month = "0" + new Integer(month+1).toString(); // month는 0부터 시작하기 때문에 +1을 해줘야 함.
                if(dayOfMonth < 10)
                    s_date = "0" + new Integer(dayOfMonth).toString();

                String date = year + " / " + s_month +" / " + s_date; // 이 형식 꼭 기억해두기!
                if(mode == START_DATE) {
                    til_sDate.getEditText().setText(date);
                }else if(mode == DUE_DATE) {
                    til_dDate.getEditText().setText(date);
                }
            }
        }, mYear, mMonth, mDate).show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home :
                finish();
                break;
            case R.id.save_todo :
                String title = til_title.getEditText().getText().toString();
                String sDate = til_sDate.getEditText().getText().toString();
                String dDate = til_dDate.getEditText().getText().toString();
                String memo = til_memo.getEditText().getText().toString();

                if(title.equals(""))
                    til_title.setError("필수요소입니다.");
                else
                    til_title.setError(null);
                if(sDate.equals(""))
                    til_sDate.setError("필수요소입니다.");
                else
                    til_sDate.setError(null);
                if(dDate.equals(""))
                    til_dDate.setError("필수요소입니다.");
                else
                    til_dDate.setError(null);

                if(!title.equals("") && ! sDate.equals("") && ! dDate.equals("")) {
                    if (sDate.compareTo(dDate) > 0) {
                        til_sDate.setError("시작 날짜가 더 느립니다.");
                        til_dDate.setError("끝나는 날짜가 더 빠릅니다.");
                    } else{
                        MyDatabase myDatabase = MyDatabase.getInstance(AddEditActivity.this);

                        if(mode == 0){
                            //todo추가
                            TodoItem todoItem = new TodoItem(title, sDate, dDate, memo);
                            myDatabase.todoDao().insertTodo(todoItem);
                        } else if(mode == 1) {
                            //todo수정
                            selectItem.setTitle(title);
                            selectItem.setStart(sDate);
                            selectItem.setDue(dDate);
                            selectItem.setMemo(memo);

                            myDatabase.todoDao().editTodo(selectItem);
                            Toast.makeText(AddEditActivity.this, "저장성공", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }

        }
        return super.onOptionsItemSelected(item);
    }
}