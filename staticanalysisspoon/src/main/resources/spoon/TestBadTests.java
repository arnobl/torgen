package torgen;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestBadTests {
	public void testNoAnnot() {
	}

	@Test
	public void testCorrectTest() {
	}

	@Test
	public void testWithTryCatchFail() {
		try {
			assertEquals("TestBadTests", getClass().getName());
		}catch(final Exception ex) {
			fail();
		}
	}
}