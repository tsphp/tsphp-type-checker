package ch.tutteli.tsphp.typechecker.test.integration.testutils;

import ch.tutteli.tsphp.common.IErrorLogger;
import ch.tutteli.tsphp.common.IParser;
import ch.tutteli.tsphp.common.exceptions.TSPHPException;
import ch.tutteli.tsphp.parser.ParserFacade;
import ch.tutteli.tsphp.typechecker.error.ErrorMessageProvider;
import ch.tutteli.tsphp.typechecker.error.ErrorReporter;
import ch.tutteli.tsphp.typechecker.error.ErrorReporterRegistry;
import ch.tutteli.tsphp.typechecker.error.IErrorReporter;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.List;

@Ignore
public abstract class ATest implements IErrorLogger
{

    protected List<Exception> exceptions = new ArrayList<>();
    protected IParser parser;

    public ATest() {
        parser = new ParserFacade();
        IErrorReporter errorReporter = new ErrorReporter(new ErrorMessageProvider());
        errorReporter.registerErrorLogger(this);
        ErrorReporterRegistry.set(errorReporter);
    }

    public void log(TSPHPException exception) {
        exceptions.add(exception);
    }
}
