package org.regele.totask2;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}//class
