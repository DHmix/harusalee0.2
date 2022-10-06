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

public class JichullActivity extends AppCompatActivity {

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

    TextView tvday;
    EditText edtxt_cate,edtxt_money,edtxt_useday;
    Button btn_save,btn_back;
    String cate,money,useday,money2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_jichull);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                JichullActivity.this, // 현재 화면의 context
                "member2.db", // 파일명
                null, // 커서 팩토리
                11); // 버전 번호

        tvday = (TextView)findViewById(R.id.tvday);
        tvday.setText((month+1)+"월"+(day)+"일");

        edtxt_cate = (EditText)findViewById(R.id.edtxt_cate);
        edtxt_money = (EditText)findViewById(R.id.edtxt_money);
        edtxt_useday = (EditText)findViewById(R.id.edtxt_useday);

        findViewById(R.id.btn_save).setOnClickListener(mClick);
        findViewById(R.id.btn_back).setOnClickListener(mClick);
    }

    View.OnClickListener mClick = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    if (edtxt_useday.getText().toString().equals("")){
                        Toast.makeText(JichullActivity.this, "날짜를 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    useday = edtxt_useday.getText().toString();

                    if (edtxt_cate.getText().toString().equals("")){
                        Toast.makeText(JichullActivity.this, "항목을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    cate = edtxt_cate.getText().toString();

                    if (edtxt_money.getText().toString().equals("")){
                        Toast.makeText(JichullActivity.this, "금액을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    money = edtxt_money.getText().toString();
                    money2 = edtxt_money.getText().toString();

                    edtxt_useday.setText(useday);
                    edtxt_cate.setText(cate);
                    edtxt_money.setText(money);
                    Toast.makeText(JichullActivity.this, "지출항목이 추가됐습니다.",  Toast.LENGTH_SHORT).show();
                    edtxt_useday.setText("");
                    edtxt_cate.setText("");
                    edtxt_money.setText("");
                    insert(useday,cate,money);
                    insert2(0);
                    selectAll();
//                    update(cate,money);


                case R.id.btn_back:
                    selectAll();
                    finish();
                    Intent act3 = new Intent(JichullActivity.this, MainActivity.class);
                    startActivity(act3);
            }
        }
    };
    //데이타베이스 메서드 처리
    public void insert(String useday,String cate,String money) {

        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        //값들을 컨트롤 하려고 클래스 생성
        ContentValues values = new ContentValues();

        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("id", useday);
        values.put("pw", cate);
        values.put("name", money);
        db.insert("member", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
        db.close();

    }

    public void insert2(int money2) {

        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        //값들을 컨트롤 하려고 클래스 생성
        ContentValues values = new ContentValues();

        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("jicull_money", money2);
        db.insert("member4", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
        // tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
        db.close();

    }


    // 회원목록전체조회
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

//        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
//        Cursor cccc = db.rawQuery("SELECT * FROM member4", null);
//        while (cccc.moveToNext()) {
//            int idx = cccc.getInt(0);
//            int jicull_money = cccc.getInt(1);
//
//            totjicullresultmoneydb += jicull_money;
//        }
//        cccc.close();


//        totjicullresultmoney.setText(totjicullresultmoneydb+"");
//        reusltmoney.setText(Result);
//        now_jasan.setText(jasan);
//        tvnowDday.setText(DbDday);

//        tvnowDday.setText(DbDday);
//        nowDday.setText(DbDday);


        db.close();
    }

//
}