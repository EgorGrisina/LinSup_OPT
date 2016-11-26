package ru.grit.egor.linsup.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class LinSupUtils {

    final static String TAG = LinSupUtils.class.getSimpleName();

    public static double innerProduction(ArrayList<Double> vec1, ArrayList<Double> vec2) {
        Double prod = 0.0;
        if (vec1.size() != vec2.size() || vec1.size() == 0) {
            Log.e(TAG, "innerProduction : bad vectors size!");
            return prod;
        }
        for (int i = 0; i < vec1.size(); i++) {
            prod += vec1.get(i)*vec2.get(i);
        }
        //Log.e(TAG, "innerProduction : prod = " + prod);
        return prod;
    }

    public static double secondVectorNorm(ArrayList<Double> vector) {
        Double norm = 0.0;
        if (vector.size() == 0) {
            Log.e(TAG, "secondVectorNorm : bad vector size");
            return norm;
        }

        for (Double a : vector) {
            norm += a*a;
        }

        //Log.e(TAG, "secondVectorNorm : norm = " + norm);
        return Math.sqrt(norm);
    }

    public static ArrayList<Double> production(ArrayList<Double> vec, Double x) {
        if (vec.size() == 0) {
            Log.e(TAG, "production : bad vector size!");
            return vec;
        }

        ArrayList<Double> result = new ArrayList<Double>();

        for (Double elem : vec) {
            result.add(elem*x);
        }

        return result;
    }

    public static ArrayList<Double> vectorsDifference(ArrayList<Double> vec1, ArrayList<Double>  vec2) {

        if (vec1.size() != vec2.size() || vec1.size() == 0) {
            Log.e(TAG, "vectorsDifference : bad vectors size!");
            return vec1;
        }

        ArrayList<Double> result = new ArrayList<Double>();

        for (int i = 0; i < vec1.size(); i++) {
            result.add(vec1.get(i) - vec2.get(i));
        }

        return result;
    }

    public static ArrayList<Double> vectorsSum(ArrayList<Double> vec1, ArrayList<Double>  vec2) {

        if (vec1.size() != vec2.size() || vec1.size() == 0) {
            Log.e(TAG, "vectorsSum : bad vectors size!");
            return vec1;
        }

        ArrayList<Double> result = new ArrayList<Double>();

        for (int i = 0; i < vec1.size(); i++) {
            result.add(vec1.get(i) + vec2.get(i));
        }

        return result;
    }

    public static int getRandom(int low, int high) {
        if (high < low) {
            Log.e(TAG, "getRandom : bad values!");
        }
        if (high == 0 && low == 0) {
            Log.i(TAG, "getRandom : zero values!");
            return 0;
        }
        Random r = new Random();
        int result = r.nextInt(high-low) + low;
        return result;
    }

    public static double getRandom(double low, double high) {
        if (high < low) {
            Log.e(TAG, "getRandom : bad values!");
        }
        if (high == 0.0 && low == 0.0) {
            Log.i(TAG, "getRandom : zero values!");
            return 0;
        }
        Random r = new Random();
        double result = low + (high - low) * r.nextDouble();
        return result;
    }

    public static ArrayList<Double> getZ (ArrayList<Double> y, Double betta, ArrayList<Double> c) {
        if (y.size() != c.size() || y.size() == 0) {
            Log.e(TAG, "getZ : bad vectors size!");
            return y;

        }

        Double k = betta * (1.0 / secondVectorNorm(c));
        ArrayList<Double> temp = production(c, k);

        ArrayList<Double> z = vectorsDifference(y,temp);
        return z;
    }

    public static double[] toArray(ArrayList<Double> list) {
        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = (double) list.get(i);
        }
        return array;
    }

    public static ArrayList<Double> toList(double[] array) {
        ArrayList<Double> list = new ArrayList<>();
        for (double i : array) {
            list.add(i);
        }
        return list;
    }


}
