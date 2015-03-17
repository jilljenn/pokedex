package net.androidsensei.pokedex;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

public class PokedexTestSuite extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(PokedexTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public PokedexTestSuite() {
        super();
    }
}