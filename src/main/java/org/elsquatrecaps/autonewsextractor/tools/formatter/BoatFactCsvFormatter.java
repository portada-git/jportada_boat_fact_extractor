package org.elsquatrecaps.autonewsextractor.tools.formatter;

import org.elsquatrecaps.autonewsextractor.model.BoatFactData;
import org.elsquatrecaps.autonewsextractor.model.MutableNewsExtractedData;

/**
 *
 * @author josep
 */
public class BoatFactCsvFormatter extends GenericCsvFileFormatter<MutableNewsExtractedData>{
    private static final String CSV_HEADER = "\"Publication date\";\"Departure date\";\"Voyage duration\";\"Arrival date\";\"Origin port\";\"Port of calls\";\"Arrival port\";"
            + "\"Ship type\";\"Ship name\";\"Ship tonnage\";\"Ship flag\";\"Master name\";\"Master role\";\"Cargo\";\"Raw text\"";
//    private static String CSV_HEADER = "\"Data publicació\",\"Data sortida\",\"Durada Viatge\",\"Data arribada\",\"Port sortida\",\"Ports ruta\",\"Port destí\","
//            + "\"Tipus embarcació\",\"Nom embarcació\",\"Tonatge embarcació\",\"Bandera embarcació\",\"Nom del responsable\","
//            + "\"Càrrec del responsnable\",\"Bens transportants\",\"Text cru\"";
    private final String fieldSeparator = ";";
//    private String fieldSeparator = ",";

    public  String factToString(BoatFactData bf) {
        StringBuilder stb = new StringBuilder();
        stb.append(String.format("%1$tY-%1$tm-%1$td", bf.getPublicationDate()));
        stb.append(fieldSeparator);
        stb.append(bf.getDepartureDate());
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getVoyageDuration());
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append(bf.getArrivalDate());
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getOrigin().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        if(bf.hasPortOfCalls()){
            stb.append(bf.getPortOfCallList().replaceAll("\"", "'"));
        }
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getArrivalPort().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getShipType().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getShipName().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getShipTonnage().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getShipFlag().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getMasterName().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append(bf.getMasterRole().replaceAll("\"", "'"));
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getCargoInformationList().replaceAll("\"", "'"));
        stb.append("\"");
        stb.append(fieldSeparator);
        stb.append("\"");
        stb.append(bf.getParsedText().replaceAll("\"", "'"));
        stb.append("\"");
        return stb.toString();           
    }    

    @Override
    protected String getCsvHeader() {
        return CSV_HEADER;
    }
    
@Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if(!isAppendFile()){
            stb.append(getCsvHeader());
        }
        for(MutableNewsExtractedData fact: this.getList()){
            BoatFactData bfd;
            if(fact instanceof BoatFactData){
                bfd = (BoatFactData) fact;
            }else{
                bfd = new BoatFactData(fact);
            }
            stb.append(this.factToString(bfd));
            stb.append("\n");
        }
        return stb.toString();
    }

    @Override
    public void toFile(String outputFileName) {
        toFile(outputFileName, (fact) -> {
            BoatFactData bfd;
            if(fact instanceof BoatFactData){
                    bfd = (BoatFactData) fact;
            }else{
                    bfd = new BoatFactData(fact);
            }
            return this.factToString(bfd);
        });
    }    
}
