/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.nlpreprosessingmaven;

import Abstract.PreProcessing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Punkid PC
 */
public class MedicalNotePreprocessing extends PreProcessing{

    private ArrayList<String> medicalNotes;
    private ArrayList<String> stopWords;
    
    public MedicalNotePreprocessing(String file, String sWord){
        super(file, sWord);
        medicalNotes = new ArrayList<>();
        stopWords = new ArrayList<>();
    }
    
    private void getStopWords(){
        try {
            String temp;
            FileReader reader = new FileReader(new File(stopWordPath));
            BufferedReader bf = new BufferedReader(reader);
            while((temp = bf.readLine()) != null){
                stopWords.add(temp);
            }
        } catch (IOException ex) {
            Logger.getLogger(MedicalNotePreprocessing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void segmentFile() {
        try{
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            CSVParser cvsParser = new CSVParser(reader, CSVFormat.EXCEL.withFirstRecordAsHeader());
            for(CSVRecord record: cvsParser){
                medicalNotes.add(record.get("TEXT"));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void eliminateNoise(String pattern, String replace) {
        ArrayList<String> auxMed = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(String s: medicalNotes){
            try{
                SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
                for(String token: tokenizer.tokenize(s)){
                    if(!token.matches(pattern) && token.length() > 2)
                        sb.append(token.toLowerCase()).append(" ");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            auxMed.add(sb.toString());
            sb.delete(0, sb.length());
        }
        medicalNotes = auxMed;
    }

    @Override
    public void eliminateStopWords() {
        ArrayList<String> auxMed = new ArrayList<>();
        StringBuilder aux = new StringBuilder();
        getStopWords();
        for(String s: medicalNotes){
            for(String k: s.split(" ")){
                if(!stopWords.contains(k.toLowerCase())){
                    aux.append(k);
                    aux.append(" ");
                }
            }
            auxMed.add(aux.toString());
            aux.delete(0, aux.length());
        }
        medicalNotes = auxMed;
    }

    @Override
    public List<String> getSegmentedFile() {
        for(Iterator<String> it = medicalNotes.iterator(); it.hasNext(); ){
            String note = it.next();
            if(note.matches("(\\n)+"))
                it.remove();
        }
        return medicalNotes;
    }

    @Override
    public void toFile() {
        
    }
    
}
