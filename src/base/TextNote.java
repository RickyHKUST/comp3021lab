package base;

public class TextNote extends Note {
	String content;
	
	public TextNote(String title) {
		super(title);
	}
	
	public TextNote(String title, String content) {
		super(title);
		this.content=content;
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
