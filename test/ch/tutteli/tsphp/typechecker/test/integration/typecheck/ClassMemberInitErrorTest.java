package ch.tutteli.tsphp.typechecker.test.integration.typecheck;

import ch.tutteli.tsphp.typechecker.error.ReferenceErrorDto;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.typecheck.ATypeCheckErrorTest;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.typecheck.ConstantInitialValueHelper;
import java.util.Collection;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ClassMemberInitErrorTest extends ATypeCheckErrorTest
{

    public ClassMemberInitErrorTest(String testString, ReferenceErrorDto[] expectedLinesAndPositions) {
        super(testString, expectedLinesAndPositions);
    }

    @Test
    public void test() throws RecognitionException {
        check();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testStrings() {
        Collection<Object[]> collection = 
                ConstantInitialValueHelper.errorTestStrings("class A{", ";}", "$a", true, false);
        
        collection.add(new Object[]{
            "class A{private int $a;private int\n $b = $this->a;}",
            new ReferenceErrorDto[]{new ReferenceErrorDto("$b", 2, 1)}
        });

        return collection;
    }
}
