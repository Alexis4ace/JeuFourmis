/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeuDesFourmis.vue;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import jeuDesFourmis.model.Fourmiliere;

/**
 *
 * @author 
 */
public class OneGame extends Pane {
    
    private Loupe l= null;
    private int clickX=30;
    private int clickY=30;
    private int  vitesse = 20 ;
    
    private Fourmiliere fourmiliere = new Fourmiliere(50,50,10);
    private MatriceGraphique matrice = new MatriceGraphique(fourmiliere);
    
    private Pane graphique = matrice.getPane();
   
    
    Button play = new Button("play");
    
    Label graine ;
    Label coup;
    Label fourmis;
    
    
     private Task calculateTask = new Task(){
                
                protected Void call() throws Exception { 
                try {
                    while(true){
                     
                       Thread.sleep(vitesse);
                       if(play.getText() == "pause" ) {
                        removeGraphique();
                        Platform.runLater(() -> {matrice.updateEvolue(); } );
                      
                        
                         getGraphique( matrice.getPane()  );
                       }
                    }
                }
                    catch (InterruptedException e) {
                  
                }
                    return null ; 
                }
            };  
    
    
    public OneGame(){
       
       ActionBouton();

    }
    
    public OneGame(Fourmiliere f , Fourmiliere oldf){
        
       
        this.fourmiliere = f ;
        f.setGame(oldf);
        matrice = new MatriceGraphique(fourmiliere);
        graphique = matrice.getPane();
        ActionBouton();
    }
    
    public Fourmiliere getFourmiliere(){
        return fourmiliere;
    }
    
    //initialisation des boutons et du slides qu'on appel dans chaque constructeur et de la grille
    private void ActionBouton(){
        
        this.getChildren().add(graphique);
        
        //slider
        SliderBetter slide = new SliderBetter("vitesse",5,100,vitesse);
        slide.setLayoutX(100);
        slide.setLayoutY(500);
        slide.valueProperty().addListener((observable, oldValue, newValue) -> {
            slide.setValue(newValue.intValue());
            this.vitesse = newValue.intValue();
        });
        this.getChildren().add(slide);
       
        //Label graine
        graine = new Label("Nombres de graine: "+this.matrice.getNbrsGraine());
        graine.setLayoutX(350);
        graine.setLayoutY(540);
        this.getChildren().add(graine);
        
        //label coup
        coup = new Label("Nombres de coup: "+this.matrice.getCoup());
        coup.setLayoutX(350);
        coup.setLayoutY(520);
        this.getChildren().add(coup);
        
        //label fourmis
        fourmis = new Label("Nombres de fourmis: "+this.matrice.getNbrsFourmis());
        fourmis.setLayoutX(350);
        fourmis.setLayoutY(530);
        this.getChildren().add(fourmis);
        
        //action sur la grille ( Pane ) quand on utilise la molette
        graphique.setOnScroll(event -> {
            int clickZ = (int) event.getDeltaY() ;
            this.matrice.setGraine((int)event.getX(), (int) event.getY(), clickZ);
        });
        
        //action sur la grille quand on click a la souris
        graphique.setOnMouseClicked(event -> {
           
            if(l!=null) // la loupe apparait quand on a cliqué
                this.getChildren().remove(l); // donc on la supprime du pane si elle est deja créer car on va rajouter une nouvelle
            
            clickX =(int) event.getX()/10; // recupere coordonnée dans la matrice
            clickY = (int) event.getY()/10;
            
            
            if(!event.isShiftDown()) // si shift n'est pas appuyé
                this.matrice.setMur(clickX, clickY); // on met un mur 
            if(event.isShiftDown())//si il n'est pas appuyer on met  on met une fourmis
                this.matrice.setFourmis(clickX, clickY);
            
            
            l = new Loupe(this.matrice,clickX,clickY);    // on créer la loupe donc la petite matrice par rapport au click 
            this.getChildren().add(l); //on l'ajoute au pane
          
        });
        // button quitter
        Button btn = new Button("QUITTER");
        btn.setOnAction(event->{
           this.calculateTask.cancel();
           Platform.exit();
        });
        btn.setLayoutX(435);
        btn.setLayoutY(575);
        this.getChildren().add(btn);
        
        //button aleatoire
        Button alea = new Button("alea");
        alea.setOnAction(event->{
            
          this.matrice.AleaGame();
          refreshLabel();
        });
        alea.setLayoutX(400);
        alea.setLayoutY(575);
        this.getChildren().add(alea);
        
        //action sur le bouton play
        play.setOnAction(event->{
            if(play.getText()== "pause"){
                
                alea.setDisable(false);
                btn.setDisable(false);
                play.setText("play ");
            }
            else{
                new Thread(getTache()).start();
                alea.setDisable(true);
                btn.setDisable(true);
                play.setText("pause");
            }
            
       });
       play.setLayoutX(200);
       play.setLayoutY(575);
       this.getChildren().add(play);
    }
    
    //on réactulise ici les valeurs des labels 
    public void refreshLabel(){// fonction appeler apres une action
        graine.setText("Nombres de graine: "+this.matrice.getNbrsGraine());
        fourmis.setText("Nombres de fourmis: "+this.matrice.getNbrsFourmis());
        coup.setText("Nombres de coup: "+this.matrice.getCoup());
    }
   
    //reduit la taille de la grille de -1 , -1
    public void upgradeMoins(){// appeler lors du click sur le bouton moins
        
        Fourmiliere newfourmiliere = new Fourmiliere(fourmiliere.getLargeur()-1,fourmiliere.getHauteur()-1,fourmiliere.getQMax());
        MatriceGraphique newmatrice = new MatriceGraphique(newfourmiliere);
        this.fourmiliere = newfourmiliere;
        this.matrice = newmatrice;
        this.graphique = this.matrice.getPane();

    }
    

    public Task getTache(){
        return this.calculateTask;
    }
    
    public void removeGraphique(){
        Platform.runLater( () -> {this.getChildren().remove(this.graphique);});
       
    }
    public void getGraphique( Pane graph ){
        this.graphique =  graph;
         Platform.runLater( () -> {this.getChildren().add(this.graphique);});
           }
}
