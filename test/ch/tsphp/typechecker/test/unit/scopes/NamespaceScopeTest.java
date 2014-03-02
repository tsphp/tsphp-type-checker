package ch.tsphp.typechecker.test.unit.scopes;

import ch.tsphp.common.IScope;
import ch.tsphp.common.ISymbol;
import ch.tsphp.common.ITSPHPAst;
import ch.tsphp.common.ITypeSymbol;
import ch.tsphp.typechecker.error.ITypeCheckerErrorReporter;
import ch.tsphp.typechecker.scopes.IGlobalNamespaceScope;
import ch.tsphp.typechecker.scopes.INamespaceScope;
import ch.tsphp.typechecker.scopes.IScopeHelper;
import ch.tsphp.typechecker.scopes.NamespaceScope;
import ch.tsphp.typechecker.symbols.IAliasSymbol;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NamespaceScopeTest
{
    private IScopeHelper scopeHelper;

    @Before
    public void setUp() {
        scopeHelper = mock(IScopeHelper.class);
    }

    @Test
    public void getScopeName_DefineTestAsScopeName_ReturnTest() {
        //no arrange necessary, createNamespaceScope passes "test" as name

        INamespaceScope namespaceScope = createNamespaceScope();
        String name = namespaceScope.getScopeName();

        assertThat(name, is("test"));
    }

    @Test
    public void getEnclosingScope_DefineGlobalNamespaceScope_ReturnDefinedGlobalScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        IScope scope = namespaceScope.getEnclosingScope();

        assertThat(scope, is((IScope) globalNamespaceScope));
    }

    @Test
    public void define_Standard_DelegateToEnclosingScopeAndSetDefinitionScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.define(symbol);

        verify(globalNamespaceScope).define(symbol);
        verify(symbol).setDefinitionScope(namespaceScope);
    }

    @Test
    public void define_TwoSymbolsWithSameName_DelegateToEnclosingScopeAndSetDefinitionScopeForBoth() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = createSymbol("a");
        ISymbol symbol2 = createSymbol("a");

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.define(symbol);
        namespaceScope.define(symbol2);

        verify(globalNamespaceScope).define(symbol);
        verify(symbol).setDefinitionScope(namespaceScope);
        verify(globalNamespaceScope).define(symbol2);
        verify(symbol2).setDefinitionScope(namespaceScope);
    }


    @Test
    public void resolve_Standard_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ITSPHPAst ast = mock(ITSPHPAst.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.resolve(ast);

        verify(globalNamespaceScope).resolve(ast);
    }

    @Test
    public void doubleDefinitionCheck_Standard_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.doubleDefinitionCheck(symbol);

        verify(globalNamespaceScope).doubleDefinitionCheck(symbol);
    }

    @Test
    public void doubleDefinitionCheckCaseInsensitive_Standard_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.doubleDefinitionCheckCaseInsensitive(symbol);

        verify(globalNamespaceScope).doubleDefinitionCheckCaseInsensitive(symbol);
    }


    @Test
    public void defineUse_Standard_SetDefinitionScope() {
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);

        verify(aliasSymbol).setDefinitionScope(namespaceScope);
    }


    @Test
    public void getSymbols_NothingDefined_ReturnEmptyList() {
        //no arrange needed

        INamespaceScope namespaceScope = createNamespaceScope();
        Map<String, List<ISymbol>> symbols = namespaceScope.getSymbols();

        assertTrue(symbols.isEmpty());
    }

    @Test
    public void getUse_NothingDefined_ReturnNull() {
        //no arrange needed

        INamespaceScope namespaceScope = createNamespaceScope();
        List<IAliasSymbol> symbols = namespaceScope.getUse("nonExistingAlias");

        assertNull(symbols);
    }


    @Test
    public void getCaseInsensitiveFirstUseDefinitionAst_NothingDefined_ReturnNull() {
        //no arrange needed

        INamespaceScope namespaceScope = createNamespaceScope();
        ITSPHPAst ast = namespaceScope.getCaseInsensitiveFirstUseDefinitionAst("nonExistingAlias");

        assertNull(ast);
    }

    @Test
    public void getUse_WrongName_ReturnNull() {
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        List<IAliasSymbol> symbols = namespaceScope.getUse("notExistingAlias");

        assertNull(symbols);
    }


    @Test
    public void getCaseInsensitiveFirstUseDefinitionAst_WrongName_ReturnNull() {
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        ITSPHPAst ast = namespaceScope.getCaseInsensitiveFirstUseDefinitionAst("nonExistingAlias");

        assertNull(ast);
    }


    @Test
    public void getUse_CaseWrong_ReturnNull() {
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        List<IAliasSymbol> symbols = namespaceScope.getUse("ALIASName");

        assertNull(symbols);
    }

    @Test
    public void getCaseInsensitiveFirstUseDefinitionAst_CaseWrong_ReturnAst() {
        ITSPHPAst expectedAst = mock(ITSPHPAst.class);
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName", expectedAst);

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        ITSPHPAst ast = namespaceScope.getCaseInsensitiveFirstUseDefinitionAst("ALIASName");

        assertThat(ast, is(expectedAst));
    }


    @Test
    public void getSymbols_OneDefined_ReturnMapWithOneListWithOne() {
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        Map<String, List<ISymbol>> symbols = namespaceScope.getSymbols();

        assertThat(symbols.size(), is(1));
        assertThat(symbols, hasKey("aliasName"));
        List<ISymbol> list = symbols.get("aliasName");
        assertThat(list.size(), is(1));
        assertThat(list, hasItem(aliasSymbol));
    }

    @Test
    public void getUse_OneDefined_ReturnListWithOne() {
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        List<IAliasSymbol> symbols = namespaceScope.getUse("aliasName");

        assertThat(symbols.size(), is(1));
        assertThat(symbols, hasItem(aliasSymbol));
    }


    @Test
    public void getCaseInsensitiveFirstUseDefinitionAst_OneDefined_ReturnAst() {
        ITSPHPAst expectedAst = mock(ITSPHPAst.class);
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName", expectedAst);

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        ITSPHPAst ast = namespaceScope.getCaseInsensitiveFirstUseDefinitionAst("aliasName");

        assertThat(ast, is(expectedAst));
    }


    @Test
    public void getSymbols_TwoDefinedSameName_ReturnMapWithOneListWithTwo() {
        IAliasSymbol aliasSymbol1 = createAliasSymbol("aliasName");
        IAliasSymbol aliasSymbol2 = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol1);
        namespaceScope.defineUse(aliasSymbol2);
        Map<String, List<ISymbol>> symbols = namespaceScope.getSymbols();

        assertThat(symbols.size(), is(1));
        assertThat(symbols, hasKey("aliasName"));
        List<ISymbol> list = symbols.get("aliasName");
        assertThat(list.size(), is(2));
        assertThat(list, hasItems((ISymbol) aliasSymbol1, aliasSymbol2));
    }

    @Test
    public void getSymbols_TwoDefinedDifferentName_ReturnMapWithTwoListWithOneEach() {
        IAliasSymbol aliasSymbol1 = createAliasSymbol("aliasName1");
        IAliasSymbol aliasSymbol2 = createAliasSymbol("aliasName2");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol1);
        namespaceScope.defineUse(aliasSymbol2);
        Map<String, List<ISymbol>> symbols = namespaceScope.getSymbols();

        assertThat(symbols.size(), is(2));
        assertThat(symbols, hasKey("aliasName1"));
        List<ISymbol> list1 = symbols.get("aliasName1");
        assertThat(list1.size(), is(1));
        assertThat(list1, hasItem(aliasSymbol1));
        List<ISymbol> list2 = symbols.get("aliasName2");
        assertThat(list2.size(), is(1));
        assertThat(list2, hasItem(aliasSymbol2));

    }

    @Test
    public void getUse_TwoDefined_ReturnListWithTwo() {
        IAliasSymbol aliasSymbol1 = createAliasSymbol("aliasName");
        IAliasSymbol aliasSymbol2 = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol1);
        namespaceScope.defineUse(aliasSymbol2);
        List<IAliasSymbol> symbols = namespaceScope.getUse("aliasName");

        assertThat(symbols.size(), is(2));
        assertThat(symbols, hasItems(aliasSymbol1, aliasSymbol2));
    }

    @Test
    public void getCaseInsensitiveFirstUseDefinitionAst_TwoDefined_ReturnFirstAst() {
        ITSPHPAst expectedAst = mock(ITSPHPAst.class);
        IAliasSymbol aliasSymbol = createAliasSymbol("aliasName", expectedAst);
        ITSPHPAst notThisAst = mock(ITSPHPAst.class);
        IAliasSymbol aliasSymbol2 = createAliasSymbol("aliasName", notThisAst);

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(aliasSymbol);
        namespaceScope.defineUse(aliasSymbol2);
        ITSPHPAst ast = namespaceScope.getCaseInsensitiveFirstUseDefinitionAst("aliasName");

        assertThat(ast, is(expectedAst));
    }

    @Test(expected = NullPointerException.class)
    /**
     * It is not possible to perform a useDefinitionCheck for a certain symbol which was never defined.
     * Thus a NullPointerException should be thrown
     */
    public void useDefinitionCheck_NothingDefined_ThrowNullPointerException() {
        IAliasSymbol symbol = createAliasSymbol("aliasName");

        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.useDefinitionCheck(symbol);

        //Assert via the @Test(expected) annotation
    }

    @Test
    public void useDefinitionCheck_IsNotDoubleDefinedAndIsNotAlreadyDefinedAsType_ReturnTrue() {
        ITSPHPAst useDefinitionAst = mock(ITSPHPAst.class);
        IAliasSymbol symbol = createAliasSymbol("aliasName", useDefinitionAst);
        //not double defined
        when(scopeHelper.checkIsNotDoubleDefinition(symbol, symbol)).thenReturn(true);

        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        //there is not type with the name "aliasName" thus it returns null
        when(globalNamespaceScope.getTypeSymbolWhichClashesWithUse(useDefinitionAst)).thenReturn(null);

        //act
        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.defineUse(symbol);
        boolean result = namespaceScope.useDefinitionCheck(symbol);

        assertTrue(result);
        verify(scopeHelper).checkIsNotDoubleDefinition(symbol, symbol);
        verify(globalNamespaceScope).getTypeSymbolWhichClashesWithUse(useDefinitionAst);
    }

    @Test
    /**
     * Strange exception of PHP which stays in TSPHP
     * There is no type name clash in the following situation: namespace{use a as b;} namespace{ class b{}}
     * because: use is defined earlier and the use statement is in a different namespace statement
     */
    public void useDefinitionCheck_IsNotDoubleDefinedAndDefinedEarlierThanTypeAndTypeInOtherNamespace_ReturnTrue() {
        ITSPHPAst useDefinitionAst = mock(ITSPHPAst.class);
        IAliasSymbol symbol = createAliasSymbol("aliasName", useDefinitionAst);
        //not double defined
        when(scopeHelper.checkIsNotDoubleDefinition(symbol, symbol)).thenReturn(true);

        ITSPHPAst classDefinitionAst = mock(ITSPHPAst.class);
        IScope anotherScope = mock(IScope.class);
        ITypeSymbol typeSymbol = createTypeSymbol(classDefinitionAst, anotherScope);
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        when(globalNamespaceScope.getTypeSymbolWhichClashesWithUse(useDefinitionAst)).thenReturn(typeSymbol);

        when(useDefinitionAst.isDefinedEarlierThan(classDefinitionAst)).thenReturn(true);

        //act
        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.defineUse(symbol);
        boolean result = namespaceScope.useDefinitionCheck(symbol);

        assertTrue(result);
        verify(scopeHelper).checkIsNotDoubleDefinition(symbol, symbol);
        verify(globalNamespaceScope).getTypeSymbolWhichClashesWithUse(useDefinitionAst);
        verify(useDefinitionAst).isDefinedEarlierThan(classDefinitionAst);
        verify(typeSymbol).getDefinitionScope();
    }

    @Test
    public void useDefinitionCheck_IsDoubleDefined_ReturnFalse() {
        IAliasSymbol firstSymbol = createAliasSymbol("aliasName");
        ITSPHPAst useDefinitionAst = mock(ITSPHPAst.class);
        IAliasSymbol symbol = createAliasSymbol("aliasName", useDefinitionAst);
        when(scopeHelper.checkIsNotDoubleDefinition(firstSymbol, symbol)).thenReturn(false);

        //act
        INamespaceScope namespaceScope = createNamespaceScope();
        namespaceScope.defineUse(firstSymbol);
        namespaceScope.defineUse(symbol);
        boolean result = namespaceScope.useDefinitionCheck(symbol);

        assertFalse(result);
        verify(scopeHelper).checkIsNotDoubleDefinition(firstSymbol, symbol);
    }

    @Test
    public void useDefinitionCheck_IsNotDoubleDefinedAndTypeDefinedEarlierInSameNamespace_ReturnFalse() {
        ITypeCheckerErrorReporter errorReporter = mock(ITypeCheckerErrorReporter.class);

        ITSPHPAst useDefinitionAst = mock(ITSPHPAst.class);
        IAliasSymbol symbol = createAliasSymbol("aliasName", useDefinitionAst);
        //not double defined
        when(scopeHelper.checkIsNotDoubleDefinition(symbol, symbol)).thenReturn(true);

        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope, errorReporter);
        ITSPHPAst classDefinitionAst = mock(ITSPHPAst.class);
        ITypeSymbol typeSymbol = createTypeSymbol(classDefinitionAst, namespaceScope);
        when(globalNamespaceScope.getTypeSymbolWhichClashesWithUse(useDefinitionAst)).thenReturn(typeSymbol);

        when(useDefinitionAst.isDefinedEarlierThan(classDefinitionAst)).thenReturn(false);

        //act
        namespaceScope.defineUse(symbol);
        boolean result = namespaceScope.useDefinitionCheck(symbol);

        assertFalse(result);
        verify(scopeHelper).checkIsNotDoubleDefinition(symbol, symbol);
        verify(globalNamespaceScope).getTypeSymbolWhichClashesWithUse(useDefinitionAst);
        verify(useDefinitionAst).isDefinedEarlierThan(classDefinitionAst);
        verify(errorReporter).determineAlreadyDefined(symbol, typeSymbol);
    }

    @Test
    public void useDefinitionCheck_IsNotDoubleDefinedAndTypeDefinedEarlierInOtherNamespace_ReturnFalse() {
        ITypeCheckerErrorReporter errorReporter = mock(ITypeCheckerErrorReporter.class);


        ITSPHPAst useDefinitionAst = mock(ITSPHPAst.class);
        IAliasSymbol symbol = createAliasSymbol("aliasName", useDefinitionAst);
        //not double defined
        when(scopeHelper.checkIsNotDoubleDefinition(symbol, symbol)).thenReturn(true);

        ITSPHPAst classDefinitionAst = mock(ITSPHPAst.class);
        IScope anotherScope = mock(IScope.class);
        ITypeSymbol typeSymbol = createTypeSymbol(classDefinitionAst, anotherScope);
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        when(globalNamespaceScope.getTypeSymbolWhichClashesWithUse(useDefinitionAst)).thenReturn(typeSymbol);

        when(useDefinitionAst.isDefinedEarlierThan(classDefinitionAst)).thenReturn(false);

        //act
        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope, errorReporter);
        namespaceScope.defineUse(symbol);
        boolean result = namespaceScope.useDefinitionCheck(symbol);

        assertFalse(result);
        verify(scopeHelper).checkIsNotDoubleDefinition(symbol, symbol);
        verify(globalNamespaceScope).getTypeSymbolWhichClashesWithUse(useDefinitionAst);
        verify(useDefinitionAst).isDefinedEarlierThan(classDefinitionAst);
        verify(errorReporter).determineAlreadyDefined(symbol, typeSymbol);
    }

    @Test
    public void useDefinitionCheck_IsNotDoubleDefinedAndTypeDefinedLaterInSameNamespace_ReturnFalse() {
        ITypeCheckerErrorReporter errorReporter = mock(ITypeCheckerErrorReporter.class);


        ITSPHPAst useDefinitionAst = mock(ITSPHPAst.class);
        IAliasSymbol symbol = createAliasSymbol("aliasName", useDefinitionAst);
        //not double defined
        when(scopeHelper.checkIsNotDoubleDefinition(symbol, symbol)).thenReturn(true);

        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope, errorReporter);
        ITSPHPAst classDefinitionAst = mock(ITSPHPAst.class);
        ITypeSymbol typeSymbol = createTypeSymbol(classDefinitionAst, namespaceScope);
        when(globalNamespaceScope.getTypeSymbolWhichClashesWithUse(useDefinitionAst)).thenReturn(typeSymbol);

        when(useDefinitionAst.isDefinedEarlierThan(classDefinitionAst)).thenReturn(true);

        //act
        namespaceScope.defineUse(symbol);
        boolean result = namespaceScope.useDefinitionCheck(symbol);

        assertFalse(result);
        verify(scopeHelper).checkIsNotDoubleDefinition(symbol, symbol);
        verify(globalNamespaceScope).getTypeSymbolWhichClashesWithUse(useDefinitionAst);
        verify(useDefinitionAst).isDefinedEarlierThan(classDefinitionAst);
        verify(typeSymbol).getDefinitionScope();
        verify(errorReporter).determineAlreadyDefined(symbol, typeSymbol);
    }

    @Test
    public void isFullyInitialised_Standard_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.isFullyInitialised(symbol);

        verify(globalNamespaceScope).isFullyInitialised(symbol);
    }

    @Test
    public void isPartiallyInitialised_Standard_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.isPartiallyInitialised(symbol);

        verify(globalNamespaceScope).isPartiallyInitialised(symbol);
    }

    @Test
    public void addToInitialisedSymbols_IsFullyInitialised_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.addToInitialisedSymbols(symbol, true);

        verify(globalNamespaceScope).addToInitialisedSymbols(symbol, true);
    }

    @Test
    public void addToInitialisedSymbols_IsPartiallyInitialised_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        ISymbol symbol = mock(ISymbol.class);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        namespaceScope.addToInitialisedSymbols(symbol, false);

        verify(globalNamespaceScope).addToInitialisedSymbols(symbol, false);
    }

    @Test
    public void getInitialisedSymbols_IsPartiallyInitialised_DelegateToEnclosingScope() {
        IGlobalNamespaceScope globalNamespaceScope = mock(IGlobalNamespaceScope.class);
        Map<String, Boolean> initialisedSymbols = new HashMap<>();
        when(globalNamespaceScope.getInitialisedSymbols()).thenReturn(initialisedSymbols);

        INamespaceScope namespaceScope = createNamespaceScope(globalNamespaceScope);
        Map<String, Boolean> result = namespaceScope.getInitialisedSymbols();

        assertThat(result, is(initialisedSymbols));
        verify(globalNamespaceScope).getInitialisedSymbols();
    }

    protected INamespaceScope createNamespaceScope() {
        return createNamespaceScope(mock(IGlobalNamespaceScope.class));
    }

    protected INamespaceScope createNamespaceScope(IGlobalNamespaceScope globalNamespaceScope) {
        return createNamespaceScope(globalNamespaceScope, mock(ITypeCheckerErrorReporter.class));
    }

    protected INamespaceScope createNamespaceScope(IGlobalNamespaceScope globalNamespaceScope,
            ITypeCheckerErrorReporter typeCheckerErrorReporter) {
        return new NamespaceScope(scopeHelper, "test", globalNamespaceScope, typeCheckerErrorReporter);
    }


    private ISymbol createSymbol(String name) {
        ISymbol symbol = mock(ISymbol.class);
        when(symbol.getName()).thenReturn(name);
        return symbol;
    }

    private ITypeSymbol createTypeSymbol(ITSPHPAst classDefinitionAst, IScope scope) {
        ITypeSymbol typeSymbol = mock(ITypeSymbol.class);
        when(typeSymbol.getDefinitionAst()).thenReturn(classDefinitionAst);
        when(typeSymbol.getDefinitionScope()).thenReturn(scope);
        return typeSymbol;
    }

    private IAliasSymbol createAliasSymbol(String name) {
        IAliasSymbol aliasSymbol = mock(IAliasSymbol.class);
        when(aliasSymbol.getName()).thenReturn(name);
        return aliasSymbol;
    }

    private IAliasSymbol createAliasSymbol(String name, ITSPHPAst ast) {
        IAliasSymbol aliasSymbol = createAliasSymbol(name);
        when(aliasSymbol.getDefinitionAst()).thenReturn(ast);
        return aliasSymbol;
    }


}