package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.AbstractCalculator;
import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.DataExtractorCalculatorMarkerAnnotation;
import org.elsquatrecaps.autonewsextractor.model.MutableNewsExtractedData;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "PortOfCallsFromOriginPortCalculator")
public class PortOfCallsFromOriginPortCalculator extends AbstractCalculator<String[], String>{
    private MutableNewsExtractedData extractedData;
    public static final int ORIGIN=0;
    
    
    @Override
    public void init(Object obj){
        if(obj instanceof MutableNewsExtractedData){
            MutableNewsExtractedData localExtractedData = (MutableNewsExtractedData) obj;
            init(localExtractedData);
        }
    }
    
    public void init(MutableNewsExtractedData obj){
        extractedData=obj;
    }
    
    @Override
    public String calculate(String[] params) {
        String origen;
        StringBuilder portOfCalls=new StringBuilder();
        String originField = params[ORIGIN].replace('\n', ' ');
        String[] values = extractedData.get(originField).split("(?:\\s+[yv]\\s+)|(?:\\s*[;,]\\s+)|(?:\\s+[;,yv]\\s*)");
        origen = values[0];
        String sep = "";
        for(int i=1; i<values.length; i++){
            portOfCalls.append(sep);
            portOfCalls.append(values[i]);
            if(sep.isEmpty()){
                sep = ",";
            }
        }
        extractedData.setCalculateValue(originField, origen);       
        return portOfCalls.toString();
    }

}
