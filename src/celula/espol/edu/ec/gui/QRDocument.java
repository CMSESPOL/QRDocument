/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.gui;

import celula.espol.edu.ec.model.Const;
import celula.espol.edu.ec.model.Document;
import celula.espol.edu.ec.model.DocumentController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;

/**
 *
 * @author Kenny Camba
 */
public class QRDocument extends BorderPane{
    private final TextField name;
    private final ComboBox<Document> templates;
    private final Button apply;
    private final Button create;
    private final FileChooser fileChooser;
    private final Stage stage;
    
    
    public QRDocument(Stage stage) {
        this.stage = stage;
        name = new TextField();
        templates = new ComboBox<>();
        apply = new Button("Aplicar");
        create = new Button("Crear");
        fileChooser = new FileChooser();
        init();
    }
    
    private void init(){
        fileChooser.setTitle("Abrir");
        this.setLeft(leftPanel()); 
        createEvent();
        templatesEvent();
        applyEvent();
    }
    
    private Pane leftPanel(){
        StackPane content = new StackPane();
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20, 20, 20, 20)); 
        Rectangle back = new Rectangle(Const.MAX_WIDTH/3, Const.MAX_HEIGHT);
        back.setFill(Color.DARKGRAY);
        panel.getChildren().addAll(topLeftPanel(), centerLeftPanel());
        content.getChildren().addAll(back, panel);
        return content;
    }
    
    private Pane topLeftPanel(){
        VBox content = new VBox(5);
        HBox namepane = new HBox(10);
        namepane.setAlignment(Pos.CENTER_LEFT); 
        namepane.getChildren().addAll(new Label("Nombre:"), name);
        content.getChildren().addAll(namepane, create);
        return content;
    }
    
    private Pane centerLeftPanel() {
        templates.getItems().add(Document.DOCUMENT_DEFAULT);
        HBox content = new HBox(10);
        content.getChildren().addAll(new Label("Plantilla:"), templates, apply);
        return content;
    }
    
    private void createEvent(){
        create.setOnAction(e-> {
           DocumentController dc = new DocumentController(name.getText()); 
           try{
               dc.generateDocument();
               name.setDisable(true); 
           }catch(Exception ex){
               System.out.println(ex.getMessage());
           }
           
        });
    }
    private void templatesEvent(){
        templates.setOnAction(e-> {
            Document doc = templates.getSelectionModel().getSelectedItem();
            if(doc == Document.DOCUMENT_DEFAULT){
                File currentTemplate = fileChooser.showOpenDialog(stage);
                if(currentTemplate != null){
                    Document document = new Document(currentTemplate.getName(), currentTemplate);
                    templates.getItems().add(document);
                    Platform.runLater(()->templates.setValue(document)); 
                }
            }
        });
    }
    
    private void applyEvent(){
        apply.setOnAction(e -> {
            Document doc = templates.getValue();
            System.out.println(doc);
            if(doc != null && doc != Document.DOCUMENT_DEFAULT){
                try {
                    OPCPackage pkg = OPCPackage.open(doc.getDirectory());
                    System.out.println(doc.getDirectory().getAbsolutePath());
                    pkg.replaceContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.template.main+xml",
                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml");
                    FileOutputStream word = new FileOutputStream(name.getText()+".docx");
                    pkg.save(word); 
                    
                } catch (InvalidFormatException | FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(QRDocument.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    Logger.getLogger(QRDocument.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
