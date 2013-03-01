package models.challenge.qa;

import static com.freebase.json.JSON.a;
import static com.freebase.json.JSON.o;

import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import models.challenge.Entry;
import models.util.SingletonCache;




import com.freebase.api.Freebase;
import com.freebase.json.JSON;

public class QAGen_PeopleBornIn implements QAGen {
	Freebase freebase = Freebase.getFreebase();
	
	public String getSupportedKey() {
		return "country";
	}

	public TreeMap<String, String> getPeopleBornHere(String country_name) throws Exception {
		String country_id = SingletonCache.getFreebaseCountryID(country_name);
		if (country_id == null)
			throw new Exception();
		JSON query = o(
				"id", country_id,
				"/location/location/people_born_here", a(o("id", null,
					    "name", null, "limit", 250))
				);
		JSON response = freebase.mqlread(query);
		JSON raw_results = response.get("result").get("/location/location/people_born_here");
		int number_of_results = raw_results.array().size();
		if (number_of_results == 0) {
			return null;
		}
		else {
			TreeMap<String, String> results = new TreeMap<String, String>();
			for (int i=0 ; i<number_of_results ; i++) {
				String name_of_people = raw_results.get(i).get("name").string();
				String id = raw_results.get(i).get("id").string();
				results.put(name_of_people, id);
				//System.out.println(name_of_people);
			}
			return results;
		}
	}
	
	
	public QA genQA(Object value) throws Exception {
		String countryName = (String)value;
		
		// get the list of all the people born in the current country
		TreeMap<String, String> born_in = getPeopleBornHere(countryName);
		// pick a personality to be used in the question
		Random generator = new Random();
		int subject_index = generator.nextInt(born_in.size());
		Object[] people = born_in.keySet().toArray();
		String subject = (String) people[subject_index];
		String question = "Where was " + subject +" born?";
		
		Vector<String> answers = new Vector<String>();
		answers.add(countryName);
		answers.addAll(SingletonCache.getRandomCountries(3, countryName, false));
		
		//shuffle
		int correctAnswer = generator.nextInt(4);
		String first = answers.firstElement();
		answers.set(0, answers.get(correctAnswer));
		answers.set(correctAnswer, first);
		
		Vector<Entry> aproposEntries = new Vector<Entry>();
		for (String country : answers) {
			aproposEntries.add(new Entry("country", country));
		}
		
		return new QA(question, answers, correctAnswer, aproposEntries);
	}

}
