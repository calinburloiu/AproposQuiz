package challenge.apropos;

import challenge.Entry;

public class AproposGen_SpokenLanguage implements AproposGen
{
	public String getSupportedKey() {
		return "language";
	}
	
	public Apropos genApropos(Object value) {
		Entry entry = new Entry("country", "France");
		String language = (String)value;
		String text = "There are ethnic groups in France that speak " + language + ".";
		
		return new Apropos(text, entry);
	}
}