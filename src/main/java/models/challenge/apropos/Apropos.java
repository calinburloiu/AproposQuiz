package models.challenge.apropos;

import models.challenge.Entry;

public class Apropos 
{
	protected String text;
	protected Entry entry;
	
	public Apropos(String text, Entry entry) {
		this.text = text;
		this.entry = entry;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
}
