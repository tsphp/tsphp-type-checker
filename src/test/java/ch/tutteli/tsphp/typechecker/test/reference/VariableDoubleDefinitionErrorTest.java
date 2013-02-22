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
package ch.tutteli.tsphp.typechecker.test.reference;

import ch.tutteli.tsphp.typechecker.error.DefinitionErrorDto;
import ch.tutteli.tsphp.typechecker.test.testutils.reference.AReferenceDefinitionErrorTest;
import java.util.ArrayList;
import java.util.Arrays;
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
public class VariableDoubleDefinitionErrorTest extends AReferenceDefinitionErrorTest
{

    public VariableDoubleDefinitionErrorTest(String testString, DefinitionErrorDto[] expectedLinesAndPositions) {
        super(testString, expectedLinesAndPositions);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        List<Object[]> collection = new ArrayList<>();

        //global variables
        collection.addAll(getVariationsInclConditionalScopes("", ""));
        collection.addAll(getVariationsInclConditionalScopes("namespace{", "}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a;", ""));
        collection.addAll(getVariationsInclConditionalScopes("namespace a{", "}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a\\b;", ""));
        collection.addAll(getVariationsInclConditionalScopes("namespace a\\b\\z{", "}"));

        //functions
        collection.addAll(getVariationsInclConditionalScopes("function void foo(){", "}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace{function void foo(){", "}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a;function void foo(){", "}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a{function void foo(){", "}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a\\b;function void foo(){", "}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a\\b\\z{function void foo(){", "}}"));

        //methods
        collection.addAll(getVariationsInclConditionalScopes("class a{ function void foo(){", "}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace{class a{ function void foo(){", "}}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a;class a{ function void foo(){", "}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a{class a{ function void foo(){", "}}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a\\b;class a{ function void foo(){", "}}"));
        collection.addAll(getVariationsInclConditionalScopes("namespace a\\b\\z{class a{ function void foo(){", "}}}"));

        collection.addAll(Arrays.asList(new Object[][]{
                    {
                        "namespace{\n int $a=1;} namespace{\n int $a=1;}",
                        new DefinitionErrorDto[]{new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5)}},
                    {
                        "namespace a {\n int $a=1;} namespace a{\n int $a=1;}",
                        new DefinitionErrorDto[]{new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5)}
                    },
                    {
                        "namespace a {\n int $a=1;} namespace a{\n int $a=1;} namespace a{\n int $a=1;}",
                        new DefinitionErrorDto[]{
                            new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5),
                            new DefinitionErrorDto("$a", 2, 5, "$a", 4, 5)
                        }
                    }
                }));
        return collection;
    }

    public static Collection<Object[]> getVariationsInclConditionalScopes(String prefix, String appendix) {
        List<Object[]> collection = new ArrayList<>();
        collection.addAll(getVariations(prefix, appendix));
        DefinitionErrorDto[] errorDto = new DefinitionErrorDto[]{new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5)};
        collection.addAll(Arrays.asList(new Object[][]{
                    {prefix + "\n int $a; if(true){\n int $a=1;}" + appendix, errorDto},
                    {prefix + "\n int $a; if(true);else \n int $a;" + appendix, errorDto},
                    {prefix + "if(true){}else{\n int $a=1;} \n int $a; " + appendix, errorDto},
                    {prefix + "if(true){\n int $a;}else{\n int $a=1;}" + appendix, errorDto},
                    {prefix + "\n int $a; int $b; switch($b){case 1: \n int $a=1;}" + appendix, errorDto},
                    {prefix + "int $b; switch($b){case 1: \n int $a=1;} \n int $a=1;" + appendix, errorDto},
                    {prefix + "\n int $a=1; for(\n int $a;;){}" + appendix, errorDto},
                    {prefix + "for(;;){\n int $a;} \n int $a; " + appendix, errorDto},
                    {prefix + "for(;;)\n int $a; \n int $a; " + appendix, errorDto},
                    {prefix + "foreach([1,2] as object $b){\n int $a;} \n int $a; " + appendix, errorDto},
                    {prefix + "\n int $a=1; foreach([1,2] as object $b){\n int $a=1;} " + appendix, errorDto},
                    {prefix + "\n int $a; while(true){ \n int $a=1;}" + appendix, errorDto},
                    {prefix + "while(true) \n int $a=1; \n int $a;" + appendix, errorDto},
                    {prefix + "do \n int $a; while(true); \n int $a;" + appendix, errorDto},
                    {prefix + "\n int $a=1+1; do{ \n int $a;}while(true); " + appendix, errorDto},
                    {prefix + "if(true){\n int $a=1+1;} do{ \n int $a;}while(true); " + appendix, errorDto},
                    //
                    {prefix + "\n int $a=1+1; do{ \n int $a;}while(true); \n int $a=1;" + appendix,
                        new DefinitionErrorDto[]{
                            new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5),
                            new DefinitionErrorDto("$a", 2, 5, "$a", 4, 5)
                        }
                    },}));
        return collection;
    }

    public static Collection<Object[]> getVariations(String prefix, String appendix) {
        DefinitionErrorDto[] errorDto = new DefinitionErrorDto[]{new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5)};
        return Arrays.asList(new Object[][]{
                    {prefix + "\n int $a;\n int $a;" + appendix, errorDto},
                    {prefix + "\n int $a=1;\n int $a;" + appendix, errorDto},
                    {prefix + "\n int $a=1;\n int $a=1;" + appendix, errorDto},
                    {prefix + "\n int $a;\n int $a=1;" + appendix, errorDto},
                    // definition list
                    {prefix + "\n int $a,\n     $a=1;" + appendix, errorDto},
                    {prefix + "\n int $a, $b=1, \n     $a=1;" + appendix, errorDto},
                    {prefix + "\n int $a,\n     $a=1;" + appendix, errorDto},
                    {prefix + "\n int $a; int $b=1, \n     $a=1;" + appendix, errorDto},
                    {prefix + "\n int $a=1+1,\n     $a=1, \n     $a;" + appendix,
                        new DefinitionErrorDto[]{
                            new DefinitionErrorDto("$a", 2, 5, "$a", 3, 5),
                            new DefinitionErrorDto("$a", 2, 5, "$a", 4, 5)
                        }
                    }
                });
    }
}
