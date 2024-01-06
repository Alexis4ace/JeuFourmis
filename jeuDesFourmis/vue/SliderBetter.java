/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeuDesFourmis.vue;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 *
 * @author 
 */
public class SliderBetter extends HBox {
    
    public Label l = new Label();
   
    public Slider s = new Slider(0,1,1);

    
    public SliderBetter(String sLabel , double min , double max , double value){
        
        this.l.setText(sLabel);
        this.s.setMin(min);
        this.s.setMax(max);
        this.s.setValue(value);
      
        this.getChildren().addAll(l,s);
        this.setAlignment(Pos.CENTER);
        
        
        this.l.textProperty().bind(Bindings.convert(this.valueProperty()));
        
        
       
    }
    
    public void setValue(int val){
        s.setValue(val);
    }
    
    public double getMin(){
        return this.s.getMin();
    }
    
    public double getMax(){
        return this.s.getMax();
    }
    
    public double getValue(){
        return this.s.getValue();
    }
    
    public DoubleProperty valueProperty(){
        return this.s.valueProperty();
    }
    
    
    
}