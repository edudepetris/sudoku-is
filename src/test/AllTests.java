package test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestBoard.class })
public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test para Board");
		suite.addTestSuite(TestBoard.class);

		return suite;
	}
}
