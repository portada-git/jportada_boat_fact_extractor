package org.elsquatrecaps.portada.boatfactextractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.elsquatrecaps.autonewsextractor.error.AutoNewsRuntimeException;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;
import org.elsquatrecaps.autonewsextractor.tools.formatter.JsonFileFormatterForExtractedData;

/**
 *
 * @author josepcanellas
 */
public class BoatFactExtractorMainClass {

    public static void main(String[] args) {
//       String[] configArgs = Arrays.copyOfRange(args, 1, args.length);
       String[] configArgs = new String[args.length-1];
       for(int i=1; i<args.length; i++){
           configArgs[i-1]=args[i].trim();
       }
       AutoNewsExtractorConfiguration config = configFromArgs(configArgs);
       BoatFactExtractorMainClass prg = new BoatFactExtractorMainClass();
        switch (args[0]) {
            case "extract_test":
                prg.extractOnlyCommand(config);
                break;
            case "cut_test":
                prg.targetFragmentCutterCommand(config.getOutputFile(), config);
                break;
            case "information_unit_test":
                prg.buildInformationUnitsCommand(config.getOutputFile(), config);
                break;
            case "extract":
                prg.extract(config);
                break;
            default:
                throw new AssertionError();
        }
    }
    
    private static AutoNewsExtractorConfiguration configFromArgs(String[] args){
        AutoNewsExtractorConfiguration config = new AutoNewsExtractorConfiguration();
        try {            
            config.parseArgumentsAndConfigure(args);
        }catch(FileNotFoundException ex){
            throw new AutoNewsRuntimeException("Error: no s'ha trobat el fitxer de configuració! Cal indicar la ruta on es troba.", ex);
        }catch(IOException ex){
            throw new AutoNewsRuntimeException("Error: no es pot llegir el factor de configuració. Verifiqueu els permisos de lectura.", ex);
        }        
        return config;
    }
    
    private void extract(AutoNewsExtractorConfiguration config){
        BoatFactExtractor boatFactExtractor = new BoatFactExtractor();
        boatFactExtractor.init(config).initProcessCallback((param) -> {
            JsonFileFormatterForExtractedData<NewsExtractedData> formatter = new JsonFileFormatterForExtractedData<>(param.getFirst(), true);
            formatter.toFile(String.format("%s_%s",config.getOutputFile(), config.getParseModel()[param.getLast()]));
            return null;
        }).initInfoCallback((param) -> {
            System.out.println(param);
            return null;
        }).extract();
    }
    
    private void buildInformationUnitsCommand(String odir, AutoNewsExtractorConfiguration config){
        BoatFactInformationUnitBuilder builder = new BoatFactInformationUnitBuilder();
        builder.init(config).init(odir).joinFiles();
    }
    
    private void targetFragmentCutterCommand(String odir, AutoNewsExtractorConfiguration config){
        BoatFactCutter boatFactCutter = new BoatFactCutter();
        int models = config.getParseModel().length;
        boatFactCutter.init(config).init(odir);
        for(int i=0; i<models; i++){
            Integer id = i;
            boatFactCutter.cutFiles(id);
        }
    }
    
    private void extractOnlyCommand(AutoNewsExtractorConfiguration config){
        List<NewsExtractedData> result = new ArrayList<>();
        BoatFactExtractor boatFactExtractor = new BoatFactExtractor();
        boatFactExtractor.init(config);
        try{
            int models = config.getParseModel().length;
            for(int i=0; i<models; i++){
                result = boatFactExtractor.extractOnly(i);
                JsonFileFormatterForExtractedData<NewsExtractedData> formatter = new JsonFileFormatterForExtractedData<>(result, true);
                formatter.toFile(String.format("%s_%s",config.getOutputFile(), config.getParseModel()[i]));
            }                        
        } catch (AutoNewsRuntimeException ex) {
            System.out.println(String.format("Error: %s", ex.getMessage()));
        }
    }
}
