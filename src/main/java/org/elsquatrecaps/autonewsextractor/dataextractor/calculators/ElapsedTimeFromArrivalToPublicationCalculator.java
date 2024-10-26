package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.AbstractCalculator;
import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.DataExtractorCalculatorMarkerAnnotation;
import org.elsquatrecaps.autonewsextractor.model.ExtractedData;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "ElapsedTimeFromArrivalToPublicationCalculator")
public class ElapsedTimeFromArrivalToPublicationCalculator extends AbstractCalculator<String[], String>{
    public static final int TIME_OF_ARRIVAL=0;

    @Override
    public String calculate(String[] params) {
        String elapsedDaysFromArrival="1";
        if(params[TIME_OF_ARRIVAL].equals("by")){
            elapsedDaysFromArrival = "2";
        }else if(params[TIME_OF_ARRIVAL].equals("y")){
            elapsedDaysFromArrival="1";
        }else if(params[TIME_OF_ARRIVAL].equals("t")){
            elapsedDaysFromArrival="0";
        }
        return elapsedDaysFromArrival;
    }
}
