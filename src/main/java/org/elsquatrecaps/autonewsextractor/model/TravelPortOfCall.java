package org.elsquatrecaps.autonewsextractor.model;

/**
 *
 * @author josep
 */
public class TravelPortOfCall extends MutableNewsExtractedData{

    public String getPortOfCallPlace(){
        return get(BoatFactFields.PORT_OF_CALL_PLACE.toString());
    }

    public String getPortOfCallArrivalDate(){
        return get(BoatFactFields.PORT_OF_CALL_ARRIVAL_DATE.toString());
    }

    public String getPortOfCallDepartureDate(){
        return get(BoatFactFields.PORT_OF_CALL_DEPARTURE_DATE.toString());
    }

    @Override
    public String toString() {
        return String.format("%s:%s, %s:%s, %s:%s", 
                BoatFactFields.PORT_OF_CALL_PLACE, getPortOfCallPlace(), 
                BoatFactFields.PORT_OF_CALL_ARRIVAL_DATE, getPortOfCallArrivalDate(),
                BoatFactFields.PORT_OF_CALL_DEPARTURE_DATE, getPortOfCallDepartureDate());
    }
}
