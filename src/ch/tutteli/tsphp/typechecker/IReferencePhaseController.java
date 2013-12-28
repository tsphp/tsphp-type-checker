package ch.tutteli.tsphp.typechecker;

import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.common.ITSPHPErrorAst;
import ch.tutteli.tsphp.common.ITypeSymbol;
import ch.tutteli.tsphp.typechecker.symbols.IScalarTypeSymbol;
import ch.tutteli.tsphp.typechecker.symbols.IVariableSymbol;
import ch.tutteli.tsphp.typechecker.symbols.erroneous.IErroneousTypeSymbol;

import java.util.List;

public interface IReferencePhaseController
{
    IVariableSymbol resolveConstant(ITSPHPAst ast);

    IVariableSymbol resolveThisSelf(ITSPHPAst $this);

    IVariableSymbol resolveParent(ITSPHPAst $this);

    IVariableSymbol resolveVariable(ITSPHPAst ast);

    IScalarTypeSymbol resolveScalarType(ITSPHPAst typeAst, boolean isNullable);

    ITypeSymbol resolvePrimitiveType(ITSPHPAst typeASt);

    /**
     * Try to resolve the type for the given typeAst and returns an
     * {@link ch.tutteli.tsphp.typechecker.symbols.erroneous.IErroneousTypeSymbol} if the type could not be found.
     *
     * @param typeAst The AST node which contains the type name. For instance, int, MyClass, \Exception etc.
     * @return The corresponding type or a {@link ch.tutteli.tsphp.typechecker.symbols.erroneous.IErroneousTypeSymbol}
     * if could not be found.
     */
    ITypeSymbol resolveType(ITSPHPAst typeAst);

    ITypeSymbol resolveUseType(ITSPHPAst typeAst, ITSPHPAst alias);

    boolean checkIsInterface(ITSPHPAst typeAst, ITypeSymbol symbol);

    boolean checkIsClass(ITSPHPAst typeAst, ITypeSymbol symbol);

    boolean checkVariableIsOkToUse(ITSPHPAst variableId);

    boolean checkIsNotForwardReference(ITSPHPAst ast);

    boolean checkIsNotOutOfConditionalScope(ITSPHPAst ast);

    boolean checkVariableIsInitialised(ITSPHPAst variableId);

    void sendUpInitialisedSymbols(ITSPHPAst blockConditional);

    void sendUpInitialisedSymbolsAfterIf(ITSPHPAst ifBlock, ITSPHPAst elseBlock);

    void sendUpInitialisedSymbolsAfterSwitch(List<ITSPHPAst> conditionalBlocks, boolean hasDefaultLabel);

    void sendUpInitialisedSymbolsAfterTryCatch(List<ITSPHPAst> conditionalBlocks);

    void checkReturnsFromFunction(boolean isReturning, boolean hasAtLeastOneReturnOrThrow, ITSPHPAst identifier);

    void checkReturnsFromMethod(boolean isReturning, boolean hasAtLeastOneReturnOrThrow, ITSPHPAst identifier);

    void checkBreakContinueLevel(ITSPHPAst root, ITSPHPAst level);

    IErroneousTypeSymbol createErroneousTypeSymbol(ITSPHPErrorAst typeAst);
}
