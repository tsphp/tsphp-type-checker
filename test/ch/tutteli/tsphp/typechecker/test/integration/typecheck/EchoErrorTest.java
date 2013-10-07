package ch.tutteli.tsphp.typechecker.test.integration.typecheck;

import ch.tutteli.tsphp.typechecker.error.ReferenceErrorDto;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.typecheck.ATypeCheckErrorTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class EchoErrorTest extends ATypeCheckErrorTest
{

    private static List<Object[]> collection;

    public EchoErrorTest(String testString, ReferenceErrorDto[] expectedLinesAndPositions) {
        super(testString, expectedLinesAndPositions);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        collection = new ArrayList<>();
        ReferenceErrorDto[] errorDto = new ReferenceErrorDto[]{new ReferenceErrorDto("$b", 2, 1)};
        ReferenceErrorDto[] errroTwo = new ReferenceErrorDto[]{
            new ReferenceErrorDto("$b", 2, 1),
            new ReferenceErrorDto("$b", 3, 1)
        };

        String[] types = new String[]{"array", "resource", "object"};
        for (String type : types) {
            collection.add(new Object[]{type + " $b;echo\n $b;", errorDto});
            collection.add(new Object[]{type + " $b;echo\n $b,\n $b;", errroTwo});
        }

        return collection;
    }
}