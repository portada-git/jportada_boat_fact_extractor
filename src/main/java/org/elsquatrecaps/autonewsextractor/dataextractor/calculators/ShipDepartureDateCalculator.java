package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.DataExtractorCalculatorMarkerAnnotation;
import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.RegexCalculator;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "ShipDepartureDateCalculator")
public class ShipDepartureDateCalculator extends RegexCalculator<String[], String>{
    public static final int SHIP_ARRIVAL_DATE=0;
    public static final int SHIP_VOYAGE_DURATION=1;
    public static final int SHIP_VOYAGE_DURATION_UNIT=2;
    

    @Override
    public String calculate(String[] params) {
        String ret="";
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date arrivalDate = formater.parse(params[SHIP_ARRIVAL_DATE]);
            Instant instant = Instant.ofEpochMilli(arrivalDate.getTime());
            int intBackTime=Integer.parseInt(params[SHIP_VOYAGE_DURATION]);
            if(params[SHIP_VOYAGE_DURATION_UNIT]!=null && params[SHIP_VOYAGE_DURATION_UNIT].toLowerCase().startsWith("h")){
                instant = instant.minus(intBackTime, ChronoUnit.HOURS);
            }else{
                instant = instant.minus(intBackTime, ChronoUnit.DAYS);
            }
            ret = String.format("%tY-%<tm-%<td", instant.toEpochMilli());
        } catch (NumberFormatException | ParseException ex) {
            ret = String.format("(%s)-(%s %s)", params[SHIP_ARRIVAL_DATE], params[SHIP_VOYAGE_DURATION], params[SHIP_VOYAGE_DURATION_UNIT]);
        }
        return ret;
    }
}
