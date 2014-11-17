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
public class UseTypeNameClashErrorTest extends AReferenceDefinitionErrorTest
{

    public UseTypeNameClashErrorTest(String testString, DefinitionErrorDto[] expectedLinesAndPositions) {
        super(testString, expectedLinesAndPositions);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        List<Object[]> collection = new ArrayList<>();
        DefinitionErrorDto[] errorDtos = new DefinitionErrorDto[]{new DefinitionErrorDto("z", 2, 1, "z", 3, 1)};
        collection.addAll(Arrays.asList(new Object[][]{
                {"namespace b; use \n b\\z; class \n z{} z $b;", errorDtos},
                {"namespace b {use\n b\\z; class \n z{} z $b;}", errorDtos},
                {"namespace b\\c {use\n b\\c\\z; class \n z{} z $b;}", errorDtos},
                {"namespace b; use \n b\\z; interface \n z{} z $b;", errorDtos},
                {"namespace b {use\n b\\z; interface \n z{} z $b;}", errorDtos},
                {"namespace b\\c {use\n b\\c\\z; interface \n z{} z $b;}", errorDtos}
        }));
        errorDtos = new DefinitionErrorDto[]{new DefinitionErrorDto("Z", 2, 1, "z", 3, 1)};
        collection.addAll(Arrays.asList(new Object[][]{
                //case insensitive - see TSPHP-622
                {"namespace b; use b\\z as\n Z; class \n z{} z $b;", errorDtos},
                {"namespace b {use b\\z as\n Z; class \n z{} z $b;}", errorDtos},
                {"namespace b; use b\\z as\n Z; interface \n z{} z $b;", errorDtos},
                {"namespace b {use b\\z as\n Z; interface \n z{} z $b;}", errorDtos},
        }));
        return collection;
    }
}
