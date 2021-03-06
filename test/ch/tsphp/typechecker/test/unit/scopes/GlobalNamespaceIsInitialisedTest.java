/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.test.unit.scopes;

import ch.tsphp.common.ISymbol;
import ch.tsphp.typechecker.scopes.GlobalNamespaceScope;
import ch.tsphp.typechecker.scopes.IGlobalNamespaceScope;
import ch.tsphp.typechecker.scopes.IScopeHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class GlobalNamespaceIsInitialisedTest
{
    public static final String GLOBAL_NAMESPACE_NAME = "\\globalNamespace\\";
    private IScopeHelper scopeHelper;

    @Before
    public void setUp() {
        scopeHelper = mock(IScopeHelper.class);
    }

    @Test
    public void isFullyInitialised_NotAtAll_ReturnFalse() {
        //no arrange needed

        IGlobalNamespaceScope globalNamespaceScope = createGlobalScope();
        boolean result = globalNamespaceScope.isFullyInitialised(mock(ISymbol.class));

        assertFalse(result);
    }

    @Test
    public void isFullyInitialised_Partially_ReturnFalse() {
        ISymbol symbol = mock(ISymbol.class);

        IGlobalNamespaceScope globalNamespaceScope = createGlobalScope();
        globalNamespaceScope.addToInitialisedSymbols(symbol, false);
        boolean result = globalNamespaceScope.isFullyInitialised(symbol);

        assertFalse(result);
    }

    @Test
    public void isFullyInitialised_Fully_ReturnTrue() {
        ISymbol symbol = mock(ISymbol.class);

        IGlobalNamespaceScope globalNamespaceScope = createGlobalScope();
        globalNamespaceScope.addToInitialisedSymbols(symbol, true);
        boolean result = globalNamespaceScope.isFullyInitialised(symbol);

        assertTrue(result);
    }

    @Test
    public void isPartiallyInitialised_NotAtAll_ReturnFalse() {
        //no arrange needed

        IGlobalNamespaceScope globalNamespaceScope = createGlobalScope();
        boolean result = globalNamespaceScope.isPartiallyInitialised(mock(ISymbol.class));

        assertFalse(result);
    }

    @Test
    public void isPartiallyInitialised_Partially_ReturnTrue() {
        ISymbol symbol = mock(ISymbol.class);

        IGlobalNamespaceScope globalNamespaceScope = createGlobalScope();
        globalNamespaceScope.addToInitialisedSymbols(symbol, false);
        boolean result = globalNamespaceScope.isPartiallyInitialised(symbol);

        assertTrue(result);
    }

    @Test
    public void isPartiallyInitialised_Fully_ReturnFalse() {
        ISymbol symbol = mock(ISymbol.class);

        IGlobalNamespaceScope globalNamespaceScope = createGlobalScope();
        globalNamespaceScope.addToInitialisedSymbols(symbol, true);
        boolean result = globalNamespaceScope.isPartiallyInitialised(symbol);

        assertFalse(result);
    }

    protected IGlobalNamespaceScope createGlobalScope() {
        return new GlobalNamespaceScope(scopeHelper, GLOBAL_NAMESPACE_NAME);
    }
}
