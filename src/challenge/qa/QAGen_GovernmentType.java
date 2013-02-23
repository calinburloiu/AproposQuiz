package challenge.qa;

import java.util.Vector;

import challenge.Entry;

public class QAGen_GovernmentType implements QAGen
{
	public String getSupportedKey() {
		return "country";
	}
	
	public QA genQA(Object value)
	{
		String countryName = (String)value;
		String question = "Which of the following is the government type from " +
		    countryName + "?";
		Vector<String> answers = new Vector<String>();
		Vector<Entry> aproposEntries = new Vector<Entry>();
		int correctAnswer;
		
		answers.add("republic");
		answers.add("monarchy");
		
		aproposEntries.add(new Entry("governmentType", "republic"));
		aproposEntries.add(new Entry("governmentType", "monarchy"));
		
		correctAnswer = answers.indexOf("monarchy");
		
		return new QA(question, answers, correctAnswer, aproposEntries);
	}
}