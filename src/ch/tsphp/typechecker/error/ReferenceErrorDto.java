/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker.error;

/**
 * Represents the meta-data of reference errors.
 */
public class ReferenceErrorDto
{

    public String identifier;
    public int line;
    public int position;

    public ReferenceErrorDto(String theIdentifier, int theLine, int thePosition) {
        identifier = theIdentifier;
        line = theLine;
        position = thePosition;

    }
}
