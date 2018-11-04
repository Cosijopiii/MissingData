package com.inaoe.ml.weka;

import weka.classifiers.Classifier;

public class wraperClassifierAndWeight {

    private Classifier classifier;
    private double weight;

    public wraperClassifierAndWeight(){

    }

    public wraperClassifierAndWeight(Classifier c,double w){
        this.classifier=c;
        this.weight=w;

    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "weight=" + weight;
    }
}
