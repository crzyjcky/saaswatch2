package edu.sjsu.comp295b.communicator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.sjsu.comp295b.Library;

public class LibraryAgentMBeanServerTest {

	@Before
	public void setUp() throws Exception {
		
		Library library = Library.getInstance();
		LibraryAgentMBeanServer mbs = new LibraryAgentMBeanServer(library);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJMXCommunicatorMBeanServer() throws InterruptedException {
		
		System.out.println("Waiting forever..."); 
        Thread.sleep(Long.MAX_VALUE);
        
		fail("Not yet implemented");
	}

}
