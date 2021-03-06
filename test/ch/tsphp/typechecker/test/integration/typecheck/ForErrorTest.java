/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.typecheck;

import ch.tsphp.typechecker.error.ReferenceErrorDto;
import ch.tsphp.typechecker.test.integration.testutils.TypeHelper;
import ch.tsphp.typechecker.test.integration.testutils.typecheck.ATypeCheckErrorTest;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ForErrorTest extends ATypeCheckErrorTest
{

    public ForErrorTest(String testString, ReferenceErrorDto[] expectedLinesAndPositions) {
        super(testString, expectedLinesAndPositions);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        List<Object[]> collection = new ArrayList<>();
        ReferenceErrorDto[] errorDto = new ReferenceErrorDto[]{new ReferenceErrorDto("for", 2, 1)};


        String[][] types = TypeHelper.getTypesInclDefaultValue();
        for (String[] type : types) {
            if (type[0].equals("bool")) {
                continue;
            }
            collection.add(new Object[]{type[0] + " $b=" + type[1] + ";\n for(;$b;);", errorDto});
            collection.add(new Object[]{type[0] + " $b=" + type[1] + ";\n for(;true, $b;);", errorDto});
            collection.add(new Object[]{type[0] + " $b=" + type[1] + ";\n for(;true, false,$b;);", errorDto});
        }

        return collection;
    }
}
