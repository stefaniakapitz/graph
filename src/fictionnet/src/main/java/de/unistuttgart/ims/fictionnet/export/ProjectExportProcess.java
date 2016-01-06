package de.unistuttgart.ims.fictionnet.export;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.processes.Process;

/**
 * Class to export Projects as XML.
 * @author David Schuetz
 * @version 09-10-2015
 */


public class ProjectExportProcess extends Process {
	
	/**
	 * Export a Project to a XML- File.
	 * @param project
	 * @throws IOException 
	 * @return inputStream
	 * @throws JAXBException 
	 */
	public InputStream write(Project project) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLStreamWriter writer = null;
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
			writer = factory.createXMLStreamWriter( outputStream, "UTF-8" );
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
        JAXBContext context;
        
		try {
			context = JAXBContext.newInstance(Project.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(project, writer);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        
        outputStream.close();
        
        return inputStream;
    }
	
	
	/**
	 * Runs the ExportProcess.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
