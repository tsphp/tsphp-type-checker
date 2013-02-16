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
tree grammar TSPHPTypeCheckerReference;
options {
	tokenVocab = TSPHP;
	ASTLabelType = ITSPHPAst;
	filter = true;
}

@header{
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
package ch.tutteli.tsphp.typechecker.antlr;

import ch.tutteli.tsphp.common.ITypeSymbol;
import ch.tutteli.tsphp.common.ITSPHPAst;
import ch.tutteli.tsphp.typechecker.ISymbolTable;
import ch.tutteli.tsphp.typechecker.scopes.INamespaceScope;
import ch.tutteli.tsphp.typechecker.scopes.ICaseInsensitiveScope;
import ch.tutteli.tsphp.typechecker.symbols.IAliasSymbol;
import ch.tutteli.tsphp.typechecker.symbols.IClassTypeSymbol;
import ch.tutteli.tsphp.typechecker.symbols.IInterfaceTypeSymbol;

}

@members {
ISymbolTable symbolTable;

public TSPHPTypeCheckerReference(TreeNodeStream input, ISymbolTable theSymbolTable) {
    this(input);
    symbolTable = theSymbolTable;
}
}

topdown
	:  	useDeclarationList
    	|	interfaceDeclaration
   	|	classDeclaration
   	|	constructDeclaration
   	|	methodFunctionDeclaration
   	|	constantDeclarationList
   	|	parameterDeclarationList
    	|	variableDeclarationList
    	;
   
    
useDeclarationList
	:	^('use'	useDeclaration+)
	;
	
useDeclaration
	:	^(USE_DECLRATARION type=TYPE_NAME alias=.)
		{
			ITypeSymbol typeSymbol = symbolTable.resolveUseType($type, $alias);
			type.setSymbol(typeSymbol);
			$alias.getSymbol().setType(typeSymbol);
			
			INamespaceScope namespaceScope = (INamespaceScope) $alias.getScope();
			namespaceScope.useDefinitionCheck((IAliasSymbol) $alias.getSymbol());
		}
	;

interfaceDeclaration
	:	^('interface' iMod=. identifier=Identifier extIds=extendsDeclaration .)
		{
			INamespaceScope namespaceScope = (INamespaceScope) $identifier.getScope();
			namespaceScope.definitionCheckCaseInsensitive($identifier.getSymbol());
		}
	;

extendsDeclaration	
	:	^('extends' allTypes+)
	|	'extends'
	;
	
classDeclaration
	:	^('class' cMod=. identifier=Identifier extIds=extendsDeclaration implIds=implementsDeclaration .) 
		{
			INamespaceScope namespaceScope = (INamespaceScope) $identifier.getScope();
			namespaceScope.definitionCheckCaseInsensitive($identifier.getSymbol());
		}
	;
	
implementsDeclaration
	:	^('implements' allTypes+)
	|	'implements'
	;

constructDeclaration
	:	^(identifier='__construct' .  ^(TYPE rtMod=. voidType) . .)
		{
			$identifier.getSymbol().setType($voidType.type); 
			ICaseInsensitiveScope scope = (ICaseInsensitiveScope) $identifier.getScope();
			scope.definitionCheckCaseInsensitive($identifier.getSymbol());
		}
	;
		
methodFunctionDeclaration
	:	^( 	(	METHOD_DECLARATION
			|	Function
			) 
			. ^(TYPE rtMod=. returnTypes) identifier=. . .
		)
		{
			$identifier.getSymbol().setType($returnTypes.type); 
			ICaseInsensitiveScope scope = (ICaseInsensitiveScope) $identifier.getScope();
			scope.definitionCheckCaseInsensitive($identifier.getSymbol());
		}
	;
	
constantDeclarationList
	:	^(CONSTANT_DECLARATION_LIST ^(TYPE tMod=. scalarTypes) constantDeclaration[$scalarTypes.type]+)
	;

constantDeclaration[ITypeSymbol type]
	:	^(identifier=Identifier .)
		{ 
			$identifier.getSymbol().setType(type); 
			$identifier.getScope().definitionCheck($identifier.getSymbol()); 
		}
	;

parameterDeclarationList
	:	^(PARAMETER_LIST parameterDeclaration+)
	;

parameterDeclaration
	:	^(PARAMETER_DECLARATION 
			^(TYPE tMod=. allTypes) variableDeclaration[$allTypes.type]
		)
	;

variableDeclarationList 
	:	^(VARIABLE_DECLARATION_LIST ^(TYPE tMod=. allTypes) variableDeclaration[$allTypes.type]+ )
        ;
        
variableDeclaration[ITypeSymbol type]
	:
		(	^(variableId=VariableId .)
		|	variableId=VariableId	
		)
		{ 
			$variableId.getSymbol().setType(type); 
			$variableId.getScope().definitionCheck($variableId.getSymbol());
		}
	;

returnTypes returns [ITypeSymbol type]
	:	allTypes {$type = $allTypes.type;}
	|	voidType {$type = $voidType.type;}
	;
	
voidType returns [ITypeSymbol type]
 	:	'void'
		{
			$type = symbolTable.resolvePrimitiveType($start);
			$start.setSymbol($type);
		}
 	;
allTypes returns [ITypeSymbol type]
	:	(	'bool'
		|	'int'
		|	'float'
		|	'string'
		|	'array'
		|	'object'
		|	'resource'
		)
		{
			$type = symbolTable.resolvePrimitiveType($start);
			$start.setSymbol($type);
		}
	|	TYPE_NAME
		{
			$type = symbolTable.resolveType($start);
			$start.setSymbol($type);
		}
	;
	
scalarTypes returns [ITypeSymbol type]
	:	(	'bool'
		|	'int'
		|	'float'
		|	'string'
		)
		{
			$type = symbolTable.resolvePrimitiveType($start);
			$start.setSymbol($type);
		}
	;

