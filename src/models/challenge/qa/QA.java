package models.challenge.qa;

import java.util.Vector;

import models.challenge.Entry;



public class QA 
{
	String question;
	Vector<String> answers;
	int correctAnswer;
	Vector<Entry> aproposEntries;
	
	public QA(String question, Vector<String> answers, int correctAnswer,
			Vector<Entry> aproposEntries) 
	{
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.aproposEntries = aproposEntries;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Vector<String> getAnswers() {
		return answers;
	}

	public void setAnswers(Vector<String> answers) {
		this.answers = answers;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Vector<Entry> getAproposEntries() {
		return aproposEntries;
	}

	public void addAproposEntry(Entry aproposEntry) {
		this.aproposEntries.add(aproposEntry);
	}
}
