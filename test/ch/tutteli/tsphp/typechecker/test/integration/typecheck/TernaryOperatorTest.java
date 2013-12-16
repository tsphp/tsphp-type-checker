package ch.tutteli.tsphp.typechecker.test.integration.typecheck;

import ch.tutteli.tsphp.typechecker.test.integration.testutils.typecheck.AOperatorTypeCheckTest;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.typecheck.TypeCheckStruct;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class TernaryOperatorTest extends AOperatorTypeCheckTest
{

    public TernaryOperatorTest(String testString, TypeCheckStruct[] struct) {
        super(testString, struct);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        //I think there are enough test which cover the identity check. Thus here only a few tests
        return Arrays.asList(new Object[][]{
                //same types
                {"float $a=1.2; float $b=1.8; true ? $a : $b;", new TypeCheckStruct[]{
                        struct("?", Float, 1, 2, 0),
                        struct("true", Bool, 1, 2, 0, 0),
                        struct("$a", Float, 1, 2, 0, 1),
                        struct("$b", Float, 1, 2, 0, 2)}
                },
                //parent type left
                {"string $a=''; int $b=1; true ? $a : $b;", new TypeCheckStruct[]{
                        struct("?", String, 1, 2, 0),
                        struct("true", Bool, 1, 2, 0, 0),
                        struct("$a", String, 1, 2, 0, 1),
                        struct("$b", Int, 1, 2, 0, 2)}
                },
                //parent type right
                {"bool $a=false; int $b=1; true ? $a : $b;", new TypeCheckStruct[]{
                        struct("?", Int, 1, 2, 0),
                        struct("true", Bool, 1, 2, 0, 0),
                        struct("$a", Bool, 1, 2, 0, 1),
                        struct("$b", Int, 1, 2, 0, 2)}
                },
        });
    }
}
