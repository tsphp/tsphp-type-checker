/*
 * This file is part of the TSPHP project published under the Apache License 2.0
 * For the full copyright and license information, please have a look at LICENSE in the
 * root folder or visit the project's website http://tsphp.ch/wiki/display/TSPHP/License
 */

package ch.tsphp.typechecker;

import ch.tsphp.common.ITSPHPAst;

import java.util.List;

public class CastingDto extends PromotionExplicitCastingLevelDto
{

    public ITSPHPAst actualParameter;
    public List<ICastingMethod> castingMethods;
    public List<CastingDto> ambiguousCasts;

    public CastingDto(int thePromotionCount, int theExplicitCastingCount) {
        this(thePromotionCount, theExplicitCastingCount, null, null);
    }

    public CastingDto(int thePromotionCount, int theExplicitCastingCount,
            List<ICastingMethod> theCastingMethods) {
        this(thePromotionCount, theExplicitCastingCount, theCastingMethods, null);
    }

    public CastingDto(int thePromotionCount, int theExplicitCastingCount,
            List<ICastingMethod> theCastingMethods, ITSPHPAst theActualParameter) {
        super(thePromotionCount, theExplicitCastingCount);
        actualParameter = theActualParameter;
        castingMethods = theCastingMethods;

    }
}
