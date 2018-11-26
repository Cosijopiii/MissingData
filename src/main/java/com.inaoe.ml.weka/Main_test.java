package com.inaoe.ml.weka;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;

import java.util.Random;
import java.util.function.Consumer;

public class Main_test {

    public static void main(String argv[]) throws Exception {

        Weka_main util = new Weka_main();
        String path = "src/main/resources/DATABASE/MICEImp/";
        String file = "Heart-c-MICE.arff";
        Instances s = util.Open(path + file);
        Classifier c1 = new IBk();
        Evaluation eval = null;
        eval = new Evaluation(s);
        eval.crossValidateModel(c1, s, 5, new Random());
        System.out.println(eval.pctCorrect());
        System.out.println(eval.weightedPrecision());


        int folds = 5;
        Random rand = new Random();   // create seeded number generator
        Instances randData = new Instances(s);   // create copy of original data
        randData.randomize(rand);
        Classifier c2 = new IBk();
        double avg = 0;
        double avg2 = 0;
        for (int i = 0; i < folds; i++) {
            Instances train = randData.trainCV(folds, i, rand);
            Instances test = randData.testCV(folds, i);

            c2.buildClassifier(train);

            Evaluation evaluation = null;

            evaluation = new Evaluation(train);


            evaluation.evaluateModel(c2, test);

            System.out.println(evaluation.pctCorrect());
            avg = avg + evaluation.pctCorrect();
            avg2 = avg2 + evaluation.weightedPrecision();
        }
        avg = avg / 5;
        avg2 = avg2 / 5;
        System.out.println("mean: " + avg);
        System.out.println("wmean: " + avg2);
    }

}
