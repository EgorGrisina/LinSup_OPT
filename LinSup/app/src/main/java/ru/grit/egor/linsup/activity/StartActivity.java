package ru.grit.egor.linsup.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ru.grit.egor.linsup.R;
import ru.grit.egor.linsup.utils.LinSupUtils;

public class StartActivity extends AppCompatActivity {

    final static String TAG = StartActivity.class.getSimpleName();

    ArrayList<ArrayList<Double>> yk = new ArrayList<>();

    HashMap<Integer, Integer> lk = new HashMap<>();

    ArrayList<ArrayList<Double>> A = new ArrayList<>();
    ArrayList<Double> B = new ArrayList<>();
    ArrayList<Double> C = new ArrayList<>();
    ArrayList<Double> y0 = new ArrayList<>();
    int N = 10;
    int I = 4;
    int J = 4;
    Double a = 0.99;
    Double lambda = 1.0;
    Double e = 0.0000000001;

    PointValuePair solution = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init_test1();

        simplex();
        printSimplex();

        startLinSup();
        printLinSup();
    }

    private void init_test1() {
        I = 5;
        J = 4;
        C.addAll(LinSupUtils.toList(new double[] { 1, 1, 1, 1  } ));
        B.addAll(LinSupUtils.toList(new double[] { 2.0, 2.0, 2.0, 2.0, 2.0} ));

        for (int i = 0; i < I; i++) {
            A.add(new ArrayList<Double>());
        }
        A.get(0).addAll(LinSupUtils.toList(new double[] {1, 0, 0, 0 } ));
        A.get(1).addAll(LinSupUtils.toList(new double[] {0, 1, 0, 0 } ));
        A.get(2).addAll(LinSupUtils.toList(new double[] {0, 0, 1, 0 } ));
        A.get(3).addAll(LinSupUtils.toList(new double[] {0, 0, 0, 1 } ));
        A.get(4).addAll(LinSupUtils.toList(new double[] {1, 0, 0, 0 } ));


        // LinSup init-s
        for (int j = 0; j < J; j++) {
            y0.add(10.0);
        }
    }

    private void init() {
        for (int j = 0; j < J; j++) {
            y0.add(2.0);
        }
        for (int j = 0; j < J; j++) {
            C.add(10.0);
        }
        for (int i = 0; i < I; i++) {
            B.add(-1.0);
        }
        for (int i = 0; i < I; i++) {
            A.add(new ArrayList<Double>());
            for (int j = 0; j < J; j++) {
                A.get(i).add(1.0);
            }
        }
    }


    private void startLinSup() {

        int k = 0;                         // k <- 0

        yk.add(new ArrayList<Double>());   // yk(0) <- y0
        yk.get(k).addAll(y0);

        lk.put(-1, 0);                      // lk(-1) <- 0
        HashMap<Integer, HashMap<Integer, ArrayList<Double>>> ykn = new HashMap<>();

        while (getProximityFun(yk.get(k)) > e) {

            int n = 0;                      // n <- 0
            int l = LinSupUtils.getRandom(k, lk.get(k-1)); // l <- rand(k,lk(k-1)

            ykn.put(k, new HashMap<Integer, ArrayList<Double>>());  // ykn <- yk
            ykn.get(k).put(n, new ArrayList<Double>());
            ykn.get(k).get(n).addAll(yk.get(k));

            while (n < N) {
                Double betta = Math.pow(a, l);   // Bkn <- nl

                ArrayList<Double> z = LinSupUtils.getZ(ykn.get(k).get(n), betta, C); // z <- ykn - betta* c / ||c||2
                //z.addAll(ykn.get(k).get(n));
                n = n+1;                        // n <- n+1
                ykn.get(k).put(n, new ArrayList<Double>());
                ykn.get(k).get(n).addAll(z);    // ykn <- z

                l = l+1;                        // l <- l+1

            }

            lk.put(k, l);                       // lk <- l

            yk.add(new ArrayList<Double>());    // y(k+1) <- A (ykn(k,N))
            yk.get(k+1).addAll(getNextYbyA(ykn.get(k).get(N)));

            k = k+1;                            // k <- k+1

        }

    }

    private double getProximityFun(ArrayList<Double> x) {
        double sum1 = 0.0;

        for (int i = 0; i < I; i++) {
            double val = Math.max((LinSupUtils.innerProduction(A.get(i),x) - B.get(i)), 0);
            val = val*val;
            double val2 = 0.0;
            for (int j = 0; j < J; j++) {
                val2 += Math.pow(A.get(i).get(j), 2);
            }

            sum1 += val/val2;
        }
        sum1 = sum1 / (2 * I);

        double sum2 = 0.0;
        for (int j = 0; j < J; j++) {
            double val = -x.get(j);
            val = Math.max(val, 0);
            val = Math.pow(val, 2);
            sum2 += val;
        }
        sum2 = sum2 / (2 * J);

        Log.d(TAG, "getProximityFun = " + (sum1+sum2));
        return sum1+sum2;
    }

    private ArrayList<Double> getPHi(ArrayList<Double> z, int i) {
        ArrayList<Double> result = new ArrayList<>();
        result.addAll(z);

        if (LinSupUtils.innerProduction(A.get(i),z) > B.get(i)) {
            Double temp = (LinSupUtils.innerProduction(A.get(i),z) - B.get(i));
            temp = (temp) / LinSupUtils.innerProduction(A.get(i), A.get(i));
            result = LinSupUtils.vectorsDifference(result, LinSupUtils.production(A.get(i), temp));
        }
        return result;
    }


    private ArrayList<Double> getNextYbyA(ArrayList<Double> prev) {
        ArrayList<Double> result = new ArrayList<>();
        for (int j = 0; j < J; j++) {
            result.add(0.0);
        }

        for (int i = 0; i < I; i++) {
            result = LinSupUtils.vectorsSum(result, LinSupUtils.vectorsDifference(getPHi(prev, i), prev));
        }
        result = LinSupUtils.production(result, (lambda/I));
        result = LinSupUtils.vectorsSum(prev, result);

        return result;




        /*

        for (int i = 0; i < I; i++) {
            if (LinSupUtils.innerProduction(A.get(i),result) > B.get(i)) {
                Double temp = (LinSupUtils.innerProduction(A.get(i),result) - B.get(i));
                temp = (temp * lambda) / LinSupUtils.innerProduction(A.get(i), A.get(i));
                result = LinSupUtils.vectorsDifference(result, LinSupUtils.production(A.get(i), temp));
            }
        }*/



    }


    private void simplex() {

        // describe the optimization problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(LinSupUtils.toArray(C), 0);
        Collection<LinearConstraint> constraints = new ArrayList();
        for (int i = 0; i < I; i++) {
            constraints.add(new LinearConstraint(LinSupUtils.toArray(A.get(i)), Relationship.LEQ, B.get(i)));
        }

        // create and run the solver
        SimplexSolver solver = new SimplexSolver();
        solution = solver.optimize(f, constraints, GoalType.MINIMIZE, true);

    }


    private void printSimplex() {
        if (solution != null) {
            Log.d(TAG, "----------- Simplex function MINIMIZE ---------");
            Log.d(TAG, "f(x) = " + solution.getValue());
            for (int j = 0; j < J; j++) {
                Log.d(TAG, "x("+j+") = " + solution.getPoint()[j]);
            }
            Log.d(TAG, "----------- -------------------------- ---------");
        }
    }

    private void printLinSup() {
        if (solution != null) {
            Log.d(TAG, "----------- Simplex function LinSup ---------");
            Log.d(TAG, "f(x) = " + LinSupUtils.innerProduction(C, yk.get(yk.size()-1)));
            for (int j = 0; j < J; j++) {
                Log.d(TAG, "x("+j+") = " + yk.get(yk.size()-1).get(j));
            }
            Log.d(TAG, "----------- -------------------------- ---------");
        }
    }

}
