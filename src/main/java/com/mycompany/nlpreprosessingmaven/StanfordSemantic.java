/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nlpreprosessingmaven;

import Abstract.SemanticAnalysis;
import Utils.Sentence;
import Utils.SentenceToken;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author erick
 */
public class StanfordSemantic extends SemanticAnalysis {
    
    
    private StanfordCoreNLP pipeline;
    private CoreDocument document;
    private Annotation annDocument;
    

    public StanfordSemantic(String text){
        super(text);
    }
    
    private void makeDocument(HashMap<String, String> properties){
        Properties prop = new Properties();
        properties.keySet().forEach((k) ->{
            prop.setProperty(k, properties.get(k));
        });
        pipeline = new StanfordCoreNLP(prop);
        annDocument = new Annotation(super.getText());
        pipeline.annotate(annDocument);    
    }

    @Override
    public ArrayList<Sentence> getNER() {
        HashMap<String,String> prop = new HashMap<>();
        prop.put("annotators", "tokenize,ssplit,pos,lemma,ner");
        prop.put("ssplit.isOneSentence", "true");
        prop.put("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        prop.put("tokenize.language", "en");
        makeDocument(prop);
        ArrayList<Sentence> tagger = new ArrayList<>();
        List<CoreMap> sentences= annDocument.get(CoreAnnotations.SentencesAnnotation.class);
        ArrayList<SentenceToken> words = new ArrayList<>();
        for(CoreMap sentence: sentences){
            words.clear();
            for(CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)){
                String key = token.get(CoreAnnotations.TextAnnotation.class);
                String value = token.get(NamedEntityTagAnnotation.class);
                words.add(new SentenceToken(key,value));
            }
            tagger.add(new Sentence(words));
        }
        return tagger; 
    }
    

    
    
}
