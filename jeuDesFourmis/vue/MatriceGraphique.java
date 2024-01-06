/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeuDesFourmis.vue;

import java.util.Random;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jeuDesFourmis.model.Fourmiliere;

/**
 *
 * @author
 */
public class MatriceGraphique {
    //valeur par defaut si j'amais on veux faire un constructeur avec aucun parametre
    private final int DEFAULT_SIZE = 50;
    private final int DEFAULT_CELLULE = 10;
    
    private Rectangle[][] matrice_graphique ;  // matrice de rectangle correspondant aux matrices de la fourmiliere
    private Fourmiliere fourmiliere ;  //la fourmiliere en question
    private final Pane pane = new Pane();  //on met tout les rectangles dans ce pane qui sera ajouter dans d'autre pane
    
    private int cellule = DEFAULT_CELLULE; // dimension d'une fourmis,mur ...
    private int coup = 0; // nombre de coup jouer
    
    
    public MatriceGraphique(Fourmiliere fourmiliere){
        
        if(fourmiliere != null){
            
            this.fourmiliere = fourmiliere;  
            matrice_graphique = new Rectangle[this.fourmiliere.getLargeur()][this.fourmiliere.getHauteur()];  //init par rapport a la taille de la fourmiliere
            this.loadMatrice(); // initialise chaque rectangle 
            this.setPane(); // met tout dans le pane
        }
    }
    
    public Pane getPane(){ 
        return this.pane;
    }
    
    public int getCellule(){
        return this.cellule;
    }
    public void setCellule(int cell){
        this.cellule = cell ;
    }
    
    public Fourmiliere getFourmiliere(){
        return this.fourmiliere;
    }
    
    public void setFourmiliere(Fourmiliere fourmiliere){
        this.fourmiliere = fourmiliere;
    }
        
    public Rectangle[][] getMatrice(){
        return this.matrice_graphique;
    }
    
    public void setMatrice(Rectangle[][] matrice){
        this.matrice_graphique = matrice;
    }
    
    //fonction private qui permet de donner un rectangle correspondant a la position x,y dans la fourmiliere
    private Rectangle setRectangle(int x ,int y){
        Color new_color ;
        
        if(fourmiliere.getMur(x, y)) //si c'est un mur alors rectangle noir
            new_color = Color.BLACK ;
        
        else if (fourmiliere.contientFourmi(x, y)) //si fourmmis alors rectangle blue ( porte ) ou vert ( ne porte pas )
            
            if(fourmiliere.getFourmis(x, y).porte())
                new_color = Color.BLUE ;
            else
                new_color = Color.GREEN ;
                
        else if(fourmiliere.getQteGraines(x, y)>0) //si il y a des graines, un rouge correspondant au ratio nombreGraine/maxGraine
            new_color = Color.rgb(255,0,0, (double) fourmiliere.getQteGraines(x, y)/fourmiliere.getQMax());
        
        else // sinon vide donc blanc
            new_color = Color.WHITE;
        
        Rectangle rect = new Rectangle(cellule , cellule ); //taille 
        rect.setLayoutX(cellule * x ); //placement
        rect.setLayoutY(cellule * y);
        rect.setFill(new_color); //color
        rect.setStroke(Color.BLACK); //bordure
        
        return rect ; //renvoie le rectangle
    }
    
    
    //ici on charge la matrice de rectangle avec le rectangle donner au dessus
    public void loadMatrice(){
        int largeur = this.fourmiliere.getLargeur();
        int hauteur = this.fourmiliere.getHauteur();
        
        for(int x=0 ; x<largeur; x++){
            for(int y = 0 ; y<hauteur ;y++){
                this.matrice_graphique[x][y] = this.setRectangle( x, y);
            }
        }
    }
    
    
    //ajoute la matrice au pane si on modifie la matrice on passe par ici pour le mettre a jour
    public void setPane(){
        this.pane.getChildren().clear(); //on efface tout les rectangles si jamais il y en avait deja
        
        int largeur = this.fourmiliere.getLargeur();
        int hauteur = this.fourmiliere.getHauteur();
        
        for(int x=0 ; x<largeur; x++)                       
            for(int y = 0 ; y<hauteur ;y++)
                this.pane.getChildren().add(this.matrice_graphique[x][y]); // on lui ajoute les rectangles
    }
    
    
    //fonction pour le scroll qui permet de mettre ajouter/retirer une graine
    public void setGraine(int x , int y , int val){
            x= x/cellule; //on récupère les coordonnées qu'on transforme a une case de la matrice
            y= y/cellule;
            
            if (val < 0)
                fourmiliere.setQteGraines(x, y, fourmiliere.getGraine(x, y)+1);
            else 
                fourmiliere.setQteGraines(x, y, fourmiliere.getGraine(x, y)-1);
            
            this.loadMatrice();
            this.setPane();
    }
    
    
    //fonction appeler quand on clique pour ajouter un mur 
    public void setMur(int x , int y){ //on teste qu'il n'y est rien où on pose le mur
        if(x>0 && x<this.fourmiliere.getLargeur() && y>0 && y<this.fourmiliere.getHauteur() && !this.fourmiliere.contientFourmi(x, y) && this.fourmiliere.getQteGraines(x, y)<=0){
            
            if(fourmiliere.getMur(x, y)) //si deja un mur alors on enleve
                fourmiliere.setMur(x,y,false);
            else//sinon on en met un
                fourmiliere.setMur(x, y, true);
            
            this.loadMatrice();
            this.setPane();
        }
    }
    
    
    //fonction appeler pour mettre une fourmis
    public void setFourmis(int x , int y){// si il n'y a rien biensur
        if(x>0 && x<this.fourmiliere.getLargeur() && y>0 && y<this.fourmiliere.getHauteur() &&this.fourmiliere.getQteGraines(x, y)<=0 && !fourmiliere.getMur(x, y) && !this.fourmiliere.contientFourmi(x, y) ){
            
            fourmiliere.ajouteFourmi(x, y);
            this.loadMatrice();
            this.setPane();
        }
    }
    

    //fonction qui avance d'un coup l'evolution et donc rafraichit la matrice et le pane 
    public void updateEvolue(){ // fonction appeler dans le threads
        this.fourmiliere.evolue();
        this.loadMatrice();
        this.setPane();
        this.coup +=1;
    }
    
    
    // fonction pour le bouton aleatoire 
    public void AleaGame(){
        int largeur = this.fourmiliere.getLargeur();
    
        Random r = new Random();
        int graine = r.nextInt(largeur/10);  // nombre de graine aleatoire entre 0 et un dixieme de la taille du plateau
        int fourmis = r.nextInt(largeur/10);   // nombre de fourmis aleatoire entre 0 et un dixieme de la taille du plateau
        
        for(int i = 0 ; i<fourmis ; i++) //pour chaque fourmis créé
            this.setFourmis(r.nextInt(largeur-1)+1, r.nextInt(largeur-1)+1); // on la place aleatoirement entre O et Taille du plateau
        
        for(int i = 0 ; i<graine ; i++) // idem pour les graines 
            fourmiliere.setQteGraines(r.nextInt(largeur-1)+1 , r.nextInt(largeur-1)+1, 3);
 
    }
    
    
    //renvoie le nombre de fourmis pour le label qui indique le nombre de fourmis
    public int getNbrsFourmis(){
        int largeur = this.fourmiliere.getLargeur();
        int hauteur = this.fourmiliere.getHauteur();
        int n= 0;
        for(int x=1 ; x<largeur-1; x++){
            for(int y = 1 ; y<hauteur-1 ;y++){
                if(this.matrice_graphique[x][y].getFill()== Color.GREEN || this.matrice_graphique[x][y].getFill()== Color.BLUE )
                    n++; 
            }
        }
        return n;
    }
    
    
    //idem pour les graines
    public int getNbrsGraine(){
        int largeur = this.fourmiliere.getLargeur();
        int hauteur = this.fourmiliere.getHauteur();
        int n= 0;
        for(int x=1 ; x<largeur-1; x++){
            for(int y = 1 ; y<hauteur-1 ;y++){
                n += this.fourmiliere.getQteGraines(x, y) ;
                    
            }
        }
        return n;
    }
    
    
    //idem pour les coups jouer
    public int getCoup(){
        return coup;
    }
}
