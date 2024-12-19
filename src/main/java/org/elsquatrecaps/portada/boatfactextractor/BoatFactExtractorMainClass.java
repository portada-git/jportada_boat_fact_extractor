package org.elsquatrecaps.portada.boatfactextractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elsquatrecaps.autonewsextractor.error.AutoNewsRuntimeException;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;
import org.elsquatrecaps.autonewsextractor.tools.formatter.BoatFactCsvFormatter;
import org.elsquatrecaps.autonewsextractor.tools.formatter.JsonFileFormatterForExtractedData;
import org.json.JSONObject;

/**
 *
 * @author josepcanellas
 */
public class BoatFactExtractorMainClass {

    public static void main(String[] args) {
       BoatFactExtractorMainClass prg = new BoatFactExtractorMainClass();
       String[] configArgs = new String[args.length-1];
       for(int i=1; i<args.length; i++){
           configArgs[i-1]=args[i].trim();
       }
       if(args[0].equalsIgnoreCase("field_info")){
           String pars;
           switch (configArgs.length) {
               case 1:
                   pars = configArgs[0];
                   break;
               case 2:
                   pars = configArgs[1];
                   break;               
               default:
                   //errror
                   throw new AssertionError();
           }
           prg.printFieldInfo(pars);
       }else{
            AutoNewsExtractorConfiguration config = configFromArgs(configArgs);
             switch (args[0].toLowerCase()) {
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
        boatFactExtractor.initConfig(config).initProcessListDataCallback((param) -> {
            JsonFileFormatterForExtractedData<NewsExtractedData> formatter = new JsonFileFormatterForExtractedData<>(param.getFirst(), true);
            String fn = String.format("%s_%s",config.getOutputFile(), config.getParseModel()[param.getLast()]);
            formatter.toFile(fn);
            JSONObject csvParams = readConfigCsv(config, param.getLast());
            if(csvParams!=null){
                BoatFactCsvFormatter csvFormatter = new BoatFactCsvFormatter();
                List d = param.getFirst();
                csvFormatter.configHeaderFields(csvParams).format(d).toFile(fn);
            }
            return null;
        }).initInfoCallback((param) -> {
            System.out.println(param);
            return null;
        }).extract();
    }
    
    private JSONObject readConfigCsv(AutoNewsExtractorConfiguration configuration, int parserId){
        JSONObject ret = null;
        JSONObject jsonCgf = null;          
        try {
            String jsc = Files.readString(Paths.get((String) configuration.getAttr("parser_config_json_file")));
            jsonCgf = new JSONObject(jsc);
            JSONObject parser = jsonCgf.optJSONObject(configuration.getParseModel()[parserId]);
            if(parser!=null){            
                ret = parser.optJSONObject("csv_view");
            }
        } catch (IOException ex) {}
        return ret;    
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
        boatFactExtractor.initConfig(config);
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

    private void printFieldInfo(String infoType) {
        System.out.println("                 FIELD INFO                      ");
        infoType = infoType.toUpperCase();
        String sep = "";
        if(infoType.indexOf('A')>-1){
            System.out.println(BoatFactFields.getFieldInformation());
        }else{
            if(infoType.indexOf('V')>-1){
                System.out.println("======================================================================");
                System.out.print("CURRENT VERSION: ");
                System.out.println(BoatFactFields.getCurrentModelVersion());
                sep = "----------------------------------------------------------------------\n";
            }
            if(infoType.indexOf('D')>-1){
                System.out.print(sep);
                System.out.println(BoatFactFields.getDescriptionOfFields());
                sep = "----------------------------------------------------------------------\n";
            }
            if(infoType.indexOf('C')>-1){
                System.out.print(sep);
                System.out.println(BoatFactFields.getChangeOfVersions());
            }
        }
    }
}
