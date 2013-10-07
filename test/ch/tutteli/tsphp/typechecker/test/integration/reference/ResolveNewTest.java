package ch.tutteli.tsphp.typechecker.test.integration.reference;

import ch.tutteli.tsphp.typechecker.test.integration.testutils.reference.AReferenceAstTest;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.ScopeTestStruct;
import java.util.Arrays;
import java.util.Collection;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ResolveNewTest extends AReferenceAstTest
{

    public ResolveNewTest(String testString, ScopeTestStruct[] testStructs) {
        super(testString, testStructs);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        return Arrays.asList(new Object[][]{
                    {
                        "class a{} a $a = new a();",
                        new ScopeTestStruct[]{
                            instanceOf("a", "\\.\\.", 1, 1, 1, 0, 0)
                        }
                    },
                    {
                        "namespace b{class a{} a $a = new a();}",
                        new ScopeTestStruct[]{
                            instanceOf("a", "\\b\\.\\b\\.",  1, 1, 1, 0, 0)
                        }
                    },
                    {
                        "class a{} use a as b; b $a = new b();",
                        new ScopeTestStruct[]{
                            instanceOf("b", "\\.\\.",  1, 2, 1, 0, 0)
                        }
                    },
                    {
                        "namespace b{class a{}} namespace x{ use b\\a as b;  b $a = new b();}",
                        new ScopeTestStruct[]{
                            instanceOf("b", "\\b\\.\\b\\.", 1, 1, 1, 1, 0, 0)
                        }
                    }
                });
    }

    public static ScopeTestStruct instanceOf(String callee, String scope, Integer... accessToScope) {
        return new ScopeTestStruct(callee, scope, Arrays.asList(accessToScope));
    }
}