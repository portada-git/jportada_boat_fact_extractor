package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.tools.RegexBuilder;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "TimeOfArrivalRelativeToPublicationCalculator")
public class TimeOfArrivalRelativeToPublicationCalculator extends RegexCalculator<String[], String>{
    public static final int ORIGINAL_TEXT=0;
    @Override
    public String calculate(String[] params) {
        String ret="y";
        String when = params[ORIGINAL_TEXT].replace('\n', ' ');
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
