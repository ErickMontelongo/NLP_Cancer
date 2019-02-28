/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.mycompany.nlpreprosessingmaven.MedicalNotePreprocessing;
import edu.stanford.nlp.simple.Document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author erick
 */
public class Lexicon {
    
    private ArrayList<String> stopWords;
    private ArrayList<String> lexicon;
    private ArrayList<String> directories;
    private File stopWordsFile;
    
    public Lexicon(){
        stopWords = new ArrayList<>();
        lexicon = new ArrayList<>();
        directories = new ArrayList<>();
    }
    
    public Lexicon(String s, ArrayList<String> dirs){
        stopWords = new ArrayList<>();
        lexicon = new ArrayList<>();
        stopWordsFile = new File(s);
        directories = dirs;
        try(Stream<String> stream = Files.lines(stopWordsFile.toPath())){
            stream.forEach( i -> stopWords.add(i));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public Lexicon(String lexPath){
        File lexFile = new File(lexPath);
        lexicon = new ArrayList<>();
        try(Stream<String> stream = Files.lines(lexFile.toPath(), StandardCharsets.ISO_8859_1)){
            stream.forEach( i -> lexicon.add(i));
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public void createLexicon(){
        try{
        for(String directory: directories){
            File folder = new File(directory);
            for(final File note: folder.listFiles()){
                MedicalNotePreprocessing preProcessing = new MedicalNotePreprocessing(note.getPath(), stopWordsFile.getPath());
                preProcessing.segmentFile();
                preProcessing.eliminateNoise(".*\\d+|_+|:|;|\\*+|,|-|\\[|\\]|#|\\(|\\).*", "");
                for(String subNote : preProcessing.getSegmentedFile()){
                    Document doc = new Document(subNote);
                    for(edu.stanford.nlp.simple.Sentence sent: doc.sentences()){
                        for(String k: sent.lemmas()){
                            if(!lexicon.contains(k.toLowerCase()) && !stopWords.contains(k.toLowerCase()))
                                lexicon.add(k.toLowerCase());
                        }
                    }
                }
            }
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void writeLexicon(){
        File lexFile = new File("./lexicon.txt");
        try{
            FileWriter fw = new FileWriter(lexFile);
            BufferedWriter bw = new BufferedWriter(fw);
            for(String s: lexicon){
                bw.write(s);
                bw.newLine();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getLexicon() {
        return lexicon;
    }
    
    
}
