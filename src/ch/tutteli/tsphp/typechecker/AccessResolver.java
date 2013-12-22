package ch.tutteli.tsphp.typechecker;

import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.common.ITypeSymbol;
import ch.tutteli.tsphp.common.exceptions.DefinitionException;
import ch.tutteli.tsphp.common.exceptions.ReferenceException;
import ch.tutteli.tsphp.typechecker.antlr.TSPHPDefinitionWalker;
import ch.tutteli.tsphp.typechecker.error.TypeCheckErrorReporterRegistry;
import ch.tutteli.tsphp.typechecker.symbols.IPolymorphicTypeSymbol;
import ch.tutteli.tsphp.typechecker.symbols.ISymbolFactory;
import ch.tutteli.tsphp.typechecker.symbols.ISymbolWithAccessModifier;
import ch.tutteli.tsphp.typechecker.symbols.IVariableSymbol;
import ch.tutteli.tsphp.typechecker.symbols.erroneous.IErroneousSymbol;

public class AccessResolver implements IAccessResolver
{

    private final ISymbolFactory symbolFactory;

    public AccessResolver(ISymbolFactory theSymbolFactory) {
        symbolFactory = theSymbolFactory;
    }

    @Override
    public IVariableSymbol resolveClassConstantAccess(ITSPHPAst accessor, ITSPHPAst identifier) {
        return resolveStaticClassMemberOrClassConstant(accessor, identifier, new IViolationCaller()
        {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            @Override
            public void callAppropriateMethod(ITSPHPAst identifier, ISymbolWithAccessModifier symbol, int accessFrom) {
                TypeCheckErrorReporterRegistry.get().visibilityViolationClassConstantAccess(
                        identifier, symbol, accessFrom);
            }
        });
    }

    @Override
    public IVariableSymbol resolveStaticMemberAccess(ITSPHPAst accessor, ITSPHPAst identifier) {
        return resolveStaticClassMemberOrClassConstant(accessor, identifier, new IViolationCaller()
        {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            @Override
            public void callAppropriateMethod(ITSPHPAst identifier, ISymbolWithAccessModifier symbol, int accessFrom) {
                TypeCheckErrorReporterRegistry.get().visibilityViolationStaticClassMemberAccess(
                        identifier, symbol, accessFrom);
            }
        });
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private IVariableSymbol resolveStaticClassMemberOrClassConstant(ITSPHPAst accessor, ITSPHPAst id,
            IViolationCaller caller) {
        IVariableSymbol variableSymbol = checkAccessorAndResolveAccess(accessor, id, caller);
        if (!variableSymbol.isStatic()) {
            TypeCheckErrorReporterRegistry.get().notStatic(accessor);
        }
        return variableSymbol;
    }

    @Override
    public IVariableSymbol resolveClassMemberAccess(ITSPHPAst expression, ITSPHPAst identifier) {
        String variableName = identifier.getText();
        identifier.setText("$" + variableName);
        IViolationCaller visibilityViolationCaller = new IViolationCaller()
        {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            @Override
            public void callAppropriateMethod(ITSPHPAst identifier, ISymbolWithAccessModifier symbol, int accessFrom) {
                TypeCheckErrorReporterRegistry.get().visibilityViolationClassMemberAccess(
                        identifier, symbol, accessFrom);
            }
        };
        IVariableSymbol variableSymbol = checkAccessorAndResolveAccess(
                expression, identifier, visibilityViolationCaller);

        identifier.setText(variableName);
        return variableSymbol;
    }

    private IVariableSymbol checkAccessorAndResolveAccess(
            ITSPHPAst accessor, ITSPHPAst identifier, IViolationCaller visibilityViolationCaller) {

        IVariableSymbol variableSymbol;
        ITypeSymbol evalType = accessor.getEvalType();
        if (!(evalType instanceof IErroneousSymbol)) {
            if (evalType instanceof IPolymorphicTypeSymbol) {
                variableSymbol = resolveAccess(
                        (IPolymorphicTypeSymbol) evalType, accessor, identifier, visibilityViolationCaller);
            } else {
                ReferenceException exception = TypeCheckErrorReporterRegistry.get().wrongTypeClassMemberAccess
                        (identifier);
                variableSymbol = symbolFactory.createErroneousVariableSymbol(identifier, exception);
                variableSymbol.setType(symbolFactory.createErroneousTypeSymbol(identifier, exception));
            }
        } else {
            IErroneousSymbol erroneousSymbol = (IErroneousSymbol) evalType;
            variableSymbol = symbolFactory.createErroneousVariableSymbol(accessor, erroneousSymbol.getException());
            variableSymbol.setType(evalType);
        }
        return variableSymbol;
    }

    private IVariableSymbol resolveAccess(IPolymorphicTypeSymbol polymorphicTypeSymbol,
            ITSPHPAst accessor, ITSPHPAst identifier, IViolationCaller visibilityViolationCaller) {

        IVariableSymbol symbol = (IVariableSymbol) polymorphicTypeSymbol.resolveWithFallbackToParent(identifier);

        if (symbol != null) {
            checkVisibility(symbol, polymorphicTypeSymbol, visibilityViolationCaller, accessor, identifier);
        } else {
            DefinitionException exception = TypeCheckErrorReporterRegistry.get().memberNotDefined(accessor, identifier);
            symbol = symbolFactory.createErroneousVariableSymbol(identifier, exception);
        }
        return symbol;
    }

    @Override
    public void checkVisibility(ISymbolWithAccessModifier methodSymbol, IPolymorphicTypeSymbol polymorphicTypeSymbol,
            IViolationCaller visibilityViolationCaller, ITSPHPAst calleeOrAccessor, ITSPHPAst identifier) {

        int accessedFrom;

        String calleeOrAccessorText = calleeOrAccessor.getText();
        switch (calleeOrAccessorText) {
            case "$this":
            case "self":
                accessedFrom = methodSymbol.getDefinitionScope() == polymorphicTypeSymbol
                        ? TSPHPDefinitionWalker.Private
                        : TSPHPDefinitionWalker.Protected;

                break;
            case "parent":
                accessedFrom = TSPHPDefinitionWalker.Protected;
                break;
            default:
                accessedFrom = TSPHPDefinitionWalker.Public;
                break;
        }

        if (!methodSymbol.canBeAccessedFrom(accessedFrom)) {
            visibilityViolationCaller.callAppropriateMethod(identifier, methodSymbol, accessedFrom);
        }
    }
}