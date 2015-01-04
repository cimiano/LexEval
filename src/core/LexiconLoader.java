package core;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.jena.riot.RDFDataMgr;

import vocabularies.LEMON;
import vocabularies.LEXINFO;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;


public class LexiconLoader {

	public  LexiconLoader()
	{
		
	}
	
	Lexicon loadFromFile(String file)
	{
		
		Model model = RDFDataMgr.loadModel(file);
		
		 Statement stmt;
		 Resource subject;
		 
		 Lexicon lexicon = new Lexicon();
		 
		 LexicalEntry entry;
		 
		 StmtIterator iter = model.listStatements(null, RDF.type, LEMON.LexicalEntry);
		 
		 while (iter.hasNext()) {
			 stmt = iter.next();
			 
			 subject = stmt.getSubject();
			 
			 entry = new LexicalEntry(subject);
			 
			 entry.setCanonicalForm(getCanonicalForm(subject,model));
			 
			 entry.setFrame(getFrame(subject,model));
			 
			 entry.setReference(getReference(subject,model));
			 
			 entry.setPOS(getPOS(subject,model));
			 
			 entry.setSyntacticArguments(getSyntacticArguments(subject,model));
			 
			 entry.setSenseArguments(getSenseArguments(subject,model));
			 
			 entry.computeMappings();
			 
			 lexicon.addEntry(entry);
			 
			 // System.out.print(entry);
		 }
		 
		 return lexicon;
		 
	}
	
	
	private String getPOS(Resource subject, Model model) {
		
		Resource pos;
		
		Statement stmt;
		
		stmt = subject.getProperty(LEXINFO.partOfSpeech);
		
		if (stmt != null)
		{
			pos = (Resource) stmt.getObject();
			
			return pos.toString();
		}
		else
		{
			return null;
		}
		
	}

	private Set<SenseArgument> getSenseArguments(Resource subject, Model model) {
		
		Resource sense;
		
		Resource object;
				
		Set<SenseArgument> senseArguments = new HashSet<SenseArgument>();
		
		Statement stmt;
		
		stmt= subject.getProperty(LEMON.sense);
		
		if (stmt != null)
		{
			
			sense = (Resource) stmt.getObject();
			
			StmtIterator it = sense.listProperties(LEMON.isA);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	senseArguments.add(new SenseArgument(stmt.getPredicate().toString(),object.toString()));
		    }	
		    	
	    	it = sense.listProperties(LEMON.subjOfProp);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	senseArguments.add(new SenseArgument(stmt.getPredicate().toString(),object.toString()));
		    	
		    }
		    
	    	it = sense.listProperties(LEMON.objOfProp);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		  
		    	senseArguments.add(new SenseArgument(stmt.getPredicate().toString(),object.toString()));	
		    	
		    }
		    	
		}
	
		return senseArguments;
		
		
	}

	private static Set<SyntacticArgument> getSyntacticArguments(Resource subject, Model model) {
		
		Resource synBehaviour;
		
		Resource object;
		
		Resource prepositionEntry;
		
		String preposition;
				
		Set<SyntacticArgument> syntacticArguments = new HashSet<SyntacticArgument>();
		
	
		Statement stmt;
		
		stmt = subject.getProperty(LEMON.syntacticBehaviour);
		
		if (stmt != null)
		{
			synBehaviour = (Resource) stmt.getObject();
			
			StmtIterator it = synBehaviour.listProperties(LEXINFO.subject);
		    while( it.hasNext() ) {
		    	
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	syntacticArguments.add(new SyntacticArgument(stmt.getPredicate().toString(),object.toString(),null));
		    }	
		    	
	    	it = synBehaviour.listProperties(LEXINFO.object);
		    while( it.hasNext() ) {
		    
			
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	syntacticArguments.add(new SyntacticArgument(stmt.getPredicate().toString(),object.toString(),null));
		    	
		    }
		    
	    	it = synBehaviour.listProperties(LEXINFO.pobject);
		   
	    	while( it.hasNext() ) {
		    
	    	 	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	prepositionEntry = (Resource) object.getProperty(LEMON.marker).getObject();
		    	
		    	if (prepositionEntry != null)
		    	{

		    		preposition = getCanonicalForm(prepositionEntry,model);
		    		
		    		// System.out.print("Preposition: "+preposition+"\n");
		    		
		    	}
		    	else
		    	{
		    		preposition = null;
		    	}
		    	
		    	syntacticArguments.add(new SyntacticArgument(stmt.getPredicate().toString(),object.toString(),preposition));	
		    	
		    }
		    	
		}
	
		   
		return syntacticArguments;
	}
	

	private static String getReference(Resource subject, Model model) {
		
		Resource sense;
		
		RDFNode reference;
		
		Statement stmt = subject.getProperty(LEMON.sense);
		
		Statement stmt2;
		
		if (stmt != null)
		{	
			sense = (Resource) stmt.getObject();
			
			stmt2 = sense.getProperty(LEMON.reference);
			
			if (stmt2 != null)
			{
				reference =  stmt2.getObject();
		
				// fix this for adjectives which have a blank node as reference
				
				if (reference != null && !reference.isAnon())
				{
					return reference.toString();
				}
				else
				{
					// System.out.print("Entry: "+subject+" has no reference!!!\n");
					return null;
				}
			}
			else
			{
				return null;
			}
		
		}
		else 
		{
			// System.out.print("Entry: "+subject+" has no sense!!!\n");
			return null;
		}
			
	}

	private static String getFrame(Resource subject, Model model) {
		
		Resource syntacticBehaviour;
		
		String value = null;
		
		Statement stmt;
		
		stmt = subject.getProperty(LEMON.syntacticBehaviour);
		
		if (stmt != null)
		{

			syntacticBehaviour = (Resource) stmt.getObject();
						
			StmtIterator it = syntacticBehaviour.listProperties(RDF.type);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	value = stmt.getObject().toString();
		    	
		    	if (!value.equals("http://lemon-model.net/lemon#Frame"))
		    	{
		    		
		    		// System.out.print(value+"\n");
			    	return value;
			    	
		    	}
		    }
			
		}
		else
		{
			// System.out.print("Entry "+subject+" has no syntactic behaviour!\n");
		}
		
		return value;
		
	}

	private static String getCanonicalForm(Resource subject, Model model) {
		
		Resource canonicalForm;
		
		Statement stmt;
		
		Literal form;
		
		stmt = subject.getProperty(LEMON.canonicalForm);
		
		if (stmt != null)
		{
			canonicalForm = (Resource) stmt.getObject();
			
			if (canonicalForm != null)
			{
				stmt = canonicalForm.getProperty(LEMON.writtenRep);
				
				if (stmt != null)
				{
				form = (Literal) canonicalForm.getProperty(LEMON.writtenRep).getObject();
					return form.toString();
				}
				else
				{
					return null;
				}
				
			}
			else
			{
				return null;
			}
		}
		else
		{
			// System.out.print("Entry "+subject+" has no canonical form!!!\n");
			return null;
		}		
	}		
}

