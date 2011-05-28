package challenge.apropos;

import java.util.Vector;

import main.SingletonCache;
import main.SparqlQuerier;

import challenge.Entry;

public class AproposGen_ClosePopulation implements AproposGen
{
	public String getSupportedKey() {
		return "population";
	}
	
	public Apropos genApropos(Object value) 
	{
		Integer inputPopulation = (Integer)value;
		String outputCountry = null;
		Vector<String> allCountries = SingletonCache.getAllCountries();
		int outputPopulation;
		float lowAlpha = 1.0f, highAlpha = 1.0f;
		
		do
		{
			if(lowAlpha >= 0.06f)
				lowAlpha -= 0.05f;
			highAlpha += 0.05f;
			for(String country : allCountries)
			{
				outputPopulation = SparqlQuerier.getPopulation(country);
				if(outputPopulation >= lowAlpha * inputPopulation &&
						outputPopulation <= highAlpha * inputPopulation)
				{
					outputCountry = country;
					break;
				}
			}
		} while(outputCountry == null);
		
		String text = "Population " +inputPopulation+ " is close to " + outputCountry + "'s.";
		Entry entry = new Entry("country", outputCountry);
		
		return new Apropos(text, entry);
	}
}