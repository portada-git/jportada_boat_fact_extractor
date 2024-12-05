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
    PUBLICATION_EDITION(NewsExtractedData.PUBLICATION_EDITION_FIELD_NAME),
    //Fact
    NEWS_SECTION("news_section", "fact_Type"),
    //Travel info
    TRAVEL_DEPARTURE_PORT("travel_departure_port","ship_departure_port"),
    TRAVEL_ARRIVAL_PORT("travel_arrival_port","ship_arrival_port"),
    TRAVEL_DEPARTURE_DATE("travel_departure_date","ship_departure_date"),
    TRAVEL_ARRIVAL_DATE("travel_arrival_date","ship_arrival_date"),
    TRAVEL_ARRIVAL_MOMENT("travel_arrival_moment", "travel_arrival_moment_value"),
    TRAVEL_DURATION_VALUE("travel_duration_value","ship_travel_time"),
    TRAVEL_DURATION_UNIT("travel_duration_unit","ship_travel_time_unit"),
    //port_of_call
    TRAVEL_PORT_OF_CALL_LIST("travel_port_of_call_list","ship_port_of_call_list"),
    PORT_OF_CALL_PLACE("port_of_call_place","ship_port_of_call_place"),
    PORT_OF_CALL_ARRIVAL_DATE("port_of_call_arrival_date","ship_port_of_call_arrival_date"),
    PORT_OF_CALL_DEPARTURE_DATE("port_of_call_departure_date","ship_port_of_call_departure_date"),
    //ship info
    SHIP_TYPE,
    SHIP_FLAG,
    SHIP_NAME,
    SHIP_TONS_CAPACITY("ship_tons_capacity", "SHIP_TONS"),
    SHIP_TONS_UNIT,
    //Ship master
    MASTER_ROLE("master_role", "SHIP_MASTER_ROLE"),
    MASTER_NAME("master_name", "SHIP_MASTER_NAME"),
    //Ship agent
    SHIP_AGENT_NAME("ship_agent_name", "SHIP_AGENT"),
    //tripulantes
    CREW_NUMBER("crew_number", "SHIP_CREW"),
    //ship_cargo
    CARGO_LIST("cargo_list", "SHIP_CARGO_LIST"),
    CARGO_MERCHANT_NAME("cargo_merchant_name", "CARGO_MERCHANT"),
    CARGO_COMMODITY("cargo_commodity","CARGO_TYPE"),
    CARGO_QUANTITY("cargo_quantity","cargo_value"),
    CARGO_UNIT,
    CARGO_PORT_ORIGIN("cargo_port_origin", "CARGO_ORIGIN"),
    CARGO_PORT_DESTINATION("cargo_port_destination", "CARGO_DESTINATION"),
    //More information
    QUARANTINE("quarantine","ship_quarantine"),
    FORCED_ARRIVAL("forced_arrival", "SHIP_FORCED_ARRIVAL"),
    //Quantitative model
    SHIP_AMOUNT,
    SHIP_ORIGIN_AREA,
    //Inteligencia marítima
    INFO_SHIP_TYPE,
    INFO_SHIP_NAME,
    INFO_MASTER_ROLE,
    INFO_MASTER_NAME,
    INFO_MEETING_PLACE,
    INFO_MEETING_DATE,
    INFO_PORT_DEPARTURE,
    CARGO_INFO_DEPARTURE_DATE,
    INFO_PORT_DESTINATION,
    INFO_BEHIND,    
    ;
    
    private static final String CURRENT_MODEL_VERSION="boat_fact-00.00.01";
    private static final Map<String, BoatFactFields> enumForNames = new HashMap<>();   
    private static java.util.ResourceBundle descriptionMessages = java.util.ResourceBundle.getBundle("BoatFactFieldDesc");
    static{
        for(BoatFactFields e: BoatFactFields.values()){
            for(String v: e.fieldName){
                enumForNames.put(v.toLowerCase(), e);
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
        fieldName.add(val.toLowerCase());
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
    
    public static String getFieldInformation(){
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================================\n");
        sb.append(java.util.ResourceBundle.getBundle("BoatFactFieldMessages").getString("CURRENT_VERSION_HEADER"));
        sb.append(getCurrentModelVersion());
        sb.append("\n\n");
        sb.append(getDescriptionOfFields());
        sb.append("\n\n");
        sb.append(getChangeOfVersions());
        return sb.toString();
    }
    
    public static String getDescriptionOfFields(){
        StringBuilder sb = new StringBuilder();
        String pre = "----------------------------------------------------------------------\n";
        sb.append("======================================================================\n");
        sb.append(java.util.ResourceBundle.getBundle("BoatFactFieldMessages").getString("FIELD_LIST_HEADER"));
        sb.append("======================================================================\n");
        sb.append(java.util.ResourceBundle.getBundle("BoatFactFieldMessages").getString("COLUMNS_HEADER"));
        for(BoatFactFields f: BoatFactFields.values()){
            sb.append(pre);
            sb.append(f.toString());                
            sb.append(":    ");
            sb.append(f.getFieldDescription());                
//            pre = "----------------------------------------------------------------------\n\n";
//            sb.append("\n\n");
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static String getChangeOfVersions(){
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================================\n");
        sb.append(java.util.ResourceBundle.getBundle("BoatFactFieldMessages").getString("CHANGES_LIST_HEADER"));
        sb.append("======================================================================\n");
        sb.append(java.util.ResourceBundle.getBundle("BoatFactFieldMessages").getString("COLUMNS_OF_CHANGES_HEADER"));
        sb.append("----------------------------------------------------------------------\n");
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

    /**
     * @return the fieldDescription
     */
    public String getFieldDescription() {
        return descriptionMessages.getString(this.toString().toUpperCase().concat("_DESC"));
    }
}
