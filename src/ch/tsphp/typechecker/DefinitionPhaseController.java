/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker;

import ch.tsphp.common.ILowerCaseStringMap;
import ch.tsphp.common.IScope;
import ch.tsphp.common.ISymbol;
import ch.tsphp.common.ITSPHPAst;
import ch.tsphp.common.LowerCaseStringMap;
import ch.tsphp.typechecker.scopes.IConditionalScope;
import ch.tsphp.typechecker.scopes.IGlobalNamespaceScope;
import ch.tsphp.typechecker.scopes.INamespaceScope;
import ch.tsphp.typechecker.scopes.IScopeFactory;
import ch.tsphp.typechecker.symbols.IAliasSymbol;
import ch.tsphp.typechecker.symbols.IClassTypeSymbol;
import ch.tsphp.typechecker.symbols.IInterfaceTypeSymbol;
import ch.tsphp.typechecker.symbols.IMethodSymbol;
import ch.tsphp.typechecker.symbols.ISymbolFactory;
import ch.tsphp.typechecker.symbols.IVariableSymbol;

public class DefinitionPhaseController implements IDefinitionPhaseController
{

    private final ISymbolFactory symbolFactory;
    private final IScopeFactory scopeFactory;

    private final ILowerCaseStringMap<IGlobalNamespaceScope> globalNamespaceScopes = new LowerCaseStringMap<>();
    private final IGlobalNamespaceScope globalDefaultNamespace;

    public DefinitionPhaseController(ISymbolFactory aSymbolFactory, IScopeFactory aScopeFactory) {
        symbolFactory = aSymbolFactory;
        scopeFactory = aScopeFactory;
        globalDefaultNamespace = getOrCreateGlobalNamespace("\\");

    }

    @Override
    public ILowerCaseStringMap<IGlobalNamespaceScope> getGlobalNamespaceScopes() {
        return globalNamespaceScopes;
    }

    @Override
    public IGlobalNamespaceScope getGlobalDefaultNamespace() {
        return globalDefaultNamespace;
    }

    @Override
    public INamespaceScope defineNamespace(String name) {
        return scopeFactory.createNamespaceScope(name, getOrCreateGlobalNamespace(name));
    }

    private IGlobalNamespaceScope getOrCreateGlobalNamespace(String name) {
        IGlobalNamespaceScope scope;
        if (globalNamespaceScopes.containsKey(name)) {
            scope = globalNamespaceScopes.get(name);
        } else {
            scope = scopeFactory.createGlobalNamespaceScope(name);
            globalNamespaceScopes.put(name, scope);
        }
        return scope;
    }

    @Override
    public void defineUse(INamespaceScope currentScope, ITSPHPAst type, ITSPHPAst alias) {
        type.setScope(currentScope);
        IAliasSymbol aliasSymbol = symbolFactory.createAliasSymbol(alias, alias.getText());
        alias.setSymbol(aliasSymbol);
        alias.setScope(currentScope);
        currentScope.defineUse(aliasSymbol);
    }

    @Override
    public void defineConstant(IScope currentScope, ITSPHPAst modifier, ITSPHPAst type, ITSPHPAst identifier) {
        defineVariable(currentScope, modifier, type, identifier);
    }


    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public IInterfaceTypeSymbol defineInterface(IScope currentScope, ITSPHPAst modifier, ITSPHPAst identifier,
            ITSPHPAst extendsIds) {
        assignScopeToIdentifiers(currentScope, extendsIds);

        IInterfaceTypeSymbol interfaceSymbol = symbolFactory.createInterfaceTypeSymbol(modifier,
                identifier, currentScope);

        define(currentScope, identifier, interfaceSymbol);
        return interfaceSymbol;
    }

    private void assignScopeToIdentifiers(IScope currentScope, ITSPHPAst identifierList) {
        int length = identifierList.getChildCount();
        for (int i = 0; i < length; ++i) {
            ITSPHPAst ast = identifierList.getChild(i);
            ast.setScope(currentScope);
        }
    }

    private void define(IScope currentScope, ITSPHPAst identifier, ISymbol symbol) {
        identifier.setSymbol(symbol);
        identifier.setScope(currentScope);
        currentScope.define(symbol);
    }

    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public IClassTypeSymbol defineClass(IScope currentScope, ITSPHPAst modifier, ITSPHPAst identifier,
            ITSPHPAst extendsIds, ITSPHPAst implementsIds) {
        assignScopeToIdentifiers(currentScope, extendsIds);
        assignScopeToIdentifiers(currentScope, implementsIds);
        IClassTypeSymbol classSymbol = symbolFactory.createClassTypeSymbol(modifier, identifier, currentScope);
        define(currentScope, identifier, classSymbol);
        return classSymbol;
    }

    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public IMethodSymbol defineConstruct(IScope currentScope, ITSPHPAst methodModifier,
            ITSPHPAst returnTypeModifier, ITSPHPAst returnType, ITSPHPAst identifier) {

        IMethodSymbol methodSymbol = defineMethod(currentScope, methodModifier,
                returnTypeModifier, returnType, identifier);

        ((IClassTypeSymbol) currentScope).setConstruct(methodSymbol);
        return methodSymbol;
    }

    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public IMethodSymbol defineMethod(IScope currentScope, ITSPHPAst methodModifier,
            ITSPHPAst returnTypeModifier, ITSPHPAst returnType, ITSPHPAst identifier) {
        returnType.setScope(currentScope);

        IMethodSymbol methodSymbol = symbolFactory.createMethodSymbol(methodModifier,
                returnTypeModifier, identifier, currentScope);

        define(currentScope, identifier, methodSymbol);
        return methodSymbol;
    }

    @Override
    public IConditionalScope defineConditionalScope(IScope currentScope) {
        return scopeFactory.createConditionalScope(currentScope);
    }

    @Override
    public void defineVariable(IScope currentScope, ITSPHPAst modifier, ITSPHPAst type, ITSPHPAst variableId) {
        type.setScope(currentScope);
        IVariableSymbol variableSymbol = symbolFactory.createVariableSymbol(modifier, variableId);
        define(currentScope, variableId, variableSymbol);
    }
}
