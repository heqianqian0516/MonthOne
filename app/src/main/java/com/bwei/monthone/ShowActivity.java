package com.bwei.monthone;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowActivity extends AppCompatActivity {

    @BindView(R.id.edi_search)
    EditText mEdiSearch;
    @BindView(R.id.text_search)
    TextView mTextSearch;
    @BindView(R.id.line)
    LinearLayout mLine;
    @BindView(R.id.btn_clean)
    Button mBtnClean;
    @BindView(R.id.main_flow)
    FlowLayout mMainFlow;
    private List<String> list=new ArrayList<>();
    private FrameLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
       mTextSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
               String s=mEdiSearch.getText().toString();
               list.add(s);
               TextView textView=new TextView(ShowActivity.this);
               textView.setText(list.get(list.size()-1));
               textView.setTextColor(Color.BLACK);
               textView.setPadding(25,15,25,15);
               textView.setTextSize(16);
               mMainFlow.addView(textView,params);
           }
       });
    }

    @OnClick({R.id.edi_search, R.id.text_search, R.id.line, R.id.btn_clean, R.id.main_flow})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.edi_search:
                break;
            case R.id.text_search:
                break;
            case R.id.line:
                break;
            case R.id.btn_clean:
                mMainFlow.removeAllViews();
                break;
            case R.id.main_flow:
                break;
        }
    }
}
