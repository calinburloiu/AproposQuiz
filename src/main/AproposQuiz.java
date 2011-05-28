package main;

import challenge.Challenge;
import challenge.ChallengeGen;
import challenge.BasicChallengeGen;
import challenge.Entry;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Vector;


public class AproposQuiz 
{
	     
	public AproposQuiz() throws IOException
	{		
//		if (SparqlQuerier.getRandomNeighbor("Niue") == null)
//			System.out.println("Victory!");
//		else
//			System.out.println("Not victory!");
//		System.exit(0);
		
		ChallengeGen challengeGen = new BasicChallengeGen();
		Entry entry = new Entry("country", "Romania");
		String aproposText = "";
		Challenge challenge;
		char letter;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String resp;
		
		while(true)
		{
			// Apropos (link from the previous challenge)
			if(aproposText.length() > 0)
				System.out.println("Apropos: " + aproposText);
			
			challenge = challengeGen.genChallenge(entry);
			
			// Q
			System.out.println("Q: " + challenge.getQuestion());
			
			// A
			letter = 'a';
			int index = 0;
			for(String answer : challenge.getAnswers())
			{
				System.out.println("\t" + (letter++) + ". " + answer
						+ ((index++ == challenge.getCorrectAnswer()) ? "":""));
			}
			System.out.print("A: ");
			resp = in.readLine();
			if(resp.charAt(0) - 'a' == challenge.getCorrectAnswer())
				System.out.println("Correct!");
			else {
				char correct_answer = (char) ('a' + challenge.getCorrectAnswer());
				System.out.println("Wrong... The correct answer was " + correct_answer + ". " + challenge.getAnswers().get(challenge.getCorrectAnswer()));
			}
			// Prepare next challenge.
			aproposText = challenge.getNextApropos().getText();
			entry = challenge.getNextApropos().getEntry();
		}		
	}

	public static void main(String[] args) throws IOException
	{
		AproposQuiz quiz = new AproposQuiz();
	}

}
