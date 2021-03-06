/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.testutils.definition;

import ch.tsphp.common.ITSPHPAst;
import ch.tsphp.typechecker.test.integration.testutils.ScopeTestHelper;
import ch.tsphp.typechecker.test.integration.testutils.ScopeTestStruct;
import org.junit.Assert;
import org.junit.Ignore;

@Ignore
public abstract class ADefinitionScopeTest extends ADefinitionTest
{

    protected ScopeTestStruct[] testStructs;

    public ADefinitionScopeTest(String testString, ScopeTestStruct[] theTestStructs) {
        super(testString);
        testStructs = theTestStructs;
    }

    @Override
    protected void verifyDefinitions() {
        super.verifyDefinitions();
        verifyDefinitions(testStructs, ast, testString);
    }

    public static void verifyDefinitions(ScopeTestStruct[] testStructs, ITSPHPAst ast, String testString) {
        for (ScopeTestStruct testStruct : testStructs) {
            ITSPHPAst testCandidate = ScopeTestHelper.getAst(ast, testString, testStruct.astAccessOrder);
            Assert.assertNotNull(testString + " failed. testCandidate is null. should be " + testStruct.astText,
                    testCandidate);
            Assert.assertEquals(testString + " failed. wrong ast text,", testStruct.astText,
                    testCandidate.toStringTree());

            Assert.assertEquals(testString + "--" + testStruct.astText + " failed. wrong scope,", testStruct.astScope,
                    ScopeTestHelper.getEnclosingScopeNames(testCandidate.getScope()));
        }

    }
}
