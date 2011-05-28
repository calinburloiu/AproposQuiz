package challenge.apropos;

import main.SparqlQuerier;
import challenge.Entry;

public class AproposGen_Neighbors implements AproposGen
{
	public String getSupportedKey() {
		return "country";
	}
	
	public Apropos genApropos(Object value) {
		String country = (String)value;
		Entry entry = new Entry("country", SparqlQuerier.getRandomNeighbor(country));
				
		String text = "Country " + country + " has neighbor: " + entry.getValue();
		
		return new Apropos(text, entry);
	}
}