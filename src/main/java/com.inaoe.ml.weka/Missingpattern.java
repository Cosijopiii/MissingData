package com.inaoe.ml.weka;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Missingpattern {
    public String file;
    Weka_main w = new Weka_main();
    /**
     * @param file Es el nombre del archivo por ejemplo  "Credit"
     */
    public Missingpattern(String file){
        this.file=file;

       ArrayList<StoreAttAndIndexInstance> MP=getPatterns(file);
        for (StoreAttAndIndexInstance aMP : MP) {
            for (Attribute attribute:aMP.attribute) {
                System.out.println(attribute);
            }
            System.out.println(aMP.anInt);
        }

    }
    public ArrayList<StoreAttAndIndexInstance> getPatterns(String file){
        String pathDB = "src/main/resources/DATABASE/OriginalDB/";
        //String file="Credit";
        
        String format = ".arff";
        Instances get=w.Open(pathDB+file+format);
        ArrayList<Attribute> temp;
        ArrayList<StoreAttAndIndexInstance> MP=new ArrayList<>();
        for (int i=0; i<get.numInstances();i++){
            temp=new ArrayList<>();
            for (int j = 0; j < get.numAttributes(); j++) {
                String v=getvalue(get.instance(i).attribute(j),get.instance(i),j);
                if (v.equals("?")||v.equals("NaN"))
                    temp.add(get.instance(i).attribute(j));
          }
          if (temp.size()>0) MP.add(new StoreAttAndIndexInstance(temp,i));
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
