package com.wisdom.mybase;

import org.junit.Test;

import static org.junit.Assert.*;

import com.wisdom.wdibrary.utils.AES256;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        String str = AES256.mySort(Arrays.toString(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        System.out.println("排序str = " + str);
    }
}