package com.inaoe.ml.weka;

import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Main_MD {

    public static void main(String argv[]) throws Exception {
   /*
   * Define imputed File
   * And Original File
   * */

        String path = "src/main/resources/DATABASE/MICEImp/";
        String file = "Credit-MICE.arff";

        String pathOr = "src/main/resources/DATABASE/OriginalDB/";
        String fileOr = "Credit.arff";



   /*
   * Generate CV 5-Folds
   * parameters
   * */
        int folds = 5;
        float [] per=new float[folds];
        Random rand = new Random(1);
        Weka_main util = new Weka_main();
        Instances miceInstance = util.Open(path + file);
        Instances originalInstance = util.Open(pathOr + fileOr);
        miceInstance.setClassIndex(miceInstance.classIndex());
        originalInstance.setClassIndex(originalInstance.classIndex());
        Instances randDataMice = new Instances(miceInstance);
        Instances randDataOriginal = new Instances(originalInstance);

        //--init
    /*
    * Testing only one fold, implemented loop for more folds
    * */
        int[]zz={4,3,0,1,2};
        for (int k=0; k < 1; k++) {


            Instances train = miceInstance.trainCV(folds, k);
            Instances trainOr = originalInstance.trainCV(folds, k);
            Instances test = originalInstance.testCV(folds, zz[k]);
            System.out.println("Size of TrainOR:"+trainOr.numInstances());
            System.out.println("Size of Test:"+test.numInstances());
            int coin=0;
            for (int i = 0; i < test.numInstances(); i++) {
                for (int j = 0; j < trainOr.numInstances(); j++) {
                    if (test.instance(i).equals(trainOr.instance((j)))){
                       coin++;
                    }
                }
            }
            System.out.println("Similitud test-train: "+ coin);
    /*
    * Training Process
    * */

            TrainProces trainProces = new TrainProces(train, trainOr);
            trainProces.TrainningProcess();
            ArrayList<WrapperClassifierAndWeight> classifierAndWeightArrayList = trainProces.classifierAndWeights;
            //  classifiers and weights

     /*
    * Application Process 116 132
    * */


  /*
     trainProces.schemaAttributes.forEach(attributes -> {
         attributes.forEach(System.out::println);
         System.out.println();
     });
*/

            ApplicationProcess applicationProcess = new ApplicationProcess(trainProces);

            //-1 all dataset >0 index

            ArrayList<ArrayList<Double>> results = new ArrayList<>();
            ArrayList<Double> testClassV = new ArrayList<>();


            for (int i = 0; i < test.numInstances(); i++) {
                ArrayList<Double> r = applicationProcess.process(test, i);
                //r.forEach(System.out::println);
                testClassV.add(test.instance(i).classValue());
                results.add(r);
            }

            double max = Double.MIN_VALUE;
            int index = 0;
            for (int i = 0; i < classifierAndWeightArrayList.size(); i++) {
                double p = classifierAndWeightArrayList.get(i).getWeight();
                if (p >= max) {
                    max = p;
                    index = i;
                }

            }

            System.out.println(classifierAndWeightArrayList.get(index).getWeight());

            // testClassV.forEach(System.out::println);
            int pt = 0;
            for (int i = 0; i < testClassV.size(); i++) {
                if (results.get(i).get(index).compareTo(testClassV.get(i)) == 0) {
                    pt++;
                }
            }
            int sizeTest = testClassV.size();
            System.out.println("Total Correct: " + pt + " Total: " + sizeTest);
            float percent;
            percent = (float) (pt / (double) sizeTest);
            System.out.println("%: " + percent);
            per[k]=percent;
        }
        float sumP=0;
        for (int i = 0; i < folds; i++) {
            sumP=per[i]+sumP;
        }
        sumP=sumP/(float) folds;
        System.out.println("Mean: "+sumP*100);
        double sd2[] = IntStream.range(0, per.length).mapToDouble(i -> per[i]*100).toArray();
        System.out.println(Arrays.toString(sd2));
        double sd= calculateSD(sd2);
        System.out.println("Standar derivation: "+ sd);

        //--end
    }
    public static double calculateSD(double numArray[])
    {

        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;

        for(double num : numArray) {
            sum += (num);
        }

        double mean = sum/length;

        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }


}
