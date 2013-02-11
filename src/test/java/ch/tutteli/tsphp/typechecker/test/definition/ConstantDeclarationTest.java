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
package ch.tutteli.tsphp.typechecker.test.definition;

import ch.tutteli.tsphp.typechecker.test.utils.ATypeCheckerDefinitionTest;
import ch.tutteli.tsphp.typechecker.test.utils.ConstantHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
@RunWith(Parameterized.class)
public class ConstantDeclarationTest extends ATypeCheckerDefinitionTest
{

    public ConstantDeclarationTest(String testString, String expectedResult) {
        super(testString, expectedResult);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        List<Object[]> collection = new ArrayList<>();

        String global = "global.";

        collection.addAll(ConstantHelper.testStrings("", "", "", global+"default.", global + "default.default.", true));
        collection.addAll(ConstantHelper.testStrings("namespace a {", "}", "", global + "a.", global + "a.a.", true));
        collection.addAll(ConstantHelper.testStrings("namespace a\\b {", "}", "",
                global + "a\\b.", global + "a\\b.a\\b.", true));

        //class constants
        collection.addAll(ConstantHelper.testStrings(
                "namespace a\\b\\c; class f{", "}", global + "a\\b\\c.f{} ",
                global + "a\\b\\c.a\\b\\c.f{}.", global + "a\\b\\c.a\\b\\c.f{}.", true));

        return collection;
    }
}
