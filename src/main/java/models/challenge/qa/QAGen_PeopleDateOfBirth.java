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


public class QAGen_PeopleDateOfBirth implements QAGen {
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

	public String getDateOfBirth(String person_id) {
		JSON query = a(o(
				"id", person_id,
				"/people/person/date_of_birth", null
				));
		JSON response = freebase.mqlread(query);
		JSON raw_results = response.get("result");
		JSON result = raw_results.get(0).get("/people/person/date_of_birth");
		if (result != null) {
			return result.string();
		}
		else {
			return null;
		}
	}
	
	public QA genQA(Object value) throws Exception{
		int index;
		int subject_index;
		String subject, answer;
		String countryName = (String)value;
		
		// get the list of all the people born in the current country
		TreeMap<String, String> born_in = getPeopleBornHere(countryName);
		if (born_in == null) throw new Exception("There are no people born in "
		    + countryName + ".");
		// pick a personality to be used in the question
		Vector<String> answers = new Vector<String>();
		Object[] people = born_in.keySet().toArray();
		Random generator = new Random();
		while (true) {
			subject_index = generator.nextInt(born_in.size());
			subject = (String) people[subject_index];
			answer = getDateOfBirth(born_in.get(subject)); 
			if (answer != null) {
				answers.add(answer);
				break;
			}
		}
		String question = "When was " + subject +" born?";
		
		if (born_in.size() >= 4) {
			int i=0;
			do {
				index = generator.nextInt(born_in.size());
				if (index == subject_index) continue;
				String person = (String) people[index];
				answer = getDateOfBirth(born_in.get(person));
				if (answer != null) {
					answers.add(getDateOfBirth(born_in.get(person)));
					i++;
				}
			} while (i<3);
		}
		else {
			System.out.println("Not enough people born in country \"" + countryName + "\"");
			throw new Exception();
		}
		
		//shuffle
		int correctAnswer = generator.nextInt(4);
		String first = answers.firstElement();
		answers.set(0, answers.get(correctAnswer));
		answers.set(correctAnswer, first);
		
		Vector<Entry> aproposEntries = new Vector<Entry>();
		aproposEntries.add(new Entry("country", countryName));
		
		return new QA(question, answers, correctAnswer, aproposEntries);
	}
}
