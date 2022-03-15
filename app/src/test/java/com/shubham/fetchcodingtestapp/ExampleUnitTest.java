package com.shubham.fetchcodingtestapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void checkCollectionsSort(){

        LinkedList<Item> testList = new LinkedList<>();
        testList.add(new Item(2,23,"Item1"));
        testList.add(new Item(1,24,"Item2"));
        testList.add(new Item(3,234,"Item3"));

        Collections.sort(testList);

        boolean result;
        if (testList.get(0).id>testList.get(1).id){
            result = false;
        }else result = true;
        assertEquals(result, true);

    }

    @Test
    public void checkFetchData(){



    }

}