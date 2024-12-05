package org.elsquatrecaps.portada.boatfactextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.elsquatrecaps.autonewsextractor.dataextractor.parser.MainAutoNewsExtractorParser;
import org.elsquatrecaps.autonewsextractor.error.AutoNewsRuntimeException;
import org.elsquatrecaps.autonewsextractor.informationunitbuilder.reader.InfromationUnitBuilderProxyClass;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.autonewsextractor.model.PublicationInfo;
import org.elsquatrecaps.autonewsextractor.targetfragmentbreaker.cutter.TargetFragmentCutterProxyClass;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;
import org.elsquatrecaps.utilities.tools.Callback;
import org.elsquatrecaps.utilities.tools.Pair;
import org.json.JSONObject;

/**
 *
 * @author josepcanellas
 */
public class BoatFactExtractor {
    AutoNewsExtractorConfiguration configuration;
    Callback<Pair<List<NewsExtractedData>, Integer>, Void> processCallback;
    Callback<String, Void> infoCallback;
    
    public BoatFactExtractor init(AutoNewsExtractorConfiguration config){
        configuration = config;
        return this;
    }
    
    public BoatFactExtractor initProcessCallback(Callback<Pair<List<NewsExtractedData>, Integer>, Void> callback){
        processCallback = callback;
        return this;
    }
    
    public BoatFactExtractor initInfoCallback(Callback<String, Void> callback){
        infoCallback = callback;
        return this;
    }
    
    public void extract(){
        final List<NewsExtractedData> extractDataList = new ArrayList<>();
        InfromationUnitBuilderProxyClass builder = InfromationUnitBuilderProxyClass.getInstance("file_name", "portada_file_name", configuration);
        TargetFragmentCutterProxyClass cutter = TargetFragmentCutterProxyClass.getInstance(configuration.getFragmentBreakerApproach(), configuration);
        MainAutoNewsExtractorParser parser = MainAutoNewsExtractorParser.getInstance(configuration);  
        JSONObject jsonConfig;
        try{
            String jsc = Files.readString(Paths.get((String) configuration.getAttr("parser_config_json_file")));
            jsonConfig = new JSONObject(jsc);     
            BoatFactVersionUpdater.BoatFactVersionUpdaterResponse r = BoatFactVersionUpdater.tryToUpdate(jsonConfig);
            if(!r.isError()){
                try{
                    int parseModels = configuration.getParseModel().length;
                    for(int i=0; i<parseModels; i++){
                        final int id = i;
                        extractDataList.clear();                
                        builder.createAndProcessEachInformationUnitFiles((param) -> {
                            try{
                                String text = param.getInformationUnitText();
                                text = cutter.init(id).getTargetTextFromText(text);                    
                                extractDataList.addAll(parser.parseFromString(text, id, param.getPublicationInfo()));
                            }catch(RuntimeException ex){
                                infoCallback.call(ex.getMessage());
                            }
                            return null;
                        }, BoatFactFields.getCurrentModelVersion());
                        processCallback.call(new Pair<>(extractDataList, i));
                    }
                    if(r.equals(BoatFactVersionUpdater.BoatFactVersionUpdaterResponse.JSON_UPDATED) || r.equals(BoatFactVersionUpdater.BoatFactVersionUpdaterResponse.JSON_UPDATED_WITH_WARNIGS)){
                        //save file
                        Files.writeString(
                            Paths.get((String) configuration.getAttr("parser_config_json_file")), 
                            jsonConfig.toString(4)
                        );
                    }
                }catch(RuntimeException ex){
                    //raload
                    jsc = Files.readString(Paths.get((String) configuration.getAttr("parser_config_json_file")));
                    jsonConfig = new JSONObject(jsc); 
                    List<BoatFactVersionVerifier.BoatFactVersionVerifierResponse> l = BoatFactVersionVerifier.verify(jsonConfig);
                    if(l.size()>1){
                        infoCallback.call("Error due to version of config json file.\n\n".concat(ex.getMessage())); //TO DO CHANGE THE MESSAGE 
                    }else{
                        infoCallback.call(ex.getMessage());
                    }
                    ex.printStackTrace();
                }
            }else{
                String m;
                switch (r) {
                    case JSON_IS_NOT_UPDATABLE:
                        m = "Update the config JSON is not possible automatically";
                        break;
                    case UNKNOWN_VERSION:
                        m = "Inknown version of config JSON. It is not possible update automatically";
                        break;
                    default:
                        m = "Unknown error trying to update the config JSON";
                }
                infoCallback.call(m);

            }
        }catch (IOException ex) {
            String filename = configuration.getAttr("parser_config_json_file");
            String message = String.format("Error: The file \"%s\" can not be read. please revise it", filename);
            infoCallback.call(message);
        }        
    }
    
    
    public List<NewsExtractedData> extractOnly(int id){
        String date;
        String text;        
        List<NewsExtractedData> result = new ArrayList<>();        
        File dir = (new File(configuration.getOriginDir())).getAbsoluteFile();
        File[] files = dir.listFiles((file) ->{
            boolean ret = file.isFile();
            ret = ret && file.getPath().endsWith(configuration.getFileExtension());
            return ret;
        });

        try {
            for(File file: files){
                if(file.getName().matches("\\d{4}_\\d{2}_\\d{2}.*")){
                    date = file.getName().substring(0, 10);
                }else{
                    date = "1855_11_09";
                }
                text = Files.readString(file.toPath());
                PublicationInfo pi = new PublicationInfo(BoatFactFields.getCurrentModelVersion(), date);
                result.addAll(extractFromText(text, id, pi));
            }
        } catch (RuntimeException | IOException ex) {
            throw new AutoNewsRuntimeException(ex);
        }
        return result;
    }
    
    public List<NewsExtractedData> extractFromText(String text, int id, PublicationInfo defData){
        List<NewsExtractedData> ret=null;
        MainAutoNewsExtractorParser instance = new MainAutoNewsExtractorParser();
        instance.init(configuration);        
        JSONObject jsonConfig;
        try{
            String jsc = Files.readString(Paths.get((String) configuration.getAttr("parser_config_json_file")));
            jsonConfig = new JSONObject(jsc);     
            BoatFactVersionUpdater.BoatFactVersionUpdaterResponse r = BoatFactVersionUpdater.tryToUpdate(jsonConfig);
            if(!r.isError()){
                ret = instance.parseFromString(text, id, defData);
            }else{
                //error
            }
        }catch (IOException ex) {
            String filename = configuration.getAttr("parser_config_json_file");
            String message = String.format("Error: The file \"%s\" can not be read. please revise it", filename);
            //ERRROR
        }  
        return ret;
    }
}
