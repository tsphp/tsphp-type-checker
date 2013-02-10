/*
 * Copyright 2012 Robert Stoll <rstoll@tutteli.ch>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ch.tutteli.tsphp.typechecker.scopes;

import ch.tutteli.tsphp.common.IScope;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Stoll <rstoll@tutteli.ch>
 */
public class ScopeFactory implements IScopeFactory
{

    private IScope globalScope = new GlobalScope();
    private Map<String, INamespaceScope> namespaces = new HashMap<>();

    @Override
    public IScope getGlobalScope() {
        return globalScope;
    }

    @Override
    public INamespaceScope createNamespace(String name, IScope currentScope) {
        INamespaceScope scope;
        if (namespaces.containsKey(name)) {
            scope = namespaces.get(name);
        } else {
            scope = !name.equals(IScope.DEFAULT_NAMESPACE)
                    ? new NamespaceScope(name, currentScope)
                    : new DefaultNamespaceScope(currentScope);
            namespaces.put(name, scope);
        }
        return scope;
    }

    @Override
    public IConditionalScope createConditionalScope(IScope currentScope) {
        return new ConditionalScope(currentScope);
    }
}
