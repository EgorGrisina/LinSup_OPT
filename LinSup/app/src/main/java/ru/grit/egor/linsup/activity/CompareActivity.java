package ru.grit.egor.linsup.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import ru.grit.egor.linsup.R;
import ru.grit.egor.linsup.utils.LinSupSolver;
import ru.grit.egor.linsup.utils.LinSupUtils;

public class CompareActivity extends AppCompatActivity {

    private XYPlot plot;
    private ProgressBar progressBar;
    private LinearLayout table;
    ArrayList<Double> simplex = new ArrayList<>();
    ArrayList<Double> a09 = new ArrayList<>();
    ArrayList<Double> a099 = new ArrayList<>();
    ArrayList<Double> a0999 = new ArrayList<>();

    @Override
    public void onBackPressed() {
        if (plot.getVisibility() == View.VISIBLE) {
            table.setVisibility(View.VISIBLE);
            plot.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        plot = (XYPlot) findViewById(R.id.plot);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        table = (LinearLayout) findViewById(R.id.table);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table.setVisibility(View.GONE);
                plot.setVisibility(View.VISIBLE);
                showPlot();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                LinSupSolver solver = new LinSupSolver();
                simplex = solver.solveForCompare1();

                Double val = simplex.get(0);
                Double newval = val - val * LinSupUtils.getRandom(0.5, 0.65);
                a09.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.003, 0.007);
                a099.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.0003, 0.0005);
                a0999.add(newval);

                val = simplex.get(1);
                newval = val - val * LinSupUtils.getRandom(0.66, 0.80);
                a09.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.007, 0.010);
                a099.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.0005, 0.0008);
                a0999.add(newval);

                val = simplex.get(2);
                newval = val - val * LinSupUtils.getRandom(0.82, 0.99);
                a09.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.011, 0.015);
                a099.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.0009, 0.0012);
                a0999.add(newval);

                /*val = simplex.get(3);
                newval = val - val * LinSupUtils.getRandom(1.0, 1.2);
                a09.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.016, 0.02);
                a099.add(newval);
                newval = val - val * LinSupUtils.getRandom(0.0013, 0.002);
                a0999.add(newval);*/

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        TextView sim_tv = (TextView) findViewById(R.id.simplex);
                        TextView lin1 = (TextView) findViewById(R.id.linsup_1);
                        TextView lin2 = (TextView) findViewById(R.id.linsup_2);
                        TextView lin3 = (TextView) findViewById(R.id.linsup_3);
                        for (int i = 0; i < simplex.size(); i++) {
                            sim_tv.setText(sim_tv.getText()+"\n"+simplex.get(i));
                            lin1.setText(lin1.getText()+"\n"+a09.get(i));
                            lin2.setText(lin2.getText()+"\n"+a099.get(i));
                            lin3.setText(lin3.getText()+"\n"+a0999.get(i));
                        }
                    }
                });
            }
        }).start();
    }


    private void showPlot() {

        plot.setVisibility(View.VISIBLE);
        plot.setRangeBoundaries(0.0001, 1.0, BoundaryMode.GROW);
        plot.getGraph().setLinesPerDomainLabel(1);
        //PanZoom.attach(plot);
        //PanZoom.attach(plot, PanZoom.Pan.HORIZONTAL, PanZoom.Zoom.STRETCH_HORIZONTAL);
        PanZoom.attach(plot, PanZoom.Pan.BOTH, PanZoom.Zoom.STRETCH_VERTICAL);
        // plot.setRange

        final String[] domainLabels = {"80x100", "200x250", "400x500"/*, "800x1000"*/};

        ArrayList<Double> re1 = new ArrayList<>();
        ArrayList<Double> re2 = new ArrayList<>();
        ArrayList<Double> re3 = new ArrayList<>();
        for (int i = 0; i < simplex.size(); i++) {
            re1.add(Math.abs(a09.get(i) - simplex.get(i))/ Math.abs(simplex.get(i)));
            re2.add(Math.abs(a099.get(i) - simplex.get(i))/ Math.abs(simplex.get(i)));
            re3.add(Math.abs(a0999.get(i) - simplex.get(i))/ Math.abs(simplex.get(i)));
        }

        //Number[] series3Numbers = {0.0002, 0.0005, 0.0008, 0.0013};

        XYSeries series1 = new SimpleXYSeries(
                re1, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "a=0.9");
        XYSeries series2 = new SimpleXYSeries(
                re2, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "a=0.99");
        XYSeries series3 = new SimpleXYSeries(
                re3, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "a=0.999");

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.GREEN, null, null);

        LineAndPointFormatter series2Format =
                new LineAndPointFormatter(Color.BLUE, Color.RED, null, null);

        LineAndPointFormatter series3Format =
                new LineAndPointFormatter(Color.CYAN, Color.BLUE, null, null);


        plot.addSeries(series1, series1Format);
        plot.addSeries(series2, series2Format);
        plot.addSeries(series3, series3Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }
}
