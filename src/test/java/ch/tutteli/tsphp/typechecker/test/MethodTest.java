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
package ch.tutteli.tsphp.typechecker.test;

import ch.tutteli.tsphp.typechecker.TSPHPTypeCheckerDefinition;
import ch.tutteli.tsphp.typechecker.symbols.ModifierHelper;
import ch.tutteli.tsphp.typechecker.test.utils.ATypeCheckerTest;
import ch.tutteli.tsphp.typechecker.test.utils.IAdder;
import ch.tutteli.tsphp.typechecker.test.utils.TypeHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
@RunWith(Parameterized.class)
public class MethodTest extends ATypeCheckerTest
{

    private static String prefix = "namespace a{ class b{";
    private static String appendix = "}}";
    private static String prefixExpected = "global.a.b ";

    public MethodTest(String testString, String expectedResult) {
        super(testString, expectedResult);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        final List<Object[]> collection = new ArrayList<>();

        TypeHelper.getAllTypesInclModifier(new IAdder()
        {
            @Override
            public void add(String type, String typeExpected, SortedSet modifiers) {
                String typeModifiers = ModifierHelper.getModifiers(modifiers);
                collection.add(new Object[]{
                            prefix + "function " + type + " get(){}" + appendix,
                            prefixExpected + "global.a.b." + typeExpected + " global.a.b.get()|"
                            + TSPHPTypeCheckerDefinition.Public + typeModifiers
                        });
            }
        });

        int fin = TSPHPTypeCheckerDefinition.Final;
        int stat = TSPHPTypeCheckerDefinition.Static;
        collection.addAll(getVariations("", new TreeSet()));
        collection.addAll(getVariations("static", new TreeSet(Arrays.asList(new Integer[]{stat}))));
        collection.addAll(getVariations("final", new TreeSet(Arrays.asList(new Integer[]{fin}))));
        collection.addAll(getVariations("static final", new TreeSet(Arrays.asList(new Integer[]{fin, stat}))));
        collection.addAll(getVariations("final static", new TreeSet(Arrays.asList(new Integer[]{fin, stat}))));

        return collection;
    }

    public static Collection<Object[]> getVariations(String modifier, SortedSet<Integer> modifiers) {
        final List<Object[]> collection = new ArrayList<>();
        modifiers.add(TSPHPTypeCheckerDefinition.Public);
        collection.add(new Object[]{
                    prefix + modifier + " function void foo(){}" + appendix,
                    prefixExpected + "global.a.b.void "
                    + "global.a.b.foo()" + ModifierHelper.getModifiers(modifiers)
                });
        collection.add(new Object[]{
                    prefix + modifier + " public function void foo(){}" + appendix,
                    prefixExpected + "global.a.b.void "
                    + "global.a.b.foo()" + ModifierHelper.getModifiers(modifiers)
                });

        modifiers.remove(TSPHPTypeCheckerDefinition.Public);
        modifiers.add(TSPHPTypeCheckerDefinition.Private);
        collection.add(new Object[]{
                    prefix + modifier + " private function void foo(){}" + appendix,
                    prefixExpected + "global.a.b.void "
                    + "global.a.b.foo()" + ModifierHelper.getModifiers(modifiers)
                });

        modifiers.remove(TSPHPTypeCheckerDefinition.Private);
        modifiers.add(TSPHPTypeCheckerDefinition.Protected);
        collection.add(new Object[]{
                    prefix + modifier + " protected function void foo(){}" + appendix,
                    prefixExpected + "global.a.b.void "
                    + "global.a.b.foo()" + ModifierHelper.getModifiers(modifiers)
                });

        return collection;
    }
}
