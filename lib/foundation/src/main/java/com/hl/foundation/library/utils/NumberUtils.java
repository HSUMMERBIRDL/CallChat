package com.hl.foundation.library.utils;

import java.util.Random;

/**
 * des:
 * Created by HL
 * on 2017-05-12.
 */

public class NumberUtils {

    public static int getRandomIntBeteen(int min,int max){
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
}
