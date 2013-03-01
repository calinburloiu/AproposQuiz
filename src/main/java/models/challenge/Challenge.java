package models.challenge;

import java.util.Vector;

import models.challenge.apropos.*;



public class Challenge 
{	
	protected String question;
	protected Vector<String> answers;
	protected int correctAnswer;
	protected Apropos nextApropos;
	
	public Challenge(String question, Vector<String> answers,
			int correctAnswer, Apropos nextApropos) 
	{
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		this.nextApropos = nextApropos;
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

	public Apropos getNextApropos() {
		return nextApropos;
	}

	public void setNextApropos(Apropos nextApropos) {
		this.nextApropos = nextApropos;
	}	
}
