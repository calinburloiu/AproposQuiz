package challenge.qa;

import static com.freebase.json.JSON.a;
import static com.freebase.json.JSON.o;

import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import main.SingletonCache;
import challenge.Entry;

import com.freebase.api.Freebase;
import com.freebase.json.JSON;

public class QAGen_EventsIn implements QAGen {

Freebase freebase = Freebase.getFreebase();
	
	public String getSupportedKey() {
		return "country";
	}
	
	public TreeMap<String, String> getEvents(String country_name) throws Exception{
		String country_id = SingletonCache.getFreebaseCountryID(country_name);
		if (country_id == null)
			throw new Exception();
		JSON query = o(
				"id", country_id,
				"/location/location/events", a(o("/time/event/start_date", null,
					    "name", null, "limit", 250))
				);
		JSON response = freebase.mqlread(query);
		JSON raw_results = response.get("result").get("/location/location/events");
		int number_of_results = raw_results.array().size();
		if (number_of_results == 0) {
			return null;
		}
		else {
			TreeMap<String, String> results = new TreeMap<String, String>();
			for (int i=0 ; i<number_of_results ; i++) {
				String event_name = raw_results.get(i).get("name").string();
				JSON event_start_date = raw_results.get(i).get("/time/event/start_date");
				if (event_start_date != null) {
					String start_date = event_start_date.string();
					if (start_date.startsWith("-")) {
						int year = Integer.parseInt(start_date.substring(1, 4));
						year += 1;
						start_date = "" + year + " B.C.E.";
					}
					results.put(event_name, start_date);
				}
				//System.out.println(name_of_people);
			}
			return results;
		}
	}

	
	public QA genQA(Object value) throws Exception {
		int index;
		int subject_index;
		String subject;
		String countryName = (String)value;
		
		// get the list of all the people born in the current country
		TreeMap<String, String> events = getEvents(countryName);
		// pick a personality to be used in the question
		Vector<String> answers = new Vector<String>();
		Object[] event_names = events.keySet().toArray();
		Random generator = new Random();
		subject_index = generator.nextInt(events.size());
		subject = (String) event_names[subject_index];
		answers.add(events.get(subject));
		
		String question = "When did the " + subject +" took place in " + countryName + "?";
		
		if (events.size() >= 4) {
			int i=0;
			do {
				index = generator.nextInt(events.size());
				if (index == subject_index) continue;
				String event_name = (String) event_names[index];
				answers.add(events.get(event_name));
				i++;
			} while (i<3);
		}
		else {
			//TODO: treat this case properly
			System.out.println("Not enough events for country \"" + countryName + "\"");
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
