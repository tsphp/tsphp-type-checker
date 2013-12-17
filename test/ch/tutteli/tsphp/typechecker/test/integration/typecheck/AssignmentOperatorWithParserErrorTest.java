
package ch.tutteli.tsphp.typechecker.test.integration.typecheck;

import ch.tutteli.tsphp.typechecker.test.integration.testutils.typecheck.ATypeCheckWithParserErrorTest;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class AssignmentOperatorWithParserErrorTest extends ATypeCheckWithParserErrorTest
{

    @Override
    protected void verifyTypeCheck() {
        // nothing to check in addition.
        // No error other than the parsing error should have occurred.
        // That's already checked in ATypeCheckWithParserErrorTest
    }

    public AssignmentOperatorWithParserErrorTest(String testString) {
        super(testString);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        Collection<Object[]> collection = new ArrayList<>();
        collection.addAll(Arrays.asList(new Object[][]{
            //see TSPHP-535 - const type missing, should cause parser error but no type check error
            {"const a = 1;"},
        }));
        return collection;
    }

}
