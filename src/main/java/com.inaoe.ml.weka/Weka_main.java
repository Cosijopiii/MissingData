/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author Fer
 */
public class Weka_main {
    
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

     String []FileDB = {"Credit","Heart-c","Heart-h","hepatitis","house-votes-84","mammographic_masses"};
     
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
         //System.out.println(dataDB); 
         filename = "DATABASE/FeatureSelection/CFS/OriginalDB/"+FileDB[i]+".arff";
         arffSaver = saveInstancesToArffFile(dataDB,filename);
         filename = "DATABASE/FeatureSelection/CFS/MICE/"+FileDB[i]+".arff";
         arffSaver = saveInstancesToArffFile(dataMICE,filename);
     }
     
     for (int i = 0; i<FileDB.length; i++)
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
            //System.out.println(dataDB); 
            //filename = "DATABASE/FeatureSelection/CFS/OriginalDB/"+FileDB[i]+".arff";
            //arffSaver = saveInstancesToArffFile(dataDB,filename);
            filename = "DATABASE/FeatureSelection/CFS/KNN/"+FileDB[i]+"-KNN-"+j+".arff";
            arffSaver = saveInstancesToArffFile(dataKNN,filename);
         }
     }
     return null;
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
     
    return null;
}

public int[] cfs_attributes(String FileName)throws Exception
{
     Instances dataMICE;
     String pathMICE = "DATABASE/MICEImp/";
     String format = ".arff";
     String imputation = "-MICE";
     dataMICE = this.Open(pathMICE+FileName+imputation+format);
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
    System.out.println(Data);
    return Data;
}
}
