/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstract;

import java.util.List;

/**
 *
 * @author Punkid PC
 */
public abstract class PreProcessing {
    
    public String filePath;
    public String stopWordPath;
    
    public PreProcessing(String file, String stop){
        filePath = file;
        stopWordPath = stop;
    }
    
    public abstract void segmentFile();
    public abstract void eliminateNoise(String pattern, String replace);
    public abstract void eliminateStopWords();
    public abstract List<String> getSegmentedFile();
    public abstract void toFile();
    
}
