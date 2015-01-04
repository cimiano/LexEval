package core;
import java.util.Date;


public class Provenance {

	String Agent;
	
	Date StartedAtTime;
	Date EndedAtTime;
	
	double Confidence;
	
	public String getAgent()
	{
		return Agent;
	}
	
	public void setAgent(String agent)
	{
		Agent = agent;
	}
	
	public double getConfidence()
	{
		return Confidence;
	}
	
	public void setConfidence(double confidence)
	{
		Confidence = confidence;
	}
	
	public void setStartedAtTime(Date date)
	{
		StartedAtTime = date;
	}
	
	public void setEndedAtTime(Date date)
	{
		EndedAtTime = date;
	}
	
	public Date getStartedAtTime(Date date)
	{
		return StartedAtTime;
	}
	
	public Date getEndedAtTime(Date date)
	{
		return EndedAtTime;
	}
	
}
