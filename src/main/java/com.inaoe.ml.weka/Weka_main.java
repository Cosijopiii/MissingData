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

import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.*;

import weka.filters.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;
import weka.attributeSelection.ASSearch;
//import weka.filters.supervised.attribute.*;

import weka.core.converters.ArffSaver;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;

 /*
 * @author Fer
 */
public class Weka_main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Instances dataCSF;       
        //System.out.println(data);
        dataCSF = Weka_main.CFS();
        //System.out.println(dataCSF);
}
    
private static Instances Open(String nameFile){
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

public static Instances CFS()throws Exception
{
     Instances dataDB, dataMICE, dataKNN;
     File arffSaver;
     String filename;
     String pathMICE = "DATABASE/MICEImp/";
     String pathDB = "DATABASE/OriginalDB/";
     String pathKNN = "DATABASE/KNNI/";
     String format = ".arff";
     
     String imputation = "-MICE";

     String []FileDB = {"Credit","Heart-c","Heart-h","hepatitis","house-votes-84","mammographic_masses"};
     
     
     
     for (int i = 0; i<FileDB.length; i++)
     {
         dataMICE = Weka_main.Open(pathMICE+FileDB[i]+imputation+format);
         dataDB = Weka_main.Open(pathDB+FileDB[i]+format);         
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
         //System.out.println(dataDB); 
         filename = "DATABASE/FeatureSelection/CFS/OriginalDB/"+FileDB[i]+".arff";
         arffSaver = Weka_main.saveInstancesToArffFile(dataDB,filename);
         filename = "DATABASE/FeatureSelection/CFS/MICE/"+FileDB[i]+".arff";
         arffSaver = Weka_main.saveInstancesToArffFile(dataMICE,filename);
     }
    return null;
}

private static File saveInstancesToArffFile(Instances instances, String filename) throws IOException
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
    //return arffSaver.retrieveFile();
}

public static Instances GA()throws Exception
{
     Instances dataDB, dataMICE, dataKNN;
     File arffSaver;
     String filename;
     String pathMICE = "DATABASE/MICEImp/";
     String pathDB = "DATABASE/OriginalDB/";
     String pathKNN = "DATABASE/KNNI/";
     String format = ".arff";
     
     String imputation = "-MICE";

     String []FileDB = {"Credit","Heart-c","Heart-h","hepatitis","house-votes-84","mammographic_masses"};
     
     for (int i = 0; i<FileDB.length; i++)
     {
         dataMICE = Weka_main.Open(pathMICE+FileDB[i]+imputation+format);
         dataDB = Weka_main.Open(pathDB+FileDB[i]+format);         
         //System.out.println(pathMICE+FileDB[i]+imputation+format+"-"+pathDB+FileDB[i]+format);
         AttributeSelection attsel = new AttributeSelection();
         
         /*GeneticSearch ga = new GeneticSearch();
         ga.setPopulationSize(50);
         ga.setMaxGenerations(100);
         */
         
       
         
         filename = "DATABASE/FeatureSelection/GA/OriginalDB/"+FileDB[i]+".arff";
         arffSaver = Weka_main.saveInstancesToArffFile(dataDB,filename);
         filename = "DATABASE/FeatureSelection/GA/MICE/"+FileDB[i]+".arff";
         arffSaver = Weka_main.saveInstancesToArffFile(dataMICE,filename);
     }
    return null;
}
}
