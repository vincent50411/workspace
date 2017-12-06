package com.huntkey;

import util.HttpClientUtil2;

/**
 * Created by wangyq on 2017/8/21 0021.
 */
class TestMain {
    public static void main(String[] args) {
        String url = args[0];
        String type = args[1];
        String stringJson  = args[2];
        if("post".equals(type)){
            HttpClientUtil2.httpPostRaw(url,stringJson,null,null);
        }
    }
}
