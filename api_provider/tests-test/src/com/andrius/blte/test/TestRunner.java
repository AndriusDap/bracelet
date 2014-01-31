package com.andrius.blte.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestRunner extends TestSuite {
	public static Test suite() {
		return new TestSuiteBuilder(TestRunner.class).includeAllPackagesUnderHere().build();
	}
}
