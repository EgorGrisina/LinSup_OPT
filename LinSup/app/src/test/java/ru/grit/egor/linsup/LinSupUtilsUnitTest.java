package ru.grit.egor.linsup;

import org.junit.Test;

import java.util.ArrayList;

import ru.grit.egor.linsup.utils.LinSupUtils;
import ru.grit.egor.linsup.utils.Log;

import static org.junit.Assert.*;

/**
 * Created by egor on 5/18/17.
 */

public class LinSupUtilsUnitTest {

    private static String TAG = LinSupUtilsUnitTest.class.getSimpleName();
    private static double delta = 0.00001;

    // ----------------- InnerProduction ----------------- //

    @Test
    public void creation_Test() throws Exception {
        Log logs = new Log();
        assertNotNull(logs);
        LinSupUtils utils = new LinSupUtils();
        assertNotNull(utils);
    }

    @Test
    public void innerProduction_isNull() throws Exception {
        assertEquals(0.0, LinSupUtils.innerProduction(null, new ArrayList<Double>()), delta);
        assertEquals(0.0, LinSupUtils.innerProduction(new ArrayList<Double>(), null), delta);
        assertEquals(0.0, LinSupUtils.innerProduction(null, null), delta);
    }

    @Test
    public void innerProduction_isEmpty() throws Exception {
        assertEquals(0.0, LinSupUtils.innerProduction(new ArrayList<Double>(), new ArrayList<Double>()), delta);
    }

    @Test
    public void innerProduction_withZeros() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 0.0,0.0,0.0);
        addToList(second, 0.0,0.0,0.0);

        assertEquals(0.0, LinSupUtils.innerProduction(first, second), delta);
    }

    @Test
    public void innerProduction_withDiffLength() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 1.0, 1.0);
        addToList(second, 1.0, 1.0, 1.0, 1.0, 1.0);

        assertEquals(0.0, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(2.0, LinSupUtils.innerProduction(second, first), delta);

    }

    @Test
    public void innerProduction_testSimpleValues() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 1.0);
        addToList(second, 1.0);
        assertEquals(1.0, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(1.0, LinSupUtils.innerProduction(second, first), delta);

        first.clear();
        second.clear();
        addToList(first, 1.0, 2.0, 3.0);
        addToList(second, 3.0, 2.0, 3.0);

        assertEquals(16.0, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(16.0, LinSupUtils.innerProduction(second, first), delta);

    }
    @Test
    public void innerProduction_testNegativeValues() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, -1.0);
        addToList(second, 1.0);

        assertEquals(-1.0, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(-1.0, LinSupUtils.innerProduction(second, first), delta);

        first.clear();
        second.clear();
        addToList(first, -1.0, 2.0, 3.0);
        addToList(second, 3.0, -2.0, 3.0);

        assertEquals(2.0, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(2.0, LinSupUtils.innerProduction(second, first), delta);

    }

    @Test
    public void innerProduction_testBigValues() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 100000.0);
        addToList(second, 10.0);
        assertEquals(1000000.0, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(1000000.0, LinSupUtils.innerProduction(second, first), delta);

        first.clear();
        second.clear();
        addToList(first, 10000.0, 20000.0, 30000.0);
        addToList(second, 30000.0, 20000.0, 30000.0);

        assertEquals(1.6E9, LinSupUtils.innerProduction(first, second), delta);
        assertEquals(1.6E9, LinSupUtils.innerProduction(second, first), delta);

    }

    // ----------------- SecondVectorNorm ----------------- //

    @Test
    public void secondVectorNorm_isEmpty() throws Exception {
        assertEquals(0.0, LinSupUtils.secondVectorNorm(new ArrayList<Double>()), delta);
    }
    @Test
    public void secondVectorNorm_isNull() throws Exception {
        assertEquals(0.0, LinSupUtils.secondVectorNorm(null), delta);
    }
    @Test
    public void secondVectorNorm_withZeros() throws Exception {
        ArrayList<Double> list = new ArrayList<>();

        addToList(list, 0.0);
        assertEquals(0.0, LinSupUtils.secondVectorNorm(list), delta);

        list.clear();
        addToList(list, 0.0, 0.0, 0.0);
        assertEquals(0.0, LinSupUtils.secondVectorNorm(list), delta);

    }
    @Test
    public void secondVectorNorm_simpleValues() throws Exception {
        ArrayList<Double> list = new ArrayList<>();

        addToList(list, 2.0);
        assertEquals(2.0, LinSupUtils.secondVectorNorm(list), delta);

        list.clear();
        addToList(list, 1.0, 1.0, 1.0, 1.0);
        assertEquals(2.0, LinSupUtils.secondVectorNorm(list), delta);

        list.clear();
        addToList(list, -2.0, 2.0, 2.0, 2.0);
        assertEquals(4.0, LinSupUtils.secondVectorNorm(list), delta);

    }

    // ----------------- production ----------------- //

    @Test
    public void production_isEmpty() throws Exception {
        assertNotNull(LinSupUtils.production(new ArrayList<Double>(), 0.0));
        assertEquals(0, LinSupUtils.production(new ArrayList<Double>(), 0.0).size());
    }
    @Test
    public void production_isNull() throws Exception {
        ArrayList<Double> list = new ArrayList<>();
        addToList(list, 0.0, 0.0, 0.0);
        assertNotNull(LinSupUtils.production(list, null));
        assertNotNull(LinSupUtils.production(null, 0.0));
        assertNotNull(LinSupUtils.production(null, null));
    }
    @Test
    public void production_withZeros() throws Exception {
        ArrayList<Double> list = new ArrayList<>();
        Double x = 0.0;

        addToList(list, 0.0);
        ArrayList<Double> result = LinSupUtils.production(list, x);
        assertNotNull(result);
        assertEquals(list.size(), result.size());
        assertEquals(result.get(0), 0.0, delta);

        list.clear();
        addToList(list, 0.0, 0.0, 0.0);
        result = LinSupUtils.production(list, x);

        assertNotNull(result);
        assertEquals(list.size(), result.size());
        for (double d : result) {
            assertEquals(d,0.0,delta);
        }

        list.clear();
        addToList(list, 1.0, 1.0, 1.0);
        result = LinSupUtils.production(list, x);

        assertNotNull(result);
        assertEquals(list.size(), result.size());
        for (double d : result) {
            assertEquals(d,0.0,delta);
        }

        list.clear();
        addToList(list, 0.0, 0.0, 0.0);
        x = 0.0;
        result = LinSupUtils.production(list, x);

        assertNotNull(result);
        assertEquals(list.size(), result.size());
        for (double d : result) {
            assertEquals(d,0.0,delta);
        }

    }
    @Test
    public void production_simpleValues() throws Exception {
        ArrayList<Double> list = new ArrayList<>();
        Double x = 1.0;

        addToList(list, 1.0);
        ArrayList<Double> result = LinSupUtils.production(list, x);
        assertNotNull(result);
        assertEquals(list.size(), result.size());
        assertEquals(result.get(0), 1.0, delta);

        list.clear();
        addToList(list, 1.0, 1.0, 1.0);
        x = 1.0;
        result = LinSupUtils.production(list, x);

        assertNotNull(result);
        assertEquals(list.size(), result.size());
        for (double d : result) {
            assertEquals(d,1.0,delta);
        }

        list.clear();
        addToList(list, 2.0, 2.0, 2.0);
        x = 3.0;
        result = LinSupUtils.production(list, x);

        assertNotNull(result);
        assertEquals(list.size(), result.size());
        for (double d : result) {
            assertEquals(d,6.0,delta);
        }

        list.clear();
        addToList(list, -2.0, -2.0, -2.0);
        x = -2.0;
        result = LinSupUtils.production(list, x);

        assertNotNull(result);
        assertEquals(list.size(), result.size());
        for (double d : result) {
            assertEquals(d,4.0,delta);
        }

    }


    // ----------------- vectorsDifference ----------------- //

    @Test
    public void vectorsDifference_isEmpty() throws Exception {
        assertNotNull(LinSupUtils.vectorsDifference(new ArrayList<Double>(), new ArrayList<Double>()));
        assertEquals(0, LinSupUtils.vectorsDifference(new ArrayList<Double>(), new ArrayList<Double>()).size());
    }
    @Test
    public void vectorsDifference_isNull() throws Exception {
        ArrayList<Double> list = new ArrayList<>();
        addToList(list, 0.0, 0.0, 0.0);
        assertNotNull(LinSupUtils.vectorsDifference(list, null));
        assertNotNull(LinSupUtils.vectorsDifference(null, list));
        assertNotNull(LinSupUtils.vectorsDifference(null, null));
        assertNotNull(LinSupUtils.vectorsDifference(new ArrayList<Double>(), null));
        assertNotNull(LinSupUtils.vectorsDifference(null, new ArrayList<Double>()));
    }

    @Test
    public void vectorsDifference_withDiffLength() throws Exception {

        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();
        addToList(first, 1.0, 1.0);
        addToList(second, 1.0, 1.0, 1.0, 1.0);

        ArrayList<Double> result = LinSupUtils.vectorsDifference(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), first.get(i), delta);
        }

        first.clear();
        second.clear();
        result.clear();
        addToList(first, 2.0, 2.0, 1.0, 1.0);
        addToList(second, 1.0, 1.0);

        result = LinSupUtils.vectorsDifference(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), 1.0, delta);
        }

    }

    @Test
    public void vectorsDifference_withZeros() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 0.0);
        addToList(second, 0.0);
        ArrayList<Double> result = LinSupUtils.vectorsDifference(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        assertEquals(result.get(0), 0.0, delta);


        first.clear();
        second.clear();
        result.clear();
        addToList(first, 0.0, 0.0, 0.0);
        addToList(second, 0.0, 0.0, 0.0);

        result = LinSupUtils.vectorsDifference(first, second);

        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,0.0,delta);
        }

    }
    @Test
    public void vectorsDifference_simpleValues() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 3.0, 3.0, 3.0);
        addToList(second, 2.0, 2.0, 2.0);
        ArrayList<Double> result = LinSupUtils.vectorsDifference(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,1.0,delta);
        }
        result = LinSupUtils.vectorsDifference(second, first);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,-1.0,delta);
        }


        first.clear();
        second.clear();
        result.clear();
        addToList(first, -2.0, -2.0, -2.0);
        addToList(second, -3.0, -3.0, -3.0);

        result = LinSupUtils.vectorsDifference(first, second);

        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,1.0,delta);
        }

        result = LinSupUtils.vectorsDifference(second, first);

        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,-1.0,delta);
        }

    }


    // ----------------- vectorsSum ----------------- //

    @Test
    public void vectorsSum_isEmpty() throws Exception {
        assertNotNull(LinSupUtils.vectorsSum(new ArrayList<Double>(), new ArrayList<Double>()));
        assertEquals(0, LinSupUtils.vectorsSum(new ArrayList<Double>(), new ArrayList<Double>()).size());
    }
    @Test
    public void vectorsSum_isNull() throws Exception {
        ArrayList<Double> list = new ArrayList<>();
        addToList(list, 0.0, 0.0, 0.0);
        assertNotNull(LinSupUtils.vectorsSum(list, null));
        assertNotNull(LinSupUtils.vectorsSum(null, list));
        assertNotNull(LinSupUtils.vectorsSum(null, null));
        assertNotNull(LinSupUtils.vectorsSum(new ArrayList<Double>(), null));
        assertNotNull(LinSupUtils.vectorsSum(null, new ArrayList<Double>()));
    }

    @Test
    public void vectorsSum_withDiffLength() throws Exception {

        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();
        addToList(first, 1.0, 1.0, 1.0, 1.0);
        addToList(second, 1.0, 1.0);

        ArrayList<Double> result = LinSupUtils.vectorsSum(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), first.get(i), delta);
        }

        first.clear();
        second.clear();
        result.clear();
        addToList(first, 1.0, 1.0);
        addToList(second, 1.0, 1.0, 2.0, 2.0);

        result = LinSupUtils.vectorsSum(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), 2.0, delta);
        }

    }

    @Test
    public void vectorsSum_withZeros() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 0.0);
        addToList(second, 0.0);
        ArrayList<Double> result = LinSupUtils.vectorsSum(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        assertEquals(result.get(0), 0.0, delta);


        first.clear();
        second.clear();
        result.clear();
        addToList(first, 0.0, 0.0, 0.0);
        addToList(second, 0.0, 0.0, 0.0);

        result = LinSupUtils.vectorsSum(first, second);

        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,0.0,delta);
        }

    }
    @Test
    public void vectorsSum_simpleValues() throws Exception {
        ArrayList<Double> first = new ArrayList<>();
        ArrayList<Double> second = new ArrayList<>();

        addToList(first, 3.0, 3.0, 3.0);
        addToList(second, 2.0, 2.0, 2.0);
        ArrayList<Double> result = LinSupUtils.vectorsSum(first, second);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,5.0,delta);
        }
        result = LinSupUtils.vectorsSum(second, first);
        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,5.0,delta);
        }


        first.clear();
        second.clear();
        result.clear();
        addToList(first, -2.0, -2.0, -2.0);
        addToList(second, 3.0, 3.0, 3.0);

        result = LinSupUtils.vectorsSum(first, second);

        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,1.0,delta);
        }

        result = LinSupUtils.vectorsSum(second, first);

        assertNotNull(result);
        assertEquals(result.size(), first.size());
        for (double d : result) {
            assertEquals(d,1.0,delta);
        }

    }

    // -------------- getRandom --------------- ///

    @Test
    public void getRandom_Integer() throws Exception {
        for (int i = 0; i < 100; i++) {
            assertEquals(LinSupUtils.getRandom(0, 0), 0);
            assertEquals(LinSupUtils.getRandom(2, 1), 0);
            assertTrue(LinSupUtils.getRandom(0, 10) >= 0);
            assertTrue(LinSupUtils.getRandom(-10, 0) <= 0);
            assertTrue(LinSupUtils.getRandom(10, 10) == 10);
            assertTrue(LinSupUtils.getRandom(-10, -10) == -10);
            assertTrue(LinSupUtils.getRandom(-20, -10) < -9);
            assertTrue(LinSupUtils.getRandom(-20, -10) > -21);
        }

    }

    @Test
    public void getRandom_Double() throws Exception {
        for (int i = 0; i < 100; i++) {
            assertEquals(LinSupUtils.getRandom(0.0, 0.0), 0.0, delta);
            assertEquals(LinSupUtils.getRandom(2.0, 1.0), 0.0, delta);
            assertTrue(LinSupUtils.getRandom(0.0, 10.0) >= 0.0);
            assertTrue(LinSupUtils.getRandom(-10.0, 0.0) <= 0.0);
            assertEquals(LinSupUtils.getRandom(10.0, 10.0), 10.0, delta);
            assertEquals(LinSupUtils.getRandom(-10.0, -10.0), -10.0, delta);
            assertTrue(LinSupUtils.getRandom(-20.0, -10.0) < -9.0);
            assertTrue(LinSupUtils.getRandom(-20.0, -10.0) > -21.0);
        }

    }

    // ------------------- toList toArray ---------------- //

    @Test
    public void toList_all() throws Exception {

        assertNotNull(LinSupUtils.toList(null));
        assertNotNull(LinSupUtils.toList(new double[]{}));
        assertNotNull(LinSupUtils.toList(new double[]{0}));
        assertEquals(LinSupUtils.toList(new double[]{}).size(), 0);
        assertEquals(LinSupUtils.toList(new double[]{1,2}).size(), 2);

        double[] array = new double[]{1,2,3,4,5};
        ArrayList<Double> list = LinSupUtils.toList(array);
        assertEquals(list.size(), array.length);
        for (int i = 0; i < array.length; i++) {
            assertEquals(list.get(i), array[i], delta);
        }

    }

    @Test
    public void toArray_all() throws Exception {

        assertNotNull(LinSupUtils.toArray(null));
        assertNotNull(LinSupUtils.toArray(new ArrayList<Double>()));
        ArrayList<Double> list = new ArrayList<>();
        addToList(list, 1,2,3);
        assertNotNull(LinSupUtils.toArray(list));
        assertEquals(LinSupUtils.toArray(new ArrayList<Double>()).length, 0);
        assertEquals(LinSupUtils.toArray(list).length, 3);

        addToList(list, 1,2,3,4,5);
        double[] array = LinSupUtils.toArray(list);
        assertEquals(list.size(), array.length);
        for (int i = 0; i < array.length; i++) {
            assertEquals(list.get(i), array[i], delta);
        }

    }


    private void addToList(ArrayList<Double> list, double ... args) {
        for (double d : args) {
            list.add(d);
        }
    }
}