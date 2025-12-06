package org.v_04_01;

import org.v_04_01.rest.RESTHelper;

public class RunServer {
    public static void main(String[] args) {
        RESTHelper restHelper = new RESTHelper();
        try {
            restHelper.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
