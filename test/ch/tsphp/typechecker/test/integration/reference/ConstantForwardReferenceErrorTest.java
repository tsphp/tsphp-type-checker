/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.reference;

import ch.tsphp.typechecker.error.DefinitionErrorDto;
import ch.tsphp.typechecker.test.integration.testutils.reference.AReferenceDefinitionErrorTest;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ConstantForwardReferenceErrorTest extends AReferenceDefinitionErrorTest
{

    private static List<Object[]> collection;

    public ConstantForwardReferenceErrorTest(String testString, DefinitionErrorDto[] expectedLinesAndPositions) {
        super(testString, expectedLinesAndPositions);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        collection = new ArrayList<>();

        //global constants
        addVariations("", "");
        addVariations("namespace{", "}");
        addVariations("namespace a;", "");
        addVariations("namespace a{", "}");
        addVariations("namespace a\\b;", "");
        addVariations("namespace a\\b\\z{", "}");

        return collection;
    }

    private static void addVariations(String prefix, String appendix) {
        DefinitionErrorDto[] errorDto = new DefinitionErrorDto[]{new DefinitionErrorDto("a#", 2, 1, "a#", 3, 1)};
        DefinitionErrorDto[] twoErrorDto = new DefinitionErrorDto[]{
                new DefinitionErrorDto("a#", 2, 1, "a#", 4, 1),
                new DefinitionErrorDto("a#", 3, 1, "a#", 4, 1)
        };
        collection.addAll(Arrays.asList(new Object[][]{

                {prefix + "\n a; const float\n a=1;" + appendix, errorDto},
                {prefix + "\n a; const float b=2.5,\n a=1;" + appendix, errorDto},
                //More than one
                {prefix + "\n a; \n a; const float\n a=null;" + appendix, twoErrorDto},
                {prefix + "\n a; \n a; const float b=1,\n a=2.2;" + appendix, twoErrorDto},
        }));
    }
}
