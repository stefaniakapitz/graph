package de.unistuttgart.ims.fictionnet.analysis;

import de.unistuttgart.ims.fictionnet.datastructure.Text;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
public class Algorithm {
	private String name;

	/**
	 * Chooses the correct algorithm to run, if no algorithm is specified and returns the result.
	 * 
	 * @param text
	 *            the text object
	 * @param filter
	 *            the filter to use
	 * @return The result of the executed algorithm
	 */

	public Result run(Text text, Filter filter) {
		Result result = new Result();
		switch (filter.getActionType()) {
		case TALKS_WITH:
			result = new AlgorithmTalksTo().run(text, filter);
			break;
		case IS_MENTIONED:
		case TALKS_ABOUT:
			result = new AlgorithmEntityMentioned().run(text, filter);
			break;
		default:
			break;

		}

		return result;

	}

	/**
	 * 
	 */
	public Algorithm() {
		super();
	}

	/**
	 * 
	 * @return Getter for the algorithm class
	 */
	public String getName() {
		return name;

	}

	/**
	 * Setter for the algorithm class.
	 * 
	 * @param name
	 *            the name of the algorithm
	 */
	public void setName(String name) {
		this.name = name;
	}

}
