package org.elsquatrecaps.autonewsextractor.model;

/**
 *
 * @author josep
 */
public class BoatFactData extends MutableNewsExtractedData{
    public BoatFactData(MutableNewsExtractedData d) {
        super(d.getExtractedData());
    }

    public String getFactType(){
        return get(BoatFactFields.FACT_TYPE.toString());
    }
    
    public String getDepartureDate() {
        return get(BoatFactFields.TRAVEL_DEPARTURE_DATE.toString());
    }

    public String getArrivalDate() {
        return get(BoatFactFields.TRAVEL_ARRIVAL_DATE.toString());
    }
    
    public String getVoyageDuration(){
        return String.format("%s %s", 
                get(BoatFactFields.TRAVEL_TIME_DURATION.toString()), 
                get(BoatFactFields.TRAVEL_TIME_UNIT_DURATION.toString()));
    }
    
    public String getVoyageDurationValue(){
        return get(BoatFactFields.TRAVEL_TIME_DURATION.toString());
    }
    
    public String getVoyageDurationUnit(){
        return get(BoatFactFields.TRAVEL_TIME_UNIT_DURATION.toString());
    }
    
    public String getOrigin(){
        return get(BoatFactFields.TRAVEL_DEPARTURE_PORT.toString());
    }
    
    
    public String getShipType() {
        return get(BoatFactFields.SHIP_TYPE.toString());
    }

    public String getShipName() {
        return get(BoatFactFields.SHIP_NAME.toString());
    }

    public String getShipTonnage() {
        //return String.format("%s %s", get(BoatFactFields.SHIP_TONS), get("ship_tonnage_unit"));
        return get(BoatFactFields.SHIP_TONS.toString());
    }

    public String getShipFlag() {
        return get(BoatFactFields.SHIP_FLAG.toString());
    }

    public String getMasterName() {
        return get(BoatFactFields.SHIP_MASTER_NAME.toString());
    }

    public String getMasterRole() {
        return get(BoatFactFields.SHIP_MASTER_ROLE.toString());
    }

    public String getCargoInformationList() {
        return get(BoatFactFields.SHIP_CARGO_LIST.toString());
    }

    public ShipCargoInfo getCargoInformation(int id) {
        throw new UnsupportedOperationException();
    }

    public String getArrivalPort() {
        return get(BoatFactFields.TRAVEL_ARRIVAL_PORT.toString());
    }

    public boolean hasPortOfCalls(){
        return this.getExtractedData().has(BoatFactFields.TRAVEL_PORT_OF_CALL_LIST.toString());
    }
    
    public String getPortOfCallList() {
        String ret = "";
        if(this.hasPortOfCalls()){
            ret = this.get(BoatFactFields.TRAVEL_PORT_OF_CALL_LIST.toString());
        }        
        return ret;
    }
    
    public TravelPortOfCall getPortOfCall(int i){
        throw new UnsupportedOperationException();
    }
    
    public String getArrivalMoment(){
        return get(BoatFactFields.TRAVEL_ARRIVAL_MOMENT_VALUE.toString());
    }

    public String getShipAgent(){
        return get(BoatFactFields.SHIP_AGENT.toString());
    }

    public String getShipCrew(){
        return get(BoatFactFields.SHIP_CREW.toString());
    }

    public String getShipIsInQuarantine(){
        return get(BoatFactFields.SHIP_NEED_QUARANTINE.toString());
    }

    public String getShipHasForcedArrival(){
        return get(BoatFactFields.SHIP_FORCED_ARRIVAL.toString());
    }
    
//    public String getAllDataAsJson(){
//        
//    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        stb.append("Boat fact =>[News date: ");
        stb.append(String.format("%1$td-%1$tm-%1$tY", this.getPublicationDate()));
        stb.append("\n Original text: ");
        stb.append(this.getParsedText());
        stb.append("\n departrure harbor: ");
        stb.append(this.getOrigin());
        if(this.hasPortOfCalls()){
            stb.append(", route harbors: ");
            stb.append(String.join(", ", this.getPortOfCallList()));
        }
        stb.append(", arrival harbor: ");
        stb.append(this.getArrivalPort());
        stb.append(", expected departure date: ");
        stb.append(this.getDepartureDate());
        stb.append(", expected arrival date: ");
        stb.append(this.getArrivalDate());
        stb.append("\n boat info: ");       
        stb.append("Boat=>[flag: ");
        stb.append(this.getShipFlag());
        stb.append(", type: ");
        stb.append(this.getShipType());
        stb.append(", name: ");
        stb.append(this.getShipName());
        stb.append(", tonnage: ");
        stb.append(this.getShipTonnage());
        stb.append("t. ]");
        stb.append("\n responsible info: ");
        
        stb.append("Master=>[name: ");
        stb.append(this.getMasterName());
        stb.append(", role: ");
        stb.append(this.getMasterRole());
        stb.append("]");
        stb.append("\n other info: ");
        stb.append(this.getCargoInformationList());
        stb.append("\n ]");      
        return stb.toString();    
    }
}
