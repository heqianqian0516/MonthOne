package com.bwei.monthone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import adapter.ShoppingAdapter;
import bean.ShoppingBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import view.Iview;

public class MainActivity extends AppCompatActivity implements Iview, View.OnClickListener {


    @BindView(R.id.banji)
    TextView mBanji;
    @BindView(R.id.expan_view)
    ExpandableListView mExpanView;
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.all_parice)
    TextView mAllParice;
    @BindView(R.id.accounts)
    Button mAccounts;
    private List<ShoppingBean.DataBean> datas = new ArrayList<>();
    private ShoppingAdapter shoppingAdapter;
    //private ExpandableListView mExpanView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // mExpanView = findViewById(R.id.expan_view);


        //解析json文件
        initData();
        intiView();
        mBanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                startActivity(intent);
            }
        });
    }



    private void initData() {
        mExpanView.setGroupIndicator(null);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open("cart.json"), "GBK");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
            String s = stringBuilder.toString();

            ShoppingBean shoppingBean = new Gson().fromJson(s, ShoppingBean.class);
            datas.addAll(shoppingBean.getData());
            shoppingAdapter = new ShoppingAdapter(datas, MainActivity.this);
            mExpanView.setAdapter(shoppingAdapter);
            shoppingAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void intiView() {
        mCheckbox.setOnClickListener(this);
        shoppingAdapter.setCallBack(new ShoppingAdapter.HttpListenerCallBack() {
            @Override
            public void setGroupCheck(int groupPosition) {
                //判断代码是否全被选中
                 boolean childerCheck = shoppingAdapter.isChilderCheck(groupPosition);
                 shoppingAdapter.isChecked(groupPosition,!childerCheck);
                 shoppingAdapter.notifyDataSetChanged();
                 flushBottomLayout();
            }

            @Override
            public void setChildCheck(int groupPosition, int childPosition) {
                 boolean childeChecked = shoppingAdapter.myChildCheck(groupPosition, childPosition);
                 shoppingAdapter.setChildChecked(groupPosition,childPosition,!childeChecked);
                 shoppingAdapter.notifyDataSetChanged();
                flushBottomLayout();
            }

            @Override
            public void setNumber(int groupPosition, int childPosition, int number) {
                //得到你要点击的商品的复选框
                shoppingAdapter.setShoppingNumb(groupPosition,childPosition,number);
                shoppingAdapter.notifyDataSetChanged();
                flushBottomLayout();
            }
        });
    }
    @Override
    public void onRequestSuccess(Object o) {

    }

    @Override
    public void onRequestFail(String error) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private void flushBottomLayout() {
        boolean allGoods = shoppingAdapter.isAllGoods();
        mCheckbox.setChecked(allGoods);
        //调用适配器的数据计算方法
        float allGoodsPrice = shoppingAdapter.isAllPrice();
        int allGoodsNumber = shoppingAdapter.isAllGoodsNumber();
        mAllParice.setText("" + allGoodsPrice);
        mAccounts.setText("去结算" + allGoodsNumber);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checkbox:
                boolean checkAll = shoppingAdapter.isAllGoods();
                shoppingAdapter.isAllGoodsChecked(!checkAll);
                shoppingAdapter.notifyDataSetChanged();
                flushBottomLayout();
                break;
            case R.id.banji:

                break;
        }
    }
}
