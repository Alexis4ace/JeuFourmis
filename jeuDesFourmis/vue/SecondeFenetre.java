package jeuDesFourmis.vue;


import javafx.scene.Scene;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;

/**
 *
 * @author 
 */
public class SecondeFenetre extends Stage {
    
  
    
    public SecondeFenetre(Rectangle[][] graphique , int zoom){ 
        Pane root = new Pane();
        
        for(int x = 0 ; x<graphique.length;x++){ // ajoute chaque rectangle de la matrice à cette fenetre ( au pane )
            for(int y = 0 ; y < graphique.length;y++){
                root.getChildren().add(graphique[x][y]);
            }
        }
        
        this.setTitle("Zoom");
        this.setScene(new Scene( root,330,330));
        
    }
    
    
}
