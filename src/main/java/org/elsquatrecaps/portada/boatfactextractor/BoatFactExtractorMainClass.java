package org.elsquatrecaps.portada.boatfactextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;
import org.elsquatrecaps.autonewsextractor.tools.formatter.BoatFactCsvFormatter;
import org.elsquatrecaps.autonewsextractor.tools.formatter.GenericCsvFileFormatter;

/**
 *
 * @author josepcanellas
 */
public class BoatFactExtractorMainClass {

    public static void main(String[] args) {
       BoatFactExtractorMainClass prg = new BoatFactExtractorMainClass();
       prg.run(args);
    }
    
    private void run(String[] args){
        BoatFactExtractor boatFactExtractor = new BoatFactExtractor();
        String text;
        AutoNewsExtractorConfiguration config = new AutoNewsExtractorConfiguration();
        config.parseArgumentsAndConfigure(args);
        boatFactExtractor.init(config);
        File dir = (new File(config.getOriginDir())).getAbsoluteFile();
        try {
            text = Files.readString(dir.listFiles()[0].toPath());
            List<NewsExtractedData> result = boatFactExtractor.extractFromText(text, "1855_11_09");
            GenericCsvFileFormatter csvf = new BoatFactCsvFormatter();
            csvf.format(result).toFile(config.getOutputFile());
        
        } catch (IOException ex) {
            Logger.getLogger(BoatFactExtractorMainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
