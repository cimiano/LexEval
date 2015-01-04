package core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Lexicon {

	HashMap<String,List<LexicalEntry>> map;
	
	List<LexicalEntry> entries;
	
	Set<String> references;
	
	public Lexicon()
	{
		entries = new ArrayList<LexicalEntry>();
		
		map = new HashMap<String,List<LexicalEntry>>();
		
		references = new HashSet<String>();
	}
	
	public void addEntry(LexicalEntry entry)
	{
		// check if entry is already contained
		
		entries.add(entry);
		
		ArrayList<LexicalEntry> list;
		
		if (map.containsKey(entry.getCanonicalForm()))
		{
			list = (ArrayList<LexicalEntry>) map.get(entry.getCanonicalForm());
			list.add(entry);
		}
		else
		{
			list = new ArrayList<LexicalEntry>();
			list.add(entry);
			map.put(entry.getCanonicalForm(), list);
		}
		references.add(entry.getReference());
		
	}

	public List<LexicalEntry> getEntries()
	{
		return entries;
	}
	
	public List<LexicalEntry> getEntriesWithCanonicalForm(String canonicalForm)
	{
		return map.get(canonicalForm);
	}
	
	public int size() {

		return entries.size();
	}
	
	public boolean containsReference(String reference)
	{
		return references.contains(reference);
	}
	
	public String getStatistics()
	{
		return "";
	}
	
	
}
