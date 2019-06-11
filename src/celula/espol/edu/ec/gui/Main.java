/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.gui;

import celula.espol.edu.ec.model.Const;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kenny Camba
 */
public class Main extends Application{
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setMaximized(true); 
        Scene scene = new Scene(new QRDocument(stage), Const.MAX_WIDTH, Const.MAX_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
