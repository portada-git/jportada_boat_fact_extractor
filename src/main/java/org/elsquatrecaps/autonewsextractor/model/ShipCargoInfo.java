package org.elsquatrecaps.autonewsextractor.model;

/**
 *
 * @author josep
 */
public class ShipCargoInfo extends MutableNewsExtractedData{

    public String getMerchant(){
        return get(BoatFactFields.CARGO_MERCHANT_NAME.toString());
    }

    public String getProduct(){
        return get(BoatFactFields.CARGO_COMMODITY.toString());
    }
    
    public String getQuantity(){
        return get(BoatFactFields.CARGO_QUANTITY.toString());
    }
    
    public String getUnit(){
        return get(BoatFactFields.CARGO_UNIT.toString());
    }
    
    public String getOrigin(){
        return get(BoatFactFields.CARGO_PORT_ORIGIN.toString());
    }
    
    public String getDestination(){
        return get(BoatFactFields.CARGO_PORT_DESTINATION.toString());
    }
    
    @Override
    public String toString() {
        String ret = String.format("%s:%s, %s:%s, %s:%s, %s:%s", 
                BoatFactFields.CARGO_MERCHANT_NAME, getMerchant(), 
                BoatFactFields.CARGO_COMMODITY, getProduct(),
                BoatFactFields.CARGO_QUANTITY, getQuantity(),
                BoatFactFields.CARGO_UNIT, getUnit()
        );
        if(!getOrigin().isEmpty()){
            ret = ret.concat(String.format(", %s:%s", BoatFactFields.CARGO_PORT_ORIGIN, getOrigin()));
        }
        if(!getDestination().isEmpty()){
            ret = ret.concat(String.format(", %s:%s", BoatFactFields.CARGO_PORT_DESTINATION, getDestination()));
        }
        return ret;
    }
    
}
