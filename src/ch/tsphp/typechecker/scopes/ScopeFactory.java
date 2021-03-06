/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.scopes;

import ch.tsphp.common.IScope;
import ch.tsphp.typechecker.error.ITypeCheckerErrorReporter;

public class ScopeFactory implements IScopeFactory
{
    private final IScopeHelper scopeHelper;
    private final ITypeCheckerErrorReporter typeCheckerErrorReporter;

    public ScopeFactory(IScopeHelper theScopeHelper, ITypeCheckerErrorReporter theTypeCheckerErrorReporter) {
        scopeHelper = theScopeHelper;
        typeCheckerErrorReporter = theTypeCheckerErrorReporter;
    }

    @Override
    public IGlobalNamespaceScope createGlobalNamespaceScope(String name) {
        return new GlobalNamespaceScope(scopeHelper, name);
    }

    @Override
    public INamespaceScope createNamespaceScope(String name, IGlobalNamespaceScope currentScope) {
        return new NamespaceScope(scopeHelper, name, currentScope, typeCheckerErrorReporter);
    }

    @Override
    public IConditionalScope createConditionalScope(IScope currentScope) {
        return new ConditionalScope(scopeHelper, currentScope, typeCheckerErrorReporter);
    }
}
