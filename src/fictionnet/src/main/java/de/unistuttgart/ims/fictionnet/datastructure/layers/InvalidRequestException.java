package de.unistuttgart.ims.fictionnet.datastructure.layers;

/**
 * @author Rafael Harth
 * @version 10-28-2015
 * 
 *          This Exception is thrown when the getAnnotation(int index) method is called on a layer that does not have a
 *          list of Annotations, but a more complex data type, like a map
 */
public class InvalidRequestException extends Exception {
	String fault;

	public InvalidRequestException(String fault) {
		this.fault = fault;
	}
}