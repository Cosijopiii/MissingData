package com.inaoe.ml.weka;

import weka.core.Attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class StoreAttAndIndexInstance {
    public ArrayList<Attribute> attribute;
    public int[] pos;
    public int anInt;

    public StoreAttAndIndexInstance(){

    }
    public StoreAttAndIndexInstance( ArrayList<Attribute> attribute,int anInt){
        this.anInt=anInt;
        this.attribute=attribute;
    }

    public int[] getPos() {

        ArrayList<Integer>temp=new ArrayList<>();

        for (int i:pos) {
            if (i>0){
                temp.add(i);
            }
        }

        int [] t2=new int[temp.size()];
         for (int i = 0; i < t2.length; i++) {
            t2[i]=temp.get(i);
        }
        return t2;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }
}
