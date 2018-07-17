package com.nick.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nick.miniweather.bean.City;

import java.util.ArrayList;

public class SelectCity extends Activity implements View.OnClickListener {

    private ClearEditTest mClearEditText;
    private ArrayList<City> filterDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView mBackBtn;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);

        mClearEditText = (ClearEditTest) findViewById(R.id.search_city);
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当输入框里面的值为空，更新为原来的列表，否则过滤数据列表
                filterData(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void filterData(String filterStr) {
        filterDataList = new ArrayList<City>();

        Log.d("Filters", filterStr);

        if (TextUtils.isEmpty(filterStr)) {

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                //selectCity的数据是使用Intent返回
                Intent intent = new Intent();
                //把返回的数据存入Intent
                intent.putExtra("cityCode", "101160101");
                //设置返回数据
                setResult(RESULT_OK, intent);
                //关闭Activity
                finish();
                break;
            default:
                break;
        }
    }


}