/*
 * Copyright 2012 Robert Stoll <rstoll@tutteli.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ch.tutteli.tsphp.typechecker.test.utils;

import ch.tutteli.tsphp.common.TSPHPAst;
import java.util.List;
import junit.framework.Assert;
import org.junit.Ignore;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
@Ignore
public abstract class ATypeCheckerScopeTest extends ATypeCheckerTest
{

    protected ScopeTestStruct[] testStructs;

    public ATypeCheckerScopeTest(String testString, ScopeTestStruct[] theTestStructs) {
        super(testString);
        testStructs = theTestStructs;
    }

    @Override
    public void verify() {

        for (int i = 0; i < testStructs.length; ++i) {
            ScopeTestStruct testStruct = testStructs[i];
            TSPHPAst testCandidate = getAst(testStruct.astAccessOrder);
            Assert.assertEquals(testString + " failed.", testStruct.astText, testCandidate.toStringTree());
            Assert.assertEquals(testString + " failed.", testStruct.astScope, ScopeHelper.getEnclosingScopeNames(testCandidate.scope));
        }

    }

    private TSPHPAst getAst(List<Integer> astAccessOrder) {
        TSPHPAst tmp = ast;
        for (Integer index : astAccessOrder) {
            tmp = (TSPHPAst) tmp.getChild(index);
        }
        return tmp;
    }
}