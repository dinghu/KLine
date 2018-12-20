package cn.ding.hu.kline;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ding.hu.klinelib.DataHelper;
import cn.ding.hu.klinelib.KLineChartAdapter;
import cn.ding.hu.klinelib.KLineChartView;
import cn.ding.hu.klinelib.KLineEntity;
import cn.ding.hu.klinelib.draw.Status;
import cn.ding.hu.klinelib.formatter.DateFormatter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<KLineEntity> datas;

    private KLineChartAdapter adapter;

    private ArrayList<TextView> subTexts = new ArrayList<>();

    //    private val subTexts: ArrayList<TextView> by lazy { arrayListOf(macdText, kdjText, rsiText, wrText) }
    // 主图指标下标
    private int mainIndex = 0;
    // 副图指标下标
    private int subIndex = -1;

    private TextView text1;
    private TextView maText;
    private TextView bollText;
    private TextView mainHide;
    private TextView text2;
    private TextView macdText;
    private TextView kdjText;
    private TextView rsiText;
    private TextView wrText;
    private TextView subHide;
    private TextView fenText;
    private TextView kText;
    private KLineChartView kLineChartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text1 = (TextView) findViewById(R.id.text1);
        maText = (TextView) findViewById(R.id.maText);
        bollText = (TextView) findViewById(R.id.bollText);
        mainHide = (TextView) findViewById(R.id.mainHide);
        text2 = (TextView) findViewById(R.id.text2);
        macdText = (TextView) findViewById(R.id.macdText);
        kdjText = (TextView) findViewById(R.id.kdjText);
        rsiText = (TextView) findViewById(R.id.rsiText);
        wrText = (TextView) findViewById(R.id.wrText);
        subHide = (TextView) findViewById(R.id.subHide);
        fenText = (TextView) findViewById(R.id.fenText);
        kText = (TextView) findViewById(R.id.kText);
        subTexts.add(macdText);
        subTexts.add(kdjText);
        subTexts.add(rsiText);
        subTexts.add(wrText);
        adapter = new KLineChartAdapter();
        kLineChartView = findViewById(R.id.kLineChartView);
        kLineChartView.setAdapter(adapter);
        kLineChartView.setDateTimeFormatter(new DateFormatter());
        kLineChartView.setGridRows(4);
        kLineChartView.setGridColumns(4);
        initData();
        maText.setOnClickListener(this);
        bollText.setOnClickListener(this);
        mainHide.setOnClickListener(this);
        subHide.setOnClickListener(this);
        fenText.setOnClickListener(this);
        kText.setOnClickListener(this);

        for (int i = 0; i < subTexts.size(); i++) {
            final TextView textView = subTexts.get(i);
            final int index = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (subIndex != index) {
                        kLineChartView.hideSelectData();
                        if (subIndex != -1) {
                            subTexts.get(subIndex).setTextColor(Color.WHITE);
                        }
                        subIndex = index;
                        textView.setTextColor(Color.parseColor("#eeb350"));
                        kLineChartView.setChildDraw(subIndex);
                    }
                }
            });
        }
    }

    private void initData() {
        kLineChartView.justShowLoading();

        datas = DataRequest.getALL(this).subList(0, 500);
        DataHelper.calculate(datas);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addFooterData(datas);
                adapter.notifyDataSetChanged();
                kLineChartView.startAnimation();
                kLineChartView.refreshEnd();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maText:
                if (mainIndex != 0) {
                    kLineChartView.hideSelectData();
                    mainIndex = 0;
                    maText.setTextColor(Color.parseColor("#eeb350"));
                    bollText.setTextColor(Color.WHITE);
                    kLineChartView.changeMainDrawType(Status.MA);
                }
                break;
            case R.id.bollText:
                if (mainIndex != 1) {
                    kLineChartView.hideSelectData();
                    mainIndex = 1;
                    bollText.setTextColor(Color.parseColor("#eeb350"));
                    maText.setTextColor(Color.WHITE);
                    kLineChartView.changeMainDrawType(Status.BOLL);
                }
                break;
            case R.id.mainHide:
                if (mainIndex != -1) {
                    kLineChartView.hideSelectData();
                    mainIndex = -1;
                    bollText.setTextColor(Color.WHITE);
                    maText.setTextColor(Color.WHITE);
                    kLineChartView.changeMainDrawType(Status.NONE);
                }
                break;
            case R.id.subHide:
                if (subIndex != -1) {
                    kLineChartView.hideSelectData();
                    subTexts.get(subIndex).setTextColor(Color.WHITE);
                    subIndex = -1;
                    kLineChartView.hideChildDraw();
                }
                break;
            case R.id.fenText:
                kLineChartView.hideSelectData();
                fenText.setTextColor(Color.parseColor("#eeb350"));
                kText.setTextColor(Color.WHITE);
                kLineChartView.setMainDrawLine(true);
                break;
            case R.id.kText:
                kLineChartView.hideSelectData();
                kText.setTextColor(Color.parseColor("#eeb350"));
                fenText.setTextColor(Color.WHITE);
                kLineChartView.setMainDrawLine(false);
                break;

        }
    }

}
