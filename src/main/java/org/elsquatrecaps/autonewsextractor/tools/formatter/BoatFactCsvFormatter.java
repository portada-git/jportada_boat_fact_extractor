package org.elsquatrecaps.autonewsextractor.tools.formatter;

import org.elsquatrecaps.autonewsextractor.model.MutableNewsExtractedData;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.json.JSONObject;

/**
 *
 * @author josep
 */
public class BoatFactCsvFormatter extends GenericCsvFileFormatter<MutableNewsExtractedData>{
//    private static final String csv_header = "\"publication_date\";\"news_section\";\"travel_departure_date\";\"travel_duration\";\"travel_arrival_date\";"
//            + "\"travel_departure_port\";\"travel_port_of_call_list\";\"travel_arrival_port\";\"ship_type\";\"ship_name\";\"ship_tons\";\"ship_flag\";"
//            + "\"master_name\";\"master_role\";\"Cargo\";\"Raw text\"";
////    private static String csv_header = "\"Data publicació\",\"Data sortida\",\"Durada Viatge\",\"Data arribada\",\"Port sortida\",\"Ports ruta\",\"Port destí\","
////            + "\"Tipus embarcació\",\"Nom embarcació\",\"Tonatge embarcació\",\"Bandera embarcació\",\"Nom del responsable\","
////            + "\"Càrrec del responsnable\",\"Bens transportants\",\"Text cru\"";
//    private final String fieldSeparator = ";";
////    private String fieldSeparator = ",";
    public static BoatFactCsvFormatter instance(){
        BoatFactCsvFormatter ret = new BoatFactCsvFormatter();
        return ret;
    }

    public static BoatFactCsvFormatter instance(JSONObject csvView){
        BoatFactCsvFormatter ret = new BoatFactCsvFormatter();
        ret.configHeaderFields(csvView);
        return ret;
    }
    
    @Override
    protected String getFormatedValue(String field, MutableNewsExtractedData fact){
        String ret;
        if(field.equals(NewsExtractedData.PUBLICATION_DATE_FIELD_NAME)){
            ret = String.format("%1$tY-%1$tm-%1$td", fact.getPublicationDate());
        }else{
            ret = super.getFormatedValue(field, fact);
        }
        return ret;
    }


//    public  String factToString(BoatFactData bf) {
//        StringBuilder stb = new StringBuilder();
//        stb.append(String.format("%1$tY-%1$tm-%1$td", bf.getPublicationDate()));
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getNewsSection());
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append(bf.getDepartureDate());
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getVoyageDuration());
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append(bf.getArrivalDate());
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getDeparturePort().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        if(bf.hasPortOfCalls()){
//            stb.append(bf.getPortOfCallList().replaceAll("\"", "'"));
//        }
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getArrivalPort().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getShipType().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getShipName().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getShipTonnage().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getShipFlag().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getMasterName().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append(bf.getMasterRole().replaceAll("\"", "'"));
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getCargoInformationList().replaceAll("\"", "'"));
//        stb.append("\"");
//        stb.append(getFieldSeparator());
//        stb.append("\"");
//        stb.append(bf.getParsedText().replaceAll("\"", "'"));
//        stb.append("\"");
//        return stb.toString();           
//    }    

//    @Override
//    public String toString() {
//        StringBuilder stb = new StringBuilder();
//        if(!isAppendFile()){
//            stb.append(getCsvHeader());
//        }
//        for(MutableNewsExtractedData fact: this.getList()){
//            BoatFactData bfd;
//            if(fact instanceof BoatFactData){
//                bfd = (BoatFactData) fact;
//            }else{
//                bfd = new BoatFactData(fact);
//            }
//            stb.append(this.factToString(bfd));
//            stb.append("\n");
//        }
//        return stb.toString();
//    }
//
//    @Override
//    public void toFile(String outputFileName) {
//        toFile(outputFileName, (fact) -> {
//            BoatFactData bfd;
//            if(fact instanceof BoatFactData){
//                    bfd = (BoatFactData) fact;
//            }else{
//                    bfd = new BoatFactData(fact);
//            }
//            return this.factToString(bfd);
//        });
//    }    

}
