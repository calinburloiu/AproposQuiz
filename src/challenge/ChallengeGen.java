package challenge;

public interface ChallengeGen
{
	boolean isKeySupported(String key);
	Challenge genChallenge(Entry seedEntry);
}
