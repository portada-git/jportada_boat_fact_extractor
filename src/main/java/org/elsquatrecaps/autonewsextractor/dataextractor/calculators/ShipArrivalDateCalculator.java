package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "ShipArrivalDateCalculator")
public class ShipArrivalDateCalculator extends RegexCalculator<String[], String>{
    public static final int VALUE_OF_ELAPSET_TIME_FROM_ARRIVAL_TO_PUBLICATION=0;
    public static final int PUBLICATION_DATE_IN_MILLIS=1;
    

    @Override
    public String calculate(String[] params) {
        int backTime;
        String ret;
        Date publicationDate;
        try{
            publicationDate = new Date(Long.parseLong(params[PUBLICATION_DATE_IN_MILLIS]));
            try{
                backTime = Integer.parseInt(params[VALUE_OF_ELAPSET_TIME_FROM_ARRIVAL_TO_PUBLICATION]);
                Instant instant = Instant.ofEpochMilli(publicationDate.getTime());
                instant = instant.minus(backTime, ChronoUnit.DAYS);
                ret = String.format("%tY-%<tm-%<td", instant.toEpochMilli());
            }catch(NumberFormatException ex){
                ret = String.format("(%tY-%<tm-%<td)-(%s d)", publicationDate, params[VALUE_OF_ELAPSET_TIME_FROM_ARRIVAL_TO_PUBLICATION]);
            }                
        }catch(NumberFormatException ex){
            ret = "????";
        }
        return ret;
    }
}
