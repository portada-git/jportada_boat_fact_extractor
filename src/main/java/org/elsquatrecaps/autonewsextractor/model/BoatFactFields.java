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
    MODEL_VERSION ("Indicates the version of the field name model.",ExtractedData.MODEL_VERSION_FIELD_NAME),
    //publication info
    PUBLICATION_DATE("Displays the date of the newspaper",NewsExtractedData.PUBLICATION_DATE_FIELD_NAME),
    PUBLICATION_NAME("Displays the name of the newspaper",NewsExtractedData.PUBLICATION_NAME_FIELD_NAME),
    //PUBLICATION_PLACE(NewsExtractedData.PUBLICATION_PLACE_FIELD_NAME),
    PUBLICATION_EDITION("Indicates the edition of the newspaper in case there is more than one per day: M for morning, T for afternoon or N for evening. In case there is only one edition, the value will be U (unique).", NewsExtractedData.PUBLICATION_EDITION_FIELD_NAME),
    //PUBLICATION_NUMBER,
    //Fact
    FACT_TYPE("This is the type of news analyzed. It can take values ​​such as E for ship entrances or M for discharge manifests."),
    //Travel info
    TRAVEL_DEPARTURE_PORT("Indicates the port of departure of the ship in this travel","ship_departure_port"),
    TRAVEL_ARRIVAL_PORT("Indicates the port of arrival (Marseille, Buenos Aires, Havana or Barcelona) of the ship in this travel. In most cases, this information does not appear in the news and is implicitly deduced depending on the newspaper","ship_arrival_port"),
    TRAVEL_DEPARTURE_DATE("It denotes the ship’s departure date from the departure port","ship_departure_date"),
    TRAVEL_ARRIVAL_DATE("Indicates the date that the ship arrived in the arrival port (Marseille, Buenos Aires, Havana or Barcelona)","ship_arrival_date"),
    TRAVEL_ARRIVAL_MOMENT_VALUE("Indicates the time of arrival at port. It can be expressed as the time of arrival or as a broader period (morning, afternoon, evening, ...)"),
    TRAVEL_TIME_DURATION("Indicates the time that the ship was travelling from the departure port to the arrival port days or hours","ship_travel_time"),
    TRAVEL_TIME_UNIT_DURATION("Indicates the unit of time in which the duration is expressed.","ship_travel_time_unit"),
    //port_of_call
    TRAVEL_PORT_OF_CALL_LIST("Indicates the list of ports (and optionally more information as arrival or departure dates) that the ship had stopped while on her way to the arrival port. If the information of this list is only the name of ports, the list will be compounded by port names separated by commas","ship_port_of_call_list"),
    PORT_OF_CALL_PLACE("Show the name of one item of the port of call list","ship_port_of_call_place"),
    PORT_OF_CALL_ARRIVAL_DATE("Show the arrival date of one item of the port of call list","ship_port_of_call_arrival_date"),
    PORT_OF_CALL_DEPARTURE_DATE("Show the departure date of one item of the port of call list","ship_port_of_call_departure_date"),
    //ship info
    SHIP_TYPE("Describes the type of the ship (brick, brick-goelette, trois-mats, vapeur etc) that the newspaper mention"),
    SHIP_FLAG("Refers to the name of the country or region of the flag of the ship describe by the newspaper"),
    SHIP_NAME("Indicates the name of the ship typically presented in full, like is mention in the newspaper source"),
    SHIP_TONS("Specifies the ship’s capacity in tons presented as a numerical value with the unit of measurement. In the case of the ships this remains always the same as it refers to the tonnage of the ship. This data is given usually with abbreviations such as \"ton.\" or \"t.\""),
    //Ship master
    SHIP_MASTER_ROLE("It refers to the category of the person who commands the ship. It can be a captain or a skipper, although in a few cases a pilot also appears. The abbreviations used to designate them are usually “c” and “p”, respectively"),
    SHIP_MASTER_NAME("It is the nominal identification of the person who commands the ship. It can appear in various ways, at least it has the surname, preceded by his position (role). Lists the surname of the ship's captain, often preceded by \"cap.\" or \"c.\""),
    //Ship agent
    SHIP_AGENT("This information could indicate either the ship agent, namely the person that is responsible for the transactions and the operation of the ship or the shipowner, namely the person that owns the ship or part of the ship. Sometimes it can also refers to shipowner"),
    //tripulantes
    SHIP_CREW("It is the numerical value of the ship's crew."),
    //ship_cargo
    SHIP_CARGO_LIST("It is the description of the list with the information related to all the cargo transported by the incoming vessel (type of cargo, quantity, person receiving of the cargo, if any or \"to order\" otherwise, etc.)"),
    CARGO_MERCHANT("It is the person to whom the cargo was destined, often it will be the merchant who had bought it and who took charge of it at the time of unloading. Indicates the recipient of the cargo, with occasional mention of \"divers\" [various/several].\n" +
                    "In this case we see names of people or companies. These names have the same characteristics and difficulties as the rest of the names. Sometimes the ships arrived in full loads and were destined for the same person, and in other cases, each load had its recipient. The expression “a la orden” also appears frequently, which in principle is a load to be sold upon arrival at the port and which, on the contrary, does not have a previous owner, beyond the captain himself personally or on behalf of someone."),
    CARGO_PRODUCT("It expresses the products or types of goods that have arrived. It is a very variable value, the most common goods are coal or cotton, but there is an extraordinary diversity of products that arrive at the port.","cargo_type"),
    CARGO_QUANTITY("Numerical expression of the amount of charge","cargo_value"),
    CARGO_UNIT("Expresses the units in which the load appears. These may be units of weight, volume, counts, or units related to packaging."),
    CARGO_ORIGIN("Port of origin of the cargo"),
    CARGO_DESTINATION("Port of destination of the cargo"),
    //More information
    SHIP_NEED_QUARANTINE("Information relative to special conditions of the arrival motivated by sanitary circumstances.","ship_quarantine"),
    SHIP_FORCED_ARRIVAL("Indication about the causes of the forced arrival"),
    //Quantitative model
    SHIP_AMOUNT("This field appears only in quantitative models where, instead of specifying the information for each ship, the number of vessels that have arrived or are about to arrive is indicated. Normally, it is a model specifically intended for cabotage transport."),
    SHIP_ORIGIN_AREA("This field appears only in quantitative models where, instead of specifying information about each ship, the area of ​​origin or transport is used. Normally, it is a model specifically intended for cabotage transport."),
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
    private final String fieldDescription;
    
    BoatFactFields(String desc){
        fieldDescription = desc;
        fieldName.add(this.name().toLowerCase());
    }
    
    BoatFactFields(String desc, String val){
        fieldDescription = desc;
        fieldName.add(val);
    }

    BoatFactFields(String desc, String... val){
        fieldDescription = desc;
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
        sb.append(" CURRENT VERSION: ");
        sb.append(getCurrentModelVersion());
        sb.append("\n\n");
        sb.append(getFieldDespcription());
        sb.append("\n\n");
        sb.append(getChangeOfVersions());
        return sb.toString();
    }
    
    public static String getFieldDespcription(){
        StringBuilder sb = new StringBuilder();
        String pre = "----------------------------------------------------------------------\n";
        sb.append("======================================================================\n");
        sb.append(" List of fildes and desciption for the current version\n");
        sb.append("======================================================================\n");
        sb.append("CURRENT NAME FIELD                 FIELD DESCRIPTION                  \n");
        for(BoatFactFields f: BoatFactFields.values()){
            sb.append(pre);
            sb.append(f.toString());                
            sb.append(":    ");
            sb.append(f.fieldDescription);                
//            pre = "----------------------------------------------------------------------\n\n";
//            sb.append("\n\n");
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static String getChangeOfVersions(){
        StringBuilder sb = new StringBuilder();
        sb.append("======================================================================\n");
        sb.append(" List of changes made in each field throughout the different versions \n");
        sb.append("======================================================================\n");
        sb.append("CURRENT NAME FIELD         <=         PREVIOUS NAME FIELDS            \n");
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
}
