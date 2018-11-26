package com.inaoe.ml.weka;


import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;

public class TrainProces {

    public ArrayList<WrapperClassifierAndWeight> classifierAndWeights;
    public int []fs;
    public  Instances miceTrain;
    public  Instances originalTrain;
    public   ArrayList<ArrayList<Attribute>> schemaAttributes;
    public TrainProces(Instances miceTrain,Instances originalTrain){
        this.miceTrain=miceTrain;
        this.originalTrain=originalTrain;
    }

    public void TrainningProcess() throws Exception {
        /*
        * Obtener las caracteristicas seleccionados
        * */
        Weka_main util=new Weka_main();
        //instancias con las carateristicas seleccionadas
        Instances instancesOriginal=this.originalTrain;
        classifierAndWeights=new ArrayList<>();

        Instances instances=this.miceTrain;

         fs=util.cfs_attributes(instances); //selecciono caracteristicas

        //Instance Selection HERE? 1
        InstanceSelection is=new InstanceSelection(util.remove_attributes(fs,instances));
        int [] arrSi= is.selectInstances();

        Instances rfs=util.remove_attributes(fs,instancesOriginal); // remuevo caracteristicas seleccionadas


        Instances selectInstances=getInstancesValues(arrSi,rfs);
        selectInstances.setClassIndex(selectInstances.classIndex());

        //System.out.println(selectInstances);
        //System.out.println(selectInstances.classIndex());
        //Instance Selection HERE? 1

     /*   */


        Missingpattern missingpattern=new Missingpattern(selectInstances); // encuentro missing patterns

        ArrayList<StoreAttAndIndexInstance> storeAttAndIndexInstances= missingpattern.getPatterns();
        storeAttAndIndexInstances.forEach(storeAttAndIndexInstance ->
               System.out.println("MP: "+Arrays.toString(storeAttAndIndexInstance.getPos())));
        schemaAttributes=new ArrayList<>();
         for (StoreAttAndIndexInstance s: storeAttAndIndexInstances) {  //por cada missin parttern hacer:

            try {
                 Instances cp= util.remove_attributes_not_Inv(s.getPos(),selectInstances);
                cp.setClassIndex(cp.classIndex());
                 ArrayList<Attribute> tempAttributes=new ArrayList<>();
                for (int i = 0; i < cp.numAttributes(); i++) {
                    tempAttributes.add(cp.attribute(i));
                }

                schemaAttributes.add(tempAttributes);
                  Classifier ci=util.Classifier_J48();
                 ci.buildClassifier(cp);

                double wi=util.Weight_Classifier(util.Evaluation_Classifier(ci,cp));
                classifierAndWeights.add(new WrapperClassifierAndWeight(ci,wi));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }



    }
    public Instances getInstancesValues(int []si,Instances instances){

        Instances temp=new Instances(instances);
        temp.delete();
        for (int i = 0; i < instances.numInstances(); i++) {
            if (si[i]==1){
                temp.add(instances.instance(i));
            }
        }

        return temp;

    }



}
