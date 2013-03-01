package models.challenge;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import models.challenge.apropos.*;
import models.challenge.qa.*;




public class BasicChallengeGen implements ChallengeGen
{
	Vector<String> supportedKeys;
	HashMap<String, Vector<QAGen>> qaGenerators;
	HashMap<String, Vector<AproposGen>> aproposGenerators;
	
	public BasicChallengeGen() 
	{
		Vector<QAGen> qaGenGroup;
		Vector<AproposGen> aproposGenGroup;
		
		qaGenerators = new HashMap<String, Vector<QAGen>>();
		aproposGenerators = new HashMap<String, Vector<AproposGen>>();
		
		// Add supported keys
		supportedKeys = new Vector<String>();
		supportedKeys.add("country");
		
		// `country` Q&A Generators
		qaGenGroup = new Vector<QAGen>();
		qaGenGroup.add(new QAGen_Population());
		//qaGenGroup.add(new QAGen_OfficialLanguage());
		qaGenGroup.add(new QAGen_Neighbors());
		qaGenGroup.add(new QAGen_PeopleBornIn());
		qaGenGroup.add(new QAGen_PeopleDateOfBirth());
		qaGenGroup.add(new QAGen_EventsIn());
		//qaGenGroup.add(new QAGen_GovernmentType());
		qaGenerators.put("country", qaGenGroup);
		
		// `country` Apropos Generators
		aproposGenGroup = new Vector<AproposGen>();
		aproposGenGroup.add(new AproposGen_Neighbors());
		aproposGenerators.put("country", aproposGenGroup);
		
		aproposGenGroup = new Vector<AproposGen>();
		aproposGenGroup.add(new AproposGen_ClosePopulation());
		aproposGenerators.put("population", aproposGenGroup);
		
		// `language` Apropos Generators
		//aproposGenGroup = new Vector<AproposGen>();
		//aproposGenGroup.add(new AproposGen_SpokenLanguage());
		//aproposGenerators.put("language", aproposGenGroup);
		
		// `governmentType` Apropos Generators
//		aproposGenGroup = new Vector<AproposGen>();
//		aproposGenGroup.add(new AproposGen_GovernmentType());
//		aproposGenerators.put("governmentType", aproposGenGroup);
	}

	public boolean isKeySupported(String key)
	{
		if(supportedKeys.contains(key))
			return true;
		
		return false;
	}
	
	public Challenge genChallenge(Entry seedEntry)
	{
		Random rnd = new Random(System.currentTimeMillis());
//		String question = "";
//		Vector<String> answers = new Vector<String>(4);
//		Vector<Entry> aproposInputEntries = new Vector<Entry>(8);
		QA qa;
		Entry aproposInputEntry;
//		String aproposOutput = "";
//		Entry aproposOutputEntry;
		Apropos apropos;
		Vector<QAGen> qaGenGroup;
		QAGen qaGen;
		Vector<AproposGen> aproposGenGroup;
		AproposGen aproposGen;
		
		// Choose a Q&A Generator
		qaGenGroup = qaGenerators.get(seedEntry.getKey());
		
		while(true)
		{
			qaGen = qaGenGroup.elementAt(Math.abs(rnd.nextInt()) % qaGenGroup.size());
			
			try {
				// Generate Q&A
				qa = qaGen.genQA(seedEntry.getValue());
			}catch(Exception e)
			{
			  e.printStackTrace();
				continue;
			}
			break;
		}
		
		// Choose an apropos input entry
		aproposInputEntry = qa.getAproposEntries().elementAt(
				Math.abs(rnd.nextInt()) % qa.getAproposEntries().size());
		
		// Choose an Apropos Generator
		aproposGenGroup = aproposGenerators.get(aproposInputEntry.getKey());
		int n = Math.abs(rnd.nextInt()) % aproposGenGroup.size();
		aproposGen = aproposGenGroup.elementAt(n);
		
		// Generate Apropos
		apropos = aproposGen.genApropos(aproposInputEntry.getValue());
		
		return new Challenge(qa.getQuestion(), qa.getAnswers(), qa.getCorrectAnswer(), apropos);
	}
}
