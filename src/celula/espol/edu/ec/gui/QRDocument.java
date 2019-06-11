/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.gui;

import celula.espol.edu.ec.model.Const;
import celula.espol.edu.ec.model.Document;
import celula.espol.edu.ec.model.DocumentController;
import celula.espol.edu.ec.resource.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private final MenuBar menuBar;
    private Menu file;
    private Menu document;
    private Menu help;
    
    
    public QRDocument(Stage stage) {
        this.stage = stage;
        name = new TextField();
        templates = new ComboBox<>();
        apply = new Button("Aplicar");
        create = new Button("Generar");
        fileChooser = new FileChooser();
        menuBar = new MenuBar();
        init();
    }
    
    private void init(){
        fileChooser.setTitle("Abrir");
        this.setTop(topPanel());
        this.setLeft(leftPanel()); 
        createEvent();
        templatesEvent();
        applyEvent();
    }
    
    private Pane topPanel(){
        file = new Menu("File");
        //Menu file
        String[] itemsFile = {"Open", "Export", "Close"};
        for(String item: itemsFile){
            String img = item.toLowerCase() + ".png";
            if(item.equals("Export")){
                Menu it = new Menu(item);
                it.setGraphic(Resource.getIcon(img)); 
                it.getItems().add(new MenuItem("PDF (*.pdf)"));
                it.getItems().add(new MenuItem("Word (*.docx)"));
                it.getItems().get(0).setOnAction(new MenuBarEvent("PDF (*.pdf)")); 
                it.getItems().get(1).setOnAction(new MenuBarEvent("Word (*.docx)")); 
                file.getItems().add(it);
            }else{
                MenuItem it = new MenuItem(item);
                it.setGraphic(Resource.getIcon(img));
                it.setOnAction(new MenuBarEvent(item)); 
                file.getItems().add(it);
            }
        }
        
        document = new Menu("Document");
        //Menu document
        MenuItem setting = new MenuItem("Settings");
        setting.setGraphic(Resource.getIcon("settings.png")); 
        setting.setOnAction(new MenuBarEvent("Settings"));
        document.getItems().add(setting);
        help = new Menu("Help");
        //Menu help
        String[] itemsHelp = {"Manual", "About"};
        for(String item: itemsHelp){
            MenuItem mi = new MenuItem(item);
            mi.setGraphic(Resource.getIcon(item.toLowerCase() + ".png")); 
            mi.setOnAction(new MenuBarEvent(item));
            help.getItems().add(mi);
        }
        menuBar.getMenus().addAll(file, document, help);
        return new VBox(menuBar);
    }
    
    private Pane leftPanel(){
        StackPane content = new StackPane();
        VBox panel = new VBox(20);
        double med = Const.MAX_WIDTH/70;
        panel.setPadding(new Insets(med, med, med, med)); 
        Rectangle back = new Rectangle(Const.MAX_WIDTH/3.5, Const.MAX_HEIGHT);
        back.setFill(Color.rgb(117, 117, 117));
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setOffsetY(2); 
        shadow.setRadius(0.5); 
        back.setEffect(shadow); 
        panel.getChildren().addAll(topLeftPanel()/*, centerLeftPanel()*/);
        content.getChildren().addAll(back, panel);
        return content;
    }
    
    private Pane topLeftPanel(){
        GridPane grid = new GridPane();
        grid.setVgap(20);
        grid.setHgap(10);
        name.setMinWidth(Const.MAX_WIDTH/5); 
        grid.addRow(0, Resource.getText("Nombre"), name);
        //Combo box de plantillas
        templates.getItems().add(Document.DOCUMENT_DEFAULT);
        grid.addRow(1, Resource.getText("Plantillas"), templates);
        grid.add(create, 0, 2, 2, 1);
        return grid;
    }
    
    private Pane centerLeftPanel() {
        
        HBox content = new HBox(10);
        content.getChildren().addAll(new Label("Plantilla:"), templates, apply);
        return content;
    }
    
    private void createEvent(){
        create.setOnAction(e-> {
           DocumentController dc = new DocumentController(name.getText()); 
           try{
               Document doc = dc.generateDocument();
               dc.generatePDF(doc);
               name.setDisable(true); 
           }catch(IOException ex){
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
                    Document docum = new Document(currentTemplate.getName(), currentTemplate);
                    templates.getItems().add(docum);
                    Platform.runLater(()->templates.setValue(docum));
                    applyEvent();
                }else
                    Platform.runLater(()->templates.setValue(null)); 
            }else
                applyEvent();
            
        });
    }
    
    private void applyEvent(){
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
    }
    
    
    private class MenuBarEvent implements EventHandler<ActionEvent> {
        private final String item;
        
        MenuBarEvent(String item){
            this.item = item;
        }
        
        @Override
        public void handle(ActionEvent event) {
            switch(item){
                case "Open":
                    break;
                case "PDF (*.pdf)":
                    break;
                case "Word (*.docx)":
                    break;
                case "Close":
                    Platform.exit();
                    break;
                case "Settings":
                    break;
                case "Manual":
                    break;
                case "About":
                    break;
                default:
                    break;
            }
        }
    
    
    }
}


