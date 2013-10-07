package ch.tutteli.tsphp.typechecker.scopes;

import ch.tutteli.tsphp.common.ILowerCaseStringMap;
import ch.tutteli.tsphp.common.ISymbol;
import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.common.ITypeSymbol;
import ch.tutteli.tsphp.common.LowerCaseStringMap;
import ch.tutteli.tsphp.typechecker.error.ErrorReporterRegistry;
import ch.tutteli.tsphp.typechecker.symbols.IAliasSymbol;
import ch.tutteli.tsphp.typechecker.utils.MapHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NamespaceScope extends AScope implements INamespaceScope
{

    private ILowerCaseStringMap<List<IAliasSymbol>> usesCaseInsensitive = new LowerCaseStringMap<>();
    private Map<String, List<IAliasSymbol>> uses = new LinkedHashMap<>();

    public NamespaceScope(String scopeName, IGlobalNamespaceScope globalNamespaceScope) {
        super(scopeName, globalNamespaceScope);
    }

    @Override
    public void define(ISymbol symbol) {
        //we define symbols in the corresponding global namespace scope in order that it can be found from other
        //namespaces as well
        enclosingScope.define(symbol);
        //However, definition scope is this one, is used for alias resolving and name clashes
        symbol.setDefinitionScope(this);
    }

    @Override
    public boolean doubleDefinitionCheck(ISymbol symbol) {
        //check in global namespace scope, because they have been defined there
        return enclosingScope.doubleDefinitionCheck(symbol);
    }

    @Override
    public boolean doubleDefinitionCheckCaseInsensitive(ISymbol symbol) {
        //check in global namespace scope, because they have been defined there
        return ((ICaseInsensitiveScope) enclosingScope).doubleDefinitionCheckCaseInsensitive(symbol);
    }

    @Override
    public void defineUse(IAliasSymbol symbol) {
        MapHelper.addToListMap(usesCaseInsensitive, symbol.getName(), symbol);
        MapHelper.addToListMap(uses, symbol.getName(), symbol);
        symbol.setDefinitionScope(this);
    }

    @Override
    public boolean useDefinitionCheck(IAliasSymbol symbol) {
        boolean isNotDoubleDefined = ScopeHelperRegistry.get().doubleDefinitionCheck(
                usesCaseInsensitive.get(symbol.getName()).get(0), symbol);
        return isNotDoubleDefined && isNotAlreadyDefinedAsType(symbol);

    }

    private boolean isNotAlreadyDefinedAsType(IAliasSymbol symbol) {
        ITypeSymbol typeSymbol = (ITypeSymbol) resolve(symbol.getDefinitionAst());
        boolean ok = hasNoTypeNameClash(symbol.getDefinitionAst(), typeSymbol);
        if (!ok) {
            //noinspection ThrowableResultOfMethodCallIgnored
            ErrorReporterRegistry.get().determineAlreadyDefined(symbol, typeSymbol);
        }
        return ok;
    }

    private boolean hasNoTypeNameClash(ITSPHPAst useDefinition, ITypeSymbol typeSymbol) {
        boolean hasNoTypeNameClash = typeSymbol == null;
        if (!hasNoTypeNameClash) {
            boolean isUseDefinedEarlier = useDefinition.isDefinedEarlierThan(typeSymbol.getDefinitionAst());
            boolean isUseInDifferentNamespaceStatement = typeSymbol.getDefinitionScope() != this;

            //There is no type name clash in the following situation: namespace{use a as b;} namespace{ class b{}}
            //because: use is defined earlier and the use statement is in a different namespace statement
            hasNoTypeNameClash = isUseDefinedEarlier && isUseInDifferentNamespaceStatement;
        }
        return hasNoTypeNameClash;

    }

    @Override
    public ISymbol resolve(ITSPHPAst ast) {
        //we resolve from the corresponding global namespace scope 
        return enclosingScope.resolve(ast);
    }

    @Override
    public List<IAliasSymbol> getUse(String alias) {
        return uses.get(alias);
    }

    @Override
    public ITSPHPAst getCaseInsensitiveFirstUseDefinitionAst(String alias) {
        return usesCaseInsensitive.containsKey(alias) ? usesCaseInsensitive.get(alias).get(0).getDefinitionAst() : null;
    }
}