package base;

import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class TextNote extends Note {
	
	private static final long serialVersionUID = 1L;
	String content;
	
	public TextNote(String title) {
		super(title);
	}
	
	/**
	* load a TextNote from File f
	* 
	* the tile of the TextNote is the name of the file
	* the content of the TextNote is the content of the file
	* 
	* @param File f 
	*/
	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}
	
	public TextNote(String title, String content) {
		super(title);
		this.content=content;
	}
	
	/**
	* get the content of a file
	* 
	* @param absolutePath of the file
	* @return the content of the file
	*/
	private String getTextFromFile(String absolutePath) {
		String result = "";
		if(absolutePath.equals("")) {absolutePath=".";}
		FileInputStream fis=null;
		ObjectInputStream in =null;
		try {
			fis = new FileInputStream(absolutePath);
			in = new ObjectInputStream(fis);
			result = (String) in.readObject();
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	* export text note to file
	* 
	* 
	* @param pathFolder path of the folder where to export the note
	* the file has to be named as the title of the note with extension ".txt"
	* 
	* if the tile contains white spaces " " they has to be replaced with underscores "_"
	* 
	* 
	*/
	public void exportTextToFile(String pathFolder) {
		if(pathFolder.equals("")) {pathFolder=".";}
		File file = new File( pathFolder+File.separator+this.getTitle().replaceAll(" ", "_")+".txt");
		FileWriter writer;
		try {
			if(file.exists()) {writer = new FileWriter(file,true);}
			else {writer = new FileWriter(file);}
			
			writer.write(content);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean hasKeywords(String keywords) {
		String[] stringList = keywords.split("\\s+");
		
		for(int i=0;i<stringList.length;i++) {
			
			if(i+2<stringList.length && (stringList[i+1].equals("OR") || stringList[i+1].equals("or"))) {
				if(title.toLowerCase().contains(stringList[i].toLowerCase()) || title.toLowerCase().contains(stringList[i+2].toLowerCase())
					||	content.toLowerCase().contains(stringList[i].toLowerCase()) || content.toLowerCase().contains(stringList[i+2].toLowerCase())) {
					i+=2;continue;
					}
				return false;
			}
			
			if(!title.toLowerCase().contains(stringList[i].toLowerCase()) && !content.toLowerCase().contains(stringList[i].toLowerCase())) {
				return false;
				}
		} 
		return true;
	}
}
