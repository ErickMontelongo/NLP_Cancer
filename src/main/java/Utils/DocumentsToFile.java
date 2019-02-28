/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.mycompany.nlpreprosessingmaven.MedicalNotePreprocessing;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Punkid PC
 */
public class DocumentsToFile {
    private ArrayList<String> directories;
    private String stopWordsPath;
    private static int TRAIN = 2;


    public DocumentsToFile(ArrayList<String> dir, String sPath){
        directories = dir;
        stopWordsPath = sPath;
    }
    
    public void writeFiles(){
        Random r = new Random();
        ArrayList<Integer> randomNumber = new ArrayList<>();
        int counter = 1;
        for(String path: directories){
            File directory = new File(path);
            for(final File note: directory.listFiles()){
                 MedicalNotePreprocessing preProcessing = new MedicalNotePreprocessing(note.getAbsolutePath(), stopWordsPath);
                 preProcessing.segmentFile();
                 preProcessing.eliminateNoise(".*\\d+|_+|:|;|\\*+|,|-|\\[|\\]|#|\\(|\\).*", "");
                 preProcessing.eliminateStopWords();
                 for(int i = 0; i < TRAIN; i++)
                    randomNumber.add(i, r.nextInt(preProcessing.getSegmentedFile().size()));
                 int j = 0;
                 for(String subNote: preProcessing.getSegmentedFile()){
                    if(randomNumber.contains(j)){
                        Path file = Paths.get("./data/unlabeled/"+path.substring(1)+"/"+path.charAt(2)+counter+".txt");
                        try{
                        Files.write(file, subNote.getBytes(),StandardOpenOption.CREATE);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        Path file = Paths.get("./data/labeled/"+path.substring(1)+"/"+path.charAt(2)+counter+".txt");
                        try{
                        Files.write(file, subNote.getBytes(),StandardOpenOption.CREATE);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    counter++;
                    j++;
                 }             
            }
            randomNumber.clear();
        }
    }
    
}
