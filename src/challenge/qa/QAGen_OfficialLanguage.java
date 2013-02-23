package challenge.qa;

import java.util.Vector;

import main.File2String;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;

import challenge.Entry;

public class QAGen_OfficialLanguage implements QAGen
{
	public String getSupportedKey() {
		return "country";
	}
	
	public String getOfficialLanguage(String country)
	{
		String queryString = String.format(File2String.getString("officialLanguage.sparql"), country);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		String var;

		try {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				Literal lit = soln.getLiteral("language_name");
				var = lit.getLexicalForm();
				//generalList.add(var);
			}
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			qexec.close();
		}
		
		return "";
	}
	
	public QA genQA(Object value)
	{
		String countryName = (String)value;
		String question = "Which of the following is an official language in " + countryName + "?";
		Vector<String> answers = new Vector<String>();
		Vector<Entry> aproposEntries = new Vector<Entry>();
		int correctAnswer = 0; //TODO
		
		getOfficialLanguage(countryName);
		
		answers.add("english");
		answers.add("romanian");
		answers.add("greek");
		answers.add("french");
		
		aproposEntries.add(new Entry("language", "english"));
		aproposEntries.add(new Entry("language", "romanian"));
		aproposEntries.add(new Entry("language", "greek"));
		aproposEntries.add(new Entry("language", "french"));
		
		correctAnswer = answers.indexOf("romanian");
		
		return new QA(question, answers, correctAnswer, aproposEntries);
	}
}