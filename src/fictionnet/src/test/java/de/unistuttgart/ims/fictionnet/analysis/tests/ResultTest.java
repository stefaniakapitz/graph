package de.unistuttgart.ims.fictionnet.analysis.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.analysis.ActionTypes;
import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;

public class ResultTest {

	//TODO: Speaker.getNames ist nun ein HashSet
	/*
	public static void main(String[] args) throws Exception {
		testCreateSingleResults();
	}

	
	public static void testCreateSingleResults() throws Exception {
		Result result = new Result();

		Set<Act> actSet = new HashSet<>();
		int actStart = 0;
		int actEnd = 299;
		for (int i = 1; i < 7; i++) {
			actSet.add(new Act(actStart, actEnd, i));
			actStart += 300;
			actEnd += 300;
		}
		Set<Scene> sceneSet = new HashSet<>();
		int sceneStart = 0;
		int sceneEnd = 99;
		for (int i = 1; i < 17; i++) {
			sceneSet.add(new Scene(sceneStart, sceneEnd, i));
			sceneStart += 100;
			sceneEnd += 100;
		}
		Set<Sentence> sentenceSet = new HashSet<>();
		int sentenceStart = 0;
		int sentenceEnd = 19;
		for (int i = 1; i < 82; i++) {
			sentenceSet.add(new Sentence(sentenceStart, sentenceEnd));
			sentenceStart += 20;
			sentenceEnd += 20;
		}
		Set<Speaker> speakerSet = new HashSet<>();
		
		
		speakerSet.add(new Speaker("Sphinx", 80, 99));
		speakerSet.add(new Speaker("Sphinx", 240, 259));
		speakerSet.add(new Speaker("Sphinx", 600, 619));
		speakerSet.add(new Speaker("Sphinx", 640, 659));
		speakerSet.add(new Speaker("Sphinx", 800, 819));
		speakerSet.add(new Speaker("Iokaste", 600, 619));
		speakerSet.add(new Speaker("Iokaste", 660, 679));
		speakerSet.add(new Speaker("Iokaste", 720, 739));
		speakerSet.add(new Speaker("Iokaste", 900, 919));
		speakerSet.add(new Speaker("Iokaste", 1200, 1219));
		
		int speakerStart = 0;
		int speakerEnd = 19;
		for (int i = 1; i < 82; i++) {
			if(speakerStart%50==0||speakerStart%60==0){
				speakerSet.add(new Speaker("Oedipus", speakerStart, speakerEnd));
			}else{
				speakerSet.add(new Speaker("Narrator", speakerStart, speakerEnd));
			}
			
			speakerStart += 20;
			speakerEnd += 20;
		}
		
		Set<Presence> presenceSet = new HashSet<>();
		int presenceStart = 0;
		int presenceEnd = 19;
		for (int i = 1; i < 82; i++) {
			Presence presence = new Presence(presenceStart, presenceEnd);
			presence.setPerson("Oedipus");
			presenceSet.add(presence);
			presenceStart += 20;
			presenceEnd += 20;
			if (presenceStart % 40 == 0) {
				Presence presence2 = new Presence(presenceStart, presenceEnd);
				presence.setPerson("Sphinx");
				presenceSet.add(presence2);
			}
			if (presenceStart % 60 == 0) {
				Presence presence3 = new Presence(presenceStart, presenceEnd);
				presence.setPerson("Iokaste");
				presenceSet.add(presence3);
			}
		}
		
		
			TreeMap<Act, ArrayList<Speaker>> map = result.getSpeakerAsMap("Iokaste");
		System.out.println();
		for (Map.Entry<Act, ArrayList<Speaker>> entry : map.entrySet()) {
			System.out.println(entry.getKey().getActNumber());
			System.out.println(entry.getValue().size());
			for (Speaker value : entry.getValue()) {
				System.out.println(value.getName());
			}
		}
		// assertEquals(Role.ADMIN, user.getRole());
	} */
}
