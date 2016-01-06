package de.unistuttgart.ims.fictionnet.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.unistuttgart.ims.fictionnet.datastructure.Text;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class Analyzer {
	private List<Algorithm> algorithms = new ArrayList<>();

	/**
	 * finds out if it is a person or not.
	 * 
	 * @param person
	 *            the person to check
	 * @param text
	 *            the text object
	 * @return boolean is it a person or not
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public boolean isPerson(String person, Text text) {
		boolean isPerson=false;
		if (text.getCastList().contains(person)) {
			isPerson=true;
		}
		return isPerson;

	}

	/**
	 * deletes an algorithm from the list.
	 * 
	 * @param algorithmName
	 *            name of the algorithm
	 * @return boolean was it succesfully deleted or not
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public boolean deleteAlgorithm(String algorithmName) {
		boolean deleted = false;

		for (final Iterator<Algorithm> it = getAlgorithms().iterator(); it.hasNext();) {
			final Algorithm item = it.next();
			if (algorithmName.equals(item.getName())) {
				it.remove();
				deleted = true;
			}
		}
		return deleted;

	}

	

	/**
	 * adds an algorithm to the list.
	 * 
	 * @param algorithm
	 *            the algorithm object
	 */
	public void addAlgorithm(Algorithm algorithm) {

		getAlgorithms().add(algorithm);

	}

	/**
	 * analyzes the layers from the text by using the filter. Chooses
	 * automatically the correct algorithm.
	 * 
	 * @param text
	 *            the text object
	 * @param filter
	 *            the filter to use
	 * @return Result the result object
	 */
	@SuppressWarnings("PMD.UnnecessaryLocalBeforeReturn")
	public Result analyze(Text text, Filter filter) {
		final Algorithm algorithm = new Algorithm();
		final Result result = algorithm.run(text, filter);
		return result;

	}

	/**
	 * analyzes the layers from the text by using the filter. Chooses the given algorithm.
	 * @param text the text object
	 * @param filter the filter to use
	 * @param algorithmName then name of the algorithm to use
	 * @return Result the result object
	 */
	public Result analyze(Text text, Filter filter, String algorithmName) {
		Result result = null;
		for (int i = 0; i < getAlgorithms().size(); i++) {
			if (algorithmName.equals(getAlgorithms().get(i).getName())) {
				result = getAlgorithms().get(i).run(text, filter);
			}
		}
		return result;

	}

	/**
	 * @return the algorithms
	 */
	public List<Algorithm> getAlgorithms() {
		return algorithms;
	}

	/**
	 * @param algorithms the algorithms to set
	 */
	public void setAlgorithms(List<Algorithm> algorithms) {
		this.algorithms = algorithms;
	}
}
