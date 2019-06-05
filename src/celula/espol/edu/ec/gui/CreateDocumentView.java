/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.gui;

import celula.espol.edu.ec.model.DocumentController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author Kenny Camba
 */
public class CreateDocumentView extends VBox {
    private TextField title;
    private Button generate;
    
    public CreateDocumentView(){
        title = new TextField();
        generate = new Button("Generar");
        init();
        topPanel();
        buttonPanel();
        events();
    }
    
    private void init(){
        this.setSpacing(30);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); 
    }
    
    private void topPanel(){
        HBox top = new HBox(15);
        top.setAlignment(Pos.CENTER);
        Text label = new Text("Ingrese título");
        label.setFill(Color.RED);
        top.getChildren().addAll(label, title);
        this.getChildren().add(top);
    }

    private void buttonPanel() {
        HBox bottom = new HBox(generate);
        bottom.setAlignment(Pos.CENTER);
        this.getChildren().add(bottom);
    }
    
    private void events(){
        generate.setOnAction(e->{
            String text = title.getText();
            DocumentController doc = new DocumentController(text);
            title.setText("");
            Alert alert = new Alert(AlertType.INFORMATION);
            try{
                File f = new File("");
                String file = Paths.get(f.getAbsolutePath(), "src", "celula", "espol", "edu", "ec", "resource", "celula.png").toString();
                System.out.println(file);
                doc.addImage(file);
                doc.generateDocument();
                alert.setTitle("Información");
                alert.setContentText("Archivo " + doc + " creado con éxito"); 
                alert.show();
            }catch(IOException | InvalidFormatException ex){
                alert.setAlertType(AlertType.ERROR);
                alert.setTitle("Error!!!");
                alert.setContentText("Error creando archivo: \n" + ex.getMessage());
                alert.show();
            }
        });
    }
    
}
