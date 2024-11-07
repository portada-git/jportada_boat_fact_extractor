/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.elsquatrecaps.portada.boatfactextractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.elsquatrecaps.autonewsextractor.error.AutoNewsRuntimeException;
import org.elsquatrecaps.autonewsextractor.targetfragmentbreaker.cutter.TargetFragmentCutterProxyClass;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;

/**
 *
 * @author josep
 */
public class BoatFactCutter {
    AutoNewsExtractorConfiguration configuration;
    String outputDir;
    
    public BoatFactCutter init(AutoNewsExtractorConfiguration config){
        configuration = config;
        return this;
    }
    
    public BoatFactCutter init(String odir){
        outputDir = odir;
        return this;
    }
    public void cutFiles(Integer id){
        String text;
        String cutText;
        TargetFragmentCutterProxyClass proxy = TargetFragmentCutterProxyClass.getInstance(configuration.getFragmentBreakerApproach(), configuration);

        File dir = (new File(configuration.getOriginDir())).getAbsoluteFile();
        File[] files = dir.listFiles((file) ->{
            boolean ret = file.isFile();
            ret = ret && file.getPath().endsWith(configuration.getFileExtension());
            return ret;
        });

        try {
            for(File file: files){
                String oFileName = file.getName();
                String ext;
                text = Files.readString(file.toPath());
                cutText = proxy.init(id).getTargetTextFromText(text);
                int dotIndex = file.getName().lastIndexOf('.');
                ext =  (dotIndex == -1) ? "" : oFileName.substring(dotIndex, oFileName.length());
                oFileName =  (dotIndex == -1) ? oFileName : oFileName.substring(0, dotIndex);
                Files.writeString(Path.of(outputDir, String.format("%s_%s%s", oFileName, configuration.getParseModel()[id]), ext), text, StandardOpenOption.CREATE);
            }
        } catch (RuntimeException | IOException ex) {
            throw new AutoNewsRuntimeException(ex);
        }
    }
    
}
