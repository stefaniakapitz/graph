package de.unistuttgart.ims.fictionnet.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import objectsForTests.TestCorpusConstructor;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Mention;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TextType;

public class IMSInteractionTest {

	@Test
	public void testWebServiceCall() {

		IMSInteractionObject object = new IMSInteractionObject();

		List<SectionWithType> sections = new ArrayList();
		// object.setSections(sections); //Struktur vom InteractionObject leicht geändert --Rafael

		SectionWithType section = new SectionWithType(0, 10);
		section.setSectionType(TextType.SPOKEN_TEXT);
		sections.add(section);

		section = new SectionWithType(2, 12);
		section.setSectionType(TextType.SPOKEN_TEXT);
		sections.add(section);

		section = new SectionWithType(3, 15);
		section.setSectionType(TextType.SPOKEN_TEXT);
		sections.add(section);

		object.setText("Hallo Welt. Dies ist ein Test!123,.-;:_{[]}()\"',äöüß");

		IMSInteractionObject object2 = IMSAnalyzer.getInstance().analyze(object);

		assertEquals(object.getText(), object2.getText());
	}

	@Test
	public void testWebServiceAnalyzer() {

		IMSInteractionObject object = new IMSInteractionObject();

		List<SectionWithType> sections = new ArrayList();
		// object.setSections(sections);

		SectionWithType section = new SectionWithType(17, 43);
		section.setSectionType(TextType.STAGE_INSTRUCTION);
		sections.add(section);

		section = new SectionWithType(53, 193);
		section.setSectionType(TextType.SPOKEN_TEXT);
		section.setSpeakerString("THIBAUT");
		sections.add(section);

		section = new SectionWithType(203, 253);
		section.setSectionType(TextType.SPOKEN_TEXT);
		section.setSpeakerString("RAIMOND");
		sections.add(section);

		section = new SectionWithType(263, 699);
		section.setSectionType(TextType.SPOKEN_TEXT);
		section.setSpeakerString("THIBAUT");
		sections.add(section);

		StringBuilder sb = new StringBuilder();
		sb.append("Zweiter Auftritt" + "\n");
		sb.append("Thibaut. Raimond. Johanna." + "\n");
		sb.append("THIBAUT." + "\n");
		sb.append("Jeanette, deine Schwestern machen Hochzeit," + "\n");
		sb.append("Ich seh sie glücklich, sie erfreun mein Alter," + "\n");
		sb.append("Du, meine Jüngste, machst mir Gram und Schmerz." + "\n");
		sb.append("RAIMOND." + "\n");
		sb.append("Was fällt Euch ein! Was scheltet Ihr die Tochter?" + "\n");
		sb.append("THIBAUT." + "\n");
		sb.append("Hier dieser wackre Jüngling, dem sich keiner" + "\n");
		sb.append("Vergleicht im ganzen Dorf, der Treffliche," + "\n");
		sb.append("Er hat dir seine Neigung zugewendet," + "\n");
		sb.append("Und wirbt um dich, schon ists der dritte Herbst," + "\n");
		sb.append("Mit stillem Wunsch, mit herzlichem Bemühn," + "\n");
		sb.append("Du stößest ihn verschlossen, kalt, zurück," + "\n");
		sb.append("Noch sonst ein andrer von den Hirten allen" + "\n");
		sb.append("Mag dir ein gütig Lächeln abgewinnen." + "\n");
		sb.append("– Ich sehe dich in Jugendfülle prangen" + "\n");
		sb.append("Dein Lenz ist da, es ist die Zeit der Hoffnung" + "\n");

		object.setText(sb.toString());

		IMSInteractionObject object2 = IMSAnalyzer.getInstance().analyze(object);

		// check if text is equal
		assertEquals(object.getText(), object2.getText());

		// check if analyzer finds 137 Tokens
		assertEquals(object2.getTokens().size(), 137);
		// check if analyzer finds 11 Sentences
		assertEquals(object2.getSentences().size(), 11);

	}

	@Test
	public void testWebServiceAnalyzerWithTEI() {

		String PATH = this.getClass().getClassLoader().getResource("tei").getPath();
		File file = new File(PATH + File.separator + "Var 2 - Macbeth.xml");
//		final String lines = Methods.concat(In.readFromFile(file));
//		final TagProcessor_TeiFile processor = new TagProcessor_TeiFile(lines);
		

//		ProcessStatusType status;
//		String message;

		

		try {
			final TextImportProcess tIP = new TextImportProcess(file, new TestCorpusConstructor());
			
			
			tIP.run();
			
			
			IMSInteractionObject iO = tIP.getIMSInteractionObject();
			System.out.println(iO.getText().length());
			System.out.println(iO.getText());

			System.out.println(iO.getSections().size());
			assertTrue(iO.getText().length() != 0);
			iO = IMSAnalyzer.getInstance().analyze(iO);
			System.out.println(iO.getMentions().size());
			System.out.println(iO.getTokens().size());
			
			for (SectionWithType s : iO.getSections()) {
				System.out.println("<section>"+iO.getText().substring(s.getStart(), s.getEnd())+"</section>");
			}
			if (true)
			for (Mention m : iO.getMentions()) {
				int left_window = Math.max(m.getStart() - 20,0);
				int right_window = Math.min(m.getEnd()+ 20,iO.getText().length());
				System.out.print(iO.getText().substring(left_window,m.getStart())+" #");

				System.out.print(iO.getText().substring(m.getStart(), m.getEnd()));
				System.out.println(" #"+iO.getText().substring(m.getEnd(),right_window));

			}
			
			for (MentionCluster cm : iO.getMentionClusters())
			{
				System.out.print(cm.getOrigin() + "\t(");
				for (MentionEntity me: cm.getAssociations())
				{
					System.out.print(iO.getText().substring(me.mention.getStart(), me.mention.getEnd())+"["+me.confidence+"],");
				}
				System.out.println(cm.getOrigin() + ")");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
