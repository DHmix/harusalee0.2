package com.chh.harusalee02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Calendar ca = Calendar.getInstance();
    int x = ca.get(Calendar.DAY_OF_YEAR);
    int year = ca.get(Calendar.YEAR);
    int month = ca.get(Calendar.MONTH);
    int day = ca.get(Calendar.DAY_OF_MONTH);
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    int minute = ca.get(Calendar.MINUTE);
    int second = ca.get(Calendar.SECOND);
    int cle=0;
    LinearLayout firstlayout, secondlayout;

    TextView tvday,reusltmoney,tvnowDday,now_jasan,totjicullresultmoney;

    DecimalFormat decimalFormat = new DecimalFormat("#,###");
    String testcommar = "";


    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                MainActivity.this, // 현재 화면의 context
                "member2.db", // 파일명
                null, // 커서 팩토리
                11); // 버전 번호

        findViewById(R.id.btn_jichull).setOnClickListener(mClick);
        findViewById(R.id.btn_jasan).setOnClickListener(mClick);
        findViewById(R.id.tvnowDday).setOnClickListener(mClick);
        findViewById(R.id.btn_delete).setOnClickListener(mClick);


        totjicullresultmoney= (TextView)findViewById(R.id.totjicullresultmoney);
        now_jasan= (TextView)findViewById(R.id.now_jasan);
        reusltmoney= (TextView)findViewById(R.id.reusltmoney);
        tvnowDday= (TextView)findViewById(R.id.tvnowDday);
        tvday = (TextView)findViewById(R.id.tvday);
        tvday.setText((month+1)+"월"+(day)+"일");


        selectAll();



    }
    View.OnClickListener mClick = new View.OnClickListener(){
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.btn_jichull:
                    selectAll();
                    Intent act1 = new Intent(MainActivity.this, JichullActivity.class);
                    startActivity(act1);
                    break;

                case R.id.tvnowDday:
                    selectAll();
                    Intent act2 = new Intent(MainActivity.this, DdayActivity.class);
                    startActivity(act2);
                    break;

                case R.id.btn_jasan:
                    selectAll();
                    Intent act3 = new Intent(MainActivity.this, JasanActivity.class);
                    startActivity(act3);
                    break;

                case R.id.btn_delete:
                    selectAll();
                    Intent act4 = new Intent(MainActivity.this, DeleteActivity.class);
                    startActivity(act4);
                    break;

            }
        }
    };

    public void selectAll() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.rawQuery("SELECT * FROM member order by id desc", null);
        int jasan = 0;
        String Result = "";
        String DbDday = "";
        int totjasan=0;
        int totmoney=0;
        int totjicullresultmoneydb = 0;


        while (c.moveToNext()) {
            int idx = c.getInt(0);
            String id = c.getString(1);
            String pw = c.getString(2);
            int name = c.getInt(3);

            totmoney += name;


            Result += id+"   "+pw+"("+name+")"+"\n";
            totjicullresultmoney.setText("-"+totmoney+"");
        }
        c.close();
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor cc = db.rawQuery("SELECT * FROM member2", null);
        while (cc.moveToNext()) {
            int idx = cc.getInt(0);
            int dbjasan = cc.getInt(1);
            jasan = dbjasan;

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
        totjasan += jasan-totmoney;


        if(totjasan > 0){
            now_jasan.setText(("+" + (totjasan) + "" ));
        }
        else if(totjasan < 0){
            now_jasan.setText((totjasan)+"");
        }
        else{
            now_jasan.setText((totjasan)+"");
        }
        reusltmoney.setText(Result);
        tvnowDday.setText(DbDday);

        db.close();
    }


}