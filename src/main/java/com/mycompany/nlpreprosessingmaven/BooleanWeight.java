/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nlpreprosessingmaven;

import Abstract.StatisticAlgorithm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Punkid PC
 */
public class BooleanWeight extends StatisticAlgorithm{
    

    private ArrayList<String> lexicon;
    private ArrayList<Boolean> booleanAnalysis;
    
    public BooleanWeight(String path) {
        super(path);
        booleanAnalysis = new ArrayList<>();
        prepareLexicon();
    }
    
    private void prepareLexicon(){
        String aux = new String();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(super.getLexiconPath()));
            BufferedReader br = new BufferedReader(reader);
            while((aux = br.readLine())!= null){
                lexicon.add(aux);
            }
        } catch (IOException ex) {
            Logger.getLogger(BooleanWeight.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doAnalysis(ArrayList<String> document) {
        booleanAnalysis.clear();
        for(String s: document){
            for(String k: s.split(" ")){
                if(lexicon.contains(k.toLowerCase()))
                    booleanAnalysis.add(Boolean.TRUE);
                else
                    booleanAnalysis.add(Boolean.FALSE);
            }
        }
    }
    
}
