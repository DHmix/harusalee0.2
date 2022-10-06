package com.chh.harusalee02;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DeleteActivity extends AppCompatActivity {

    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;


    Calendar ca = Calendar.getInstance();
    int x = ca.get(Calendar.DAY_OF_YEAR);
    int year = ca.get(Calendar.YEAR);
    int month = ca.get(Calendar.MONTH);
    int day = ca.get(Calendar.DAY_OF_MONTH);
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    int minute = ca.get(Calendar.MINUTE);
    int second = ca.get(Calendar.SECOND);

    TextView tvday,tvDelList;
    EditText edtxt_cate,edtxt_money,edtxt_delete_useday;
    Button btn_delete_save,btn_delete_back,btn_findday;
    String cate,money,useday,money2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_delete);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                DeleteActivity.this, // 현재 화면의 context
                "member2.db", // 파일명
                null, // 커서 팩토리
                11); // 버전 번호

        tvday = (TextView)findViewById(R.id.tvday);
        tvday.setText((month+1)+"월"+(day)+"일");


        tvDelList = (TextView) findViewById(R.id.tvDelList);
        edtxt_cate = (EditText)findViewById(R.id.edtxt_cate);
        edtxt_money = (EditText)findViewById(R.id.edtxt_money);
        edtxt_delete_useday = (EditText)findViewById(R.id.edtxt_delete_useday);

        findViewById(R.id.btn_findday).setOnClickListener(mClick);
        findViewById(R.id.btn_delete_save).setOnClickListener(mClick);
        findViewById(R.id.btn_delete_back).setOnClickListener(mClick);

        selectAll();
    }

    View.OnClickListener mClick = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_findday:
                    if (edtxt_delete_useday.getText().toString().equals("")){
                        Toast.makeText(DeleteActivity.this, "삭제할 날짜를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String delFindId = edtxt_delete_useday.getText().toString();
                    selectDel(delFindId);
                    break;
                case R.id.btn_delete_save:
                    if (edtxt_delete_useday.getText().toString().equals("")){
                        Toast.makeText(DeleteActivity.this, "삭제할 날짜를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(DeleteActivity.this, "삭제완료",  Toast.LENGTH_SHORT).show();
                    String delId = edtxt_delete_useday.getText().toString();
                    delete(delId);
                    selectAll();
                    break;



                case R.id.btn_delete_back:
                    selectAll();
                    finish();
                    Intent act3 = new Intent(DeleteActivity.this, MainActivity.class);
                    startActivity(act3);
            }
        }
    };

    // delete
    public void delete(String delId) {
        db = helper.getWritableDatabase();
        db.delete("member", "id='"+delId+"'", null);
        db.close();

        //삭제후처리
        edtxt_delete_useday.setText("");
        tvDelList.setText("지출내역이 삭제되었습니다.");

    }

    // 삭제 아이디 찾기
    public void selectDel(String delId) {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member where id='"+delId+"'", null);

        String Result = "";
        boolean check=false;
        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            String name = c.getString(3);

            Result="날짜 불러오기완료\n";
            Result+= id+"\n"+pw+"\n"+name+"\n";
            check=true;
        }
        if(check==false)
        {
            tvDelList.setText("날짜를 확인해주세요");
        }else {
            tvDelList.setText(Result);
        }
        c.close();
        db.close();
    }




    public void selectAll() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member", null);
        String jasan = "";
        String Result = "";
        String DbDday = "";
        int totjicullresultmoneydb = 0;

        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            int name = c.getInt(3);

            Result += id+"   "+pw+"("+name+")"+"\n";
        }
        c.close();
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor cc = db.rawQuery("SELECT * FROM member2", null);
        while (cc.moveToNext()) {
            int idx = cc.getInt(0);
            int dbjasan = cc.getInt(1);
            jasan = dbjasan+" ";

        }
        cc.close();

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor ccc = db.rawQuery("SELECT * FROM member3", null);
        while (ccc.moveToNext()) {
            int idx = ccc.getInt(0);
            String dbday = ccc.getString(1);
            DbDday = dbday+" ";
        }
        ccc.close();




        db.close();
    }

//
}