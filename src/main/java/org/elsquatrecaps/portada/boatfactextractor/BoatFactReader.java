package org.elsquatrecaps.portada.boatfactextractor;

import java.util.ArrayList;
import java.util.List;
import org.elsquatrecaps.autonewsextractor.informationunitbuilder.reader.InfromationUnitBuilderProxyClass;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.autonewsextractor.tools.configuration.InformationUnitBuilderrConfiguration;
import org.elsquatrecaps.utilities.tools.Callback;

/**
 *
 * @author josep
 */
public class BoatFactReader {
    //AutoNewsExtractorConfiguration configuration;
    Callback<TextParserInfo, List<NewsExtractedData>> processInformationUnitCallback;
    String joinerType=null;
    String metadataSource=null;
    InformationUnitBuilderrConfiguration config;

    

//    public BoatFactReader initConfig(AutoNewsExtractorConfiguration config){
//        configuration = config;
//        return this;
//    }
    
    public BoatFactReader initInfoUnitBuilderConfig(InformationUnitBuilderrConfiguration cfg){
        config = cfg;
        if(joinerType==null){
            joinerType = config.getInformationUnitBuilderType();
        }
        if(metadataSource==null){
            metadataSource = config.getMetadataSource();
        }
        return this;
    }
    
    public BoatFactReader initJoinerType(String joinerType){
        this.joinerType = joinerType;
        return this;
    }
    
    public BoatFactReader initMetadataSource(String metadataSource){
        this.metadataSource = metadataSource;
        return this;
    }
    
    public BoatFactReader initProcessInformationUnitCallback(Callback<TextParserInfo, List<NewsExtractedData>> callback){
        processInformationUnitCallback = callback;
        return this;
    }
    

    public List<NewsExtractedData>  processFiles(String originDir, String extension, int parserModelId, Callback<TextParserInfo, List<NewsExtractedData>> callback){
        final List<NewsExtractedData> extractDataList = new ArrayList<>();        
        InfromationUnitBuilderProxyClass builder = InfromationUnitBuilderProxyClass
                .getInstance(joinerType, metadataSource, config);
        final int id = parserModelId;
        builder.createAndProcessEachInformationUnitFiles((param) -> {
            List<NewsExtractedData> data = callback.call(
                new TextParserInfo(
                    param.getInformationUnitText(),
                    id, 
                    param.getPublicationInfo(),
                    param.getInfomationUnitName(),
                    param.getCompletedRatio()
                )
            );
            if(data!=null){
                extractDataList.addAll(data);
            }
            return null;
        }, originDir, extension, BoatFactFields.getCurrentModelVersion());
        return extractDataList;
    }
    
    public List<NewsExtractedData>  processFiles(String originDir, String extension, int parserModelId){
        return processFiles(originDir, extension, parserModelId, processInformationUnitCallback);
    }
}
