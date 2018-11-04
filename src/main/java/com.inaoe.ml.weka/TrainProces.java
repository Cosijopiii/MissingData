package com.inaoe.ml.weka;


import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class TrainProces {

    public String path;//="src/main/resources/DATABASE/MICEImp/";
    public String file;//="Credit-MICE";
    public String path2;
    public String file2;
    public ArrayList<wraperClassifierAndWeight> classifierAndWeights;
    public int []fs;
    public TrainProces(String path,String file,String path2, String file2){
        this.path=path;
        this.file=file;
        this.path2=path2;
        this.file2=file2;
    }

    /*TODO: Hacer que los metodos de clasificacion retornen el modelo y los pesos
      TODO: Necesito los valores que se reducieron al momento de la seleccion de caracteristicas
      Explicacion: Genera N clasificadores por cada patron completo (Le quito a los datos que estan reducidas en caracteristicas
      las columnas con valores faltantes), es un tipo de clasificador por cada dato.
     */
    public void TrainningProcess() throws Exception {
        /*
        * Obtener las caracteristicas seleccionados
        * */
        Weka_main util=new Weka_main();
        //instancias con las carateristicas seleccionadas
        Instances instancesOriginal=util.Open(path2+file2);
        classifierAndWeights=new ArrayList<>();
        Instances instances=util.Open(path+file);// instancia ya imputada
        fs=util.cfs_attributes(instances); //selecciono caracteristicas
        Instances rfs=util.remove_attributes(fs,instancesOriginal); // remuevo caracteristicas seleccionadas
        Missingpattern missingpattern=new Missingpattern(rfs); // encuentro missing patterns
        ArrayList<StoreAttAndIndexInstance> storeAttAndIndexInstances= missingpattern.getPatterns();// obtengo un array
        storeAttAndIndexInstances.forEach(storeAttAndIndexInstance -> System.out.println(Arrays.toString(storeAttAndIndexInstance.pos)));
        System.out.println(rfs);
        for (StoreAttAndIndexInstance s: storeAttAndIndexInstances) {  //por cada missin parttern hacer:
            try {
                Instances cp= util.remove_attributes(s.getPos(),rfs);
                Classifier ci=util.Classifier_J48(cp);
                double wi=util.Weight_Classifier(util.Evaluation_Classifier(ci,cp));
                classifierAndWeights.add(new wraperClassifierAndWeight(ci,wi));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }



    }

    public void print(){
        System.out.println(Arrays.toString(fs));
        classifierAndWeights.forEach(System.out::println);
    }


}
