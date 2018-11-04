package com.inaoe.ml.weka;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Missingpattern {
    public String file;
     Instances instances;


    public Missingpattern(Instances instances){
        this.instances=instances;
    }


    public ArrayList<StoreAttAndIndexInstance> getPatterns( ){

         //String format = ".arff";
        // Instances get=w.Open( file+format);
        Instances get=instances;
        ArrayList<Attribute> temp;
        ArrayList<StoreAttAndIndexInstance> MP=new ArrayList<>();

        for (int i=0; i<get.numInstances();i++){
            temp=new ArrayList<>();
            int[] ints=new int[get.numAttributes()-1];
            for (int k = 0; k < get.numAttributes()-1; k++) {
                ints[k]=0;
            }
            for (int j = 0; j < get.numAttributes(); j++) {
                String v=getvalue(get.instance(i).attribute(j),get.instance(i),j);
                if (v.equals("?")||v.equals("NaN")) {
                    temp.add(get.instance(i).attribute(j));
                    ints[j] = 1;
                }
          }
          if (temp.size()>0) {
                StoreAttAndIndexInstance tt=new StoreAttAndIndexInstance(temp,i);
                tt.setPos(ints);
                MP.add(tt);
            }
        }
      return MP;
    }
    private String getvalue(Attribute a, Instance i, int k){
        if (a.isNominal()){
            return i.stringValue(k);
        }
        if (a.isNumeric()){
            return String.valueOf(i.value(k));
        }
        return " ";
    }
}
