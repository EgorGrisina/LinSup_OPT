package ru.grit.egor.linsup.utils;

import android.util.Log;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LinSupSolver {

    final static String TAG = LinSupSolver.class.getSimpleName();

    ArrayList<ArrayList<Double>> yk = new ArrayList<>();

    HashMap<Integer, Integer> lk = new HashMap<>();

    ArrayList<ArrayList<Double>> A = new ArrayList<>();
    ArrayList<Double> B = new ArrayList<>();
    ArrayList<Double> C = new ArrayList<>();
    ArrayList<Double> y0 = new ArrayList<>();
    int N = 10;
    int I = 4;
    int J = 4;
    Double a = 0.999;
    Double lambda = 2.99;
    Double e = 1.0E-10;
    Double e2 = 1.0E-5;


    PointValuePair solution = null;

    private callback mCallback;

    public void setmCallback(callback callback) {
        mCallback = callback;
    }

    public interface callback {
        void error(String error);
        void simplexResult(String result);
        void linSupResult(String result);
    }


    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //init_test2();
        for (int iter = 0; iter < N; iter++) {
            yk = new ArrayList<>();
            lk = new HashMap<>();
            A = new ArrayList<>();
            B = new ArrayList<>();
            C = new ArrayList<>();
            y0 = new ArrayList<>();

            Log.i(TAG, "-------------------------------------------------------------------");
            Log.i(TAG, "Iteration " + iter);
            init();
            try {
                try {
                    int i = simplex();
                    printSimplex(i);
                } catch (MaxCountExceededException e ) {
                    Log.i(TAG, "Max Count Exceeded Exception");
                }

                startLinSup();
                printLinSup();
            } catch (UnboundedSolutionException e ) {
                Log.i(TAG, "Unbounded Solution Exception");
            }
        }
        Log.i(TAG, "-------------------------------------------------------------------");
    }*/

    void clear() {
        yk = new ArrayList<>();
        lk = new HashMap<>();
        A = new ArrayList<>();
        B = new ArrayList<>();
        C = new ArrayList<>();
        y0 = new ArrayList<>();
        solution = null;
    }


    public ArrayList<Double> solveForCompare1() {
        ArrayList<Double> result = new ArrayList<>();
        clear();
        I = 80;
        J = 100;
        initRandom();
        try {
            int i = simplex();
            printSimplex(i);
            result.add(solution.getValue());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        clear();
        I = 200;
        J = 150;
        initRandom();
        try {
            int i = simplex();
            printSimplex(i);
            result.add(solution.getValue());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        clear();
        I = 400;
        J = 500;
        initRandom();
        try {
            int i = simplex();
            printSimplex(i);
            result.add(solution.getValue());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        clear();
        I = 800;
        J = 1000;
        initRandom();
        try {
            int i = simplex();
            printSimplex(i);
            result.add(solution.getValue());
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        return result;
    }


    public void initAndSolve(int ii, int jj, ArrayList<Double> bb, ArrayList<Double> cc, ArrayList<Double> yy, ArrayList<ArrayList<Double>> aa) {

        yk = new ArrayList<>();
        lk = new HashMap<>();

        I = ii;
        J = jj;
        C = cc;
        B = bb;
        y0 = yy;
        A = aa;
        //init_test2();

        try {
            try {
                int i = simplex();
                printSimplex(i);
            } catch (MaxCountExceededException e ) {
                Log.i(TAG, "Max Count Exceeded Exception");
                if (mCallback != null) {
                    mCallback.error("Max Count Exceeded Exception");
                }
            }

            startLinSup();
            printLinSup();
        } catch (UnboundedSolutionException e ) {
            Log.i(TAG, "Unbounded Solution Exception");
            if (mCallback != null) {
                mCallback.error("Unbounded Solution Exception");
            }
        }
    }

    private void init_test1() {
        I = 3;
        J = 2;
        C.addAll(LinSupUtils.toList(new double[] { 1, 1} ));
        B.addAll(LinSupUtils.toList(new double[] { 16.0, 10.0, 8.0} ));

        for (int i = 0; i < I; i++) {
            A.add(new ArrayList<Double>());
        }
        A.get(0).addAll(LinSupUtils.toList(new double[] {1, 0} ));
        A.get(1).addAll(LinSupUtils.toList(new double[] {0, 1 } ));
        A.get(2).addAll(LinSupUtils.toList(new double[] {0.5, 0.4 } ));


        // LinSup init-s
        for (int j = 0; j < J; j++) {
            y0.add(10.0);
        }
    }

    private void init_test2() {
        I = 5;
        J = 5;
        C.addAll(LinSupUtils.toList(new double[] { -0.6, 1.9, -1.2, 2.6, 0.1} ));
        B.addAll(LinSupUtils.toList(new double[] { 10.5, 10.2, 10.5, 12.0, 11.6} ));

        for (int i = 0; i < I; i++) {
            A.add(new ArrayList<Double>());
        }
        A.get(0).addAll(LinSupUtils.toList(new double[] {0.5, -1.2, 0.8, 1.9, 1.1} ));
        A.get(1).addAll(LinSupUtils.toList(new double[] {0.9, 0.2, -1.8, -0.7, 0.3} ));
        A.get(2).addAll(LinSupUtils.toList(new double[] {2.0, -1.4, 0.5, 1.1, 1.9} ));
        A.get(3).addAll(LinSupUtils.toList(new double[] {1.5, -0.3, -0.6, 2.0, 0.4} ));
        A.get(4).addAll(LinSupUtils.toList(new double[] {-0.7, 1.9, 1.1, 1.0, 1.6} ));

        // LinSup init-s
        for (int j = 0; j < J; j++) {
            y0.add(10.0);
        }
    }

    private void initRandom() {
        for (int j = 0; j < J; j++) {
            y0.add(10.0);
        }
        for (int j = 0; j < J; j++) {
            C.add(LinSupUtils.getRandom(-2.0, 3.0));
        }
        for (int i = 0; i < I; i++) {
            A.add(new ArrayList<Double>());
            for (int j = 0; j < J; j++) {
                A.get(i).add(LinSupUtils.getRandom(-1.0,2.0));
            }
        }

        for (int i = 0; i < I; i++) {
            B.add(LinSupUtils.getRandom(5.0,15.0));
        }
    }

    private boolean stop = false;
    private void startLinSup() {
        stop = false;
        int k = 0;                         // k <- 0

        yk.add(new ArrayList<Double>());   // yk(0) <- y0
        yk.get(k).addAll(y0);

        lk.put(-1, 0);                      // lk(-1) <- 0
        HashMap<Integer, HashMap<Integer, ArrayList<Double>>> ykn = new HashMap<>();

        while (getProximityFun(yk.get(k)) > e ||
                (k==0 || getStoppingRule(yk.get(k-1),yk.get(k)) > e2)) {

            int n = 0;                      // n <- 0
            int l = LinSupUtils.getRandom(k, lk.get(k-1)); // l <- rand(k,lk(k-1)

            ykn.put(k, new HashMap<Integer, ArrayList<Double>>());  // ykn <- yk
            ykn.get(k).put(n, new ArrayList<Double>());
            ykn.get(k).get(n).addAll(yk.get(k));

            while (n < N) {

                Double betta = Math.pow(a, l);   // Bkn <- nl
                ArrayList<Double> z = LinSupUtils.getZ(ykn.get(k).get(n), betta, C); // z <- ykn - betta* c / ||c||2

                n = n + 1;                        // n <- n+1

                ykn.get(k).put(n, new ArrayList<Double>());
                ykn.get(k).get(n).addAll(z);    // ykn <- z

                l = l + 1;                        // l <- l+1

            }


            lk.put(k, l);                       // lk <- l

            yk.add(new ArrayList<Double>());    // y(k+1) <- A (ykn(k,N))
            yk.get(k+1).addAll(getNextYbyA(ykn.get(k).get(N)));

            k = k+1;                            // k <- k+1

        }

    }

    private double getStoppingRule(ArrayList<Double> prev, ArrayList<Double> next) {
        ArrayList<Double> temp = LinSupUtils.vectorsDifference(next, prev);
        double result = LinSupUtils.secondVectorNorm(temp) / LinSupUtils.secondVectorNorm(next);
        //Log.d(TAG, "getStoppingRule " + result);
        return result;
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

        //Log.d(TAG, "getProximityFun = " + (sum1+sum2));

        return sum1+sum2;
    }

    private ArrayList<Double> getPHi(ArrayList<Double> z, int i) {
        ArrayList<Double> result = new ArrayList<>();
        result.addAll(z);

        if (LinSupUtils.innerProduction(A.get(i),z) > B.get(i)) {
            Double temp = (LinSupUtils.innerProduction(A.get(i),z) - B.get(i));
            temp = (temp) / LinSupUtils.secondVectorNorm(A.get(i));
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



        if (getProximityFun(result) < e) {
            stop = true;
        }

        for (int a = 0; a < result.size(); a++) {
            if (result.get(a) < 0.0) {
                result.set(a,0.0);
            }
        }
        //getProximityFun(result);

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


    private int simplex() {

        // describe the optimization problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(LinSupUtils.toArray(C), 0);
        Collection<LinearConstraint> constraints = new ArrayList();
        for (int i = 0; i < I; i++) {
            constraints.add(new LinearConstraint(LinSupUtils.toArray(A.get(i)), Relationship.LEQ, B.get(i)));
        }

        // create and run the solver
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(10000);
        solution = solver.optimize(f, constraints, GoalType.MINIMIZE, true);
        return solver.getIterations();
    }


    private void printSimplex(int iters) {
        if (solution != null) {
            String log = "Simplex MINIMIZE";
            Log.i(TAG, "----------- Simplex function MINIMIZE ---------");
            //Log.i(TAG, "STEPS: " + iters);
            Log.i(TAG, "f(x) = " + solution.getValue());
            log += "\n" + "f(x) = " + solution.getValue();
            for (int j = 0; j < J; j++) {
                Log.i(TAG, "x("+j+") = " + solution.getPoint()[j]);
                log += "\n" + "x("+j+") = " + solution.getPoint()[j];
            }
            Log.i(TAG, "----------- -------------------------- ---------");
            log+="\n--------------------";
            if (mCallback != null) {
                mCallback.simplexResult(log);
            }
        }
    }

    private void printLinSup() {
        Log.i(TAG, "----------- LinSup function MINIMIZE ---------");
        String log = "LinSup MINIMIZE";
        //Log.i(TAG, "STEPS: " + (yk.size() - 1));
        Log.i(TAG, "f(x) = " + LinSupUtils.innerProduction(C, yk.get(yk.size() - 1)));
        log += "\n" + "f(x) = " + LinSupUtils.innerProduction(C, yk.get(yk.size() - 1));
        for (int j = 0; j < J; j++) {
            Log.i(TAG, "x(" + j + ") = " + yk.get(yk.size() - 1).get(j));
            log+= "\n" + "x(" + j + ") = " + yk.get(yk.size() - 1).get(j);
        }
        Log.i(TAG, "----------- -------------------------- ---------");
        log+="\n--------------------";
        if (mCallback != null) {
            mCallback.simplexResult(log);
        }

    }

}
