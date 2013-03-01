package models.challenge.apropos;

public interface AproposGen 
{
	String getSupportedKey();
	Apropos genApropos(Object value);
}
