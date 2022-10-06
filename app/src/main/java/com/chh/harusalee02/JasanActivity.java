package com.chh.harusalee02;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

public class JasanActivity extends AppCompatActivity {

    //SQLITE 데이타베이스 관련변수
    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    Calendar ca = Calendar.getInstance();
    int month = ca.get(Calendar.MONTH);
    int day = ca.get(Calendar.DAY_OF_MONTH);

    TextView tvday;
    EditText edtxt_jasan;
    Button btn_save,btn_back;
    String jasan_money;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private EditText wonEt;
    private String result="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nowjasan);

        //데이베이스 생성.
        helper = new MySQLiteOpenHelper(
                JasanActivity.this, // 현재 화면의 context
                "member2.db", // 파일명
                null, // 커서 팩토리
                11); // 버전 번호

        tvday = (TextView)findViewById(R.id.tvday);
        tvday.setText((month+1)+"월"+(day)+"일");

        edtxt_jasan = (EditText)findViewById(R.id.edtxt_jasan);
        
        //콤마찍기리스너
//        edtxt_jasan.addTextChangedListener(new NumberTextWatcher(edtxt_jasan));

        findViewById(R.id.btn_jasansave).setOnClickListener(mClick);
        findViewById(R.id.btn_jasanback).setOnClickListener(mClick);

        selectAll();
    }

    View.OnClickListener mClick = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_jasansave:
                    if (edtxt_jasan.getText().toString().equals("")){
                        Toast.makeText(JasanActivity.this, "자산을 입력하세요.",  Toast.LENGTH_SHORT).show();
                        break;
                    }
                    
                    jasan_money = edtxt_jasan.getText().toString();
                    edtxt_jasan.setText(jasan_money);
                    Toast.makeText(JasanActivity.this, "자산이 추가됐습니다.",  Toast.LENGTH_SHORT).show();
                    edtxt_jasan.setText("");
                    insert(jasan_money);
                    selectAll();


                case R.id.btn_jasanback:
                    selectAll();
                    finish();
                    Intent act3 = new Intent(JasanActivity.this, MainActivity.class);
                    startActivity(act3);
            }
        }
    };


    //데이타베이스 메서드 처리
    public void insert(String jasan_money) {

        db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
        //값들을 컨트롤 하려고 클래스 생성
        ContentValues values = new ContentValues();
        // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
        // 데이터의 삽입은 put을 이용한다.
        values.put("dbjasan", jasan_money);
        db.insert("member2", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
        db.close();


    }

    //단위콤마찍기 클래스
//    class NumberTextWatcher implements TextWatcher {
//
//        private DecimalFormat df;
//        private DecimalFormat dfnd;
//        private boolean hasFractionalPart;
//
//        private EditText et;
//
//        public NumberTextWatcher(EditText et)
//        {
//            df = new DecimalFormat("#,###.##");
//            df.setDecimalSeparatorAlwaysShown(true);
//            dfnd = new DecimalFormat("#,###");
//            this.et = et;
//            hasFractionalPart = false;
//        }
//
//        @SuppressWarnings("unused")
//        private static final String TAG = "NumberTextWatcher";
//
//        public void afterTextChanged(Editable s)
//        {
//            et.removeTextChangedListener(this);
//
//            try {
//                int inilen, endlen;
//                inilen = et.getText().length();
//
//                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
//                Number n = df.parse(v);
//                int cp = et.getSelectionStart();
//                if (hasFractionalPart) {
//                    et.setText(df.format(n));
//                } else {
//                    et.setText(dfnd.format(n));
//                }
//                endlen = et.getText().length();
//                int sel = (cp + (endlen - inilen));
//                if (sel > 0 && sel <= et.getText().length()) {
//                    et.setSelection(sel);
//                } else {
//                    // place cursor at the end?
//                    et.setSelection(et.getText().length() - 1);
//                }
//            } catch (NumberFormatException nfe) {
//                // do nothing?
//            } catch (ParseException e) {
//                // do nothing?
//            }
//
//            et.addTextChangedListener(this);
//        }
//
//        public void beforeTextChanged(CharSequence s, int start, int count, int after)
//        {
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count)
//        {
//            if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
//            {
//                hasFractionalPart = true;
//            } else {
//                hasFractionalPart = false;
//            }
//        }
//    }



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


}