package org.elsquatrecaps.portada.boatfactextractor;

import java.util.List;
import org.elsquatrecaps.autonewsextractor.dataextractor.parser.MainAutoNewsExtractorParser;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.utilities.tools.configuration.Configuration;

/**
 *
 * @author josepcanellas
 */
public class BoatFactExtractor {
    Configuration configuration;
    
    public void init(Configuration config){
        configuration = config;
    }
    
    public List<NewsExtractedData> extractFromText(String text, String date){
        MainAutoNewsExtractorParser instance = new MainAutoNewsExtractorParser();
        instance.init(configuration);
        List<NewsExtractedData> result = instance.parseFromString(text, date);
        return result;
    }
}
