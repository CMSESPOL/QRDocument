/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.resource;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Kenny Camba
 */
public class Resource {
    public Resource() {
        //
    }
    
    public static ImageView getIcon(String img){
        Resource r = new Resource();
        return new ImageView(r.getImage(img));
    }
    
    private Image getImage(String img){
        return new Image(Resource.class.getResourceAsStream(img));
    }
    
    public static Text getText(String t){
        Text txt = new Text(t);
        txt.setFill(Color.WHITE);
        txt.setFont(Font.font(txt.getFont().getFamily(), FontWeight.BOLD, 14));
        return txt;
    }
    
    /*private String getStylepriv(){
        return Resource.class.getResource("style.css").toExternalForm();
    }
    
    public static String getStyle(){
        Resource r = new Resource();
        return r.getStylepriv(); 
    }*/
}
