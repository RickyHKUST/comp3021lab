package base;
import java.util.Date;
import java.util.Objects;

public class Note implements Comparable<Note> {
	private Date date;
	protected String title;
	
	public Note(String title) {
		this.title=title;
		this.date = new Date(System.currentTimeMillis());
	}
	
	public String getTitle() {
		return title;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Note))
			return false;
		Note other = (Note) obj;
		return Objects.equals(title, other.title);
	}

	@Override
	public int compareTo(Note o) {
		return this.date.compareTo(o.date);
	}
	
	public String toString() {
		return date.toString()+"\t"+title;
	}

	public boolean hasKeywords(String keywords) {
		String[] stringList = keywords.split("\\s+");
		
		for(int i=0;i<stringList.length;i++) {
			
			if(i+2<stringList.length && (stringList[i+1].equals("OR") || stringList[i+1].equals("or"))) {
				if(title.toLowerCase().contains(stringList[i].toLowerCase()) || title.toLowerCase().contains(stringList[i+2].toLowerCase())) {
					i+=2;continue;
					}
				return false;
			}
			
			if(!title.toLowerCase().contains(stringList[i].toLowerCase())) {
				return false;
				}
		} 
		return true;
	}
	
}
