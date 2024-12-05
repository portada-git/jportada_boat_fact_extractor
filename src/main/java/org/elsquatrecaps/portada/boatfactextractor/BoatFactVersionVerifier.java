package org.elsquatrecaps.portada.boatfactextractor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.DataExtractorCalculatorProxyClass;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author josep
 */
public class BoatFactVersionVerifier {
    
    public static int compareVersions(String currentVersion, String version) {
        int ret = Integer.MIN_VALUE;
        //boat_fact-00.00.00   18-8 = 10
        if (currentVersion.length() == version.length() && currentVersion.substring(0, currentVersion.length() - 8).equals(version.substring(0, version.length() - 8))) {
            ret = currentVersion.substring(currentVersion.length() - 8).compareTo(version.substring(version.length() - 8));
        }
        return ret;
    }
        
    public static List<BoatFactVersionVerifierResponse> verify(JSONObject jsonConfiguration){
        return verify(jsonConfiguration, false);
    }
    
    public static List<BoatFactVersionVerifierResponse> verify(JSONObject jsonConfiguration, boolean onlyIfCorrectVersion){
        List<BoatFactVersionVerifierResponse> ret =  new ArrayList<>();
        BoatFactVersionVerifier updater = new BoatFactVersionVerifier();
        for(String key: jsonConfiguration.keySet()){
            updater.verifyModel(jsonConfiguration.getJSONObject(key), onlyIfCorrectVersion, ret);
        }        
        return ret;
    }

    protected Set<String> temporaryFileds = new HashSet<>();
    
    protected void verifyModel(JSONObject jsonConfiguration, 
            boolean onlyIfCorrectVersion, List<BoatFactVersionVerifierResponse> ret){
        int comparev = compareVersions(BoatFactFields.getCurrentModelVersion(), 
                                        jsonConfiguration.getString("field_version"));
        boolean continueVerif = false;
        switch (comparev) {
            case 0:
                ret.add(BoatFactVersionVerifierResponse.instance(
                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.CORRECT_VERSION,
                        "Version is correct")
                );
                continueVerif = true;
                break;
            case Integer.MIN_VALUE:
                ret.add(BoatFactVersionVerifierResponse.instance(
                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.UNKNOWN_VERSION,
                        "Version is unknown")
                );
                continueVerif = !onlyIfCorrectVersion;
                break;
            default:
                if(comparev<0){
                    ret.add(BoatFactVersionVerifierResponse.instance(
                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.LARGER_VERSION_THAN_EXPECTED,
                        "Version is unknown")
                    );
                    break;
                }                
                ret.add(BoatFactVersionVerifierResponse.instance(
                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.NEED_UPDATE,
                        "Version is not correct. Need update")
                );
                continueVerif = !onlyIfCorrectVersion;
        }
        if(continueVerif){
            //verify
            int pos = 0;
            while(pos < jsonConfiguration.getJSONArray("config").length()){
                JSONObject configApproach = jsonConfiguration.getJSONArray("config").getJSONObject(pos);
                switch(configApproach.getString("approach_type")){
                    case "regex":
                        verifyExtractFieldNamesForRegex(
                            configApproach.getJSONObject(
                                    "configuration").optJSONArray("fields_to_extract"), 
                            ret
                        );
                        verifyCalculateFieldNamesForRegex(
                            configApproach.getJSONObject("configuration").optJSONArray("fields_to_calculate"), 
                            ret
                        );
                        break;
                    case "openai":
                        verifyExtractFieldNamesForOpenAI(
                            configApproach.getJSONObject("configuration").optJSONArray("fields_to_extract"), 
                            ret
                        );
                }
                pos++;
            }                          
        }
    }
    
    protected void verifyExtractFieldNamesForOpenAI(JSONArray fieldsToExtract, 
            List<BoatFactVersionVerifierResponse> ret){
        if(fieldsToExtract==null){
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    protected void verifyExtractFieldNamesForRegex(JSONArray fieldsToExtract,
            List<BoatFactVersionVerifierResponse> ret){
        if(fieldsToExtract==null){
            return;
        }
        for(int i=0; i< fieldsToExtract.length(); i++){
            JSONObject fieldToExtract = fieldsToExtract.getJSONObject(i);
            String keyName = fieldToExtract.getString("key");
            if(fieldToExtract.optBoolean("temporary_field", temporaryFileds.contains(keyName))){
                temporaryFileds.add(keyName);
            }else{
                String newName = BoatFactFields.getFieldName(keyName);
                if(newName==null || !keyName.equals(newName)){
                    ret.add(
                            BoatFactVersionVerifierResponse.instance(
                                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS,
                                    String.format("Error in field name to extract. In this version %s is not correct", 
                                            keyName))
                    );
                }
            }
        }
    }
    
    protected void verifyCalculateFieldNamesForRegex(JSONArray fieldsToCalculate,
            List<BoatFactVersionVerifierResponse> ret){
        if(fieldsToCalculate==null){
            return;
        }
        for(int i=0; i< fieldsToCalculate.length(); i++){
            JSONObject fieldToCalculate = fieldsToCalculate.getJSONObject(i);
            String keyName = fieldToCalculate.getString("key");
            if(fieldToCalculate.optBoolean("temporary_field", temporaryFileds.contains(keyName))){
                temporaryFileds.add(keyName);
            }else{
                String newName = BoatFactFields.getFieldName(keyName);
                if(newName==null || !keyName.equals(newName)){
                    ret.add(
                            BoatFactVersionVerifierResponse.instance(
                                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                    String.format("Error in field name to calculate. In this version %s is not correct", 
                                            keyName))
                    );
                }
            }
        }
    }
    
    protected void verifyCalculateParamsForRegex(JSONObject fieldToCalculate,
            List<BoatFactVersionVerifierResponse> ret){
        boolean temporary;
        JSONArray params;
        params = fieldToCalculate.optJSONArray("fieldParams");
        if(params!=null){
            for(int i=0; i<params.length(); i++){
                String[] pathField = params.getString(i).split("\\.");
                for(int pathInd=1; pathInd<pathField.length; pathInd++){
                    temporary = temporaryFileds.contains(pathField[pathInd]);
                    if(!temporary){
                        String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                        if(pathFieldOfCurrentVersion==null && pathInd==1){
                            //ERROR IRREPARABLE
                            ret.add(
                                BoatFactVersionVerifierResponse.instance(
                                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                        String.format("Error in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                         fieldToCalculate.getString("key"),
                                         pathField[pathInd]))
                            );
                            break;
                        }else if(pathFieldOfCurrentVersion==null){
                            //WARNING
                            ret.add(
                                BoatFactVersionVerifierResponse.instance(
                                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_WARNIGS, 
                                        String.format("Warning in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                         fieldToCalculate.getString("key"),
                                         pathField[pathInd]))
                            );
                        }else if(!pathFieldOfCurrentVersion.equals(BoatFactFields.getFieldName(pathField[pathInd]))){
                            ret.add(
                                BoatFactVersionVerifierResponse.instance(
                                    BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                        String.format("Warning in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                         fieldToCalculate.getString("key"),
                                         pathField[pathInd]))    
                            );
                        }
                    }
                }
            }
        }
        params = fieldToCalculate.optJSONArray("params");
        if(params!=null){
            for(int i=0; i<params.length(); i++){
                JSONObject param = params.getJSONObject(i);
                if(param.getString("type").equals(DataExtractorCalculatorProxyClass.FIELD_VALUE_PARAM_TYPE)){
                    String[] pathField = param.getString("value").split("\\.");
                    boolean ch = false;
                    for(int pathInd=1; pathInd<pathField.length; pathInd++){
                        temporary = temporaryFileds.contains(pathField[pathInd]);
                        if(!temporary){
                            String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                            if(pathFieldOfCurrentVersion==null && pathInd==1){
                                ret.add(
                                    BoatFactVersionVerifierResponse.instance(
                                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                            String.format("Error in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                             fieldToCalculate.getString("key"),
                                             pathField[pathInd]))
                                );
                                break;
                            }else if(pathFieldOfCurrentVersion==null){
                                //WARNING
                                ret.add(
                                    BoatFactVersionVerifierResponse.instance(
                                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_WARNIGS, 
                                            String.format("Warning in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                             fieldToCalculate.getString("key"),
                                             pathField[pathInd]))
                                );
                            }else if(!pathFieldOfCurrentVersion.equals(BoatFactFields.getFieldName(pathField[pathInd]))){
                                ret.add(
                                    BoatFactVersionVerifierResponse.instance(
                                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                            String.format("Warning in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                             fieldToCalculate.getString("key"),
                                             pathField[pathInd]))    
                                );
                            }
                        }
                    }
                }

                if(param.getString("type").equals(DataExtractorCalculatorProxyClass.FIELD_NAME_PARAM_TYPE)){
                    String[] pathField = params.getString(i).split("\\.");
                    int start = 1;
                    if (pathField.length==1){
                        start = 0;
                    }
                    for(int pathInd=start; pathInd<pathField.length; pathInd++){
                        temporary = temporaryFileds.contains(pathField[pathInd]);
                        if(!temporary){
                            String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                            if(pathFieldOfCurrentVersion==null && pathInd==1){
                                ret.add(
                                    BoatFactVersionVerifierResponse.instance(
                                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                            String.format("Error in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                             fieldToCalculate.getString("key"),
                                             pathField[pathInd]))
                                );
                                break;
                            }else if(pathFieldOfCurrentVersion==null){
                                //WARNING
                                ret.add(
                                    BoatFactVersionVerifierResponse.instance(
                                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_WARNIGS, 
                                            String.format("Warning in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                             fieldToCalculate.getString("key"),
                                             pathField[pathInd]))
                                );
                            }else if(!pathFieldOfCurrentVersion.equals(BoatFactFields.getFieldName(pathField[pathInd]))){
                                ret.add(
                                    BoatFactVersionVerifierResponse.instance(
                                        BoatFactVersionVerifierResponse.BoatFactVersionVerifierResponseValues.THERE_ARE_ERRORS, 
                                            String.format("Warning in parameters of field to calculate named %s. One parameter is a field with wrong name (%s)", 
                                             fieldToCalculate.getString("key"),
                                             pathField[pathInd]))    
                                );
                            }
                        }
                    }
                }
            }
        }
        
    }
    
    public static class BoatFactVersionVerifierResponse{
        public static enum BoatFactVersionVerifierResponseValues{
            CORRECT_VERSION(0),
            NEED_UPDATE(1),
            THERE_ARE_NOT_ERRORS(2),
            THERE_ARE_WARNIGS(4),
            SUMARY(3),
            UNKNOWN_VERSION(-1),
            LARGER_VERSION_THAN_EXPECTED(-2),
            THERE_ARE_ERRORS(-3),
            ;
            private int status;
            BoatFactVersionVerifierResponseValues(int v){
                status=v;
            }
        }
        private BoatFactVersionVerifierResponseValues response;
        private String message;

        public static BoatFactVersionVerifierResponse instance(BoatFactVersionVerifierResponseValues response, String message){
           return new BoatFactVersionVerifierResponse(response, message);
        }
        
        public BoatFactVersionVerifierResponse(BoatFactVersionVerifierResponseValues response, String message) {
            this.response = response;
            this.message = message;
        }
        
        protected void setMessage(String m){
            message=m;
        }
        protected void setResponseValue(BoatFactVersionVerifierResponseValues v){
            response=v;
        }
        public BoatFactVersionVerifierResponseValues getResponseValue(){
            return response;
        }

        public String getMessage(){
            return message;
        }
    }
}
