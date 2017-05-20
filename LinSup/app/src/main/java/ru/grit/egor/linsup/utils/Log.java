package ru.grit.egor.linsup.utils;

/**
 * Created by egor on 5/18/17.
 */

public class Log {
    public static void d(String TAG, String mes) {
        print("debug",TAG, mes);
    }
    public static void v(String TAG, String mes) {
        print("verbose",TAG, mes);
    }
    public static void w(String TAG, String mes) {
        print("warning",TAG, mes);
    }
    public static void e(String TAG, String mes) {
        print("error",TAG, mes);
    }
    public static void i(String TAG, String mes) {
        print("info",TAG, mes);
    }
    private static void print(String mode, String TAG, String mes) {
        System.out.println(mode+"/"+TAG+ "/ "+mes);
    }
}
