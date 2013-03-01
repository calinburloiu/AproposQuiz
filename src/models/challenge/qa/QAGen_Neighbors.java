package models.challenge.qa;

import java.util.Random;
import java.util.Vector;

import models.challenge.Entry;
import models.util.SingletonCache;
import models.util.SparqlQuerier;




public class QAGen_Neighbors implements QAGen{
	public static final int NR_OF_ANSWERS = 4;
	
	@Override
	public QA genQA(Object value) {
		
		String countryName = (String)value;
		String question = "Which of the following is a neighbour of " + countryName + "?";
		Vector<String> answers = new Vector<String>();
		Vector<Entry> aproposEntries = new Vector<Entry>();
		/* generate correct answer index */
		Random random = new Random();
		int correctAnswer = random.nextInt(NR_OF_ANSWERS);
		
		//String neighbor = getRandomNeighbor(countryName);
		String neighbor = SparqlQuerier.getRandomNeighbor(countryName);
		/*if (neighbor ==  null) {
			nei
		}*/
		Vector<String> wrongAnswers = SingletonCache.getRandomCountries(NR_OF_ANSWERS - 1, countryName, true);
		int wrong_index = 0;
		
		for (int i = 0; i < NR_OF_ANSWERS; i++) {
			if (i != correctAnswer) {
				String answer = wrongAnswers.get(wrong_index);
				answers.add(answer);
				wrong_index++;
				aproposEntries.add(new Entry("country", answer));
			} else {
				if (neighbor == null)
					answers.add("No neighbors");
				else
				{
					answers.add(neighbor);
					aproposEntries.add(new Entry("country", neighbor));
				}
			}
		}
	
		//System.out.println("Question text: " + question);
		for (int i = 0; i< answers.size(); i++) {
			//System.out.println("Varianta de raspuns: " + answers.get(i));
		}
		//System.out.println("correct answer: " + answers.get(correctAnswer));
		return new QA(question, answers, correctAnswer, aproposEntries);
	}

	@Override
	public String getSupportedKey() {
		return "hasBorderWith";
	}
	
	/**
	 * Returns the neighbor of a specific country - makes a SPARQL request using Jena
	 * @param countryName - the name of the country
	 * @return String randomNeighbour - a neighbor of countryName  
	 */
	/*private String getRandomNeighbor(String countryName) {
		Vector<String> generalList = new Vector<String>();
		//String queryString = File2String.getString("neighbor.sparql");
		 String queryString = String.format(File2String.getString("neighbor.sparql"), countryName);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://192.168.1.66/openrdf-workbench/repositories/quiz/query", query);
		String var;

		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				Resource res = soln.getResource("nei");
				//Literal lit = soln.getLiteral("popt");
				var = res.getLocalName();
				//var = lit.getLexicalForm();
				generalList.add(var);
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			qexec.close();
		}
		//System.out.println(generalList);
		
		
		Random rand = new Random();
		int index = rand.nextInt(generalList.size());
		
		System.out.println("raspuns corect random: " + generalList.get(index));
		return generalList.get(index);
	}*/
	
}
