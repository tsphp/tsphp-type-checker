/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.definition;

import ch.tsphp.typechecker.test.integration.testutils.definition.ADoubleDefinitionTest;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class DoubleVariableDefinitionTest extends ADoubleDefinitionTest
{

    public DoubleVariableDefinitionTest(String testString, String theNamespace, String theIdentifier, int howMany) {
        super(testString, theNamespace, theIdentifier, howMany);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        List<Object[]> collection = new ArrayList<>();
        collection.addAll(getDifferentNamespaces("int $a;", "$a", 1));
        collection.addAll(getDifferentNamespaces("int $a; int $a;", "$a", 2));
        collection.addAll(getDifferentNamespaces("int $a; int $b; int $a;", "$a", 2));
        collection.addAll(getDifferentNamespaces("int $a; { int $a;}", "$a", 2));
        return collection;
    }
}
