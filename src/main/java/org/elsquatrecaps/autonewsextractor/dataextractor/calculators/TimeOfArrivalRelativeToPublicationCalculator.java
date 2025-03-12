package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.tools.RegexBuilder;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "TimeOfArrivalRelativeToPublicationCalculator")
public class TimeOfArrivalRelativeToPublicationCalculator extends RegexCalculator<String>{
    public static final int ORIGINAL_TEXT=0;
    @Override
    public String calculate(Object[] params) {
        String when = ((String)params[ORIGINAL_TEXT]).replace('\n', ' ');
        String ret=when;
        if(when.matches(RegexBuilder.getInstance(this.getBasePath(), this.getSearchPath(), this.getVariant()).getStrPatternFromFile("contains_anteayer"))){
            ret = "by";
        }else if(when.matches(RegexBuilder.getInstance(this.getBasePath(), this.getSearchPath(), this.getVariant()).getStrPatternFromFile("contains_ayer"))){
            ret="y";
        }else if(when.matches(RegexBuilder.getInstance(this.getBasePath(), this.getSearchPath(), this.getVariant()).getStrPatternFromFile("contains_hoy"))){
            ret="t";
        }
        return ret;
    }

}
