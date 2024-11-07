package org.elsquatrecaps.portada.boatfactextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.elsquatrecaps.autonewsextractor.informationunitbuilder.reader.InfromationUnitBuilderProxyClass;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;

/**
 *
 * @author josep
 */
public class BoatFactInformationUnitBuilder {
    AutoNewsExtractorConfiguration configuration;
    String outputDir;
    
    public BoatFactInformationUnitBuilder init(AutoNewsExtractorConfiguration config){
        configuration = config;
        return this;
    }
    
    public BoatFactInformationUnitBuilder init(String odir){
        outputDir = odir;
        return this;
    }
    
    public void joinFiles(){
        InfromationUnitBuilderProxyClass instance = InfromationUnitBuilderProxyClass.getInstance("file_name", "portada_file_name", configuration);
        instance.createAndProcessEachInformationUnitFiles((param) -> {
            String oFileName = param.getFileNames().get(0);
            String ext;
            String text = param.getInformationUnitText();
            int dotIndex = oFileName.lastIndexOf('.');
            ext =  (dotIndex == -1) ? "" : oFileName.substring(dotIndex, oFileName.length());
            oFileName =  (dotIndex == -1) ? oFileName : oFileName.substring(0, dotIndex);
            try {
                File dir = new File(outputDir);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                Files.writeString(Paths.get(outputDir, String.format("%s_%s%s", oFileName, "informationUnit", ext)), text, StandardOpenOption.CREATE);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            return null;
        }, BoatFactFields.getCurrentModelVersion());
        
    }

    
}
