/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeuDesFourmis.main;

import jeuDesFourmis.vue.OneGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jeuDesFourmis.model.Fourmiliere;

/**
 *
 * @author abott
 */
public class Lancement extends Application {
    
    private OneGame jeu = new OneGame(); // Partis graphique ( un pane ) qui contient la grille et quelques boutons
    
    private final Button reset = new Button("RESET");// on ajoute les boutons modifiant la partie 
    private final Button plus = new Button("+");
    private final Button moins = new Button("-");
    private final Pane root = new Pane();
    
   
    @Override
    public void start(Stage primaryStage) {
        
       // on ajoute la pane du jeu 
       root.getChildren().add(jeu);
       //Si on clique on met a jour les labels 
       root.setOnMouseClicked(event->{
           jeu.refreshLabel();
       });
       
       // on definis une action sur reset 
       reset.setOnAction(event->{
            jeu.getTache().cancel(); //on arrete la tache
            root.getChildren().remove(jeu); // on retirer le jeu actuel
            jeu = new OneGame(); //on créer un nouveau jeu 
            root.getChildren().add(jeu); // on l'ajoute le nouveau
            updateButton(); // on atualise les boutons de cette classe
       });
       reset.setLayoutX(0);
       reset.setLayoutY(575);
       root.getChildren().add(reset);
       
       // action sur le bouton plus
       plus.setOnAction(event->{
            Fourmiliere oldFourmiliere = jeu.getFourmiliere();
            //on créer une fourmiliere plus grande de 1 
            Fourmiliere newFourmiliere = new Fourmiliere(jeu.getFourmiliere().getLargeur()+1 ,jeu.getFourmiliere().getLargeur()+1 ,jeu.getFourmiliere().getQMax());
            jeu.getTache().cancel(); // on arrete la tache
            root.getChildren().remove(jeu); // on retire le jeu 
            jeu = new OneGame(newFourmiliere,oldFourmiliere); // on recreer une grille plus grande de 1
  
            root.getChildren().add(jeu); // on l'ajoute au pane
            updateButton();
       });
       plus.setLayoutX(50);
       plus.setLayoutY(550);
       root.getChildren().add(plus);
       
       
      // idem pour le bouton moins
       moins.setOnAction(event->{
           
            Fourmiliere oldFourmiliere = jeu.getFourmiliere();
            
            Fourmiliere newFourmiliere = new Fourmiliere(jeu.getFourmiliere().getLargeur()-1 ,jeu.getFourmiliere().getLargeur()-1 ,jeu.getFourmiliere().getQMax());
      
            jeu.getTache().cancel();
            root.getChildren().remove(jeu);
            jeu = new OneGame(newFourmiliere,oldFourmiliere);
            root.getChildren().add(jeu);
            updateButton();
   
          
       });
       moins.setLayoutX(50);
       moins.setLayoutY(575);
       root.getChildren().add(moins);
       
        Scene scene = new Scene(root, 500, 600);
        
        primaryStage.setTitle("Grand thef auto : Fourmis city");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void updateButton(){
        
        root.getChildren().remove(reset);
        root.getChildren().add(reset);
        
        root.getChildren().remove(plus);
        root.getChildren().add(plus);
        
         root.getChildren().remove(moins);
        root.getChildren().add(moins);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
