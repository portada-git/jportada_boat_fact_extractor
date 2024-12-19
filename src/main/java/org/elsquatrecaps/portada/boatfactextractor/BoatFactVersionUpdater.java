/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.portada.boatfactextractor;

import java.util.HashMap;
import java.util.Map;
import org.elsquatrecaps.autonewsextractor.dataextractor.calculators.DataExtractorCalculatorProxyClass;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author josep
 */
public class BoatFactVersionUpdater extends BoatFactVersionVerifier {
    
    public static BoatFactVersionUpdaterResponse tryToUpdate(JSONObject jsonConfiguration){
        BoatFactVersionUpdaterResponse ret =  BoatFactVersionUpdaterResponse.CORRECT_VERSION;
        BoatFactVersionUpdater updater = new BoatFactVersionUpdater();
        for(String key: jsonConfiguration.keySet()){
            ret = updater.tryToUpdateOnModel(jsonConfiguration.getJSONObject(key));
            if(ret.isError()){
                break;
            }
        }        
        return ret;
    }
    
    protected BoatFactVersionUpdaterResponse tryToUpdateOnModel(JSONObject jsonConfiguration){
        BoatFactVersionUpdaterResponse ret =  BoatFactVersionUpdaterResponse.NOT_PROCESSED;
        int comparev = compareVersions(BoatFactFields.getCurrentModelVersion(), 
                                        jsonConfiguration.getString("field_version"));
        switch (comparev) {
            case 0:
                ret = BoatFactVersionUpdaterResponse.CORRECT_VERSION;
                break;
            case Integer.MIN_VALUE:
                ret = BoatFactVersionUpdaterResponse.UNKNOWN_VERSION;
                break;
            default:
                if(comparev<0){
                    ret = BoatFactVersionUpdaterResponse.LARGER_VERSION_THAN_EXPECTED;
                    break;
                }
                int pos = 0;
                do{
                    JSONObject configApproach = jsonConfiguration.getJSONArray("config").getJSONObject(pos);
                    switch(configApproach.getString("approach_type")){
                        case "regex":
                            ret = updateExtractFieldNamesForRegex(
                                    configApproach.getJSONObject("configuration").optJSONArray("fields_to_extract"));
                            if(!ret.isError()){
                                ret = updateCalculateFieldNamesForRegex(
                                        configApproach.getJSONObject("configuration").optJSONArray("fields_to_calculate"));
                            }
                            break;
                        case "openai":
                            ret = updateExtractFieldNamesForOpenAI(
                                    configApproach.getJSONObject("configuration").optJSONArray("fields_to_extract"));
                    }
                    pos++;
                }while(!ret.isError() && pos < jsonConfiguration.getJSONArray("config").length());   
                if(!ret.isError()){
                    jsonConfiguration.put("field_version", BoatFactFields.getCurrentModelVersion());
                }
                break;
        }        
        return ret;
    }
    
    protected BoatFactVersionUpdaterResponse updateExtractFieldNamesForOpenAI(JSONArray fieldsToExtract){
        if(fieldsToExtract==null){
            return BoatFactVersionUpdaterResponse.JSON_UPDATED;
        }
        throw new UnsupportedOperationException();
    }
    
    protected BoatFactVersionUpdaterResponse updateExtractFieldNamesForRegex(JSONArray fieldsToExtract){
        BoatFactVersionUpdaterResponse ret = BoatFactVersionUpdaterResponse.JSON_UPDATED;
        if(fieldsToExtract==null){
            return ret;
        }
        for(int i=0; ret==BoatFactVersionUpdaterResponse.JSON_UPDATED && i< fieldsToExtract.length(); i++){
            JSONObject fieldToExtract = fieldsToExtract.getJSONObject(i);
            String keyName = fieldToExtract.getString("key");
            if(fieldToExtract.optBoolean("temporary_field", temporaryFileds.contains(keyName))){
                temporaryFileds.add(keyName);
            }else{
                String newName = BoatFactFields.getFieldName(keyName);
                if(!keyName.equals(newName)){
                    fieldToExtract.put("key", newName);
                }else if(newName==null){
                    ret = BoatFactVersionUpdaterResponse.JSON_IS_NOT_UPDATABLE;
                }
            }
        }
        return ret;
    }
    
    protected BoatFactVersionUpdaterResponse updateCalculateFieldNamesForRegex(JSONArray fieldsToCalculate){
        BoatFactVersionUpdaterResponse ret = BoatFactVersionUpdaterResponse.JSON_UPDATED;
        if(fieldsToCalculate==null){
            return ret;
        }
        for(int i=0; !ret.isError() && i< fieldsToCalculate.length(); i++){
            JSONObject fieldToCalculate = fieldsToCalculate.getJSONObject(i);
            String keyName = fieldToCalculate.getString("key");
            if(fieldToCalculate.optBoolean("temporary_field", temporaryFileds.contains(keyName))){
                temporaryFileds.add(keyName);
            }else{
                String newName = BoatFactFields.getFieldName(keyName);
                if(!keyName.equals(newName)){
                    fieldToCalculate.put("key", newName);
                }else if(newName==null){
                    ret = BoatFactVersionUpdaterResponse.JSON_IS_NOT_UPDATABLE;
                }
            }
            if(!ret.isError()){
                ret = updateCalculateParamsForRegex(fieldToCalculate);
            }
        }
        return ret;
    }
    
    protected BoatFactVersionUpdaterResponse updateCalculateParamsForRegex(JSONObject fieldToCalculate){
        BoatFactVersionUpdaterResponse ret = BoatFactVersionUpdaterResponse.JSON_UPDATED;
        boolean temporary;
        JSONArray params;
        params = fieldToCalculate.optJSONArray("fieldParams");
        if(params!=null){
            for(int i=0; !ret.isError() && i<params.length(); i++){
                String[] pathField = params.getString(i).split("\\.");
                boolean ch = false;
                for(int pathInd=1; pathInd<pathField.length; pathInd++){
                    temporary = temporaryFileds.contains(pathField[pathInd]);
                    if(!temporary){
                        String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                        if(pathFieldOfCurrentVersion==null && pathInd==1){
                            //ERROR IRREPARABLE
                            ret = BoatFactVersionUpdaterResponse.JSON_IS_NOT_UPDATABLE;
                            break;
                        }else if(pathFieldOfCurrentVersion==null){
                            //WARNING
                            ret = BoatFactVersionUpdaterResponse.JSON_UPDATED_WITH_WARNIGS;
                        }else if(!pathFieldOfCurrentVersion.equals(pathField[pathInd])){
                            //OK
                            pathField[pathInd] = pathFieldOfCurrentVersion;
                            ch = true;
                        }
                    }
                }
                if(ch){
                    params.put(i, String.join(".", pathField));
                }
            }
        }
        if(!ret.isError()){
            params = fieldToCalculate.optJSONArray("literalParams");
            if(!ret.equals(BoatFactVersionUpdaterResponse.JSON_UPDATED_WITH_WARNIGS) && params!=null){
                for(int i=0; !ret.isError() && i<params.length(); i++){
                    String value = params.optString(i);
                    if(!value.isEmpty()){
                        String fieldOfCurrentVersion = BoatFactFields.getFieldName(value);
                        if(fieldOfCurrentVersion!=null && !fieldOfCurrentVersion.equals(value)){
                            ret = BoatFactVersionUpdaterResponse.JSON_UPDATED_WITH_WARNIGS;
                        }
                    }
                }
            }
            params = fieldToCalculate.optJSONArray("params");
            if(params!=null){
                for(int i=0; !ret.isError() && i<params.length(); i++){
                    JSONObject param = params.getJSONObject(i);
                    if(param.getString("type").equals(DataExtractorCalculatorProxyClass.FIELD_VALUE_PARAM_TYPE)){
                        String[] pathField = param.getString("value").split("\\.");
                        boolean ch = false;
                        for(int pathInd=1; pathInd<pathField.length; pathInd++){
                            temporary = temporaryFileds.contains(pathField[pathInd]);
                            if(!temporary){
                                String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                                if(pathFieldOfCurrentVersion==null && pathInd==1){
                                    //ERROR IRREPARABLE
                                    ret = BoatFactVersionUpdaterResponse.JSON_IS_NOT_UPDATABLE;
                                    break;
                                }else if(pathFieldOfCurrentVersion==null){
                                    //WARNING
                                    ret = BoatFactVersionUpdaterResponse.JSON_UPDATED_WITH_WARNIGS;
                                }else if(!pathFieldOfCurrentVersion.equals(pathField[pathInd])){
                                    //OK
                                    pathField[pathInd] = pathFieldOfCurrentVersion;
                                    ch = true;
                                }
                            }
                        }
                        if(ch){
                            param.put("value", String.join(".", pathField));
                        }                        
                    }
                    
                    if(param.getString("type").equals(DataExtractorCalculatorProxyClass.FIELD_NAME_PARAM_TYPE)){
                        String[] pathField = param.getString("value").split("\\.");
                        int start = 1;
                        if (pathField.length==1){
                            start = 0;
                        }
                        boolean ch = false;
                        for(int pathInd=start; pathInd<pathField.length; pathInd++){
                            temporary = temporaryFileds.contains(pathField[pathInd]);
                            if(!temporary){
                                String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                                if(pathFieldOfCurrentVersion==null && pathInd==1){
                                    //ERROR IRREPARABLE
                                    ret = BoatFactVersionUpdaterResponse.JSON_IS_NOT_UPDATABLE;
                                    break;
                                }else if(pathFieldOfCurrentVersion==null){
                                    //WARNING
                                    ret = BoatFactVersionUpdaterResponse.JSON_UPDATED_WITH_WARNIGS;
                                }else if(!pathFieldOfCurrentVersion.equals(pathField[pathInd])){
                                    //OK
                                    pathField[pathInd] = pathFieldOfCurrentVersion;
                                    ch = true;
                                }
                            }
                        }
                        if(ch){
                            param.put("value", String.join(".", pathField));
                        }                        
                    }
                }
            }
        }
        return ret;
    }
    
    public static enum BoatFactVersionUpdaterResponse{
        CORRECT_VERSION(0),
        JSON_UPDATED(1),
        JSON_UPDATED_WITH_WARNIGS(2),
        NOT_PROCESSED(3),
        UNKNOWN_VERSION(-1),
        LARGER_VERSION_THAN_EXPECTED(-2),
        JSON_IS_NOT_UPDATABLE(-3),
        ;
        private int status;
        BoatFactVersionUpdaterResponse(int v){
            status=v;
        }
        transient 
        private static Map<Integer, BoatFactVersionUpdaterResponse> map = new HashMap<>();
        static {
            for(BoatFactVersionUpdaterResponse r: BoatFactVersionUpdaterResponse.values()){
                map.put(r.status, r);
            }
        }
        
        
        public static BoatFactVersionUpdaterResponse getFromStatus(int status){
            return map.get(status);
        }
        
        public boolean isError(){
            return status<0;
        }
        
        public int getStatus(){
            return status;
        }
    }
}
