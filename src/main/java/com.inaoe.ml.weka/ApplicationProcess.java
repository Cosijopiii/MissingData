package com.inaoe.ml.weka;

import weka.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ApplicationProcess {

    private TrainProces trainProces;

    public ApplicationProcess(TrainProces proces) {
        trainProces = proces;
    }

    public ApplicationProcess() {

    }

    /*
    * X nueva instancia, de esta instancia se le quitaran los atributos que no esten en su archivo de caracteristicas seleccionadas
    *
    * por cada valor incompleto en X reducida
    * y por cada clasificador que este en AC,
     * si el clasificador require alguna caraterstica, en otras palabras el clasificador requiera una columna que no este en los datos del modelo
     * del clasificador entonces este clasificador se quita del arreglo de clasificadores AC
     * al terminar estos dos ciclos entonces se obtiene un conjunto de clasificadores que pueden lidiar con la nueva instancia
     * se aplica cada clasificador que sobro en AC a X y la clase es el clasificador que tiene mayor peso W.
    *
    *
    * */
    public ArrayList<Double> process(Instances n, int index) throws Exception {

        int sf[] = trainProces.fs;

        ArrayList<WrapperClassifierAndWeight> classifierAndWeights = trainProces.classifierAndWeights;
        Weka_main util = new Weka_main();
        Instances instances = util.remove_attributes(sf, n);
         Instance x = instances.instance(index);
        /*
        IntStream.range(0, x.numAttributes()).
                mapToObj(x::attribute).forEach(System.out::println);
*/
        int[][] values= CheckInstanceToClassifier(x);
        /**/
  //      System.out.println(Arrays.deepToString(values));
        /**/
    //    System.out.println(Arrays.toString(sf));

        //Instance theGoodInstance =removeattAndGetInstance(instances,values[1],index);



        ArrayList<Double> c = new ArrayList<>();
         for (int i = 0; i < classifierAndWeights.size(); i++) {
            WrapperClassifierAndWeight m = classifierAndWeights.get(i);
            double d;
            try {
                Instance toInstance=removeattAndGetInstance(instances,values[i],index);
               //  System.out.println(toInstance);
                 boolean ch= checkInstance(toInstance,i);
                if (!ch){
                    d = m.getClassifier().classifyInstance(toInstance);

                    c.add(d);
                }else {
                    c.add(-1.0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    return c;
    }

    public boolean checkInstance(Instance instance,int indexS){

        ArrayList<Attribute> mv=new ArrayList<>();
        for (int i = 0; i < instance.numAttributes(); i++) {
            if (String.valueOf(instance.value(i)).compareTo("NaN")==0||String.valueOf(instance.value(i)).compareTo("?")==0){
                mv.add(instance.attribute(i));
            }
        }
        boolean flag=false;
        for (int i = 0; i <trainProces.schemaAttributes.get(indexS).size(); i++) {
            Attribute ai=trainProces.schemaAttributes.get(indexS).get(i);
            for (Attribute aMv : mv) {
                if (ai.equals(aMv)) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                break;
        }

       // mv.forEach(System.out::println);

        return flag;
    }




    public Instance removeattAndGetInstance(Instances instances,int[] vals,int index) throws Exception {

        Instances temp=new Instances(instances);
        int c;
        //System.out.println(Arrays.toString(vals));
        c = (int) IntStream.range(0, vals.length).filter(i -> vals[i] == 0).count();
        int []fs=new int[c];
        for (int i=0,j = 0; i < vals.length; i++) {
            if (vals[i]==0){
                fs[j]=i;
                j++;
            }
        }
        c=0;






        /*TODO: agregar los indices si es que tiene valores faltantes*/
        for (int i = 0; i < fs.length; i++) {
            temp.deleteAttributeAt(fs[i]-c);
            c++;
        }



         return temp.instance(index);
    }

    private int[][] CheckInstanceToClassifier(Instance instance){
        int numOfClassifiers=trainProces.schemaAttributes.size();
        int[][] matrixSim=new int[numOfClassifiers][instance.numAttributes()];
        ArrayList<ArrayList<Attribute>> schemaAttributes = trainProces.schemaAttributes;
        for (int i1 = 0; i1 < schemaAttributes.size(); i1++) {
            ArrayList<Attribute> a = schemaAttributes.get(i1);
            for (Attribute att : a) {
                for (int i = 0; i < instance.numAttributes(); i++) {
                    if (att.equals(instance.attribute(i))) {
                        matrixSim[i1][i]=i+1;
                    }
                }
            }
        }
        return matrixSim;
    }




/*

    private Instance instanceRemoveC(Instance instance,int[]vals, Instances ss){
         double[] v=new double[Arrays.stream(vals).sum()];
        ArrayList<Attribute> attributeArrayList=new ArrayList<>();
          for (int i=0,j = 0; i < instance.numAttributes(); i++) {
            if (vals[i]==1){
                v[j]=instance.value(i);
                attributeArrayList.add(instance.attribute(i));
                j++;

            }
        }
        Instances cn=new Instances("c1",attributeArrayList,1);
        Instance temp=new DenseInstance(1,v);
        temp.setDataset(cn);
          return temp;
    }

*/





    //OK
    private String getvalue(Attribute a, Instance i, int k) {
        if (a.isNominal()) {
            return i.stringValue(k);
        }
        if (a.isNumeric()) {
            return String.valueOf(i.value(k));
        }
        return " ";
    }


}
