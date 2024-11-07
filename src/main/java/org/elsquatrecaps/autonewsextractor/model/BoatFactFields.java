package org.elsquatrecaps.autonewsextractor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author josep
 */
public enum BoatFactFields{
    //model_version
    MODEL_VERSION (ExtractedData.MODEL_VERSION_FIELD_NAME),
    //publication info
    PUBLICATION_DATE(NewsExtractedData.PUBLICATION_DATE_FIELD_NAME),
    PUBLICATION_NAME(NewsExtractedData.PUBLICATION_NAME_FIELD_NAME),
    PUBLICATION_PLACE(NewsExtractedData.PUBLICATION_PLACE_FIELD_NAME),
    PUBLICATION_EDITION(NewsExtractedData.PUBLICATION_EDITION_FIELD_NAME),
    PUBLICATION_NUMBER,
    //Fact
    FACT_TYPE,
    //Travel info
    TRAVEL_DEPARTURE_PORT("ship_departure_port"),
    TRAVEL_ARRIVAL_PORT("ship_arrival_port"),
    TRAVEL_DEPARTURE_DATE("ship_departure_date"),
    TRAVEL_ARRIVAL_DATE("ship_arrival_date"),
    TRAVEL_ARRIVAL_MOMENT_VALUE("ship_arrival_moment_value"),
    TRAVEL_ARRIVAL_MOMENT_UNIT("ship_arrival_moment_unit"),
    TRAVEL_TIME_DURATION("ship_travel_time"),
    TRAVEL_TIME_UNIT_DURATION("ship_travel_time_unit"),
    //port_of_call
    TRAVEL_PORT_OF_CALL_LIST("ship_port_of_call_list"),
    PORT_OF_CALL_PLACE("ship_port_of_call_place"),
    PORT_OF_CALL_ARRIVAL_DATE("ship_port_of_call_arrival_date"),
    PORT_OF_CALL_DEPARTURE_DATE("ship_port_of_call_departure_date"),
    //ship info
    SHIP_TYPE,
    SHIP_FLAG,
    SHIP_NAME,
    SHIP_TONS,
    //Ship master
    SHIP_MASTER_ROLE,
    SHIP_MASTER_NAME,
    //Ship agent
    SHIP_AGENT,
    //ship_cargo
    SHIP_CARGO_LIST,
    CARGO_MERCHANT,
    CARGO_PRODUCT("cargo_type"),
    CARGO_QUANTITY("cargo_value"),
    CARGO_UNIT,
    CARGO_ORIGIN,
    CARGO_DESTINATION,
    //tripulantes
    SHIP_CREW,
    //More information
    SHIP_NEED_QUARANTINE("ship_quarantine"),
    SHIP_FORCED_ARRIVAL,
    //Quantitative model
    SHIP_AMOUNT,
    SHIP_ORIGIN_AREA,
    ;
    
    private static final String CURRENT_MODEL_VERSION="boat_fact-00.00.00";
    private static final Map<String, BoatFactFields> enumForNames = new HashMap<>();    
    static{
        for(BoatFactFields e: BoatFactFields.values()){
            for(String v: e.fieldName){
                enumForNames.put(v, e);
            }
        }
    }

    public static BoatFactFields getValue(String v){
        return enumForNames.get(v.toLowerCase());
    }
    
    public static String getFieldName(String alias){
        String ret = null;
        if(enumForNames.containsKey(alias.toLowerCase())){
            ret = enumForNames.get(alias.toLowerCase()).toString();
        }
        return ret;
    }
    
    private final List<String> fieldName = new ArrayList<>();
    
    BoatFactFields(){
        fieldName.add(this.name().toLowerCase());
    }
    
    BoatFactFields(String val){
        fieldName.add(val);
    }

    BoatFactFields(String... val){
        fieldName.addAll(Arrays.asList(val));
    }
    
    public static String getCurrentModelVersion(){
        return CURRENT_MODEL_VERSION;
    }

    
    @Override
    public String toString(){
        return this.fieldName.get(0);
    }
    
    public static String getVersionInfo(){
        Map<String, List<String>> changes = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" List of changes made in each field throughout the different versions \n");
        sb.append("----------------------------------------------------------------------\n");
        sb.append("CURRENT NAME FIELD         <=         PREVIOUS NAME FIELDS            \n");
        for(BoatFactFields f: BoatFactFields.values()){
            String pre = "";
            for(String fn: f.fieldName){
                sb.append(pre);
                pre = " <= ";
                sb.append(fn);                
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
