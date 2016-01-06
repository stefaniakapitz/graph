package de.unistuttgart.ims.fictionnet.datastructure.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Token;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.TokenWrapper;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          This concrete Layer stores occurrences in which a person mentions
 *          one or more others in the source text.
 * 
 *          See Layer.java for additional Information.
 */
@DatabaseTable(tableName = "tokenLayer")
public class TokenLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	ArrayList<TokenWrapper> tokens = new ArrayList<>();

	public TokenLayer() {
		this.tokens = new ArrayList<TokenWrapper>();
	}

	public TokenLayer(List<Token> tokens) {
		ArrayList<TokenWrapper> tokenWrappers = new ArrayList<>();

		for (int i = 0; i < tokens.size(); i++) {
			tokenWrappers.add(new TokenWrapper(tokens.get(i)));
		}
		this.tokens = tokenWrappers;
	}

	@Override
	public Annotation getAnnotation(int index) {
		return tokens.get(index).getToken();
	}

	/*
	 * Basic Getters and Setters
	 */

	/**
	 * @param tokens
	 *            the tokens to set
	 */
	public void setTokens(List<Token> tokens) {
		ArrayList<TokenWrapper> tokenWrappers = new ArrayList<>();

		for (int i = 0; i < tokens.size(); i++) {
			tokenWrappers.add(new TokenWrapper(tokens.get(i)));
		}
		this.tokens = tokenWrappers;
	}

	/**
	 * @return the tokens
	 */
	@XmlTransient
	public List<Token> getTokens() {
		List<Token> newTokens = new ArrayList<>();

		for (int i = 0; i < tokens.size(); i++) {
			newTokens.add(tokens.get(i).getToken());
		}
		return newTokens;
	}
	
	/**
	 * Just for XML Serialization, use getTokens()
	 * @return
	 */
	@XmlElement (name= "tokens")
	public List<TokenWrapper> getTokenWrappers(){
		return tokens;
	}

}