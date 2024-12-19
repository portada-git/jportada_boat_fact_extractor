package org.elsquatrecaps.portada.boatfactextractor;

import org.elsquatrecaps.autonewsextractor.model.PublicationInfo;

/**
 *
 * @author josep
 */
public class TextParserInfo {
    private final String textToParse;
    private final int parserId;
    private final PublicationInfo publicationInfo;
    private String informationUnitName;
    private float completedRatio;
    

    public TextParserInfo(String textToParse, int parserId, PublicationInfo publicationInfo, String informationUnitName, float completedRatio) {
        this.textToParse = textToParse;
        this.parserId = parserId;
        this.publicationInfo = publicationInfo;
        this.informationUnitName = informationUnitName;
        this.completedRatio = completedRatio;
    }

    public String getTextToParse() {
        return textToParse;
    }

    public int getParserId() {
        return parserId;
    }

    public PublicationInfo getPublicationInfo() {
        return publicationInfo;
    }

    public String getInformationUnitName() {
        return informationUnitName;
    }

    public float getCompletedRatio() {
        return completedRatio;
    }
    
}
