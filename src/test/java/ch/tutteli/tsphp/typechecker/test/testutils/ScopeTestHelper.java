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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class ScopeTestHelper  
{

    public static String getEnclosingScopeNames(IScope scope) {
        StringBuilder stringBuilder = new StringBuilder();
        while (scope != null) {
            stringBuilder.insert(0, ".");
            stringBuilder.insert(0, scope.getScopeName());
            scope = scope.getEnclosingScope();
        }
        return stringBuilder.toString();
    }

    public static Collection<Object[]> testStringsDefaultNamespace() {
        return testStrings("", "", "\\.\\", new Integer[]{1});
    }

    public static Collection<Object[]> testStrings(String prefix, String appendix,
            String fullScopeName, Integer[] accessToScope) {

        fullScopeName += ".";
        List<Object[]> collection = new ArrayList<>();

        String[][] variableIds = new String[][]{
            {"$b", "$b", "$b"},
            {"$this", "$this"},
            {"self::$b", "(sMemAccess self $b)"},
            {"parent::$b", "(sMemAccess parent $b)"},
            {"foo()", "(fCall foo args)"},
            {"$a->foo()", "(mCall $a foo args)"},
            {"$this->foo()", "(mCall $this foo args)"},
            {"self::foo()", "(smCall self foo args)"},
            {"parent::foo()", "(smCall parent foo args)"},};

        for (String[] variableId : variableIds) {
            collection.addAll(getVariations(prefix, appendix, variableId[0], variableId[1],
                    fullScopeName, accessToScope));
            collection.addAll(getAccessVariations(prefix, appendix, variableId[0], variableId[1],
                    fullScopeName, accessToScope));
        }
        collection.addAll(getVariations(prefix, appendix, "b", "b",
                fullScopeName, accessToScope));
        collection.addAll(getVariations(prefix, appendix, "self::b", "(sMemAccess self b)",
                fullScopeName, accessToScope));
        collection.addAll(getVariations(prefix, appendix, "parent::b", "(sMemAccess parent b)",
                fullScopeName, accessToScope));

        String[] types = TypeHelper.getClassInterfaceTypes();
        for (String type : types) {
            collection.addAll(getVariations(prefix, appendix, type + "::b", "(sMemAccess " + type + " b)",
                    fullScopeName, accessToScope));
            collection.addAll(getVariations(prefix, appendix, type + "::$b", "(sMemAccess " + type + " $b)",
                    fullScopeName, accessToScope));
            collection.addAll(getAccessVariations(prefix, appendix, type + "::$b", "(sMemAccess " + type + " $b)",
                    fullScopeName, accessToScope));
            collection.addAll(getAccessVariations(prefix, appendix, type + "::foo()", "(smCall " + type + " foo args)",
                    fullScopeName, accessToScope));
        }


        return collection;
    }

    private static Collection<Object[]> getVariations(String prefix, String appendix, String variableId, String astText,
            String fullScopeName, Integer[] accessToScope) {

        return Arrays.asList(new Object[][]{
                    {prefix + "int $a = " + variableId + ";" + appendix, new ScopeTestStruct[]{
                            // vars $a $b
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0))
                        }
                    },
                    {prefix + "int $a = " + variableId + " + $c;" + appendix, new ScopeTestStruct[]{
                            //vars $a + variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0, 0)),
                            //vars $a + $c
                            new ScopeTestStruct("$c", fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0, 1))
                        }
                    },
                    {prefix + "int $a = $c + " + variableId + ";" + appendix, new ScopeTestStruct[]{
                            //vars $a + $c
                            new ScopeTestStruct("$c", fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0, 0)),
                            //vars $a + variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0, 1))
                        }
                    },
                    {prefix + "int $a = 1 + " + variableId + " + $c;" + appendix, new ScopeTestStruct[]{
                            //vars $a + + variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0, 0, 1)),
                            //vars $a + $c
                            new ScopeTestStruct("$c", fullScopeName, getAstAccessOrder(accessToScope, 0, 1, 0, 1))
                        }
                    },
                    {prefix + variableId + " = $a;" + appendix, new ScopeTestStruct[]{
                            //= variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 0)),
                            //= $a
                            new ScopeTestStruct("$a", fullScopeName, getAstAccessOrder(accessToScope, 0, 1))
                        }
                    },
                    //there are no nested local scopes
                    {prefix + " { " + variableId + " = $a; } " + appendix, new ScopeTestStruct[]{
                            //= variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 0)),
                            //= $a
                            new ScopeTestStruct("$a", fullScopeName, getAstAccessOrder(accessToScope, 0, 1))
                        }
                    },
                    //there are no nested local scopes, does not matter how many {} we declare
                    {prefix + " { { $a = " + variableId + ";} int $a = $c; } " + appendix, new ScopeTestStruct[]{
                            //= $a
                            new ScopeTestStruct("$a", fullScopeName, getAstAccessOrder(accessToScope, 0, 0)),
                            //= "+variableId+"
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 1)),
                            //vars $a "+variableId+"
                            new ScopeTestStruct("$c", fullScopeName, getAstAccessOrder(accessToScope, 1, 1, 0))
                        }
                    }
                });
    }

    private static Collection<Object[]> getAccessVariations(String prefix, String appendix, String variableId,
            String astText, String fullScopeName, Integer[] accessToScope) {

        return Arrays.asList(new Object[][]{
                    {prefix + variableId + "[0];" + appendix, new ScopeTestStruct[]{
                            //arrAccess variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 0))
                        }
                    },
                    {prefix + variableId + "[1+1][0];" + appendix, new ScopeTestStruct[]{
                            //arrAccess arrAccess variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 0, 0))
                        }
                    },
                    {prefix + variableId + "->foo();" + appendix, new ScopeTestStruct[]{
                            //smCall/mCall/fCall variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 0))
                        }
                    },
                    {prefix + variableId + "->foo()->bar();" + appendix, new ScopeTestStruct[]{
                            //smCall/mCall/fCall smCall/mCall/fCall variableId
                            new ScopeTestStruct(astText, fullScopeName, getAstAccessOrder(accessToScope, 0, 0, 0))
                        }
                    }
                });
    }

    private static List<Integer> getAstAccessOrder(Integer[] accessToScope, Integer... accessToTestCandidate) {
        List<Integer> acessOrder = new ArrayList<>();
        acessOrder.addAll(Arrays.asList(accessToScope));
        acessOrder.addAll(Arrays.asList(accessToTestCandidate));
        return acessOrder;
    }
}