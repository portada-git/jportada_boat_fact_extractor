/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.elsquatrecaps.autonewsextractor.tools.RegexBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author josepcanellas
 */
@DataExtractorCalculatorMarkerAnnotation(id = "ReplaceIdemByValueInItemFromListCalculator")
public class ReplaceIdemByValueInItemFromListCalculator extends RegexCalculator<JSONArray>{
    public static final int VALUE_TO_CHECK=0;
    public static final int DEFAULT_VALUE_FOR_REPLACING=1;
    
    public JSONArray calculate(Object[] param) {
        JSONArray ret = null;
        if(!isEmptyParam(param[VALUE_TO_CHECK])){
            JSONArray forReplacing;
            JSONArray toCheck = (JSONArray) param[VALUE_TO_CHECK];
            if(!isEmptyParam(param[DEFAULT_VALUE_FOR_REPLACING])){
                forReplacing = (JSONArray) param[DEFAULT_VALUE_FOR_REPLACING];
            }else{
                forReplacing = null;
            }
            JSONObject defaultReplacing = null;
            if(!isEmptyParam(forReplacing)
                    && forReplacing.length()>1 
                    && forReplacing.optJSONObject(0)!=null
                    && forReplacing.getJSONObject(0).has("cargo")
                    && forReplacing.getJSONObject(0).getJSONArray("cargo").length()>0){
                defaultReplacing = forReplacing.getJSONObject(0).getJSONArray("cargo").getJSONObject(forReplacing.getJSONObject(0).getJSONArray("cargo").length()-1);                                
                if(toCheck.length()>0 && toCheck.getJSONObject(0).getJSONArray("cargo").length()>0){
                    toCheck.getJSONObject(0).getJSONArray("cargo").put(0, 
                        replaceIdem(toCheck.getJSONObject(0).getJSONArray("cargo").getJSONObject(0), 
                            defaultReplacing));
                }
            }
            defaultReplacing = null;
            for(int i=0; i<toCheck.length(); i++){
                JSONArray cargo = toCheck.getJSONObject(i).getJSONArray("cargo");
                for(int j = 0; j<cargo.length(); j++){
                    if(defaultReplacing!=null){
                        cargo.put(j, replaceIdem(cargo.getJSONObject(j), defaultReplacing));
                    }
                    defaultReplacing = cargo.getJSONObject(j);
                }
            }
            ret = toCheck;
        }
        return ret;
    }
    
    private JSONObject replaceIdem(JSONObject value, JSONObject toReplace){
        value.put(BoatFactFields.CARGO_QUANTITY.toString(), replaceIdem(value.getString(BoatFactFields.CARGO_QUANTITY.toString()), toReplace.getString(BoatFactFields.CARGO_QUANTITY.toString())));
        value.put(BoatFactFields.CARGO_UNIT.toString(), replaceIdem(value.getString(BoatFactFields.CARGO_UNIT.toString()), toReplace.getString(BoatFactFields.CARGO_UNIT.toString())));
        value.put(BoatFactFields.CARGO_COMMODITY.toString(), replaceIdem(value.getString(BoatFactFields.CARGO_COMMODITY.toString()), toReplace.getString(BoatFactFields.CARGO_COMMODITY.toString())));
        return value;
    }
    
    private String replaceIdem(String backTime, String r){
        String ret=backTime;
        String strIdemPattern = RegexBuilder.getInstance(this.getBasePath(), this.getSearchPath(), this.getVariant()).getStrPatternFromFile("idem");

        if(backTime.matches(strIdemPattern)){   
            ret = r;
        }    
        return ret;
    }   
}
