package com.inaoe.ml.weka;

import weka.core.Attribute;

import java.util.ArrayList;

public class StoreAttAndIndexInstance {
    public ArrayList<Attribute> attribute;
    public int anInt;

    public StoreAttAndIndexInstance(){

    }
    public StoreAttAndIndexInstance( ArrayList<Attribute> attribute,int anInt){
        this.anInt=anInt;
        this.attribute=attribute;
    }

}
