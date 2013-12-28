package ch.tutteli.tsphp.typechecker.test.unit;

import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.typechecker.IReferencePhaseController;
import ch.tutteli.tsphp.typechecker.ISymbolResolver;
import ch.tutteli.tsphp.typechecker.ReferencePhaseController;
import ch.tutteli.tsphp.typechecker.error.ITypeCheckErrorReporter;
import ch.tutteli.tsphp.typechecker.error.TypeCheckErrorReporterRegistry;
import ch.tutteli.tsphp.typechecker.scopes.IGlobalNamespaceScope;
import ch.tutteli.tsphp.typechecker.symbols.ISymbolFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReferencePhaseControllerTest
{

    private ISymbolFactory symbolFactory;
    private ISymbolResolver symbolResolver;
    private ITypeCheckErrorReporter errorReporter;

    @Before
    public void setUp() {
        symbolFactory = mock(ISymbolFactory.class);
        symbolResolver = mock(ISymbolResolver.class);
        errorReporter = mock(ITypeCheckErrorReporter.class);
        TypeCheckErrorReporterRegistry.set(errorReporter);
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    @Test
    public void checkBreakContinueLevel_Level0_CallsErrorReporter0NotAllowed() {
        ITSPHPAst root = mock(ITSPHPAst.class);
        ITSPHPAst level = mock(ITSPHPAst.class);
        when(level.getText()).thenReturn("0");

        IReferencePhaseController controller = createReferencePhaseController();
        controller.checkBreakContinueLevel(root, level);

        verify(errorReporter).breakContinueLevelZeroNotAllowed(root);
    }

    protected IReferencePhaseController createReferencePhaseController() {
        return new ReferencePhaseController(
                symbolFactory, symbolResolver, mock(IGlobalNamespaceScope.class));
    }

}

