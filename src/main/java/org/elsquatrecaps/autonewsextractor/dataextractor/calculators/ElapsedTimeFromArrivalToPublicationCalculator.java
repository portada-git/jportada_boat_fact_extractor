package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "ElapsedTimeFromArrivalToPublicationCalculator")
public class ElapsedTimeFromArrivalToPublicationCalculator extends AbstractCalculator<String>{
    public static final String BEFORE_YESTERDAY="by";
    public static final String YESTERDAY="y";
    public static final String TODAY="t";
    public static final int TIME_OF_ARRIVAL=0;

    @Override
    public String calculate(Object[] params) {
        String elapsedDaysFromArrival="1";
        if(params[TIME_OF_ARRIVAL].equals(BEFORE_YESTERDAY)){
            elapsedDaysFromArrival = "2";
        }else if(params[TIME_OF_ARRIVAL].equals(YESTERDAY)){
            elapsedDaysFromArrival="1";
        }else if(params[TIME_OF_ARRIVAL].equals(TODAY)){
            elapsedDaysFromArrival="0";
        }
        return elapsedDaysFromArrival;
    }
}
