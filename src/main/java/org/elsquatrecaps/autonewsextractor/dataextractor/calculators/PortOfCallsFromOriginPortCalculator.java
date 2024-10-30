package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.model.MutableNewsExtractedData;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "PortOfCallsFromOriginPortCalculator")
public class PortOfCallsFromOriginPortCalculator extends AbstractCalculator<String[], String>{
    public static final int ORIGIN=0;
    
    @Override
    public String calculate(String[] params) {
        String origen;
        StringBuilder portOfCalls=new StringBuilder();
        String originField = params[ORIGIN].replace('\n', ' ');
        String[] values = getExtractedData().get(originField).split("(?:\\s+[yv]\\s+)|(?:\\s*[;,]\\s+)|(?:\\s+[;,yv]\\s*)");
        origen = values[0];
        String sep = "";
        for(int i=1; i<values.length; i++){
            portOfCalls.append(sep);
            portOfCalls.append(values[i]);
            if(sep.isEmpty()){
                sep = ",";
            }
        }
        getExtractedData().setCalculateValue(originField, origen);       
        return portOfCalls.toString();
    }
    
    public MutableNewsExtractedData getExtractedData(){
        return getInitData(EXTRACTED_DATA);
    }

}
