import java.io.InputStream;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;


public class LexiconEvaluation {

	public static void main(String[] args) {
		

		if (args.length != 2)
		{
			System.out.print("Usage: LexiconEvaluation <Lexicon.rdf> <GoldLexicon.rdf>\n");
			return;
		}
		
		String lexiconFile = args[0];
		String goldFile = args[1];
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon lexicon = loader.loadFromFile(lexiconFile);
		Lexicon gold = loader.loadFromFile(goldFile);
		
		System.out.print(lexicon.getStatistics());
		
		System.out.print(gold.getStatistics());
		
		evaluate(lexicon,gold);
		
		
		 
		
	}

	private static void evaluate(Lexicon lexicon, Lexicon gold) {
		
		int lemma_correctness = 0;
		
		int syntactic_correctness = 0;
		
		int mapping_correctness = 0;
		
		int lex_entries = 0;
		
		double lemma_precision;
		
		double syntactic_precision;
		
		double mapping_precision;
		
		Boolean foundLemma;
		
		Boolean foundSyntax;
		
		Boolean foundMapping;
		
		
		for (LexicalEntry entry: lexicon.getEntries())
		{
			foundLemma = false;
			foundSyntax = false;
			
			if (entry.getReference() != null) 
			{
				lex_entries++;
				
				for (LexicalEntry gold_entry: gold.getEntriesWithCanonicalForm(entry.getCanonicalForm()))
				{
					if (checkLemmaReference(entry,gold_entry)) foundLemma = true;
					if (checkLemmaReference(entry,gold_entry) && checkTypeArgs(entry,gold_entry)) foundSyntax = true;

				}
				if (foundLemma) 
				{
					lemma_correctness ++;
				}
				
				if (foundSyntax)
				{
					syntactic_correctness ++;
				}
				else
				{
					System.out.print("Syntax wrong for: "+entry+"\n");
				}
			}
									
		}
		
		lemma_precision = (double) lemma_correctness / lex_entries;
		syntactic_precision = (double) syntactic_correctness / lex_entries;
		
		System.out.print("Precision at lemma level: "+ lemma_precision+"\n");
		System.out.print("Precision at syntax level: "+ syntactic_precision+"\n");
		
		
	}

	private static boolean checkTypeArgs(LexicalEntry entry, LexicalEntry gold_entry) {
		
		boolean found = true;
		
		if (entry.getFrameType() != null && gold_entry.getFrameType() != null)
		{
			if (!entry.getFrameType().equals(gold_entry.getFrameType())) return false;
		}
		
		for (SyntacticArgument synArg: entry.getSyntacticArguments())
		{
			found = false;
			
			for (SyntacticArgument synGold: gold_entry.getSyntacticArguments())
			{
				if (synGold.getArgumentType().equals(synArg.getArgumentType()))
				{
					if (synGold.getPreposition() != null)
					{	
						if (synArg.getPreposition() != null && synArg.getPreposition().equals(synGold.getPreposition())) 
						{
							found = true;
						}
					}
					else
					{
						found = true;
					}
				}
			}
			
			if (!found) return false;
				
			
		}
	
		return true;
	
	}

	private static boolean checkLemmaReference(LexicalEntry entry, LexicalEntry gold_entry) {
	
		if (entry.getReference() != null && gold_entry.getReference() != null)
			return entry.getCanonicalForm().equals(gold_entry.getCanonicalForm()) && entry.getReference().equals(gold_entry.getReference());
		else
			return entry.getCanonicalForm().equals(gold_entry.getCanonicalForm());
	}

	
}
