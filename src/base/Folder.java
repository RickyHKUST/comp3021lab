package base;
import java.util.ArrayList;
import java.util.Objects;

public class Folder implements Comparable<Folder> {
	
	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name) {
		this.name = name;
		notes = new ArrayList<Note>();
	}
	
	public void addNote(Note n) {
		notes.add(n);
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Note> getNotes(){
		return notes;
	}
	
	@Override
	public String toString() {
		int nText=0;
		int nImage=0;
		
		for(Note n: notes) {
			if(n instanceof TextNote) {nText++;}
			else if(n instanceof ImageNote) {nImage++;}
		}
		
		return name+":"+nText+":"+nImage;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int compareTo(Folder o) {
		return this.name.compareTo(o.name);
	}
	
	public void sortNotes() {
		notes.sort(null);
	}
	
	public ArrayList<Note> searchNotes(String keywords){
		ArrayList<Note> list = new ArrayList<Note>();
		for(Note n:notes) {
			if(n.hasKeywords(keywords))
			list.add(n);
		}
		return list;
	}
}
