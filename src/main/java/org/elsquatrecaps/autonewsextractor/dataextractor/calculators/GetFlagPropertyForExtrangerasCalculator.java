/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.autonewsextractor.dataextractor.calculators;

import org.elsquatrecaps.autonewsextractor.tools.RegexBuilder;

/**
 *
 * @author josep
 */
@DataExtractorCalculatorMarkerAnnotation(id = "GetFlagPropertyForExtrangerasCalculator")
public class GetFlagPropertyForExtrangerasCalculator  extends RegexCalculator<String>{
    private static final int SHIP_FLAG=0;
    private static final int SHIP_TYPE=1;

    @Override
    public String calculate(Object[] params) {        
        String shipFlag = ((String)params[SHIP_FLAG]).trim();
        String shipType = ((String)params[SHIP_TYPE]).trim();
        String ret=shipFlag;
        if(shipFlag.matches(RegexBuilder.getInstance(this.getBasePath(), this.getSearchPath(), this.getVariant()).getStrPatternFromFile("extrangeras"))){
            String[] aret = shipType.split("\\s+");
            if (aret.length>1){
                ret = aret[aret.length-1];
            }
        }
        return ret;

    }
    
}
