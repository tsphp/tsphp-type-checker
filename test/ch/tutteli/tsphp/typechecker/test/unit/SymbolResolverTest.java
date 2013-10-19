package ch.tutteli.tsphp.typechecker.test.unit;

import ch.tutteli.tsphp.common.ILowerCaseStringMap;
import ch.tutteli.tsphp.common.ISymbol;
import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.common.LowerCaseStringMap;
import ch.tutteli.tsphp.typechecker.ISymbolResolver;
import ch.tutteli.tsphp.typechecker.SymbolResolver;
import ch.tutteli.tsphp.typechecker.scopes.IGlobalNamespaceScope;
import ch.tutteli.tsphp.typechecker.scopes.INamespaceScope;
import ch.tutteli.tsphp.typechecker.scopes.IScopeHelper;
import ch.tutteli.tsphp.typechecker.symbols.ISymbolFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SymbolResolverTest
{
    private IScopeHelper scopeHelper;
    private ISymbolFactory symbolFactory;
    private ILowerCaseStringMap<IGlobalNamespaceScope> globalNamespaceScopes = new LowerCaseStringMap<>();
    private IGlobalNamespaceScope globalDefaultNamespace;


    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        scopeHelper = mock(IScopeHelper.class);
        symbolFactory = mock(ISymbolFactory.class);
        globalNamespaceScopes = mock(ILowerCaseStringMap.class);
        globalDefaultNamespace = mock(IGlobalNamespaceScope.class);
    }

    @Test
    public void isAbsolute_AbsoluteFromDefaultNamespace_ReturnTrue(){
        //no arrange needed

        ISymbolResolver symbolResolver = createSymbolResolver();
        boolean result = symbolResolver.isAbsolute("\\a");

        assertTrue(result);
    }

    @Test
    public void isAbsolute_AbsoluteFromNamespace_ReturnTrue(){
        //no arrange needed

        ISymbolResolver symbolResolver = createSymbolResolver();
        boolean result = symbolResolver.isAbsolute("\\a\\a");

        assertTrue(result);
    }

    @Test
    public void isAbsolute_AbsoluteFromSubNamespace_ReturnTrue(){
        //no arrange needed

        ISymbolResolver symbolResolver = createSymbolResolver();
        boolean result = symbolResolver.isAbsolute("\\a\\b\\a");

        assertTrue(result);
    }


    @Test
    public void isAbsolute_Local_ReturnFalse(){
        //no arrange needed

        ISymbolResolver symbolResolver = createSymbolResolver();
        boolean result = symbolResolver.isAbsolute("a");

        assertFalse(result);
    }

    @Test
    public void isAbsolute_RelativeFromSubNamespace_ReturnFalse(){
        //no arrange needed

        ISymbolResolver symbolResolver = createSymbolResolver();
        boolean result = symbolResolver.isAbsolute("a\\a");

        assertFalse(result);
    }

    @Test
    public void resolveGlobalIdentifier_AbsoluteIdentifier_DelegateToGlobalNamespaceScope(){
        ITSPHPAst ast = createAst("\\symbol");
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        when(scopeHelper.getCorrespondingGlobalNamespace(globalNamespaceScopes, "\\symbol"))
                .thenReturn(globalNamespaceScope);

        ISymbolResolver symbolResolver = createSymbolResolver();
        symbolResolver.resolveGlobalIdentifier(ast);

        verify(globalNamespaceScope).resolve(ast);
    }


    @Test
    public void resolveGlobalIdentifier_AbsoluteIdentifierGlobalNamespaceDoesNotExists_ReturnNull(){
        ITSPHPAst ast = createAst("\\nonExistingNamespace\\symbol");
        when(scopeHelper.getCorrespondingGlobalNamespace(globalNamespaceScopes, "\\nonExistingNamespace\\symbol"))
                .thenReturn(null);

        ISymbolResolver symbolResolver = createSymbolResolver();
        ISymbol result = symbolResolver.resolveGlobalIdentifier(ast);

        assertNull(result);
    }

    @Test
    public void resolveGlobalIdentifier_Local_DelegateToNamespaceScope(){
        ITSPHPAst ast = createAst("symbol");
        INamespaceScope namespaceScope = mock(INamespaceScope.class);
        when(scopeHelper.getEnclosingNamespaceScope(ast)).thenReturn(namespaceScope);

        ISymbolResolver symbolResolver = createSymbolResolver();
        symbolResolver.resolveGlobalIdentifier(ast);

        verify(namespaceScope).resolve(ast);
    }

    @Test
    public void resolveGlobalIdentifier_LocalNoEnclosingNamespace_DelegateToNamespaceScope(){
        ITSPHPAst ast = createAst("symbol");
        INamespaceScope namespaceScope = mock(INamespaceScope.class);
        when(scopeHelper.getEnclosingNamespaceScope(ast)).thenReturn(namespaceScope);

        ISymbolResolver symbolResolver = createSymbolResolver();
        symbolResolver.resolveGlobalIdentifier(ast);

        verify(namespaceScope).resolve(ast);
    }

    private ISymbolResolver createSymbolResolver() {
        return new SymbolResolver(scopeHelper,symbolFactory,globalNamespaceScopes, globalDefaultNamespace);
    }

    private ITSPHPAst createAst(String name) {
        ITSPHPAst ast = mock(ITSPHPAst.class);
        when(ast.getText()).thenReturn(name);
        return ast;
    }
}
