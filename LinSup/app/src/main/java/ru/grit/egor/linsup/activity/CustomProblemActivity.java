package ru.grit.egor.linsup.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.grit.egor.linsup.R;
import ru.grit.egor.linsup.utils.LinSupSolver;

public class CustomProblemActivity extends AppCompatActivity {

    static final String TAG = CustomProblemActivity.class.getSimpleName();

    Context mContext;
    TextView text_I;
    TextView text_J;
    TextView text_C;
    TextView text_B;
    TextView text_Y;
    LinearLayout matrix_A;
    TextView text_result;
    Button solve;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_problem);
        mContext = this;
        text_I = (TextView) findViewById(R.id.i);
        text_J = (TextView) findViewById(R.id.j);
        text_C = (TextView) findViewById(R.id.c);
        text_B = (TextView) findViewById(R.id.b);
        text_Y = (TextView) findViewById(R.id.y0);
        text_result = (TextView) findViewById(R.id.result);
        matrix_A = (LinearLayout) findViewById(R.id.matrix_a);
        solve = (Button) findViewById(R.id.solve);
        solve.setOnClickListener(solveClicked);

        text_I.addTextChangedListener(textWatcher);
        text_J.addTextChangedListener(textWatcher);
    }

    View.OnClickListener solveClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                text_result.setText("");
                final int i = Integer.parseInt(text_I.getText().toString());
                final int j = Integer.parseInt(text_J.getText().toString());
                final ArrayList<Double> c = getDoubleArray(text_C.getText().toString());
                final ArrayList<Double> b = getDoubleArray(text_B.getText().toString());
                final ArrayList<Double> y0 = getDoubleArray(text_Y.getText().toString());
                if (c == null || b == null || y0 == null) {
                    return;
                }

                final ArrayList<ArrayList<Double>> A = new ArrayList<>();
                for (int count = 0; count < matrix_A.getChildCount(); count++) {
                    ArrayList<Double> temp = getDoubleArray(((EditText) matrix_A.getChildAt(count)).getText().toString());
                    if (temp != null) {
                            A.add(temp);
                    } else {
                        return;
                    }
                }

                Log.d(TAG, "all done! start solve");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LinSupSolver solver = new LinSupSolver();
                        solver.setmCallback(new LinSupSolver.callback() {
                            @Override
                            public void error(final String error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        text_result.setText(error);
                                    }
                                });
                            }

                            @Override
                            public void simplexResult(final String result) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        text_result.setText(text_result.getText().toString()+"\n"+result);
                                    }
                                });
                            }

                            @Override
                            public void linSupResult(final String result) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        text_result.setText(text_result.getText().toString()+"\n"+result);
                                    }
                                });
                            }
                        });
                        solver.initAndSolve(i,j,b,c,y0,A);
                    }
                }).start();

            } catch (Exception e) {
                Log.d(TAG, "solveClicked parse exception");
            }

        }
    };

    private ArrayList<Double> getDoubleArray(String text) {
        try {
            ArrayList<Double> result = new ArrayList<>();
            text.replaceAll(" ", "");
            String[] array = text.split(",");
            for (int i = 0; i < array.length; i++) {
                result.add(Double.parseDouble(array[i]));
            }
            return result;
        } catch (Exception e) {
            Log.d(TAG, "getDoubleArray parse exception");
        }
        return null;
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int size_i = Integer.parseInt(text_I.getText().toString());
                int size_j = Integer.parseInt(text_J.getText().toString());
                String text = "";
                if (size_j > 0) {
                    text += "0";
                    for (int i = 1; i < size_j; i++) {
                        text += ", 0";
                    }
                }
                matrix_A.removeAllViews();
                for (int i = 0 ; i < size_i; i++) {
                    EditText view = new EditText(mContext);
                    view.setText(text);
                    matrix_A.addView(view);
                }
            } catch (NumberFormatException e) {
                Log.d(TAG, "afterTextChanged parse exception");
            }
        }
    };
}
