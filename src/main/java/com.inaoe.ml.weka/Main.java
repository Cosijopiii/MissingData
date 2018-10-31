package com.inaoe.ml.weka;

import weka.Weka_main;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

     public static void main (String argv[]){
        Weka_main wm = new Weka_main();
        Missingpattern m=new Missingpattern("Credit");
        ArrayList<StoreAttAndIndexInstance> t= m.getPatterns();
         for (StoreAttAndIndexInstance s:t) {
             System.out.println(Arrays.toString(s.getPos())+" "+s.anInt);
         }
        //cfs_attributes(
    }

}
/*
* Utilizar reductores de instancias ara poder mejorar el accuray de clasificador añandiendo esto despues de la seleccion de caracteristicas
* se deberia probar tanto antes de la seleccion de caracteristicas o despues para probar que tan mejor funciona
*
*aplicar reductores de instancias antes de realizar la imputacion  para tratar de reducir los patrones perdidos
* y aligerar el dataset
*
*
* */