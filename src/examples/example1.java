package examples;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import serialization.LexiconSerialization;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import core.LexicalEntry;
import core.Lexicon;
import core.Provenance;
import core.SenseArgument;
import core.SyntacticArgument;

public class example1 {

	// This examples creates a transitive verb lexical entry and serializes it
	
	public static void main(String[] args) throws IOException {
		
		// This examples creates a transitive verb
		
		Lexicon lexicon = new Lexicon();
		
		LexicalEntry entry = lexicon.createNewEntry("marry");
			
		entry.setReference("http://dbpedia.org/ontology/spouse");
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#verb");
		
		entry.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#TransitiveFrame");
				
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#subject","1",null));
		entry.addSyntacticArgument(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#directObject","2",null));
		
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#subfOfProp","1"));
		entry.addSenseArgument(new SenseArgument("http://lemon-model.net/lemon#objOfProp","2"));
		
		Provenance provenance = new Provenance();
		
		provenance.setAgent("http://sc.citec.uni-bielefeld.de/matoll");
		
		provenance.setConfidence(0.8);
		
		provenance.setEndedAtTime(new Date());
		
		entry.setProvenance(provenance);
		
		Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		serializer.serialize(lexicon,model);
		
		FileWriter writer = new FileWriter("example1.ttl");
		
		model.write(writer, "TURTLE");
		
		
	}
	
}
