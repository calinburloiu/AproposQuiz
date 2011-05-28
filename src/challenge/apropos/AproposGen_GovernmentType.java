package challenge.apropos;

import challenge.Entry;

public class AproposGen_GovernmentType implements AproposGen
{
	public String getSupportedKey() {
		return "governmentType";
	}
	
	public Apropos genApropos(Object value) {
		Entry entry = new Entry("country", "Greece");
		String governmentType = (String)value;
		String text = "The government type in Greece is also " + governmentType + ".";
		
		return new Apropos(text, entry);
	}
}
