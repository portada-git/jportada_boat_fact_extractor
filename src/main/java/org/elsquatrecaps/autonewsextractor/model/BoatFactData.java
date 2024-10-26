package org.elsquatrecaps.autonewsextractor.model;

/**
 *
 * @author josep
 */
public class BoatFactData extends MutableNewsExtractedData{

    public BoatFactData() {
        super();
    }
    
    public BoatFactData(MutableNewsExtractedData d) {
        super(d.getExtractedData());
    }

    
    /**
     * @return the date
     */
    public String getDepartureDate() {
        return get("ship_departure_date");
    }

    public String getArrivalDate() {
        return get("ship_arrival_date");
    }
    
    public String getVoyageDuration(){
        return String.format("%s %s", get("ship_voyage_duration"), get("ship_voyage_duration_unit"));
    }
    
    public String getOrigin(){
        return get("ship_origin");
    }
    
    
    public String getShipType() {
        return get("ship_type");
    }

    public String getShipName() {
        return get("ship_name");
    }

    public String getShipTonnage() {
        return String.format("%s %s", get("ship_tonnage"), get("ship_tonnage_unit"));
    }

    public String getShipFlag() {
        return get("ship_flag");
    }

    public String getMasterName() {
        return get("ship_master_name");
    }

    public String getMasterRole() {
        return get("ship_master_role");
    }

    public String getOtherInformation() {
        return get("ship_cargo");
    }

    public String getArrivalHarbor() {
        return get("ship_arrival_port");
    }

    public boolean hasPortOfCalls(){
        return this.getExtractedData().has("ship_port_of_call_place");
    }
    
    public String getPortOfCalls() {
        String ret = "";
        if(this.hasPortOfCalls()){
            ret = this.get("ship_port_of_call_place");
        }        
        return ret;

//        StringBuilder stb = new StringBuilder();
//        if(this.hasPortOfCalls()){
//            stb.append(this.getExtractedData().getJSONArray("ship_port_of_call_place").join(", "));
//        }        
//        return stb.toString();
    }
    
    public String getPortOfCall(int i){
        String ret = null;
        String[] ports = this.get("ship_port_of_call_place").split(",");
        if(0<=i && ports.length>0){
            ret = ports[i];
        }
        return ret;
        
//        String ret = null;
//        if(0<i && hasPortOfCalls() && i<this.getExtractedData().getJSONArray("ship_port_of_call_place").length()){
//            ret = this.getExtractedData().getJSONArray("ship_port_of_call_place").getString(i);
//        }
//        return ret;
    }   

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
            stb.append(String.join(", ", this.getPortOfCalls()));
        }
        stb.append(", arrival harbor: ");
        stb.append(this.getArrivalHarbor());
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
        stb.append(this.getOtherInformation());
        stb.append("\n ]");      
        return stb.toString();    
    }
}
