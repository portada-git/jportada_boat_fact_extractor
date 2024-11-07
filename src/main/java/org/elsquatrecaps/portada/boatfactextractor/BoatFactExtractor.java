package org.elsquatrecaps.portada.boatfactextractor;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.json.JSONArray;
import org.json.JSONException;
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
        
        try{
            Pair<Boolean, String> r = verifyExtractorConfigJsonFile();
            if(r.getFirst()){
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
                }catch(RuntimeException ex){
                    infoCallback.call(ex.getMessage());
                }
            }else{
                infoCallback.call(r.getLast());
            }
        }catch(RuntimeException ex){
            //version no compatible
            infoCallback.call(ex.getMessage());
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
        MainAutoNewsExtractorParser instance = new MainAutoNewsExtractorParser();
        instance.init(configuration);
        verifyOrUpdateExtractorConfigJsonFile(false);
        return instance.parseFromString(text, id, defData);
    }
    
    private Pair<Boolean, String> verifyExtractorConfigJsonFile(){
        Boolean isOk=true;
        String message="OK";
        String jsc=null;   
        JSONObject jsonConfig;
        try{
            jsc = Files.readString(Paths.get((String) configuration.getAttr("parser_config_json_file")));
            jsonConfig = new JSONObject(jsc);        
            Set<String> setKeys = jsonConfig.keySet();
            for(String extractModel: setKeys){
                JSONObject extractorConfigJson = jsonConfig.getJSONObject(extractModel);
                int compare = compareVersions(BoatFactFields.getCurrentModelVersion(), extractorConfigJson.getString("field_version"));
                if(compare==0){
                    //versión OK verificar campos!
                    Pair<Boolean, String> r = compareFields(extractorConfigJson.getJSONArray("config"), extractModel);
                    isOk = r.getFirst();
                    if(!isOk){
                        message = String.format(
                            "ERROR IN A FIELD NAME: %s.\nPlease, revise the file and adapt it following the current version:\n%s", 
                            r.getLast(), 
                            BoatFactFields.getVersionInfo());
                    }
                }else if(compare>0){
                    //versión anterior, mostrar cambios de verisón i pedir adaptación
                    isOk=false;
                    message = String.format(
                            "The field version of the json config file is previous to current version (%s). Please, revise the file and adapt it following the current version:\n%s", 
                            BoatFactFields.getCurrentModelVersion(),
                            BoatFactFields.getVersionInfo());
                }else if(compare==Integer.MIN_VALUE){
                    //versión desconocida. Revisar la versión indicada en el fichero
                    isOk=false;
                    message = String.format(
                            "The field version of the json config file is unknown. Please, revise the file and adapt it following the current version:\n%s", 
                            BoatFactFields.getVersionInfo());
                }else{
                    //error versión config file > current! Revisar la versión indicada en el fichero
                    isOk=false;
                    message = String.format(
                            "The field version of the json config file is greater than the current version (%s). Please, revise the file and adapt it following the current version:\n%s", 
                            BoatFactFields.getCurrentModelVersion(),
                            BoatFactFields.getVersionInfo());
                }
            }            
        }catch (JSONException ex) {
            isOk = false;
            String filename = configuration.getAttr("parser_config_json_file");
            message = String.format("Error: Bad json format in the file \"%s\". please revise it", filename);

        }catch (IOException ex) {
            isOk = false;
            String filename = configuration.getAttr("parser_config_json_file");
            message = String.format("Error: The file \"%s\" can not be read. please revise it", filename);
        }
        return new Pair(isOk, message);
    }
    
    private Pair<Boolean, String> compareFields(JSONArray config, String extractModel){
        String message="OK";
        Set<String> temporaryFileds = new HashSet<>();
        Boolean isOk=true;
        for(int i=0; i< config.length(); i++){
            JSONArray fieldsToExtract = config.getJSONObject(i).getJSONObject("configuration").getJSONArray("fields_to_extract");
            for(int find=0; find<fieldsToExtract.length(); find++){
                String fieldInConfig = fieldsToExtract.getJSONObject(find).getString("key");
                boolean temporary = fieldsToExtract.getJSONObject(find).optBoolean("temporary_field", false);
                if(temporary){
                    temporaryFileds.add(fieldInConfig);
                }else{
                    String fieldOfCurrentVersion = BoatFactFields.getFieldName(fieldInConfig);
                    if(fieldOfCurrentVersion==null || !fieldOfCurrentVersion.equals(fieldInConfig)){
                        //ERROR!
                        isOk=false;
                        String filename = configuration.getAttr("parser_config_json_file");
                        message = String.format(
                                "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                fieldInConfig, extractModel, filename, BoatFactFields.getCurrentModelVersion());
                    }
                }
            } 
            //calculate
            JSONArray fieldsToCalculate = config.getJSONObject(i).getJSONObject("configuration").getJSONArray("fields_to_calculate");
            for(int find=0; find<fieldsToCalculate.length(); find++){
                String fieldInConfig = fieldsToCalculate.getJSONObject(find).getString("key");
                boolean temporary = temporaryFileds.contains(fieldInConfig) || fieldsToCalculate.getJSONObject(find).optBoolean("temporary_field", false);
                if(temporary){
                    temporaryFileds.add(fieldInConfig);
                }else{
                    String fieldOfCurrentVersion = BoatFactFields.getFieldName(fieldInConfig);
                    if(fieldOfCurrentVersion==null || !fieldOfCurrentVersion.equals(fieldInConfig)){
                        //ERROR!
                        isOk = false;
                        String filename = configuration.getAttr("parser_config_json_file");
                        message = String.format(
                                "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                fieldInConfig, extractModel, filename, BoatFactFields.getCurrentModelVersion());
                    }
                }
                if(fieldsToCalculate.getJSONObject(find).has("fieldParams")){
                    JSONArray fieldParameters = fieldsToCalculate.getJSONObject(find).getJSONArray("fieldParams");
                    for(int pind=0; pind<fieldParameters.length(); pind++){
                        String[] pathField = fieldParameters.getString(pind).split("\\.");
                        for(int pathInd=1; pathInd<pathField.length; pathInd++){
                            temporary = temporaryFileds.contains(pathField[pathInd]);
                            if(!temporary){
                                String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                                if(pathFieldOfCurrentVersion==null || !pathFieldOfCurrentVersion.equals(pathField[pathInd])){
                                    //ERROR!
                                    isOk=false;
                                    String filename = configuration.getAttr("parser_config_json_file");
                                    message = String.format(
                                            "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                            pathField[pathInd], extractModel, filename, BoatFactFields.getCurrentModelVersion());
                                }                                
                            }
                        }
                    }
                }
            }
        }
        return new Pair(isOk, message);
    }
    
    private void verifyOrUpdateExtractorConfigJsonFile(boolean verifyOnly){
        boolean changes=false;
        String jsc=null;   
        JSONObject jsonConfig;
        try{
            jsc = Files.readString(Paths.get((String) configuration.getAttr("parser_config_json_file")));
            jsonConfig = new JSONObject(jsc);        
            Set<String> setKeys = jsonConfig.keySet();
            for(String extractModel: setKeys){
                changes = verifyOrUpdateExtractorConfigJson(jsonConfig, extractModel) || changes;
            }            
            if(changes){
                if(verifyOnly){
                    //inform of changes
                    throw new AutoNewsRuntimeException(String.format("incompatible fields with version of fielsd %s were found. Plese revise it. Our porpusse would be:\n%s", BoatFactFields.getCurrentModelVersion(), jsonConfig.toString(4)));
                }else{
                    try{
                        Files.move(Paths.get((String) configuration.getAttr("parser_config_json_file")), Paths.get((String) configuration.getAttr("parser_config_json_file"), ".old"), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
                        Files.writeString(Paths.get((String) configuration.getAttr("parser_config_json_file")), jsonConfig.toString(4), StandardOpenOption.CREATE);
                    } catch (IOException ex) {
                        throw new AutoNewsRuntimeException(String.format("File %s can't be written. Please revise the permissions", configuration.getAttr("parser_config_json_file")), ex);
                    }     
                }
            }            
        }catch (JSONException ex) {
            String filename = configuration.getAttr("parser_config_json_file");
            throw new AutoNewsRuntimeException(String.format("Error: Bad json format in the file \"%s\". please revise it", 
                    filename));

        }catch (IOException ex) {
            String filename = configuration.getAttr("parser_config_json_file");
            throw new AutoNewsRuntimeException(String.format("Error: The file \"%s\" can not be read. please revise it", 
                    filename));
        }
    }
    
    private boolean verifyOrUpdateExtractorConfigJson(JSONObject jsonConfiguration, String extractModel){
        Set<String> temporaryFileds = new HashSet<>();
        boolean ret = false;
        JSONObject extractorConfigJson = jsonConfiguration.getJSONObject(extractModel);
        int compare = compareVersions(BoatFactFields.getCurrentModelVersion(), extractorConfigJson.getString("field_version"));
        if(compare==0){
            JSONArray config = extractorConfigJson.getJSONArray("config");
            for(int i=0; i< config.length(); i++){
                JSONArray fieldsToExtract = config.getJSONObject(i).getJSONObject("configuration").getJSONArray("fields_to_extract");
                for(int find=0; find<fieldsToExtract.length(); find++){
                    String fieldInConfig = fieldsToExtract.getJSONObject(find).getString("key");
                    boolean temporary = fieldsToExtract.getJSONObject(find).optBoolean("temporary_field", false);
                    if(temporary){
                        temporaryFileds.add(fieldInConfig);
                    }else{
                        String fieldOfCurrentVersion = BoatFactFields.getFieldName(fieldInConfig);
                        if(fieldOfCurrentVersion==null || !fieldOfCurrentVersion.equals(fieldInConfig)){
                            //ERROR!
                            String filename = configuration.getAttr("parser_config_json_file");
                            throw new AutoNewsRuntimeException(String.format(
                                    "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                    fieldInConfig, extractModel, filename, BoatFactFields.getCurrentModelVersion()));
                        }
                    }
                } 
                //calculate
                JSONArray fieldsToCalculate = config.getJSONObject(i).getJSONObject("configuration").getJSONArray("fields_to_calculate");
                for(int find=0; find<fieldsToCalculate.length(); find++){
                    String fieldInConfig = fieldsToCalculate.getJSONObject(find).getString("key");
                    boolean temporary = temporaryFileds.contains(fieldInConfig) || fieldsToCalculate.getJSONObject(find).optBoolean("temporary_field", false);
                    if(temporary){
                        temporaryFileds.add(fieldInConfig);
                    }else{
                        String fieldOfCurrentVersion = BoatFactFields.getFieldName(fieldInConfig);
                        if(fieldOfCurrentVersion==null || !fieldOfCurrentVersion.equals(fieldInConfig)){
                            //ERROR!
                            String filename = configuration.getAttr("parser_config_json_file");
                            throw new AutoNewsRuntimeException(String.format(
                                    "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                    fieldInConfig, extractModel, filename, BoatFactFields.getCurrentModelVersion()));
                        }
                    }
                    JSONArray fieldParameters = fieldsToCalculate.getJSONObject(find).getJSONArray("fieldParams");
                    for(int pind=0; pind<fieldParameters.length(); pind++){
                        String[] pathField = fieldParameters.getString(pind).split("\\.");
                        for(int pathInd=1; pathInd<pathField.length; pathInd++){
                            temporary = temporaryFileds.contains(pathField[pathInd]);
                            if(!temporary){
                                String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                                if(pathFieldOfCurrentVersion==null || !pathFieldOfCurrentVersion.equals(pathField[pathInd])){
                                    //ERROR!
                                    String filename = configuration.getAttr("parser_config_json_file");
                                    throw new AutoNewsRuntimeException(String.format(
                                            "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                            pathField[pathInd], extractModel, filename, BoatFactFields.getCurrentModelVersion()));
                                }                                
                            }
                        }
                    }
                }
            }
        }else if(compare>0){
            JSONArray config = extractorConfigJson.getJSONArray("config");
            for(int i=0; i< config.length(); i++){
                JSONArray fieldsToExtract = config.getJSONObject(i).getJSONObject("configuration").getJSONArray("fields_to_extract");
                for(int find=0; find<fieldsToExtract.length(); find++){
                    String fieldInConfig = fieldsToExtract.getJSONObject(find).getString("key");
                    boolean temporary = fieldsToExtract.getJSONObject(find).optBoolean("temporary_field", false);
                    if(temporary){
                        temporaryFileds.add(fieldInConfig);
                    }else{
                        String fieldOfCurrentVersion = BoatFactFields.getFieldName(fieldInConfig);
                        if(fieldOfCurrentVersion==null){
                            //ERROR!
                            String filename = configuration.getAttr("parser_config_json_file");
                            String message;
                            message = String.format(
                                "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to current version (%s). Please, revise it", 
                                fieldInConfig, extractModel, filename, BoatFactFields.getCurrentModelVersion());
                            throw new AutoNewsRuntimeException(message);
                        }else if(!fieldInConfig.equals(fieldOfCurrentVersion)){
                            fieldsToExtract.getJSONObject(find).put("key", fieldOfCurrentVersion);
                            ret = true;
                        }
                    }
                } 
                //calculate
                JSONArray fieldsToCalculate = config.getJSONObject(i).getJSONObject("configuration").getJSONArray("fields_to_extract");
                for(int find=0; find<fieldsToCalculate.length(); find++){
                    String fieldInConfig = fieldsToCalculate.getJSONObject(find).getString("key");
                    boolean temporary = temporaryFileds.contains(fieldInConfig) || fieldsToCalculate.getJSONObject(find).optBoolean("temporary_field", false);
                    if(temporary){
                        temporaryFileds.add(fieldInConfig);
                    }else{
                        String fieldOfCurrentVersion = BoatFactFields.getFieldName(fieldInConfig);
                        if(fieldOfCurrentVersion==null){
                            //ERROR!
                            String filename = configuration.getAttr("parser_config_json_file");
                            String message;
                            message = String.format(
                                "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to current version (%s). Please, revise it", 
                                fieldInConfig, extractModel, filename, BoatFactFields.getCurrentModelVersion());
                            throw new AutoNewsRuntimeException(message);
                        }else if(!fieldInConfig.equals(fieldOfCurrentVersion)){
                            fieldsToCalculate.getJSONObject(find).put("key", fieldOfCurrentVersion);
                            ret = true;
                        }
                    }
                    JSONArray fieldParameters = fieldsToCalculate.getJSONObject(find).getJSONArray("fieldParams");
                    for(int pind=0; pind<fieldParameters.length(); pind++){
                        String[] pathField = fieldParameters.getString(pind).split("\\.");
                        boolean ch = false;
                        for(int pathInd=1; pathInd<pathField.length; pathInd++){
                            temporary = temporaryFileds.contains(pathField[pathInd]);
                            if(!temporary){
                                String pathFieldOfCurrentVersion = BoatFactFields.getFieldName(pathField[pathInd]);
                                if(pathFieldOfCurrentVersion==null){
                                    //ERROR!
                                    String filename = configuration.getAttr("parser_config_json_file");
                                    throw new AutoNewsRuntimeException(String.format(
                                            "Error: The field named \"%s\" found in the extractor model named \"%s\" of in the file \"%s\" does not belong to version %s. Please, revise it", 
                                            pathField[pathInd], extractModel, filename, BoatFactFields.getCurrentModelVersion()));
                                }else if(pathField[pathInd].equals(pathFieldOfCurrentVersion)){
                                    pathField[pathInd] = pathFieldOfCurrentVersion;
                                    ch = true;
                                }                                
                            }
                        }
                        if(ch){
                            StringBuilder strb = new StringBuilder();
                            String pre = "";
                            for(int pathInd=0; pathInd<pathField.length; pathInd++){
                                strb.append(pre);
                                pre=".";
                                strb.append(pathField[pathInd]);
                            }
                            fieldParameters.put(pind, strb.toString());
                            ret = ret || ch;
                        }
                    }
                }
            }
        }else if(compare==Integer.MIN_VALUE){
            //error version desconocida
            String filename = configuration.getAttr("parser_config_json_file");
            throw new AutoNewsRuntimeException(String.format(
                                "Error incompatible versions: The current version for boat fact fields is %s, the version for extractor model named %s in file %s is %s. They are not compatible", 
                                BoatFactFields.getCurrentModelVersion(), extractModel, filename, extractorConfigJson.getString("field_version")));
        }else{
            //error versión config file > current!
            String filename = configuration.getAttr("parser_config_json_file");
            throw new AutoNewsRuntimeException(String.format(
                                "Error file versions greater than current version fileds: The current version for boat fact fields is %s, the version for extractor model named %s in file %s is %s. The current versions can not be more little than the file version.", 
                                BoatFactFields.getCurrentModelVersion(), extractModel, filename, extractorConfigJson.getString("field_version")));            
        }        
        return  ret;
    }    
    
    private int compareVersions(String currentVersion, String version){
        int ret = Integer.MIN_VALUE;
        //boat_fact-00.00.00   18-8 = 10
        if(currentVersion.length()==version.length() && currentVersion.substring(0, currentVersion.length()-8).equals(version.substring(0, version.length()-8))){
            ret = currentVersion.substring(currentVersion.length()-8).compareTo(version.substring(version.length()-8));
        }
        return ret;
    }
}
