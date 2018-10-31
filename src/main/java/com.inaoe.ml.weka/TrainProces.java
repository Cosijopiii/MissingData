package com.inaoe.ml.weka;


import weka.core.Instances;

import java.util.ArrayList;

public class TrainProces {

    public String path="src/main/resources/DATABASE/FeatureSelection/CFS/MICE/";
    public String file="Credit";
    public ArrayList<Double> w=new ArrayList<>();
    public TrainProces(){

    }

    /*TODO: Hacer que los metodos de clasificacion retornen el modelo y los pesos
      TODO: Necesito los valores que se reducieron al momento de la seleccion de caracteristicas
      Explicacion: Genera N clasificadores por cada patron completo (Le quito a los datos que estan reducidas en caracteristicas
      las columnas con valores faltantes), es un tipo de clasificador por cada dato.

     */
    public void TrainningProcess(){
        /*
        * Obtener las caracteristicas seleccionados
        * */
        Weka_main util=new Weka_main();
        Instances instances=util.Open(path+file);
        Missingpattern missingpattern=new Missingpattern(path+file);
        ArrayList<StoreAttAndIndexInstance> storeAttAndIndexInstances= missingpattern.getPatterns();
        for (StoreAttAndIndexInstance s: storeAttAndIndexInstances) {

            try {
            Instances cp= util.remove_attributes(s.getPos(),instances);
          double wi= util.Classifier_J48(cp);
            w.add(wi);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }



    }


}
