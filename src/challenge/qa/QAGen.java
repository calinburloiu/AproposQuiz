package challenge.qa;

public interface QAGen 
{
	/**
	 * Every question has an entry(input) <key, value>
	 * Gets the key (a property name from a specific ontology)
	 */
	String getSupportedKey();
	/**
	 * Generates a question
	 * @param value - the value of a property name from a specific ontology
	 * @return QA - the question
	 */
	QA genQA(Object value) throws Exception;
}
