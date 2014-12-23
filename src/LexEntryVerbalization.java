
public class LexEntryVerbalization {

	
	public LexEntryVerbalization()
	{
		
	}
	
	public String verbalize(LexicalEntry entry, String language)
	{
		String verbalization;
		
		//English is the default verbalization
		
		verbalization =  verbalize_EN(entry);
		
		if(language.equals("de") || language.equals("DE"))
		
			verbalization= verbalize_DE(entry);
			
		if(language.equals("es") || language.equals("ES"))
			
			verbalization = verbalize_ES(entry);
				
		return verbalization;
		
	}

	private String verbalize_EN(LexicalEntry entry) {
		
		String verbalization = entry.getCanonicalForm();
		
		if (entry.getFrameType() != null)
		{
			if (entry.getFrameType() != null)
			{
				if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame"))
				{
					
				}
				if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame"))
				{
					
				}
				if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePPFrame"))
				{
					
				}
			}
		}
		
		
		return verbalization;
		
	}

	private String verbalize_ES(LexicalEntry entry) {

		String verbalization = entry.getCanonicalForm();
		
		if (entry.getFrameType() != null)
		{
			if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame"))
			{
				
			}
			if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame"))
			{
				
			}
			if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePPFrame"))
			{
				
			}
		}
		
		
		return verbalization;
	}

	private String verbalize_DE(LexicalEntry entry) {	
		
		String verbalization = entry.getCanonicalForm();
		
		if (entry.getFrameType() != null)
		{
			if (entry.getFrameType() != null)
			{
				if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame"))
				{
					
				}
				if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#NounPPFrame"))
				{
					
				}
				if (entry.getFrameType().equals("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePPFrame"))
				{
					
				}
			}
		}
		
		return verbalization;
	}
	
}
	

