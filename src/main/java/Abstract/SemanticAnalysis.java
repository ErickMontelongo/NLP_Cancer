/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abstract;

import Utils.Sentence;
import java.util.ArrayList;

/**
 *
 * @author erick
 */
public abstract class SemanticAnalysis {
    
    private String text;
    
    public SemanticAnalysis(String t){
        text = t;
    }
    
    public abstract ArrayList<Sentence> getNER();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
}
