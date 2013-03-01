package models.util;

import static com.freebase.json.JSON.a;
import static com.freebase.json.JSON.o;

import java.text.Normalizer;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;


import com.freebase.api.Freebase;
import com.freebase.json.JSON;

public class SingletonCache 
{
	protected static Vector<String> allCountries = null;
	protected static Vector<String> allCapitals = null;
	protected static Vector<String> allLanguages = null;
	protected static Freebase freebase = Freebase.getFreebase();
	protected static TreeMap<String, String> freebaseContriesID = null;
//	protected static TreeMap<String, Integer> populationOfCountries = null;
	
	public static void init()
	{
		//initAllCountries();
	}
	
	public static void initAllCountries()
	{
		JSON query = a(o(
				"id", null,
				"name", null,
				"type", "/location/country",
				"limit", 250
				));
		JSON response = freebase.mqlread(query);
		JSON raw_results = response.get("result");
		int number_of_results = raw_results.array().size();
		
		Vector <String> allCountries_freebase = new Vector<String>();
		freebaseContriesID = new TreeMap<String, String>();
		for (int i=0 ; i<number_of_results ; i++) {
			String name = raw_results.get(i).get("name").string();
			String id = raw_results.get(i).get("id").string();
			name = Normalizer.normalize(name, Normalizer.Form.NFD);
	        name = name.replaceAll("[^\\p{ASCII}]", "");
	        allCountries_freebase.add(name);
			freebaseContriesID.put(name, id);
		}
//		System.out.println("Geopolitical countries: " + allCountries_freebase.size());
		Vector<String> allCountries_geopolitical = SparqlQuerier.getGeneralList("countries.sparql", "country_name");
//		System.out.println("Geopolitical countries: " + allCountries_geopolitical.size());
		allCountries = new Vector <String> ();
		for (String country : allCountries_freebase)
			if (allCountries_geopolitical.contains(country))
				allCountries.add(country);
		
		for (String country: allCountries) {
      System.out.print(country + ", ");
    }
    System.out.println();
		System.out.println(allCountries.size());
	}
	
//	public static void initAllCapitals()
//	{
//		allCapitals = SingletonCache.getGeneralList("allCapitals.sparql", "capital_name");
//	}
//	
//	public static void initAllLanguages()
//	{
//		allLanguages = SingletonCache.getGeneralList("allLanguages.sparql", "language_name");
//	}

	public static Vector<String> getAllCountries() {
		if(allCountries == null)
			initAllCountries();
		
		return allCountries;
	}

//	public static Vector<String> getAllCapitals() {
//		if(allCapitals == null)
//			initAllCapitals();
//		return allCapitals;
//	}
	
	public static String getFreebaseCountryID(String country_name) {
		if(freebaseContriesID == null)
			initAllCountries();
		if (!freebaseContriesID.containsKey(country_name)) {
			return null;
		}
		return freebaseContriesID.get(country_name);
	}
	
	public static Vector<String> getRandomCountries(int count, String differentCountryName, boolean checkNeighbors) {
		Vector<String> countries = getAllCountries();
		Vector<String> randomCountries = new Vector<String>();
		
		TreeSet<Integer> indexes = new TreeSet<Integer>();
		Random random = new Random();
		int randomIndex;
		String country, randomNeighbor;
		for (int i = 0; i < count; i++) {
			do {
				randomIndex = random.nextInt(countries.size());
				country = countries.get(randomIndex);
				randomNeighbor = SparqlQuerier.getRandomNeighbor(country);
			} while(indexes.contains(randomIndex) ||
					randomNeighbor == null ||
					country.equals(differentCountryName));
			//System.out.println(country + " ----> " + randomNeighbor);
			randomCountries.add(country);
		}
		
		for (int i = 0; i <  randomCountries.size(); i++) {
			//System.out.println("varianta de raspuns: " + randomCountries.get(i));
		}
		
		return randomCountries;
	
	}

/*	
	public static int getPopulationOfCountries(String country_name) {
		if(populationOfCountries == null)
			initAllCountries();
		if (!populationOfCountries.containsKey(country_name)) {
			System.out.println("\"" + country_name + "\" is not in the countries list!");
			System.exit(-1);
		}
		return populationOfCountries.get(country_name);
	}
*/
}
