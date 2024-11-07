package org.elsquatrecaps.autonewsextractor.model;

/**
 *
 * @author josep
 */
public class ShipCargoInfo extends MutableNewsExtractedData{

    public String getMerchant(){
        return get(BoatFactFields.CARGO_MERCHANT.toString());
    }

    public String getProduct(){
        return get(BoatFactFields.CARGO_PRODUCT.toString());
    }
    
    public String getQuantity(){
        return get(BoatFactFields.CARGO_QUANTITY.toString());
    }
    
    public String getUnit(){
        return get(BoatFactFields.CARGO_UNIT.toString());
    }
    
    public String getOrigin(){
        return get(BoatFactFields.CARGO_ORIGIN.toString());
    }
    
    public String getDestination(){
        return get(BoatFactFields.CARGO_DESTINATION.toString());
    }
    
    @Override
    public String toString() {
        String ret = String.format("%s:%s, %s:%s, %s:%s, %s:%s", 
                BoatFactFields.CARGO_MERCHANT, getMerchant(), 
                BoatFactFields.CARGO_PRODUCT, getProduct(),
                BoatFactFields.CARGO_QUANTITY, getQuantity(),
                BoatFactFields.CARGO_UNIT, getUnit()
        );
        if(!getOrigin().isEmpty()){
            ret = ret.concat(String.format(", %s:%s", BoatFactFields.CARGO_ORIGIN, getOrigin()));
        }
        if(!getDestination().isEmpty()){
            ret = ret.concat(String.format(", %s:%s", BoatFactFields.CARGO_DESTINATION, getDestination()));
        }
        return ret;
    }
    
}
