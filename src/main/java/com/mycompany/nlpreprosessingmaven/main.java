/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nlpreprosessingmaven;

import Abstract.PreProcessing;
import Abstract.SemanticAnalysis;
import Abstract.SyntacticAnalysis;
import NLPAlgorithms.BagOfWords;
import NLPAlgorithms.DocumentBOW;
import Utils.DocumentsToFile;
import Utils.Lexicon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author Punkid PC
 */
public class main {
    
    public static void main(String[] args){
        
        ArrayList<String> directories = new ArrayList<>();
        directories.add("./pecho");
        directories.add("./estomago");
        directories.add("./pulmon");
        String stopWordsPath = ".\\stopDictionary.txt";
        String lexiconPath = ".\\lexicon.txt";
//        Lexicon lex = new Lexicon(stopWordsPath, directories);
//        lex.createLexicon();
//        lex.writeLexicon();
//        BagOfWords bagOfWords = new BagOfWords(directories, lex.getLexicon(), stopWordsPath);
//        ArrayList<DocumentBOW> documents = bagOfWords.calculateBagOfWords();

        DocumentsToFile toFile = new DocumentsToFile(directories, stopWordsPath);
        toFile.writeFiles();
        System.out.println("hello");
             
        
//        PreProcessing preProcessing = new MedicalNotePreprocessing(".\\note.txt", ".\\stopDictionary.txt");
//        preProcessing.segmentFile();
//        preProcessing.eliminateNoise("\\*|_|#");
//        List<String> sentences = preProcessing.getSegmentedFile();
//        SyntacticAnalysis stanford;
//        SemanticAnalysis stanfordSemantic;
//        int i = 1;
//        for(String s: sentences){
//            stanford = new StanfordAnalysis(s);
//            stanfordSemantic = new StanfordSemantic(s);
//            System.out.println("Document"+i+": "+stanford.getLemmatizedPOS(s)+"\n");
//            System.out.println("SEMANTIC"+i+": "+stanfordSemantic.getNER()+"\n");
//            i++;
//        }
//        
        
        
        
    }
    
}
