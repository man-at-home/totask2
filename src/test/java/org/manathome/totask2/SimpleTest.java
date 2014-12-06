package org.manathome.totask2;

import org.junit.Before;
import org.junit.Test;
import org.manathome.totask2.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/** unit test. */
public class SimpleTest {

	public static final Logger LOG = LoggerFactory.getLogger(SimpleTest.class);
	
	@Before
	public void setUp() throws Exception {
		LOG.info("setUp");
	}

	@Test
	public void test() {
		LOG.info("in test");
	}
	
	@Test
	public void testGetInfo() {
	    LOG.debug("test getInfo");
	    Assert.hasText(Application.getInfo() , "info text must be given.");	    
	}

}//class
