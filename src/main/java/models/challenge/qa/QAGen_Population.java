package models.challenge.qa;

import java.util.Random;
import java.util.Vector;

import models.challenge.Entry;
import models.util.SingletonCache;
import models.util.SparqlQuerier;




public class QAGen_Population implements QAGen
{

	@Override
	public String getSupportedKey() 
	{
		return "country";
	}

	@Override
	public QA genQA(Object value) 
	{
		String countryName = (String)value;
		String question = "Which is the population of " + countryName + "?";
		Vector<String> answers = new Vector<String>();
		Vector<Entry> aproposEntries = new Vector<Entry>();
		Random rand = new Random();
		int correctAnswer = rand.nextInt(4);
		Vector<String> allCountries = SingletonCache.getAllCountries();
		
		for(int i=0; i<4; i++)
		{
			if(correctAnswer == i)
			{
				answers.add(((float)SparqlQuerier.getPopulation(countryName)/1000000) + " millions");
				aproposEntries.add(new Entry("population", SparqlQuerier.getPopulation(countryName)));
			}
			else
			{
				String randCountry = allCountries.elementAt(rand.nextInt(allCountries.size()));
				answers.add(((float)SparqlQuerier.getPopulation(randCountry)/1000000) + " millions");
			}
		}
		
		return new QA(question, answers, correctAnswer, aproposEntries);
	}

}
