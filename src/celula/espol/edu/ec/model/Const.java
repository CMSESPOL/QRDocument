/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.model;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 *
 * @author Kenny Camba
 */
public class Const {
    
    public static final Rectangle2D VISUAL_BOUNDS = Screen.getPrimary().getVisualBounds();
    public static final double MAX_WIDTH = VISUAL_BOUNDS.getWidth();
    public static final double MAX_HEIGHT = VISUAL_BOUNDS.getHeight();
    public static final double MAX_X = VISUAL_BOUNDS.getMaxX();
    public static final double MAX_Y = VISUAL_BOUNDS.getMaxY();
    public static final double MIN_X = VISUAL_BOUNDS.getMinX();
    public static final double MIN_Y = VISUAL_BOUNDS.getMinY();
    
    private Const(){
        //
    }
}
