package com.inaoe.ml.weka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class Main {

     public static void main (String argv[]) throws Exception {
//        Weka_main wm = new Weka_main();
//        Missingpattern m=new Missingpattern("Credit");
//        ArrayList<StoreAttAndIndexInstance> t= m.getPatterns();
//         for (StoreAttAndIndexInstance s:t) {
//             System.out.println(Arrays.toString(s.getPos())+" "+s.anInt);
//         }
        //cfs_attributes(
         String path="src/main/resources/DATABASE/MICEImp/";
          String file="Credit-MICE.arff";
         String pathOr="src/main/resources/DATABASE/OriginalDB/";
         String fileOr="Credit.arff";
         TrainProces trainProces=new TrainProces(path,file,pathOr,fileOr);
         trainProces.TrainningProcess();
        trainProces.print();



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