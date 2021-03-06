/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.integration.testutils;

import ch.tsphp.typechecker.symbols.ModifierHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

public class VariableDeclarationListHelper
{

    private VariableDeclarationListHelper() {
    }

    public static Collection<Object[]> testStringsDefinitionPhase(String prefix, String appendix,
            String prefixExpected, String appendixExpected, String scopeName, SortedSet<Integer> modifier) {
        return testStrings(prefix, appendix, prefixExpected, appendixExpected, scopeName, modifier, true);
    }

    private static Collection<Object[]> testStrings(final String prefix, final String appendix,
            final String prefixExpected, final String appendixExpected, final String scopeName,
            final SortedSet<Integer> modifier, final boolean isDefinitionPhase) {


        final List<Object[]> collection = new ArrayList<>();
        TypeHelper.getAllTypesInclModifier(new IAdder()
        {
            @SuppressWarnings("unchecked")
            @Override
            public void add(String type, String typeExpected, SortedSet modifiers) {
                String typeExpected2 = isDefinitionPhase ? "" : typeExpected;
                if (modifier != null) {
                    modifiers.addAll(modifier);
                }
                String typeModifiers = ModifierHelper.getModifiers(modifiers);
                collection.add(new Object[]{
                        prefix + type + "$a" + appendix,
                        prefixExpected + scopeName + typeExpected + " "
                                + scopeName + "$a" + typeExpected2 + typeModifiers + appendixExpected
                });
                collection.add(new Object[]{
                        prefix + type + "$a=1" + appendix,
                        prefixExpected + scopeName + typeExpected + " "
                                + scopeName + "$a" + typeExpected2 + typeModifiers + appendixExpected
                });
            }
        });

        String typeModifiers = ModifierHelper.getModifiers(modifier);

        String typeExpected = (isDefinitionPhase ? "" : "int") + typeModifiers;
        collection.addAll(getVariations(prefix + "int", "=", appendix,
                prefixExpected, appendixExpected, scopeName, scopeName, "int", typeExpected));

        typeExpected = (isDefinitionPhase ? "" : "object") + typeModifiers;
        collection.addAll(getVariations(prefix + "object", "=", appendix,
                prefixExpected, appendixExpected, scopeName, scopeName, "object", typeExpected));

        typeExpected = (isDefinitionPhase ? "" : "float") + typeModifiers;
        collection.addAll(getVariations(prefix + "float", "=()", appendix,
                prefixExpected, appendixExpected, scopeName, scopeName, "float", typeExpected));

        typeExpected = (isDefinitionPhase ? "" : "int") + typeModifiers;
        collection.addAll(Arrays.asList(new Object[][]{
                {
                        prefix + "int $a                    " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a,     $b            " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a,     $b,     $c    " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b=1,   $c    " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b,     $c=1  " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b=1,   $c=1  " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b=()1, $c    " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b=()1, $c=1  " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b,     $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b=1,   $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=()1, $b=()1, $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a,     $b=()1, $c    " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a,     $b=()1, $c=1  " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a,     $b=()1, $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a,     $b=1,   $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=1,   $b=()1, $c    " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=1,   $b=()1, $c=1  " + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=1,   $b=()1, $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + "int $a=1,   $b=1,   $c=()1" + appendix,
                        prefixExpected + scopeName + "int " + scopeName + "$a" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$b" + typeExpected + " "
                                + scopeName + "int " + scopeName + "$c" + typeExpected + appendixExpected
                }
        }));
        return collection;
    }

    private static Collection<Object[]> getVariations(String prefix, String operator, String appendix,
            String prefixExpected, String appendixExpected, String scopeName, String fullScopeName, String type,
            String typeExpected) {
        return Arrays.asList(new Object[][]{
                {
                        prefix + " $a, $b, $c" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a" + operator + "1, $b, $c" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a" + operator + "1, $b" + operator + "1, $c" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a" + operator + "1, $b, $c" + operator + "1" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a" + operator + "1, $b" + operator + "1, $c" + operator + "1" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a, $b" + operator + "1, $c" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a, $b" + operator + "1, $c" + operator + "1" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                },
                {
                        prefix + " $a, $b, $c" + operator + "1" + appendix,
                        prefixExpected + fullScopeName + type + " " + scopeName + "$a" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$b" + typeExpected + " "
                                + fullScopeName + type + " " + scopeName + "$c" + typeExpected + appendixExpected
                }
        });
    }
}
