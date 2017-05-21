package ru.grit.egor.linsup.utils;

import java.util.ArrayList;
import java.util.Random;

public class LinSupUtils {

    final static String TAG = LinSupUtils.class.getSimpleName();
    final static boolean DEBUG = true;

    /*
     * Описание: Скалярное произведение векторов
     * Тип данных: Double
     * Входные данные: Два вектора одинаковой размерности
     * Возвращаемые данные: Число, произведение векторов
     */
    public static double innerProduction(ArrayList<Double> vec1, ArrayList<Double> vec2) {
        if (DEBUG) Log.i(TAG, "[innerProduction] start");
        double prod = 0.0;

        if (vec1 == null || vec2 == null) {
            if (DEBUG) Log.e(TAG, "[innerProduction] bad vector null");
            return prod;
        }

        if (DEBUG) Log.v(TAG, "[innerProduction] vec1.size: " + vec1.size() + " vec2.size: " + vec2.size());

        if (vec1.size() > vec2.size()) {
            if (DEBUG) Log.e(TAG, "[innerProduction] bad vectors size!");
            return prod;
        }

        if (vec1.size() != vec2.size() || vec1.size() == 0) {
            if (DEBUG) Log.w(TAG, "[innerProduction] bas vectors size");
        }

        for (int i = 0; i < vec1.size(); i++) {
            prod += vec1.get(i)*vec2.get(i);
            if (DEBUG) Log.d(TAG, "[innerProduction] step: " + i + " vec1[i]: " +vec1.get(i) +" vec2[i]: " + vec2.get(i) + " prod: " + prod);
        }

        if (DEBUG) Log.i(TAG, "[innerProduction] stop");
        if (DEBUG) Log.v(TAG, "[innerProduction] prod: " + prod);

        return prod;
    }

    /*
     * Описание: Вычисление второй нормы вектора
     * Тип данных: Double
     * Входные данные: Вектор чисел
     * Возвращаемые данные: Число, квадратный корень из суммы квадратов вектора
     */
    public static double secondVectorNorm(ArrayList<Double> vector) {
        if (DEBUG) Log.i(TAG, "[secondVectorNorm] start");

        double norm = 0.0;
        if (vector == null) {
            Log.e(TAG, "[secondVectorNorm] bad vector null");
            return norm;
        }
        if (vector.size() == 0) {
            Log.w(TAG, "[secondVectorNorm] bad vector size = 0");
            return norm;
        }

        if (DEBUG) Log.v(TAG, "[secondVectorNorm] vec.size: " + vector.size());

        for (Double a : vector) {
            norm += a*a;
            Log.d(TAG, "[secondVectorNorm] norm: " + norm + " a: " + a);
        }

        if (DEBUG) Log.i(TAG, "[secondVectorNorm] stop");
        if (DEBUG) Log.v(TAG, "[secondVectorNorm] norm: " + norm);

        return Math.sqrt(norm);
    }

    /*
     * Описание: Произведение вектора на число
     * Тип данных: Double
     * Входные данные: Вектор чисел и множитель
     * Возвращаемые данные: Вектор чисел, равный произведению каждого элемента на множитель
     */
    public static ArrayList<Double> production(ArrayList<Double> vec, Double x) {

        if (DEBUG) Log.i(TAG, "[production] start");

        ArrayList<Double> result = new ArrayList<Double>();

        if (vec == null || x == null) {
            if (DEBUG) Log.e(TAG, "[production] bad input values null");
            return result;
        }

        if (DEBUG) Log.v(TAG, "[production] vec.size: " + vec.size() + " x: " + x);

        if (vec.size() == 0) {
            if (DEBUG) Log.w(TAG, "[production] bad vector size = 0");
            return result;
        }

        for (int i = 0; i < vec.size(); i++) {
            result.add(vec.get(i)*x);
            if (DEBUG) Log.d(TAG, "[production] step: " + i + " vec1[i]: " +vec.get(i) +" x: " + x + " result: " + result.get(i));
        }

        if (DEBUG) {
            Log.i(TAG, "[production] stop");
            String res = "";
            for (Double d : result) res += d + " ";
            Log.v(TAG, "[production] prod: " + res);
        }

        return result;
    }

    /*
     * Описание: Разница векторов
     * Тип данных: Double
     * Входные данные: Два вектора одинаковой размерности
     * Возвращаемые данные: Вектор той же размерности, равный разнице элементов первого и второго вектора. res = (vec1 - vec2)
     */
    public static ArrayList<Double> vectorsDifference(ArrayList<Double> vec1, ArrayList<Double>  vec2) {

        if (DEBUG) Log.i(TAG, "[vectorsDifference] start");

        ArrayList<Double> result = new ArrayList<Double>();

        if (vec1 == null || vec2 == null) {
            if (DEBUG) Log.e(TAG, "[vectorsDifference] bad vector null");
            return result;
        }

        if (DEBUG) Log.v(TAG, "[vectorsDifference] vec1.size: " + vec1.size() + " vec2.size: " + vec2.size());

        if (vec1.size() > vec2.size() || vec1.size() == 0) {
            if (DEBUG) Log.w(TAG, "[vectorsDifference] bad vectors size!");
            return result;
        }

        for (int i = 0; i < vec1.size(); i++) {
            result.add(vec1.get(i) - vec2.get(i));
            if (DEBUG) Log.d(TAG, "[vectorsDifference] step: " + i + " vec1[i]: " +vec1.get(i) +" vec2[i]: " + vec2.get(i) + " result[i]: " + result.get(i));
        }

        if (DEBUG) {
            Log.i(TAG, "[vectorsDifference] stop");
            String res = "";
            for (Double d : result) res += d + " ";
            Log.v(TAG, "[vectorsDifference] result: " + res);
        }

        return result;
    }

    /*
     * Описание: Сумма векторов
     * Тип данных: Double
     * Входные данные: Два вектора одинаковой размерности
     * Возвращаемые данные: Вектор той же размерности, равный сумме элементов первого и второго вектора. res = (vec1 + vec2)
     */
    public static ArrayList<Double> vectorsSum(ArrayList<Double> vec1, ArrayList<Double>  vec2) {

        if (DEBUG) Log.i(TAG, "[vectorsSum] start");

        ArrayList<Double> result = new ArrayList<Double>();

        if (vec1 == null || vec2 == null) {
            if (DEBUG) Log.e(TAG, "[vectorsSum] bad vector null");
            return result;
        }

        if (DEBUG) Log.v(TAG, "[vectorsSum] vec1.size: " + vec1.size() + " vec2.size: " + vec2.size());

        if (vec1.size() > vec2.size() || vec1.size() == 0) {
            if (DEBUG) Log.w(TAG, "[vectorsSum] bad vectors size!");
            return result;
        }

        for (int i = 0; i < vec1.size(); i++) {
            result.add(vec1.get(i) + vec2.get(i));
            if (DEBUG) Log.d(TAG, "[vectorsSum] step: " + i + " vec1[i]: " +vec1.get(i) +" vec2[i]: " + vec2.get(i) + " result[i]: " + result.get(i));

        }

        if (DEBUG) {
            Log.i(TAG, "[vectorsSum] stop");
            String res = "";
            for (Double d : result) res += d + " ";
            Log.v(TAG, "[vectorsSum] result: " + res);
        }

        return result;
    }

    /*
     * Описание: Случайное число
     * Тип данных: Integer
     * Входные данные: Верхняя и нижняя граница случайных чисел.
     * Возвращаемые данные: Случайное число в заданном диапазоне
     */
    public static int getRandom(int low, int high) {

        if (DEBUG) Log.i(TAG, "[getRandom int] start");
        if (DEBUG) Log.v(TAG, "[getRandom int] low: " + low + " high: " + high);

        if (high < low) {
            if (DEBUG) Log.e(TAG, "[getRandom int] : bad values!");
            return 0;
        }

        if (high == 0 && low == 0) {
            if (DEBUG) Log.w(TAG, "[getRandom int] zero values!");
            return 0;
        }

        if (high == low) {
            if (DEBUG) Log.w(TAG, "[getRandom int] equals values!");
            return low;
        }

        Random r = new Random();
        int result = r.nextInt(high-low) + low;

        if (DEBUG) {
            Log.i(TAG, "[getRandom int] stop");
            Log.v(TAG, "[getRandom int] result: " + result);
        }

        return result;
    }

    /*
     * Описание: Случайное число
     * Тип данных: Double
     * Входные данные: Верхняя и нижняя граница случайных чисел.
     * Возвращаемые данные: Случайное число в заданном диапазоне
     */
    public static double getRandom(double low, double high) {

        if (DEBUG) Log.i(TAG, "[getRandom double] start");
        if (DEBUG) Log.v(TAG, "[getRandom double] low: " + low + " high: " + high);

        if (high < low) {
            if (DEBUG) Log.e(TAG, "[getRandom double] : bad values!");
            return 0;
        }

        if (high == 0.0 && low == 0.0) {
            if (DEBUG) Log.w(TAG, "[getRandom double] zero values!");
            return 0;
        }

        Random r = new Random();
        double result = low + (high - low) * r.nextDouble();

        if (DEBUG) {
            Log.i(TAG, "[getRandom double] stop");
            Log.v(TAG, "[getRandom double] result: " + result);
        }

        return result;
    }

    /*
     * Описание: Вычисление вектора Z
     * Тип данных: Double
     * Входные данные: Вектор Y и С одинаковых размерностей и множитель betta.
     * Возвращаемые данные: Вектор той же размерности Z = Y - C * (betta/||C||)
     */
    public static ArrayList<Double> getZ(ArrayList<Double> y, Double betta, ArrayList<Double> c) {

        if (DEBUG) Log.i(TAG, "[getZ] start");

        if (y.size() == 0 || c.size() == 0) {
            if (DEBUG) Log.w(TAG, "[getZ] some vector size = 0");
        }

        if (y.size() != c.size()) {
            if (DEBUG) Log.e(TAG, "[getZ] bad vectors size!");
            return y;
        }
        if (DEBUG) Log.v(TAG, "[getZ] y.size: " + y.size() + "c.size: " + c.size() + " betta: " + betta);
        Double norm = secondVectorNorm(c);
        Double k = betta * (1.0 / secondVectorNorm(c));
        if (DEBUG) Log.d(TAG, "[getZ] k = betta/||c|| = " + k + " ||c|| = " + norm);
        ArrayList<Double> temp = production(c, k);
        if (DEBUG) {
            String res = "";
            for (Double d : temp) res += d + " ";
            Log.d(TAG, "[getZ] temp = c*k = " + res);
        }
        ArrayList<Double> z = vectorsDifference(y,temp);
        if (DEBUG) {
            String res = "";
            for (Double d : z) res += d + " ";
            Log.d(TAG, "[getZ] result = y - c*k = " + res);
            Log.i(TAG, "[getZ] stop");
        }

        return z;
    }

    /*
     * Описание: Преобразование односвязного списка в массив чисел
     * Тип данных: Double
     * Входные данные: Односвязный список из чисел.
     * Возвращаемые данные: Массив чисел той же размерности
     */
    public static double[] toArray(ArrayList<Double> list) {

        if (DEBUG) Log.i(TAG, "[toArray] start");

        if (list == null) {
            if (DEBUG) Log.e(TAG, "[toArray] vector is null");
            return new double[0];
        }

        if (list.size() == 0) {
            if (DEBUG) Log.w(TAG, "[toArray] vector size = 0");
        }

        double[] array = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = (double) list.get(i);
        }

        if (DEBUG) {
            String res = "";
            for (Double d : array) res += d + " ";
            Log.d(TAG, "[toArray] array: " + res);
            Log.i(TAG, "[toArray] stop");
        }

        return array;
    }

    /*
     * Описание: Преобразование массива чисел в односвязный список
     * Тип данных: Double
     * Входные данные: Массив чисел.
     * Возвращаемые данные: Односвязный список из чисел той же размерности
     */
    public static ArrayList<Double> toList(double[] array) {

        if (DEBUG) Log.i(TAG, "[toList] start");

        if (array == null) {
            if (DEBUG) Log.e(TAG, "[toList] array is null");
            return new ArrayList<Double>();
        }

        if (DEBUG && array.length == 0) {
            Log.w(TAG, "[toList] array.length = 0");
        }

        ArrayList<Double> list = new ArrayList<>();
        for (double i : array) {
            list.add(i);
        }

        if (DEBUG) {
            String res = "";
            for (Double d : list) res += d + " ";
            Log.d(TAG, "[toList] list: " + res);
            Log.i(TAG, "[toList] stop");
        }

        return list;
    }

}
