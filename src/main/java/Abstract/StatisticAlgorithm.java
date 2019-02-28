/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstract;

import java.util.ArrayList;

/**
 *
 * @author Punkid PC
 */
public abstract class StatisticAlgorithm {
    
    private String lexiconPath;
    
    public StatisticAlgorithm(String path){
        lexiconPath = path;
    }
    
    public String getLexiconPath(){
        return lexiconPath;
    }
    
    public abstract void doAnalysis(ArrayList<String> document);
}
