/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package celula.espol.edu.ec.model;

import java.io.File;

/**
 *
 * @author Kenny Camba
 */
public class Document {
    public final static Document DOCUMENT_DEFAULT = new Document("Cargar nueva...", null);
    private String name;
    private File directory;
    
    public Document(String name, File directory){
        this.name = name;
        this.directory = directory;
    }
    
    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name.substring(0, name.indexOf('.')); 
    }

    public void setName(String name) {
        this.name = name + this.name.substring(this.name.indexOf('.')); 
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    
}
