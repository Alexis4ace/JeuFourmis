/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeuDesFourmis.vue;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author
 */
public class Loupe extends Button {
    
    public SecondeFenetre scene2 ; 

    private final int size ;
    private final int DEFAULT_LOUPE = 11;
    private Rectangle[][] rects = new Rectangle[DEFAULT_LOUPE][DEFAULT_LOUPE]; //plus petite matrice pour afficher le zoom
    private int cellule = 30 ;
    
    public Loupe(MatriceGraphique graphique,int x , int y){
        
        super("loupe");
        this.size = graphique.getFourmiliere().getLargeur();
        this.setLayoutX(250);
        this.setLayoutY(550);
       
        
 

        this.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {  // si on appui sur le bouton alors
                setZoom(graphique,x,y); //on remplis la petite matrice
                SecondeFenetre scene2 = new SecondeFenetre(rects,DEFAULT_LOUPE); // on lance la fenetre avec la matrice a afficher
                scene2.show(); // on montre la fenetre
                
            }
        });
    }
    
    // ici on vérifie si le click est près des murs si c'est le cas alors on part du murs vers le centre du plateau
    public void setZoom(MatriceGraphique graphique , int x ,int y){
       
        if(x<size/2 && y<size/2) // coin en haut a gauche
            remplirMatrice(graphique,min(x), min(y));
        else if (x<size/2 && y>size/2) //coin en bas a gauche
            remplirMatrice(graphique,min(x), max(y)-DEFAULT_LOUPE);
        else if ( x>size/2 && y<size/2) // coin en haut a droite
            remplirMatrice( graphique,max(x)-DEFAULT_LOUPE, min(y));
        else // coin en bas a droite
            remplirMatrice(graphique,max(x)-DEFAULT_LOUPE, max(y)-DEFAULT_LOUPE);
    }
    
    // remplis la petite matrice avec des plus gros rectangles et de la bonne couleur
    public void remplirMatrice(MatriceGraphique graphique,int valx,int valy ){
        
        for(int x=0 ; x<DEFAULT_LOUPE; x++){
            for(int y = 0 ; y<DEFAULT_LOUPE ;y++){
               
                Rectangle rectangle = new Rectangle(cellule,cellule);
                rectangle.setLayoutX(x*cellule);
                rectangle.setLayoutY(y*cellule);
                rectangle.setFill(graphique.getMatrice()[x+valx][y+valy].getFill());
                rectangle.setStroke(Color.BLACK);
               
                this.rects[x][y] = rectangle;
                
            }
        }
        
    }
   
   
    // fonction pour éviter de sortir de la grille
    public int min(int x){
        for(int i=0 ; i<5 ; i++){
            if( x > 0)
                x -= 1;
            
        }
        return x;
    }
    

    
    public int max(int x){
        for(int i=0 ; i<5 ; i++){
            if( x < size)
                x += 1;          
        }
        return x;
    }
    
   
    
 
}
