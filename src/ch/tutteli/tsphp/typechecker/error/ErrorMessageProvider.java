package ch.tutteli.tsphp.typechecker.error;

import java.util.Arrays;
import java.util.HashMap;

public class ErrorMessageProvider extends AErrorMessageProvider
{

    @Override
    protected void loadDefinitionErrorMessages() {
        definitionErrors = new HashMap<>();
        definitionErrors.put("alreadyDefined", "Line %lineN%|%posN% - %idN% was already defined on "
                + "line %line%|%pos% %id%");
        definitionErrors.put("definedInOuterScope",
                "Line %lineN%|%posN% - %idN% was either already defined in outer scope or in another conditional scope."
                        + " First definition was on line %line%|%pos% %id%\n"
                        + "Please be aware, that conditional scopes are not real scopes, they do not actually create a new "
                        + "scope");
        definitionErrors.put("aliasForwardReference",
                "Line %lineN%|%posN% - alias %idN% is used before its use declaration. Corresponding use declaration is"
                        + " on line %line%|%pos%");
        definitionErrors.put("forwardReference",
                "Line %lineN%|%posN% - %idN% is used before its declaration. Corresponding declaration is "
                        + "on line %line%|%pos%");
        definitionErrors.put("methodNotDefined", "Line %lineN%|%posN% - method %idN% is not defined in %id%");
        definitionErrors.put("memberNotDefined", "Line %lineN%|%posN% - class member %idN% is not defined in %id%");

        definitionErrors.put("variableDefinedInOtherConditionalScope", "Line %lineN%|%posN% - variable %idN% is "
                + "defined in another conditional scope which is not an outer scope of the current conditional scope.\n"
                + "The definition was on line %line%|%pos%");
        definitionErrors.put("variableDefinedInConditionalScope", "Line %lineN%|%posN% - variable %idN% is defined in "
                + "a nested conditional scope and can therefore not be used in the current (outer) scope.\n"
                + "The definition was on line %line%|%pos% ");
    }

    @Override
    protected void loadReferenceErrorMessages() {
        referenceErrors = new HashMap<>();
        referenceErrors.put("unknownType", "Line %line%|%pos% - The type \"%id%\" could not be resolved.");
        referenceErrors.put("interfaceExpected", "Line %line%|%pos% - Interface expected, "
                + "\"%id%\" is not an interface.");
        referenceErrors.put("classExpected", "Line %line%|%pos% - class expected, \"%id%\" is not a class.");
        referenceErrors.put("variableExpected", "Line %line%|%pos% - assignments can only be made to variables,"
                + " \"%id%\" is not a variable.");
        referenceErrors.put("notInClass", "Line %line%|%pos% - %id% is used outside a class.");

        referenceErrors.put("notInMethod", "Line %line%|%pos% - %id% is used outside a method or function.");

        referenceErrors.put("noParentClass", "Line %line%|%pos% - class %id% has no parent class.");
        referenceErrors.put("notDefined", "Line %line%|%pos% - %id% was never defined.");
        referenceErrors.put("notStatic", "Line %line%|%pos% - %id% is not static.");
        referenceErrors.put("toManyBreakContinueLevels", "Line %line%|%pos% - cannot %id% so many levels.");
    }

    @Override
    protected void loadWrongArgumentTypeErrorMessages() {
        wrongArgumentTypeErrors = new HashMap<>();
        wrongArgumentTypeErrors.put("wrongOperatorUsage", "Line %line%|%pos% - usage of operator %id% is wrong.\n"
                + "It cannot be applied to the given types for LHS/RHS: %aParams%\n"
                + "existing overloads: %overloads%");
        wrongArgumentTypeErrors.put("ambiguousOperatorUsage", "Line %line%|%pos% - usage of operator %id% is ambiguous."
                + "\ntypes LHS/RHS: %aParams%\n"
                + "ambiguous overloads: %overloads%");
        wrongArgumentTypeErrors.put("wrongFunctionCall", "Line %line%|%pos% - no applicable overload found for the "
                + "function %id%.\n"
                + "Given argument types: %aParams%\n"
                + "existing overloads: %overloads%");
        wrongArgumentTypeErrors.put("wrongMethodCall", "Line %line%|%pos% - no applicable overload found for the "
                + "method %id%.\n"
                + "Given argument types: %aParams%\n"
                + "existing overloads: %overloads%");
    }

    @Override
    protected void loadTypeCheckErrorMessages() {
        typeCheckErrors = new HashMap<>();
        typeCheckErrors.put("equalityOperator", "Line %line%|%pos% - usage of operator %id% is wrong.\n"
                + "LHS/RHS cannot be compared because they are not from the same type, non of them is a sub-type of "
                + "the other and there does not exists an explicit cast from one type to the other.\n"
                + "The following types where found: [%tExp%, %tFound%]");
        typeCheckErrors.put("wrongCast", "Line %line%|%pos% - cannot cast %tFound% to %tExp%");
        typeCheckErrors.put("identityOperator", "Line %line%|%pos% - usage of operator %id% is wrong.\n"
                + "LHS/RHS have to be from the same type or one has to be a sub-type of the other.\n"
                + "The following types where found: [%tExp%, %tFound%]");
        typeCheckErrors.put("wrongAssignment", "Line %line%|%pos% - cannot assign RHS to LHS using the operator %id% "
                + "types are not compatible.\n"
                + "types LHS/RHS: [%tExp%, %tFound%]");
        typeCheckErrors.put("notSameOrParentType", "Line %line%|%pos% - %id% has a wrong type. "
                + "Type %tExp% or a parent-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeIf", "Line %line%|%pos% - the condition in the if statement "
                + "evaluates to a wrong type. Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeSwitch", "Line %line%|%pos% - switch cannot be used for the given "
                + "type %tFound%. %tExp% or one of its sub-types can be used.");
        typeCheckErrors.put("wrongTypeSwitchCase", "Line %line%|%pos% - The type of the switch case does not "
                + "correspond to the type specified in the switch header. "
                + "Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeFor", "Line %line%|%pos% - the condition in the for statement "
                + "evaluates to a wrong type. Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeForeach", "Line %line%|%pos% - the given expression in the foreach statement "
                + "evaluates to a wrong type. Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeWhile", "Line %line%|%pos% - the condition in the while statement "
                + "evaluates to a wrong type. Type %tExp% or a sub-type expected but %tFound% found.");
        typeCheckErrors.put("wrongTypeDoWhile", "Line %line%|%pos% - the condition in the do-while statement "
                + "evaluates to a wrong type. Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeThrow", "Line %line%|%pos% - the expression of the throw statement "
                + "evaluates to a wrong type. Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeCatch", "Line %line%|%pos% - Only %tExp% or a sub-type can be caugth with a "
                + "catch statement but %tFound% found.");

        typeCheckErrors.put("arrayExpected", "Line %line%|%pos% - the array access operator [] expects the \n"
                + "type %tExp% or a sub-type on its LHS but the corresponding expression evaluates to %tFound%.");

        typeCheckErrors.put("noReturnValueExpected", "Line %line%|%pos% - the enclosing method has not specified a "
                + "return value but a value of type %tFound% was found.");

        typeCheckErrors.put("returnValueExpected", "Line %line%|%pos% - the enclosing method has specified a return "
                + "value but none was given.");

        typeCheckErrors.put("wrongTypeReturn", "Line %line%|%pos% - the return statement evaluates to wrong type. "
                + "Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeTernaryCondition", "Line %line%|%pos% - the condition of the operator ? "
                + "(the ternary operator) evaluates to wrong type. "
                + "Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeTernaryCondition", "Line %line%|%pos% - the condition of the operator ? "
                + "(the ternary operator) evaluates to wrong type. "
                + "Type %tExp% or a sub-type expected but %tFound% found.");

        typeCheckErrors.put("onlySingleValue", "Line %line%|%pos% - expressions are not allowed at this point. Only "
                + "a single constant value of type %tExp% can be used (type %tFound% found).");

        typeCheckErrors.put("onlyConstantValue", "Line %line%|%pos% - Only a constant value of type %tExp% is allowed "
                + "at this point. Non constant value of type %tFound% found.");

        typeCheckErrors.put("wrongTypeMethodCall", "Line %line%|%pos% - the callee evaluates to "
                + "a wrong type. Class-/Interface-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeClone", "Line %line%|%pos% - the clone statement evaluates to "
                + "a wrong type. Class-/Interface-type expected but %tFound% found.");

        typeCheckErrors.put("wrongTypeInstanceof", "Line %line%|%pos% - %id% evaluates to "
                + "a wrong type. The instanceof operator expects a class-/Interface-type but %tFound% found.");

        typeCheckErrors.put("wrongTypeClassMemberAccess", "Line %line%|%pos% - %id% evaluates to "
                + "a wrong type. Can only retrieve class members from class-types but %tFound% found.");
    }

    @Override
    protected void loadAmbiguousCastsErrorMessages() {
        ambiguousCastsErrors = new HashMap<>();
        ambiguousCastsErrors.put("operatorBothSideCast", "Line %line%|%pos% - ambiguous cast detected in conjunction "
                + "with the operator %id%. LHS can be casted to RHS and RHS can be casted to LHS.\n"
                + "cast LHS to RHS: %LHS%\n"
                + "cast RHS to LHS: %RHS%");
        ambiguousCastsErrors.put("operatorAmbiguousCasts", "Line %line%|%pos% - ambiguous cast detected in conjunction "
                + "with the operator %id%. LHS can be casted to RHS and RHS can be casted to LHS.\n"
                + "cast LHS to RHS: %LHS%\n"
                + "cast RHS to LHS: %RHS%\n"
                + "Further ambiguities:\n"
                + "ambiguous casts LHS to RHS: %ambLHS%\n"
                + "ambiguous casts RHS to LHS: %ambRHS%");

        ambiguousCastsErrors.put("ambiguousCasts", "Line %line%|%pos% - cast from %RHS% to %LHS% is "
                + "ambiguous.\n"
                + "The following ambiguous casts were found:\n"
                + "%ambRHS%");
    }

    @Override
    protected void loadVisibilityViolationErrorMessages() {
        visibilityViolationErrors = new HashMap<>();
        visibilityViolationErrors.put("classMemberAccess", "Line %line%|%pos% - cannot access the class member %id%.\n"
                + "%id%'s visibility is %vis% and it would need at least %access% access in order that you can "
                + "access it from this location.");
        visibilityViolationErrors.put("staticClassMemberAccess", "Line %line%|%pos% - cannot access the static class "
                + "member %id%.\n"
                + "%id%'s visibility is %vis% and it would need at least %access% access in order that you can "
                + "access it from this location.");
        visibilityViolationErrors.put("classConstantAccess", "Line %line%|%pos% - cannot access the class "
                + "constant %id%.\n"
                + "%id%'s visibility is %vis% and it would need at least %access% access in order that you can "
                + "access it from this location.");
        visibilityViolationErrors.put("methodCall", "Line %line%|%pos% - cannot call the method %id%.\n"
                + "%id%'s visibility is %vis% and it would need at least %access% access in order that you can "
                + "call it from this location.");
    }

    @Override
    protected String getStandardDefinitionErrorMessage(String key, DefinitionErrorDto dto) {
        return "DefinitionException occurred, corresponding error message for \"" + key + "\" not defined. "
                + "Please report bug to http://tsphp.tutteli.ch\n"
                + "However, the following information was gathered.\n"
                + "Line " + dto.line + "|" + dto.position + " - " + dto.identifier + " was already defined on line "
                + dto.lineNewDefinition + "|" + dto.positionNewDefinition + ".";
    }

    @Override
    protected String getStandardReferenceErrorMessage(String key, ReferenceErrorDto dto) {
        return "ReferenceException occurred, corresponding error message for \"" + key + "\" is not defined. "
                + "Please report bug to http://tsphp.tutteli.ch\n"
                + "However, the following information was gathered.\n"
                + "Line " + dto.line + "|" + dto.position + " - " + dto.identifier + " could not been resolved to its "
                + "corresponding reference.";
    }

    @Override
    protected String getStandardWrongArgumentTypeErrorMessage(String key, WrongArgumentTypeErrorDto dto) {
        return "WrongArgumentTypeException occurred, corresponding error message for \"" + key + "\" is not defined. "
                + "Please report bug to http://tsphp.tutteli.ch\n"
                + "However, the following information was gathered.\n"
                + "Line " + dto.line + "|" + dto.position + " - usage of " + dto.identifier + " was wrong.\n"
                + "types actual parameters: " + Arrays.toString(dto.actualParameterTypes) + "\n"
                + "existing overloads: " + getOverloadSignatures(dto.possibleOverloads);
    }

    @Override
    protected String getStandardTypeCheckErrorMessage(String key, TypeCheckErrorDto dto) {
        return "TypeCheckException occurred, corresponding error message for \"" + key + "\" is not defined. "
                + "Please report bug to http://tsphp.tutteli.ch/jira\n"
                + "However, the following information was gathered.\n"
                + "Line " + dto.line + "|" + dto.position + " - usage of " + dto.identifier + " was wrong.\n"
                + "type expected: " + dto.typeExpected + "\n"
                + "type found:: " + dto.typeFound;
    }

    @Override
    protected String getStandardAmbiguousCastsErrorMessage(String key, AmbiguousCastsErrorDto dto) {
        return "AmbiguousCastsException occurred, corresponding error message for \"" + key + "\" is not defined. "
                + "Please report bug to http://tsphp.tutteli.ch/jira\n"
                + "However, the following information was gathered.\n"
                + "Line " + dto.line + "|" + dto.position + " - usage of " + dto.identifier + " was wrong.\n"
                + "casts LHS to RHS or LHS type: " + getCastsSequence(dto.leftToRightCasts) + "\n"
                + "casts RHS to LHS or RHS type: " + getCastsSequence(dto.rightToLeftCasts) + "\n"
                + "ambiguous casts LHS to RHS:" + getAmbiguousCastsSequences(dto.leftAmbiguities) + "\n"
                + "ambiguous casts RHS to LHS:" + getAmbiguousCastsSequences(dto.rightAmbiguities);
    }

    @Override
    protected String getStandardVisibilityViolationErrorMessage(String key, VisibilityErrorDto dto) {
        return "VisibilityViolationException occurred, corresponding error message for \"" + key + "\" is not defined. "
                + "Please report bug to http://tsphp.tutteli.ch/jira\n"
                + "However, the following information was gathered.\n"
                + "Line " + dto.line + "|" + dto.position + " - cannot access " + dto.identifier + ".\n"
                + dto.identifier + "'s visibility is " + dto.visibility + " and it would need at least "
                + dto.accessedFrom + " access in order that you can access it from this location.";
    }
}
