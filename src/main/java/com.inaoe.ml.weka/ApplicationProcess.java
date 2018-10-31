package com.inaoe.ml.weka;

import weka.core.Attribute;
import weka.core.Instance;

import java.util.ArrayList;

public class ApplicationProcess {

public ApplicationProcess (){

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
        public void process(Instance X,int sf[], ArrayList<Object> Models){
            Instance Xsf=reduceXtSF(X,sf);
            ArrayList CModels=Models;

            for (int i = 0; i < Xsf.numAttributes(); i++) {
                String v=getvalue(Xsf.attribute(i),Xsf,i);
                if (v.equals("?")||v.equals("NaN")) {
                    for (Object m:Models) {
                    /*TODO: la firma de la instancia debe ser igual a la firma del clasificaor Ni, Todos los clasificadores que  conincidan con la firma
                    de la instancia seran clasificados, entonces encontrar un metodo que ubique la firma del metodo
                    * */


                    }


                }


            }






        }
        public  Instance reduceXtSF(Instance X, int sf[]){


            return null;
        }
    private String getvalue(Attribute a, Instance i, int k){
        if (a.isNominal()){
            return i.stringValue(k);
        }
        if (a.isNumeric()){
            return String.valueOf(i.value(k));
        }
        return " ";
    }


}
