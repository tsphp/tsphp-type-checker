/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.definition;

import ch.tsphp.typechecker.test.integration.testutils.definition.ADefinitionTest;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class NamespaceErrorTest extends ADefinitionTest
{


    public NamespaceErrorTest(String testString) {
        super(testString);

    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Override
    protected void verifyParser() {
        assertTrue(testString.replaceAll("\n", " ") + " failed - parser exception expected but non was thrown",
                parser.hasFoundError());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        return Arrays.asList(new Object[][]{
                //see TSPHP-736 - wrong syntax can mess up namespace scope generation which causes NullPointerException
                //switch without case"
                {"function void foo(){if(true){return;}else{int $a; switch($a){return;}}}"},
                //class in class
                {" class B{$ class A extends B{}"},
                //function in function
                {"function void foo(){$ function void bar(){}"},
                // return type missing
                {"function test($arg1, $arg2){}"}
        });
    }

    @Override
    protected void registerParserErrorLogger() {
        //no need to write parser errors to the console, we expect some
    }
}
