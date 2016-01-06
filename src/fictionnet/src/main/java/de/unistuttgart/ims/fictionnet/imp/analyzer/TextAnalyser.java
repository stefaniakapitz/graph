package de.unistuttgart.ims.fictionnet.imp.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.unistuttgart.ims.fictionnet.datastructure.Synonym;
import de.unistuttgart.ims.fictionnet.datastructure.Text;

public abstract class TextAnalyser {

	private final String regex = "[^a-zA-ZÄÖÜäöü]";
	protected final Text text;
	
	public TextAnalyser(Text text) {
		this.text = text;
	}
	
	protected HashSet<String> findEntities (String sentence, HashSet<String> entities) {
		final String spokenTextCompare = sentence.replace(regex, "").toLowerCase();
		final HashSet<String> foundEntities = new HashSet<String>();
		//Find all entities in the given textstring
		String regexEntitie = "";
		for (final String entitie : entities) {
			regexEntitie = entitie.replaceAll(regex, "").toLowerCase();
			if (spokenTextCompare.contains(regexEntitie) && !regexEntitie.isEmpty()) {
				final List<String> spokenAbouts = text.getCastMembersForSynonym(entitie);
				if (spokenAbouts.size()>0) {
					foundEntities.addAll(spokenAbouts);
				} else {
					//Synonym doesnt exist
					boolean synExist = false;
					for (final String member: text.getCastList()) {
						if (member.equalsIgnoreCase(regexEntitie)) {
							foundEntities.add(member);
							synExist = true;
							break;
						}
					}
					
					if (!synExist) {
						foundEntities.add(regexEntitie);
					}
				}
			}
		}
		return foundEntities;
	}
	
	/**
	 * 
	 * @param castList
	 * @param synonyms
	 * @return Returns all Entities (Castmember + Synonyms)
	 */
	protected HashSet<String> getAllEntities() {
		final HashSet<String> castList = text.getCastList();
		final HashMap<String, ArrayList<Synonym>> synonyms = text.getSynonyms();
		final HashSet<String> entities = new HashSet<String>();
		for (final String castMember : castList) {
			entities.add(castMember);
		}
		
		for (final String key : synonyms.keySet()) {
			entities.add(key);
			for (final Synonym synonym : synonyms.get(key)) {
				entities.add(synonym.getName());
			}
		}
		return entities;
	}
	
	public abstract void analyze();
}
