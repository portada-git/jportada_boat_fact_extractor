package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.model.MutableNewsExtractedData;
import org.json.JSONArray;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "PortOfCallsFromOriginPortCalculator")
public class PortOfCallsFromOriginPortCalculator extends AbstractCalculator<JSONArray>{
    public static final int ORIGIN=0;
    public static final int REGEX=1;
    
    @Override
    public JSONArray calculate(Object[] params) {
        String regex = "(?:\\s+[yv]\\s+)|(?:\\s*[;,]\\s+)|(?:\\s+[;,yv]\\s*)";
        String origen;
        JSONArray portOfCalls=new JSONArray();
        String originField = ((String)params[ORIGIN]).replace('\n', ' ');
        if (params.length>1){
            regex = ((String)params[REGEX]);
        }
        String[] values = getExtractedData().get(originField).split(regex);
        origen = values[0];
        for(int i=1; i<values.length; i++){
            portOfCalls.put(values[i]);
        }
        getExtractedData().setCalculateValue(originField, origen);       
        return portOfCalls;
    }
    
    public MutableNewsExtractedData getExtractedData(){
        return getInitData(EXTRACTED_DATA);
    }

}
