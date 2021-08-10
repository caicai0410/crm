package com.yjxxt.utils;

import com.yjxxt.exceptions.ParamsException;

public class AssertUtil {

    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }

}
