package de.unistuttgart.ims.fictionnet.imp;

import java.util.HashSet;
import java.util.Set;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Mention;

/**
 * @author Rafael Harth
 * @version 11-06-2015
 * 
 *          The class is used to create clusters of Mention instances:
 * 
 *          When A*, A+, A~ and A' are all found to reference A by an IMS algorithm (the left side variables are single
 *          occurrences in the source text, the right one (for now) an entity of the cast list), then this can be stored
 *          in the model provided below.
 *          
 *          WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */
public class MentionCluster {
	private String origin; // the 'root' element of the cluster (element of a cast list)
	private Set<MentionEntity> associations = new HashSet<>(); // elements that have are said to reference it

	/**
	 * @param rootElement
	 */
	public MentionCluster(String rootElement) {
		origin = rootElement;
	}

	/**
	 * @param element
	 *        the new element for the cluster; supposedly a mention that references the root element
	 * @param confidence
	 *        ?
	 * @return true IF the new element wasn't already in the set.
	 */
	public boolean addElement(Mention element, float confidence) {
		for (MentionEntity mE : associations) {
			if (mE.mention.equals(element)) {
				return false;
			}
		}

		associations.add(new MentionEntity(element, confidence));
		return true;
	}

	/**
	 * @return
	 */
	public Set<MentionEntity> getAssociations() {
		return associations;
	}

	/**
	 * @return
	 */
	public String getOrigin() {
		return origin;
	}
}