package ch.tutteli.tsphp.typechecker.test.integration.definition;

import ch.tutteli.tsphp.typechecker.antlr.TSPHPDefinitionWalker;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.definition.ConstantHelper;
import ch.tutteli.tsphp.typechecker.test.integration.testutils.definition.ADefinitionSymbolTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ConstantDeclarationTest extends ADefinitionSymbolTest
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


        collection.addAll(ConstantHelper.testStrings("", "", "", "\\.\\.", true));
        collection.addAll(ConstantHelper.testStrings("namespace a {", "}", "", "\\a\\.\\a\\.", true));
        collection.addAll(ConstantHelper.testStrings("namespace a\\b {", "}", "", "\\a\\b\\.\\a\\b\\.", true));

        //class constants
        collection.addAll(ConstantHelper.testStrings(
                "namespace a\\b\\c; class f{", "}", "\\a\\b\\c\\.\\a\\b\\c\\.f ",
                "\\a\\b\\c\\.\\a\\b\\c\\.f.", true));

        //interface constants
        collection.addAll(ConstantHelper.testStrings(
                "namespace a\\b\\c; interface f{", "}",
                "\\a\\b\\c\\.\\a\\b\\c\\.f|" + TSPHPDefinitionWalker.Abstract + " ",
                "\\a\\b\\c\\.\\a\\b\\c\\.f.", true));

        return collection;
    }
}
