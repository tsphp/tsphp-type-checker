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
package ch.tutteli.tsphp.typechecker.test.testutils;

import ch.tutteli.tsphp.common.IScope;
import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.common.TSPHPAst;
import org.antlr.runtime.CommonToken;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class AstTestHelper
{

    private AstTestHelper() {
    }

     public static ITSPHPAst getAstWithTokenText(String text, IScope scope, int tokenIndex) {
         ITSPHPAst ast = getAstWithTokenText(text, scope);
         ast.setTokenStartIndex(tokenIndex);
         return ast;
     }
    public static ITSPHPAst getAstWithTokenText(String text, IScope scope) {
        ITSPHPAst ast = getAstWithTokenText(text);
        ast.setScope(scope);
        return ast;
    }

    public static ITSPHPAst getAstWithTokenText(String text) {
        return new TSPHPAst(new CommonToken(0, text));
    }
}