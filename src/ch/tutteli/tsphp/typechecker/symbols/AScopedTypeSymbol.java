package ch.tutteli.tsphp.typechecker.symbols;

import ch.tutteli.tsphp.common.AstHelperRegistry;
import ch.tutteli.tsphp.common.ILowerCaseStringMap;
import ch.tutteli.tsphp.common.IScope;
import ch.tutteli.tsphp.common.ISymbol;
import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.common.ITypeSymbol;
import ch.tutteli.tsphp.common.LowerCaseStringMap;
import ch.tutteli.tsphp.typechecker.antlr.TSPHPDefinitionWalker;
import ch.tutteli.tsphp.typechecker.scopes.ICaseInsensitiveScope;
import ch.tutteli.tsphp.typechecker.scopes.IScopeHelper;
import ch.tutteli.tsphp.typechecker.utils.MapHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AScopedTypeSymbol extends AScopedSymbol implements ICaseInsensitiveScope, IPolymorphicTypeSymbol
{

    protected Set<ITypeSymbol> parentTypeSymbols = new HashSet<>();
    protected final ILowerCaseStringMap<List<ISymbol>> symbolsCaseInsensitive = new LowerCaseStringMap<>();
    private boolean isObjectTheParentTypeSymbol = false;

    public AScopedTypeSymbol(
            IScopeHelper scopeHelper,
            ITSPHPAst definitionAst,
            Set<Integer> modifiers,
            String name,
            IScope enclosingScope,
            ITypeSymbol theParentTypeSymbol) {
        super(scopeHelper, definitionAst, modifiers, name, enclosingScope);
        parentTypeSymbols.add(theParentTypeSymbol);
        isObjectTheParentTypeSymbol = theParentTypeSymbol.getName().equals("object");
    }

    @Override
    public void define(ISymbol symbol) {
        super.define(symbol);
        MapHelper.addToListMap(symbolsCaseInsensitive, symbol.getName(), symbol);
    }

    @Override
    public boolean doubleDefinitionCheckCaseInsensitive(ISymbol symbol) {
        return scopeHelper.checkIsNotDoubleDefinition(symbolsCaseInsensitive, symbol);
    }

    @Override
    public ISymbol resolveWithFallbackToParent(ITSPHPAst ast) {
        ISymbol symbol = scopeHelper.resolve(this, ast);
        if (symbol == null && !parentTypeSymbols.isEmpty()) {
            for (ITypeSymbol parentTypeSymbol : parentTypeSymbols) {
                if (parentTypeSymbol instanceof IPolymorphicTypeSymbol) {
                    symbol = ((IPolymorphicTypeSymbol) parentTypeSymbol).resolveWithFallbackToParent(ast);
                    if (symbol != null) {
                        break;
                    }
                }
            }
        }
        return symbol;
    }

    @Override
    public Set<ITypeSymbol> getParentTypeSymbols() {
        return parentTypeSymbols;
    }

    @Override
    public void addParentTypeSymbol(IPolymorphicTypeSymbol aParent) {
        if (isObjectTheParentTypeSymbol) {
            parentTypeSymbols = new HashSet<>();
            isObjectTheParentTypeSymbol = false;
        }
        parentTypeSymbols.add(aParent);
    }

    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public ITSPHPAst getDefaultValue() {
        return AstHelperRegistry.get().createAst(TSPHPDefinitionWalker.Null, "null");
    }

    @Override
    public boolean isFullyInitialised(ISymbol symbol) {
        //all symbols in a scoped type symbol are implicitly initialised
        return true;
    }

    @Override
    public boolean isPartiallyInitialised(ISymbol symbol) {
        //all symbols in a scoped type symbol are implicitly initialised
        return false;
    }
}
