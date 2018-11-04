/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inaoe.ml.weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.net.search.global.GeneticSearch;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author Fer
 */
public class Weka_main {{

}
    
public Instances Open(String nameFile){
Instances datos;
try {
        FileReader fileReader = new FileReader(nameFile);
        BufferedReader reader = new BufferedReader(fileReader);
        datos = new Instances(reader);
        datos.setClassIndex(datos.numAttributes() - 1); //elige el último atributo como clase
        fileReader.close();
    
    } catch (IOException e) {
        System.out.println("Hay algo mal al leer el archivo " + e);
    return null;
    }
return datos;
}
public Instances CFS()throws Exception
{
     Instances dataDB, dataMICE, dataKNN;
     File arffSaver;
     String filename;
     String pathMICE = "DATABASE/MICEImp/";
     String pathDB = "DATABASE/OriginalDB/";
     String pathKNN = "DATABASE/KnnImputation/";
     String format = ".arff";
     
     String imputation = "-MICE";

     String []FileDB = {"Credit","Heart-c","Heart-h","hepatitis","house-votes-84","mammographic_masses", "Tumor"};
     
     for (int i = 0; i<FileDB.length; i++)
     {
         dataMICE = Open(pathMICE+FileDB[i]+imputation+format);
         dataDB = Open(pathDB+FileDB[i]+format);         
         //System.out.println(pathMICE+FileDB[i]+imputation+format+"-"+pathDB+FileDB[i]+format);
         AttributeSelection attsel = new AttributeSelection();
         CfsSubsetEval eval = new CfsSubsetEval();
         BestFirst search = new BestFirst();
         
         attsel.setEvaluator(eval);
         attsel.setSearch(search);
         attsel.SelectAttributes(dataMICE);
         dataMICE = attsel.reduceDimensionality(dataMICE);
         
         //System.out.println(dataMICE);
         attsel.SelectAttributes(dataDB);
         dataDB = attsel.reduceDimensionality(dataDB);

        NumericToNominal filter = new NumericToNominal();
        filter.setInputFormat(dataMICE);
        dataMICE = Filter.useFilter(dataMICE, filter);
        
        filter.setInputFormat(dataDB);
        dataDB = Filter.useFilter(dataDB, filter);
        
         //System.out.println(dataDB); 
         filename = "DATABASE/FeatureSelection/CFS/OriginalDB/"+FileDB[i]+".arff";
         arffSaver = saveInstancesToArffFile(dataDB,filename);
         filename = "DATABASE/FeatureSelection/CFS/MICE/"+FileDB[i]+".arff";
         arffSaver = saveInstancesToArffFile(dataMICE,filename);
     }
     
     for (int i = 0; i<FileDB.length;
          i++)
     {
         for(int j=5; j<=20; j=j+5)
         {
            dataKNN = Open(pathKNN+FileDB[i]+"-KNNI-"+j+format);
            dataDB = Open(pathDB+FileDB[i]+format);         
            //System.out.println(pathMICE+FileDB[i]+imputation+format+"-"+pathDB+FileDB[i]+format);
            AttributeSelection attsel = new AttributeSelection();
            CfsSubsetEval eval = new CfsSubsetEval();
            BestFirst search = new BestFirst();
            attsel.setEvaluator(eval);
            attsel.setSearch(search);
            attsel.SelectAttributes(dataKNN);
            dataMICE = attsel.reduceDimensionality(dataKNN);
            //System.out.println(dataMICE);
            attsel.SelectAttributes(dataDB);
            dataDB = attsel.reduceDimensionality(dataDB);
            
            NumericToNominal filter = new NumericToNominal();
            filter.setInputFormat(dataKNN);
            dataKNN = Filter.useFilter(dataKNN, filter);
        
            //System.out.println(dataDB); 
            //filename = "DATABASE/FeatureSelection/CFS/OriginalDB/"+FileDB[i]+".arff";
            //arffSaver = saveInstancesToArffFile(dataDB,filename);
            filename = "DATABASE/FeatureSelection/CFS/KNN/"+FileDB[i]+"-KNN-"+j+".arff";
            arffSaver = saveInstancesToArffFile(dataKNN,filename);
         }
     }
     return null;
}

public Instances IG(Instances data)throws Exception
{
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
	Ranker search = new Ranker();
	search.setOptions(new String[] { "-T", "0.05" });	// information gain threshold
	AttributeSelection attSelect = new AttributeSelection();
	attSelect.setEvaluator(eval);
	attSelect.setSearch(search);
	// apply attribute selection
	attSelect.SelectAttributes(data);
	int indices[] = attSelect.selectedAttributes();
	// remove the attributes not selected in the last run
	data = attSelect.reduceDimensionality(data);
        //System.out.println(data);
    return data;
}

private  File saveInstancesToArffFile(Instances instances, String filename) throws IOException
{
    try
    {
        File outputFile = new File(filename);
    if (outputFile.exists())
    {
        outputFile.delete();
        outputFile.createNewFile();
    }
    ArffSaver arffSaver = new ArffSaver();
    arffSaver.setInstances(instances);
    arffSaver.setFile(outputFile);
    arffSaver.writeBatch();  
     return arffSaver.retrieveFile();
    }catch(IOException e) {
        System.out.println("Hay algo mal al leer el archivo " + e);
    return null;
    }
}

public  Instances GA()throws Exception
{
     Instances dataDB, dataMICE, dataKNN;
     File arffSaver;
     String filename;
     String pathMICE = "DATABASE/MICEImp/";
     String pathDB = "DATABASE/OriginalDB/";
     String pathKNN = "DATABASE/KnnImputation/";
     String format = ".arff";
     String imputation = "-MICE";
     String []FileDB = {"Credit","Heart-c","Heart-h","hepatitis","house-votes-84","mammographic_masses"};
     for (int i = 0; i<FileDB.length; i++)
     {
         dataMICE = Open(pathMICE+FileDB[i]+imputation+format);
         AttributeSelection attsel = new AttributeSelection();
         WrapperSubsetEval eval = new WrapperSubsetEval();
         //WrapperSubsetEval WrapperEval_nbc = new WrapperSubsetEval(); 
         String[] WrapperEvalOpts_nbc = {"-B weka.classifiers.bayes.BayesNet -F 5 -T 0.01 -R 1 -E ACC -- -Q weka.classifiers.bayes.net.search.local.GeneticSearch -- -L 50 -A 100 -U 100 -R 1 -M 0.9 -C 0.1 -S ENTROPY -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5"};
         GeneticSearch ga = new GeneticSearch();
         BayesNet bayesNet = new BayesNet();
         bayesNet.buildClassifier(dataMICE);
         
         ga.buildStructure(bayesNet, dataMICE);
         //ga.setPopulationSize(50);
         //ga.descendantPopulationSizeTipText();
         //ga.setOptions("-L 50 -A 100 -U 100 -R 1 -M 0.9 -C 0.1");
         //eval.setOptions(WrapperEvalOpts_nbc); 
         eval.buildEvaluator(dataMICE); 
         
         //BestFirst search = new BestFirst();
         attsel.setEvaluator(eval);
         //attsel.setSearch(search);
         attsel.SelectAttributes(dataMICE);
         
         int indices[] = attsel.selectedAttributes();
         //System.out.println(indices[0]);
         dataMICE = attsel.reduceDimensionality(dataMICE);
         //System.out.println(dataMICE);
         filename = "DATABASE/FeatureSelection/GA/MICE/"+FileDB[i]+".arff";
         arffSaver = saveInstancesToArffFile(dataMICE,filename);
     }
    return null;
}

public int[] cfs_attributes(Instances dataMICE)throws Exception
{
     AttributeSelection attsel = new AttributeSelection();
     CfsSubsetEval eval = new CfsSubsetEval();
     BestFirst search = new BestFirst();
     attsel.setEvaluator(eval);
     attsel.setSearch(search);
     attsel.SelectAttributes(dataMICE);
     int indices[] = attsel.selectedAttributes();
    return indices;
}

public Instances remove_attributes(int[] indices, Instances data) throws Exception
{
    Instances Data;
    Remove removeFilter = new Remove();
    removeFilter.setAttributeIndicesArray(indices);
    removeFilter.setInvertSelection(true);
    removeFilter.setInputFormat(data);
    Data = Filter.useFilter(data, removeFilter);
    //System.out.println(Data);
    return Data;
}


    public Instances remove_attributes_not_Inv(int[] indices, Instances data) throws Exception
    {
        Instances Data;
        Remove removeFilter = new Remove();
        removeFilter.setAttributeIndicesArray(indices);
         removeFilter.setInputFormat(data);
        Data = Filter.useFilter(data, removeFilter);
        //System.out.println(Data);
        return Data;
    }


public double Weight_Classifier(Evaluation eval)throws Exception
{
    System.out.println(eval.weightedPrecision());
    double w = eval.weightedPrecision();
    return w;
}

public Classifier Classifier_J48(Instances data) throws Exception
{
    String[] options = new String[1];
    options[0] = "-U";            // unpruned tree
    J48 tree = new J48();         // new instance of tree
    tree.setOptions(options);     // set the options
    tree.buildClassifier(data);   // build classifier
    return tree;
}
public Evaluation Evaluation_Classifier(Classifier C, Instances data)throws Exception
{
    Evaluation eval = new Evaluation(data);
    eval.crossValidateModel(C, data, 10, new Random(1));
    System.out.println(eval.toSummaryString("\nResults\n======\n", false));
    //System.out.println(eval.weightedPrecision());
    return eval;
}
public Classifier Classifier_PART(Instances data) throws Exception
{
    PART part = new PART();
    part.buildClassifier(data);
    return part;
}

public Classifier Classifier_MLP(Instances data) throws Exception
{
    MultilayerPerceptron mlp = new MultilayerPerceptron();
    //Setting Parameters
    mlp.setLearningRate(0.1);
    mlp.setMomentum(0.2);
    mlp.setTrainingTime(2000);
    mlp.setHiddenLayers("1");
    mlp.buildClassifier(data);
    return mlp;
}

public Classifier Classifier_KNN(Instances data) throws Exception
{
   IBk knn = new IBk();
   knn.buildClassifier(data);
   return knn;
}
public Classifier Classifier_NB(Instances data)throws Exception{
    NaiveBayesMultinomial NBM = new NaiveBayesMultinomial();
    NBM.buildClassifier(data);
    return NBM;
}

public Classifier Classifier_RF(Instances data)throws Exception
{
    RandomForest RF = new RandomForest();
    RF.buildClassifier(data);
    return RF;
}
}
