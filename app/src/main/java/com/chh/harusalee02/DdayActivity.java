package com.chh.harusalee02;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DdayActivity extends AppCompatActivity{

    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    // 현재 날짜를 알기 위해 사용
    private Calendar mCalendar;

    // D-day result
    private TextView mTvResult;

    String dbdday="";

    Button save_dday;



    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            // D-day 계산 결과 출력
            mTvResult.setText(getDday(a_year, a_monthOfYear, a_dayOfMonth));

            dbdday = mTvResult.getText().toString();
            insert(dbdday);
        }



    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dday);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                DdayActivity.this, // 현재 화면의 context
                "member2.db", // 파일명
                null, // 커서 팩토리
                11); // 버전 번호

        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);

        // 현재 날짜를 알기 위해 사용
        mCalendar = new GregorianCalendar();

        // Today 보여주기
        TextView tvDate = findViewById(R.id.tv_date);
        tvDate.setText(getToday());

        // D-day 보여주기
        mTvResult = findViewById(R.id.tv_result);

        save_dday=(Button)findViewById(R.id.save_dday);

        // Input date click 시 date picker 호출
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(DdayActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        };
        findViewById(R.id.btn_input_date).setOnClickListener(clickListener);

        save_dday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent act3 = new Intent(DdayActivity.this, MainActivity.class);
                startActivity(act3);
                selectAll();

            }
        });
    }

    /**
     * Today 반환
     */
    private String getToday() {
        // 지정된 format 으로 string 표시
        final String strFormat = getString(R.string.format_today);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat(strFormat);
        return CurDateFormat.format(mCalendar.getTime());
    }

    /**
     * D-day 반환
     */
    private String getDday(int a_year, int a_monthOfYear, int a_dayOfMonth) {
        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(a_year, a_monthOfYear, a_dayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        final String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "D-Day";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount;
    }

    //데이타베이스 메서드 처리
    public void insert(String dbdday) {

        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
        //값들을 컨트롤 하려고 클래스 생성
        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("dbday", dbdday);
        db.insert("member3", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
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
}