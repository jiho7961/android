package com.ljh.myapplication;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static  com.ljh.myapplication.R.id.edt5;


public class MainActivity extends TabActivity {

    myDBHelper myHelper;
    EditText edtName;
    Button  btnSelect;
    TextView tv0, edtNameResult, edtNumberResult,edt5;
    ImageView imv1;
    SQLiteDatabase sqlDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtName = (EditText) findViewById(R.id.edt1);
        edtNameResult = (TextView) findViewById(R.id.edt3);
        //효능 텍스트뷰
        edtNumberResult = (TextView) findViewById(R.id.edt4);
        //사용법 텍스트뷰
        edt5 = (TextView)findViewById(R.id.edt5);
        //주의사항 텍스트뷰
        imv1 = (ImageView)findViewById(R.id.imv1);
        tv0 = (TextView)findViewById(R.id.tv0);
        btnSelect = (Button) findViewById(R.id.btn1);
        //변수 대입


        TabHost tabhost = getTabHost();

        TabHost.TabSpec tabSpec1 = tabhost.newTabSpec("A").setIndicator("약 검색");
        tabSpec1.setContent(R.id.tab1);
        tabhost.addTab(tabSpec1);
        //첫번째 탭 추가

        TabHost.TabSpec tabSpec2 = tabhost.newTabSpec("B").setIndicator("검색 내역");
        tabSpec2.setContent(R.id.tab2);
        tabhost.addTab(tabSpec2);
        //두번째 탭 추가

        final ArrayList<String> mid = new ArrayList<String>();
        ListView list =(ListView)findViewById(R.id.list1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mid);
        list.setAdapter(adapter);
        //리스트뷰 항목 추가를 위해 배열대신 arraylist형으로 변수 선언

        myHelper = new myDBHelper(this);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mid.add( edtName.getText().toString() );
                // 이 method를 호출하지 않을 경우, ListView에 추가된 문자열이 보이지 않는다.
                adapter.notifyDataSetChanged();

                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL where gName like '%" +edtName.getText().toString()+"%';", null);
                //검색시 부분 문자로도 검색이 가능하도록

                String strNames = "";
                String strNumbers = "";
                String struse ="";
                String str1 = " ";
                while (cursor.moveToNext()) {
                    str1 += cursor.getString(0);// 데이터베이스 테이블 항목 번호에 해당
                    strNames += cursor.getString(1);
                    strNumbers += cursor.getString(2);
                    byte [] image = cursor.getBlob(3);
                    struse += cursor.getString(4);

                    ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
                    Bitmap bm = BitmapFactory.decodeStream(imageStream);
                    imv1.setImageBitmap(bm);
                    //비트맵 이미지 불러오기
                }

                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);
                edt5.setText(struse);
                tv0.setText(str1);
                cursor.close();
                sqlDB.close();
            }
        });

    }
    public class myDBHelper extends SQLiteOpenHelper {
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
