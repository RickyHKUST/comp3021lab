package base;
import java.util.ArrayList;

public class NoteBook {
	
	private ArrayList<Folder> folders;
	
	public NoteBook() {
		folders = new ArrayList<Folder>();
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
	
	public ArrayList<Note> searchNotes(String keywords){
		ArrayList<Note> list = new ArrayList<Note>();
		for(Folder f:folders) {
			list.addAll(f.searchNotes(keywords));
		}
		return list;
	}

}
