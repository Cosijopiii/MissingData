package com.inaoe.ml.weka;

import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class InstanceSelection {

    private Instances instances;
    private static final int MAX_ITERATIONS=40;
    private int num;
    private int len;
    public InstanceSelection(){

    }

    public InstanceSelection(Instances instances){
        this.instances=instances;
    }

    public int[] selectInstances(){
        len=instances.numInstances();
        num=60;
        int[] r=geneticSelection();



        return r;
    }

    private int[] geneticSelection(){
        int cruzaNUM=num/2;//numero de hijos
        int p[][]= genPoblacion(num,len);
        double f[]=new double[num];
        int pn[][]=new int[cruzaNUM][len];
        int hij[][]=new int[cruzaNUM][len];
        int ng[][]=new int[num][len];


        for (int i = 0; i < MAX_ITERATIONS; i++) {
            //eval todos
            for (int j = 0; j < num; j++) {
                f[j]=eval(p[j]);
            }
            // System.out.println(Arrays.toString(f));
            int s[]=ruleta(f,cruzaNUM);
            // System.out.println(Arrays.toString(s));
            for(int j = 0; j < cruzaNUM; j++) {
                pn[j]=p[s[j]];
            }

            //numeros de cruzas
            for (int j = 0; j < cruzaNUM; j=j+2) {

                int[][]pair=cruzaTwo(p[s[j]],p[s[j+1]]);
                hij[j]=pair[0];
                hij[j+1]=pair[1];
            }




            for (int j = 0; j < num; j++) {
                if (j<cruzaNUM){
                    ng[j]=hij[j];
                }else {
                    ng[j]=pn[j-(cruzaNUM)];
                }
            }
            ng=mutacion(ng,num);
            //printP(ng);
            p=ng;
//            for (int j = 0; j < num; j++) {
//                f[j]=eval(p[j]);
//            }
//            ArrayList<Object> a=sortarray(p,f);
//            p= (int[][]) a.get(0);




        }
        for (int j = 0; j < num; j++) {
            f[j]=eval(p[j]);
        }
        ArrayList<Object> a=sortarray(p,f);
        p= (int[][]) a.get(0);
        f= (double[])  a.get(1);

        for (int j = 0; j < num; j++) {
            f[j]=eval(p[j]);
        }


        System.out.println("Resultado final mejor individuo   "+f[0]);
        System.out.println("num of instances: "+ Arrays.stream(p[0]).sum());
        evalE(p[0]);


        int cs[] = IntStream.range(0, len).map(i -> 1).toArray();
        System.out.println("Complete instance set: "+ eval(cs));
        System.out.println("num of instances: "+ Arrays.stream(cs).sum());
        evalE(cs);

        return p[0];



    }
    public  ArrayList<Object> sortarray(int pob[][],double f[]){

        for (int i = 0; i < f.length; i++) {
            for (int j = 1; j < f.length; j++) {
                if (f[j-1]<f[j]){
                    f=swap(f,j-1,j);
                    pob=swap(pob,j-1,j);
                }
            }
        }
       // Arrays.stream(f).forEach(System.out::println);
       // printP(pob);
        ArrayList<Object> temp=new ArrayList<>();
        temp.add(pob);
        temp.add(f);
        return temp;
    }
    public int[][] swap(int[][]pob,int i,int j){

        int []aux=pob[j];
        pob[j]=pob[i];
        pob[i]=aux;

        return pob;
    }

    public double [] swap(double a[],int i,int j ) {
        double aux=a[j];

        a[j]=a[i];
        a[i]=aux;
        return a;
    }
    public int [][] mutacion(int[][]p,int manymut){
        Random r = new Random();
        int[] randomNumber = r.ints(manymut, 0, num).toArray();

        for (int i = 0; i < manymut; i++) {
            int t[]=p[randomNumber[i]];
            if (Math.random()<0.2) {
                int[] rn = r.ints(10, 0, len).toArray();
                IntStream.range(0, rn.length).forEachOrdered(j -> t[rn[j]] = t[rn[j]] == 0 ? 1 : 0);
            }
            p[randomNumber[i]]=t;
        }
         return p;
    }
    public int[] ruleta(double f[],int manySelects){

        double sum= Arrays.stream(f).sum();
        int selects[] =new int[manySelects];
        double prob[] = IntStream.range(0, num).mapToDouble(i -> f[i] / sum).toArray();
        for (int i = 0; i < manySelects; i++) {
            double a=Math.random();
            double sig=0;
            for (int j = 0; j < num; j++) {
                sig=sig+prob[j];
                if (sig>a){
                    selects[i]=j;
                    break;
                }
            }
        }
        return selects;
    }
    public void printP(int p[][]){
        Arrays.stream(p).forEach(ints -> {
            Arrays.stream(ints).forEach(System.out::print);
            System.out.println();
        });
    }

    public int[][] cruzaTwo(int ind1[],int ind2[]){
        Random r = new Random();
        int[] randomNumber = r.ints(2, 0, len).toArray();
        int p1 = randomNumber[0];
        int p2 = randomNumber[1];
        // System.out.println(p1+" "+p2);
        int []t1=new int[len];
        int []t2=new int[len];
        for (int i = 0; i < len; i++) {
            t1[i]=ind1[i];
            t2[i]=ind2[i];
        }
        // si es mayor p2 que p1 volteamos
        if (p1>p2){
            p1=randomNumber[1];
            p2=randomNumber[0];
        }else
        {
            p1=randomNumber[0];
            p2=randomNumber[1];
        }
        for (int i = p1; i <p2 ; i++) {
            t1[i]=ind2[i];
            t2[i]=ind1[i];
        }
        int [][]pair=new int[2][len];
        pair[0]=t1;
        pair[1]=t2;
        return pair;
    }



    public int[][]genPoblacion(int num,int len){
        int[][] pob=new int[num][len];
         for (int i = 0; i < num; i++) {
            for (int j = 0; j < len; j++) {
                pob[i][j] = (Math.random() >= 0.5) ? 1 : 0;
            }
        }
        return pob;
    }






    public double eval(int[] x){

    Instances temp=new Instances(instances);
    temp.delete();
        for (int i = 0; i < instances.numInstances(); i++) {
            if (x[i]==1){
                temp.add(instances.instance(i));
            }
        }
        int [] clasV=new int[temp.numClasses()];

        for (int i = 0; i < temp.numInstances(); i++) {
            for (int j = 0; j < clasV.length; j++) {
                if (temp.instance(i).classValue()==j){
                    clasV[j]=clasV[j]+1;
                }
            }
        }

       // System.out.println(Arrays.toString(clasV));
        Weka_main util=new Weka_main();
        Classifier ci;
        double wi=0;
        try {
            ci = util.Classifier_J48();
            wi=util.Weight_Classifier(util.Evaluation_Classifier(ci,temp));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int absR=0;
        int R=0;


        for (int i = 0; i < clasV.length-1; i++) {
            absR=clasV[i]-clasV[i+1];
            absR=Math.abs(absR);
            R=R+absR;
        }

        float wii=(float)R/100;
      //  System.out.println(wii);
      //  System.out.println(wi-wii);
        return wi-wii;
    }
//  poderarlo segun el balanceo de clases<a

    public void evalE(int []x){
        Instances temp=new Instances(instances);
        temp.delete();
        for (int i = 0; i < instances.numInstances(); i++) {
            if (x[i]==1){
                temp.add(instances.instance(i));
            }
        }
        int [] clasV=new int[temp.numClasses()];

        for (int i = 0; i < temp.numInstances(); i++) {
            for (int j = 0; j < clasV.length; j++) {
                if (temp.instance(i).classValue()==j){
                    clasV[j]=clasV[j]+1;
                }
            }
        }

        System.out.println("Class Balance: "+Arrays.toString(clasV));
    }

}

