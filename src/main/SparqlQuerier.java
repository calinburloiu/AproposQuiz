package main;

import java.util.Random;
import java.util.Vector;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;

public class SparqlQuerier 
{
	public static final String SPARQL_ENDPOINT =
	    "http://localhost:3030/geopolitical/query";
	
	public static Vector<String> getGeneralList(String fileName, String varName)
	{
		Vector<String> generalList = new Vector<String>();
		String queryString = File2String.getString(fileName);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(
		    SparqlQuerier.SPARQL_ENDPOINT, query);
		String var;

		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				Literal lit = soln.getLiteral(varName);
				var = lit.getLexicalForm();
				generalList.add(var);
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			qexec.close();
		}
		
		return generalList;
	}
	
	public static int getPopulation(String country)
	{
		String queryString = String.format(File2String.getString("population.sparql"), country);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(SparqlQuerier.SPARQL_ENDPOINT, query);
		Float populationTotal;
		String populationUnit;
		int population = 0;

		try {
			ResultSet results = qexec.execSelect();
			if(results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				populationTotal = soln.getLiteral("population_total").getFloat();
				populationUnit = soln.getLiteral("population_unit").getString();
				population = (int)(populationTotal * Float.parseFloat(populationUnit));
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			qexec.close();
		}
		
		return population;
	}
	
	/**
	 * Returns the neighbor of a specific country - makes a SPARQL request using Jena
	 * @param countryName - the name of the country
	 * @return String randomNeighbour - a neighbor of countryName  
	 */
	public static String getRandomNeighbor(String countryName) {
		Vector<String> generalList = new Vector<String>();
		//String queryString = File2String.getString("neighbor.sparql");
		//System.out.println("country name: " + countryName);
		 String queryString = String.format(File2String.getString("neighbor.sparql"), countryName);
		Query query = QueryFactory.create(queryString);
		//System.out.println("QUERY: " + query);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(SPARQL_ENDPOINT, query);
		String var;

		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				//Resource res = soln.getResource("nei");
				Literal lit = soln.getLiteral("country_name");
				//var = res.getLocalName();
				var = lit.getString();
				generalList.add(var);
			}
		} 
		catch(Exception e) {
			//e.printStackTrace();
			return null;
		}
		finally {
			qexec.close();
		}
		
		if(generalList.isEmpty())
			return null;
		
		/* choose a random neighbor from the list of neighbors */
		Random rand = new Random();
		int index = rand.nextInt(generalList.size());
		
		//System.out.println("raspuns corect random: " + generalList.get(index));
		return generalList.get(index);
	}
}
