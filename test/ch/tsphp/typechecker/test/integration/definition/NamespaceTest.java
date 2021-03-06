/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.definition;

import ch.tsphp.common.IScope;
import ch.tsphp.typechecker.test.integration.testutils.ScopeTestHelper;
import ch.tsphp.typechecker.test.integration.testutils.definition.ADefinitionTest;
import org.antlr.runtime.RecognitionException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class NamespaceTest extends ADefinitionTest
{

    private String namespaces;

    public NamespaceTest(String testString, String theNamespaces) {
        super(testString);
        namespaces = theNamespaces;
    }

    @Test
    public void test() throws RecognitionException {
        check();
        Assert.assertEquals(testString + " failed.", namespaces, getNamespacesAsString());
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        String deflt = "\\.\\.";
        String b = "\\b\\.\\b\\.";
        String ab = "\\a\\b\\.\\a\\b\\.";

        return Arrays.asList(new Object[][]{
                {"int $a=1;", deflt},
                {"namespace{}", deflt},
                {"namespace a\\b;", ab},
                {"namespace a\\b{}", ab},
                {"namespace{} namespace{}", deflt + " " + deflt},
                {"namespace b{} namespace b{}", b + " " + b},
                {"namespace{} namespace b{} namespace a\\b{}", deflt + " " + b + " " + ab},
                {"namespace{} namespace{}  namespace a\\b{}", deflt + " " + deflt + " " + ab},
                {"namespace{} namespace b{} namespace{} ", deflt + " " + b + " " + deflt},
                {"namespace{} namespace{} namespace{} ", deflt + " " + deflt + " " + deflt},
                {"namespace b{} namespace b{} namespace a\\b{} ", b + " " + b + " " + ab},
                {"namespace b{} namespace{} namespace b{} ", b + " " + deflt + " " + b},
                {"namespace b{} namespace b{} namespace b{} ", b + " " + b + " " + b}
        });
    }

    private String getNamespacesAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean isNotFirst = false;
        for (IScope scope : scopeFactory.scopes) {
            if (isNotFirst) {
                stringBuilder.append(" ");
            }
            isNotFirst = true;
            stringBuilder.append(ScopeTestHelper.getEnclosingScopeNames(scope));

        }
        return stringBuilder.toString();
    }
}
