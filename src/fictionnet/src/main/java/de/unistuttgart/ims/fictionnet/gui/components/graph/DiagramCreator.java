package de.unistuttgart.ims.fictionnet.gui.components.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab.GraphType;

/**
 * A Class to set the properties of the diagrams. Get the values from the Client
 * and notify the class Diagram
 *
 * @author Estefania, Günay, Erik-Felix Tinsel
 *
 */
public class DiagramCreator extends AbstractLocalizedCustomComponent {

	private static final long serialVersionUID = 1L;
	private final Diagram diagram;
	private final VerticalLayout layout;
	private List<Integer> chapterAct = new ArrayList<Integer>();
	private List<Integer> chapterConv = new ArrayList<Integer>();
	private List<Integer> newChapter = new ArrayList<Integer>();
	private List<Integer> newPerson = new ArrayList<Integer>();
	private List<Integer> choosenChapterList = new ArrayList<Integer>();
	private List<Integer> choosenPersonList = new ArrayList<Integer>();
	private List<Integer> actualAct = new ArrayList<Integer>();
	private List<String> conversationPersons = new ArrayList<String>();
	private List<String> anySpeaker = new ArrayList<String>();
	private List<String> persons = new ArrayList<String>();
	private List<String> conv = new ArrayList<String>();

	private int[] chapterArray;
	private int[] personArray;
	private int[] choosenChapter;
	private int[] choosenPerson;
	private String[] converPersons;
	private String[] allPersons;
	private int[] groups;
	private int[] allGroups;
	private int[] source;
	private int[] target;
	private int[] valueG;
	private int[] sourceAll;
	private int[] targetAll;
	private int[] valueAll;
	
	public DiagramCreator(final GraphType graphType, final Text text,
			final Set<Act> acts, final List<SingleResult> results) {
		
			layout = new VerticalLayout();
			layout.setSizeUndefined();
			layout.setMargin(true);
			layout.setSpacing(true);
			setCompositionRoot(layout);
			diagram = new Diagram();
			create(graphType, results, text, acts);	
			
	}
	
	private void create(GraphType graphType, List<SingleResult> results, Text text, Set<Act> acts) {
		try {
			if (results == null || graphType == null || text == null) {
				layout.addComponent(new Label(getLocal("NO_GRAPH_SELECTED")));
			} else {
				conversationPersons.add(results.get(0).getSpeaker().toString());
				for (int i = 0; i <= results.size() - 1; i++) {
					String e = results.get(i).getSpeaker().toString().replace("[", "");
					String f=e.replace("]", "");
					if (!conv.contains(f)) {						
						conv.add(f);
					}
					for (String s : results.get(i).getSpeaker()) {
						if (!anySpeaker.contains(s)) {
							anySpeaker.add(s);
						}
					}
					for (String s : results.get(i).getConversationObjects()) {
						persons.add(s);
						if (i == 0) {
							conversationPersons.add(s);
							actualAct.add(results.get(i).getAct()
									.getActNumber());
						}
						chapterConv.add(results.get(i).getAct().getActNumber());
						if (i >= 1) {
							if (chapterConv.get(i - 1) == chapterConv.get(i)
									&& !conversationPersons.contains(s)) {
								conversationPersons.add(s);
								actualAct.add(results.get(i).getAct()
										.getActNumber());
							} else if (chapterConv.get(i - 1) != chapterConv
									.get(i)) {
								conversationPersons.add(s);
								actualAct.add(results.get(i).getAct()
										.getActNumber());
							}
						}
					}
					chapterAct.add(results.get(i).getAct().getActNumber());
				}

				source = new int[conversationPersons.size() - 1];
				target = new int[conversationPersons.size() - 1];
				valueG = new int[conversationPersons.size() - 1];

				for (int i = 0; i <= conversationPersons.size() - 2; i++) {
					source[i] = 0;
					target[i] = i + 1;
				}

				/**
				 * Calculate Value
				 */
				List<Integer> valuel = new ArrayList<Integer>();
				List<String> temp = new ArrayList<String>();
				int valuet = 0;
				for (int i = 0; i <= persons.size() - 1; i++) {
					if (temp.contains(persons.get(i))) {

					} else {
						for (String s : persons) {
							if (s.equals(persons.get(i))) {
								valuet++;
							}
						}
						temp.add(persons.get(i));
						valuel.add(valuet);
						valuet = 0;
					}
				}

				for (int i = 0; i <= valuel.size() - 1; i++) {
					valueG[i] = valuel.get(i);
				}

				/**
				 * Für den any any Filter source und target richtig befüllen
				 */
				List<Integer> source2 = new ArrayList<Integer>();
				List<Integer> target2 = new ArrayList<Integer>();

				for (int i = 0; i <= results.size() - 1; i++) {
//					System.out.println("result speaker: "
//							+ results.get(i).getSpeaker().toString());
//					System.out.println("result conversObj: "
//							+ results.get(i).getConversationObjects()
//									.toString());
					String a= results.get(i).getSpeaker().toString().replace("[", "");
					String b= a.replace("]","");
					
	for(String s: results.get(i).getConversationObjects() ){
		
		if (conv.contains(b)) {
			for (int j = 0; j <= conv.size() - 1; j++) {
				if (conv.get(j).equals(b)) {
					source2.add(j);
				}
			}
		}
		
		if (conv.contains(s)) {
			for (int j = 0; j <= conv.size() - 1; j++) {
				if (conv.get(j).equals(s)) {
					target2.add(j);
				}
			}
		}
		if (!conv.contains(s)) {
			conv.add(s);
			System.out.println("add d "+s);
			System.out.println("conv "+conv);
			target2.add(conv.size()-1);
		}	
					}			
				}

				System.out.println("Result Size:" + results.size());
				System.out.println("Source " + source2.toString());
				System.out.println("Target: " + target2.toString());
				System.out.println("Source size" + source2.size());
				System.out.println("Target Size: " + target2.size());
				System.out.println("Convpersons: "+conv);
				System.out.println("Conv:" + conv.size());

				sourceAll = new int[source2.size()];
				targetAll = new int[target2.size()];
				valueAll = new int[target2.size()];
				// 91 macbeth
				for (int i = 0; i <= (source2.size() - 1); i++) {
					sourceAll[i] = source2.get(i);
				}
				for (int i = 0; i <= (source2.size() - 1); i++) {
					targetAll[i] = target2.get(i);
				}
				for (int i = 0; i <= (source2.size() - 1); i++) {
					valueAll[i] = 1;
				}

				int value = 1;
				for (int i = 0, j = 1; j <= chapterAct.size() - 1; i++, j++) {
					if (chapterAct.get(i) == chapterAct.get(j)) {
						value++;
					} else {
						newChapter.add(chapterAct.get(i));
						newPerson.add(value);
						value = 1;
					}
				}
				if (chapterAct.get(0) == chapterAct.get(chapterAct.size() - 1)) {
					newPerson.add(chapterAct.size());
				} else {
					newPerson.add(value);
				}
				newChapter.add(chapterAct.get(chapterAct.size() - 1));
				chapterArray = new int[newChapter.size()];
				for (int i = 0; i <= newChapter.size() - 1; i++) {
					chapterArray[i] = newChapter.get(i);
				}
				personArray = new int[newPerson.size()];
				for (int i = 0; i <= newPerson.size() - 1; i++) {
					personArray[i] = newPerson.get(i);
				}

				/**
				 * Create an array with conversation objects
				 */
				converPersons = new String[conversationPersons.size()];
				for (int i = 0; i <= conversationPersons.size() - 1; i++) {
					converPersons[i] = conversationPersons.get(i);
				}

				allPersons = new String[conv.size()];
				for (int i = 0; i <= conv.size() - 1; i++) {
					//String s = conv.get(i).replace("[", "");
					allPersons[i] = conv.get(i).toString();
				}

				/**
				 * Create an array with persons ordered in groups, persons from
				 * conversationObject and getSpeaker
				 */
				groups = new int[actualAct.size() + 1];
				groups[0] = actualAct.get(0);
				for (int i = 0; i <= actualAct.size() - 1; i++) {
					groups[i + 1] = actualAct.get(i);
				}

				allGroups = new int[conv.size()];
				for (int i = 0; i <= conv.size() - 1; i++) {
					allGroups[i] = chapterAct.get(i);
				}

				if (acts != null) {
					choosenChapter = new int[acts.size()];
					Iterator<Act> iterator = acts.iterator();

					while (iterator.hasNext()) {
						Act act = iterator.next();
						choosenChapterList.add(act.getActNumber());
					}
					choosenChapterList.sort(new Comparator<Integer>() {

						@Override
						public int compare(Integer o1, Integer o2) {
							// TODO Auto-generated method stub
							return o1 - o2;
						}
					});

					int[] actMentions = new int[choosenChapterList.size()];

					// System.out.println("Das sind ausgewählte Akte" +
					// choosenChapterList);

					for (SingleResult result : results) {
						for (int i = 0; i < choosenChapterList.size(); i++) {
							if (result.getAct().getActNumber() == choosenChapterList
									.get(i)) {
								actMentions[i]++;
							}
						}
					}

					for (int actMention : actMentions) {
						choosenPersonList.add(actMention);
					}

					// choosenPersonList.add(value2);
					// System.out.println("Anzahl Personen des ausgewählten Acts: "
					// + choosenPersonList);

					/**
					 * create an chapter array from list
					 */
					for (int i = 0; i <= choosenChapterList.size() - 1; i++) {
						choosenChapter[i] = choosenChapterList.get(i);
					}
					/**
					 * Create a person array from choosen chapter
					 */
					choosenPerson = new int[choosenPersonList.size()];
					for (int i = 0; i <= choosenPersonList.size() - 1; i++) {
						choosenPerson[i] = choosenPersonList.get(i);
					}

					diagram.setSelectedGraph(graphType.toString());
					diagram.setChapter(choosenChapter);
					diagram.setPerson(choosenPerson);
					if (anySpeaker.size() > 1 && conversationPersons.size() > 2) {
						diagram.setConversationObject(allPersons);
						diagram.setGroups(allGroups);
						diagram.setSource(sourceAll);
						diagram.setTarget(targetAll);
						diagram.setValue(valueAll);
					} else {
						diagram.setConversationObject(converPersons);
						diagram.setGroups(groups);
						diagram.setSource(source);
						diagram.setTarget(target);
						diagram.setValue(valueG);
					}
				} else {
					diagram.setSelectedGraph(graphType.toString());
					diagram.setChapter(chapterArray);
					diagram.setPerson(personArray);
					if (anySpeaker.size() > 1 && conversationPersons.size() > 2) {
						diagram.setConversationObject(allPersons);
						diagram.setGroups(allGroups);
						diagram.setSource(sourceAll);
						diagram.setTarget(targetAll);
						diagram.setValue(valueAll);
					} else {
						diagram.setConversationObject(converPersons);
						diagram.setGroups(groups);
						diagram.setSource(source);
						diagram.setTarget(target);
						diagram.setValue(valueG);
					}

				}
				layout.addComponent(diagram);
				
			}
		} catch (Exception e) {
			System.out.println("Exception occured in force directed graph: "
					+ e.getMessage());
		}
	}
	
	public Diagram getDiagram(){
		return diagram;
	}
}