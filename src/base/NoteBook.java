package base;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class NoteBook implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
	}
	/**
	 * Constructor of an object NoteBook from an object serialization on disk
	 * @param file, the path of the file for loading the object serialization
	 */
	public NoteBook(String file) {
		FileInputStream fis=null;
		ObjectInputStream in =null;
		if(file.equals("")) {file.concat(".");}
		try {
			fis = new FileInputStream(file);
			in = new ObjectInputStream(fis);
			NoteBook temp = (NoteBook) in.readObject();
			this.folders = temp.folders;
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean createImageNote(String folderName, String title) {
		ImageNote n = new ImageNote(title);
		return insertNote(folderName,n);
	}
	
	public boolean createTextNote(String folderName,String title) {
		TextNote n = new TextNote(title);
		return insertNote(folderName,n);
	}
	
	//Overloading method createTextNote
	public boolean createTextNote(String folderName,String title, String content) {
		TextNote n = new TextNote(title,content);
		return insertNote(folderName,n);
	}
	
	public ArrayList<Folder> getFolders() {
		return folders;
	}
	
	public boolean insertNote(String folderName, Note note) {
		Folder f = null;
		for(Folder f1:folders) {
			if(f1.getName().equals(folderName)) {f=f1;}
		}
		if(f==null) {
			f = new Folder(folderName);
			folders.add(f);
		}
		
		for(Note n: f.getNotes()) {
			if(n.equals((Note)note)) {
				System.out.println("Creating note "+note.getTitle()+" under folder "+folderName+" failed");
				return false;
			}
		}
		
		f.addNote(note);
		return true;
	}
	
	public void sortFolders() {
		for(Folder f:folders) {
			f.sortNotes();
		}
		folders.sort(null);
	}
	
	public void addFolder(String folderName) {
		folders.add(new Folder(folderName));
	}
	
	public ArrayList<Note> searchNotes(String keywords){
		ArrayList<Note> list = new ArrayList<Note>();
		for(Folder f:folders) {
			list.addAll(f.searchNotes(keywords));
		}
		return list;
	}
	
	public Folder getFolder(String folderName) {
		for(Folder folder:folders) {
			if(folder.getName().equals(folderName)) {return folder;}
		}
		return null;
	}
	
	public Note getNote(String folderName,String noteName) {
		Folder folder = getFolder(folderName);
			for(Note note:folder.getNotes()) {
				if(note.getTitle().equals(noteName)) {return note;}
			}
		return null;
	}
	
	public Note getNote(String noteName) {
		for(Folder folder:folders) {
			for(Note note:folder.getNotes()) {
				if(note.getTitle().equals(noteName)) {return note;}
			}
		}
		return null;
	}
	
	
	/**
	 * method to save the NoteBook instance to file
	 * 
	 * @param file, the path of the file where to save the object serialization
	 * @return true if save on file is successful, false otherwise
	 */
	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		if(file.equals("")) {file.concat(".");}
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	public void saveNote(String folderName,String noteName, String content) {
		Note note = getNote(folderName,noteName);
		if(note instanceof TextNote) {
			TextNote textnote = (TextNote) note;
			textnote.content = content;
		}
		else {return;}
	}
	public void removeNote(String folderName, String noteName) {
		Folder folder = getFolder(folderName);
		Note note = getNote(folderName,noteName);
		folder.getNotes().remove(note);
		
	}

}
