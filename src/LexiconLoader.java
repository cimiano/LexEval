import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

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
		Model model = ModelFactory.createDefaultModel();
		
		InputStream in = FileManager.get().open( file );
		 if (in == null) {
		     throw new IllegalArgumentException(
		                                  "File: " + file + " not found");
		 }

		 // read in the fist lexicon
		 model.read(in, null);
				
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
			 
			 entry.setSyntacticBehaviour(getSyntacticBehaviour(subject,model));
			 
			 entry.setReference(getReference(subject,model));
			 
			 entry.setSyntacticArguments(getSyntacticArguments(subject,model));
			 
			 entry.setSenseArguments(getSenseArguments(subject,model));
			 
			 entry.computeMappings();
			 
			 lexicon.addEntry(entry);
			 
			 // System.out.print(entry);
		 }
		 
		 return lexicon;
		 
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
		    	
		    	senseArguments.add(new SenseArgument("isA",object.toString()));
		    }	
		    	
	    	it = sense.listProperties(LEMON.subjOfProp);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	senseArguments.add(new SenseArgument("subjOfProp",object.toString()));
		    	
		    }
		    
	    	it = sense.listProperties(LEMON.objOfProp);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		  
		    	senseArguments.add(new SenseArgument("objOfProp",object.toString()));	
		    	
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
			
			StmtIterator it = synBehaviour.listProperties(LingInfo.subject);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	syntacticArguments.add(new SyntacticArgument("subject",object.toString(),null));
		    }	
		    	
	    	it = synBehaviour.listProperties(LingInfo.object);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	syntacticArguments.add(new SyntacticArgument("object",object.toString(),null));
		    	
		    }
		    
	    	it = synBehaviour.listProperties(LingInfo.pobject);
		    while( it.hasNext() ) {
		    
		    	stmt = it.next();
		    	
		    	object = (Resource) stmt.getObject();
		    	
		    	prepositionEntry = (Resource) object.getProperty(LEMON.marker).getObject();
		    	
		    	if (prepositionEntry != null)
		    	{

		    		preposition = getCanonicalForm(prepositionEntry,model);
		    		
		    	}
		    	else
		    	{
		    		preposition = null;
		    	}
		    	
		    	syntacticArguments.add(new SyntacticArgument("pobject",object.toString(),preposition));	
		    	
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
					System.out.print("Entry: "+subject+" has no reference!!!\n");
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
			System.out.print("Entry: "+subject+" has no sense!!!\n");
			return null;
		}
			
	}

	private static String getSyntacticBehaviour(Resource subject, Model model) {
		
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
			    	return value;
		    	}
		    }
			
		}
		else
		{
			System.out.print("Entry "+subject+" has no syntactic behaviour!\n");
		}
		
		return value;
		
	}

	private static String getCanonicalForm(Resource subject, Model model) {
		
		Resource canonicalForm;
		
		Literal form;
		
		canonicalForm = (Resource) subject.getProperty(LEMON.canonicalForm).getObject();
		
		if (canonicalForm != null)
		{
			form = (Literal) canonicalForm.getProperty(LEMON.writtenRep).getObject();
		
			return form.toString();
		}
		else
		{
			System.out.print("Entry "+subject+" has no canonical form!!!\n");
			return null;
		}
	}

	
}
