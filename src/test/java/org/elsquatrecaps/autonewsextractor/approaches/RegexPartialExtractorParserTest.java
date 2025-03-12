package org.elsquatrecaps.autonewsextractor.approaches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.elsquatrecaps.autonewsextractor.dataextractor.parser.RegexExtractorParser;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elsquatrecaps.autonewsextractor.dataextractor.parser.MainAutoNewsExtractorParser;
import org.elsquatrecaps.autonewsextractor.model.BoatFactFields;
import org.elsquatrecaps.autonewsextractor.model.ExtractedData;
import org.elsquatrecaps.autonewsextractor.model.MutableNewsExtractedData;
import org.elsquatrecaps.autonewsextractor.model.NewsExtractedData;
import org.elsquatrecaps.autonewsextractor.model.PublicationInfo;
import org.elsquatrecaps.autonewsextractor.tools.configuration.AutoNewsExtractorConfiguration;
import org.elsquatrecaps.autonewsextractor.tools.formatter.BoatFactCsvFormatter;
import org.elsquatrecaps.autonewsextractor.tools.formatter.JsonFileFormatterForExtractedData;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author josep
 */
public class RegexPartialExtractorParserTest {
    static AutoNewsExtractorConfiguration configuration = new AutoNewsExtractorConfiguration();
    static JSONObject jsonConfig;
    
    public static void main(String[] args){
        setUpClass();
        RegexPartialExtractorParserTest test = new RegexPartialExtractorParserTest();
        
        test.setUp();
        test.testParseFromString();
        test.tearDown();
        
        test.setUp();
        test.testParseWithMultiParserFromString();
        test.tearDown();
        
        tearDownClass();
    }
    
    public RegexPartialExtractorParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        String[] args ={
            "-c",
            "config/conf_db/init.properties",
            "-cc",
            "bcn"
        };
        try{
            configuration.parseArgumentsAndConfigure(args);    
            String jsc = Files.readString(Paths.get(configuration.getParserConfigJsonFile()));
            jsonConfig = new JSONObject(jsc);
        } catch (IOException ex) {
            Logger.getLogger(RegexPartialExtractorParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of parseFromString method, of class RegexExtractorParser.
     */
    @Test
    public void testParseFromString() {
        System.out.println("parseFromString");
        String bonText = ""
                + "Embarcaciones llegadas al puerto en el dia de ayer.\n" 
                + "Mercantes españolas.\n" 
                + "De Christiansund en 33 d. bergantin F'ama, de 109 t., c. D. V. Ramon Rodriguez, con 5930 vog. bacalao y 200 de pezpalo á la orden. \n" 
                + "De Almería y Aguilas en 15 d. laud Aguila, de 35 t., p. S. Lopez, con 130 gq. perdigon es á D. A. Sala, 50 id. á D. J. Margarit. 400 de plomo á D. J. Serratosa, 200 fanegas cebada á Don B. Solá y Amat, y 12 millares esparto á D. S. Garriga.\n" 
                + "De Vinaroz en 3 d, laud Tres Amigos, de 23 t., p. C. Roca, con 1000 a. algarrobas á los señores Caralt Matheu y Segarra, 11 pipas aceite, à D. C. Torrens y Miralda, 10 id. á D.-S. Pou, 4 id. á D. E. Beltran y 4 id. á D. J. Verges.\n" 
                + "De Adra y Denia en 3 d. laud Dolores, de 52 t., p. M. Lopez, con 472 cajas plomo, 212 rollós id., 100 barriles litargirio en hojas y 10 de minio á D. P. Marsal, 18 rollos plomo á D. C. Truyano, 43 bultos trapos y 9 de retazos de papel á la órden.\n" 
                + "De Seiilla en 24 d. mistico Sevillano, de 72 t., p. J. Bosch, con C00 fanegas trigo, 90 sacos sémola à D. P. Delmases, 66 id. á D. M. Montaña, 30 id. á D. F. Romeva , 268 saquetas lana à D. P. M. Serdá, 26 bultos trapos y cortaduras de papel á D. I. Bomaña y 3089 astas de carnero à la órden.\n" 
                + "De id., Cádiz y Sta. Pola en 31 d. laud Jóven Eusebio, de 52 t., p. J. Roca, con 212 fanegas trigo y 100 gq. granada á D. B. Solá y Amat, 343 fanegas hahones á D. J. Sirvent, 53 herpiles tropos á dou" 
                + "F. Soler, 62 cajas loza á los señores Pichmsan y compañia, 80 saquetas laua à D. 1. Vieta , 192 sacos sémola á D. M. Montaña y 40 barriles tabaco á D. J. Fontanillas.\n" 
                + "De Cartagena en 11 d. místico Ventura, de 53 t., p. A. Calvet, con 80 a. granada, 13 de trapos y 13 de alpargatas á D. J. Artigas, 71 qq. gualda á D. D. Miralles , 300 de barrilla á la señiora viuda Moré y 900 de mineral à D. M. Camps\n" 
                + "De Sevilla, Cädiz y Cartagena en 33 d. laud Sau Sebastian, de 43 t., c. D. J. B, Ramon, con 1000 fanegas trigo y 380 de habones á D. S. Servet, y 20 sacos sémola à D. J. Estrany\n" 
                + "De Ayamonte, Alicante y Valencia en 11 d. laud Leon, de 57 t., p. J. Maria Gutierrez, con 43 cascos sardina y 8 medias pipas aceite à D. J. Poch.\n" 
                + "De Cartagens en 13 d. polacra goleta Sau José, de 70 t., p. J. Gomez, con 1509 qu. mineral à D. J. Jacas y Cuadras, y 50 de granada á los Sres. Coma, Ciuró v Clavell.\n" 
                + "De Vera en 15 d. laud Soledad, de 32 t., p. J. Bufort, con 200 fanegas cebada à D. J. Estrany, y 400 1q jaboncillo 4 D. B. Solá v Amat.\n" 
                + "De Cullera y Valencia en 4 d. laud Jûcar, de 13 t., p. F. Miñano, con 100 sacos arroz y 130 qq. rubia á la órden.\n" 
                + "De Alicante, Denia y Vinaroz en 13 d. laud Cármon, de 21 t., p. B. Borrás, con 490 fa negas trigo y 128 de algarrobas á los Sres. Castelló y compañía.\n" 
                + "De id. en 6 d. laud Rayo, de 41 t., p. V. Ferro, con 1050 fanegas trigo à los señores Caralt, Mathen y Segarra.\n" 
                + "De Tavire en 14 d. laud Almas, de 56 t., p. J. M. Martinez, con 940 quintales algarrobas à don 1. Moreu.\n" 
                + "De Alicante en 6 d. laud Camila, de 47 t., p. J. Fornos, con 1125 fanegas trigo à D. J. Estrany y 81 fardos pleita á la señora viuda Lejust. \n" 
                + "Además 11 buques de la costa de este Principado, con 20 cargas obra de barro à D. S. Gili, 120 fanegas centeno y 1/2 pipa aceite para Blanes, 18 carretadas madera à D. J. Cubi, 50 docenas tablones à D. J. Gurri, 500 botijuelas aceite y 110 pipas vino trasbordo\n" 
                + "Se dirige al puerto la polacra española Constancia, procedente de Marsella.\n" 
                + "Id. danesa.\n" 
                + "De Calmau y Elseneur en 40 d. bergantin Terpsicore, de 132, t. c. P. N. Thave, con 286 docenas tablones de pino y 8 piezas arboladura á los Sres. Ortembach y C.\n" 
                + "Mercante francesa.\n" 
                + "De Marsella en 21 horas vapor Elba, de 210 t., c. S. Gabriel , con 69,000 francos á los señores Vidal y Cuadras hermanos, 83,000 id. á los señores Girona hermanos, Clavé y compañía , 23,000 id. D. J. M. Serra, 21,900 id. á los señores Serra y Parladé, 13.000 id. á D. B. Roca y Cortada, 15.000 id. á los señores Staguo, Torrens y compañia, 1500 id. á D. 1. Domenech, 3 cajas sangnijuclas á don B. Solá y Amat, otros efertos para esta, y 103 baltos de varios géneros de tránsito y 30 pasajeros, consignado á los señores Martorell y Bosill.\n" 
                + "Id. inglesas.\n" 
                + "De Sunderland en 41 d. corbeta Scott, de 370 t., c. Johu Stainobuyh, con 504 toneladas carbon de piedra á D. J. Gil.\n" 
                + "De Newcastle en 37 d. bergantin Thoburn, de 287 t., c. Robert Browo, con 395 toneladas carbon de piedra à D. J. Gil.\n"
                + "inglesa.\n" 
                + "De Newcastle en 28 d. bergantin Socret, de 273 t., c. W. Storey, con 400 toneladas carbon de piedra á los señores Martorell y Bofill.\n" 
                + "Se dirige al puerto el bergantin español Constante.";
        RegexExtractorParser instance = new RegexExtractorParser();
        instance.init(configuration, 0);
        instance.init(jsonConfig.getJSONObject("boatdata.extractor").getJSONArray("config").getJSONObject(0).getJSONObject("configuration"));
        instance.init(jsonConfig.getJSONObject("boatdata.extractor").getJSONObject("constants"));
        MutableNewsExtractedData partialExtractedDataToCopy = instance.getDefaultData(
                new PublicationInfo(BoatFactFields.getCurrentModelVersion(), "1850-09-21"));
        List<ExtractedData> result = instance.parseFromString(bonText, partialExtractedDataToCopy);
        System.out.println(result);
        assertEquals(5, result.size());
        // TODO review the gnerated test code and remove the default call to fail.
    }
    
    @Test
    public void testParseWithMultiParserFromString() {
        System.out.println("ParseWithMultiParserFromString");
        String bonText = ""
                + "Embarcaciones llegadas al puerto en el dia de ayer.\n" 
                + "Mercantes españolas.\n" 
                + "De Christiansund en 33 d. bergantin F'ama, de 109 t., c. D. V. Ramon Rodriguez, con 5930 vog. bacalao y 200 de pezpalo á la orden. \n" 
                + "De Almería y Aguilas en 15 d. laud Aguila, de 35 t., p. S. Lopez, con 130 gq. perdigon es á D. A. Sala, 50 id. á D. J. Margarit. 400 de plomo á D. J. Serratosa, 200 fanegas cebada á Don B. Solá y Amat, y 12 millares esparto á D. S. Garriga.\n" 
                + "De Vinaroz en 3 d, laud Tres Amigos, de 23 t., p. C. Roca, con 1000 a. algarrobas á los señores Caralt Matheu y Segarra, 11 pipas aceite, à D. C. Torrens y Miralda, 10 id. á D.-S. Pou, 4 id. á D. E. Beltran y 4 id. á D. J. Verges.\n" 
                + "De Adra y Denia en 3 d. laud Dolores, de 52 t., p. M. Lopez, con 472 cajas plomo, 212 rollós id., 100 barriles litargirio en hojas y 10 de minio á D. P. Marsal, 18 rollos plomo á D. C. Truyano, 43 bultos trapos y 9 de retazos de papel á la órden.\n" 
                + "De Seiilla en 24 d. mistico Sevillano, de 72 t., p. J. Bosch, con C00 fanegas trigo, 90 sacos sémola à D. P. Delmases, 66 id. á D. M. Montaña, 30 id. á D. F. Romeva , 268 saquetas lana à D. P. M. Serdá, 26 bultos trapos y cortaduras de papel á D. I. Bomaña y 3089 astas de carnero à la órden.\n" 
                + "De id., Cádiz y Sta. Pola en 31 d. laud Jóven Eusebio, de 52 t., p. J. Roca, con 212 fanegas trigo y 100 gq. granada á D. B. Solá y Amat, 343 fanegas hahones á D. J. Sirvent, 53 herpiles tropos á dou" 
                + "F. Soler, 62 cajas loza á los señores Pichmsan y compañia, 80 saquetas laua à D. 1. Vieta , 192 sacos sémola á D. M. Montaña y 40 barriles tabaco á D. J. Fontanillas.\n" 
                + "De Cartagena en 11 d. místico Ventura, de 53 t., p. A. Calvet, con 80 a. granada, 13 de trapos y 13 de alpargatas á D. J. Artigas, 71 qq. gualda á D. D. Miralles , 300 de barrilla á la señiora viuda Moré y 900 de mineral à D. M. Camps\n" 
                + "De Sevilla, Cädiz y Cartagena en 33 d. laud Sau Sebastian, de 43 t., c. D. J. B, Ramon, con 1000 fanegas trigo y 380 de habones á D. S. Servet, y 20 sacos sémola à D. J. Estrany\n" 
                + "De Ayamonte, Alicante y Valencia en 11 d. laud Leon, de 57 t., p. J. Maria Gutierrez, con 43 cascos sardina y 8 medias pipas aceite à D. J. Poch.\n" 
                + "De Cartagens en 13 d. polacra goleta Sau José, de 70 t., p. J. Gomez, con 1509 qu. mineral à D. J. Jacas y Cuadras, y 50 de granada á los Sres. Coma, Ciuró v Clavell.\n" 
                + "De Vera en 15 d. laud Soledad, de 32 t., p. J. Bufort, con 200 fanegas cebada à D. J. Estrany, y 400 1q jaboncillo 4 D. B. Solá v Amat.\n" 
                + "De Cullera y Valencia en 4 d. laud Jûcar, de 13 t., p. F. Miñano, con 100 sacos arroz y 130 qq. rubia á la órden.\n" 
                + "De Alicante, Denia y Vinaroz en 13 d. laud Cármon, de 21 t., p. B. Borrás, con 490 fa negas trigo y 128 de algarrobas á los Sres. Castelló y compañía.\n" 
                + "De id. en 6 d. laud Rayo, de 41 t., p. V. Ferro, con 1050 fanegas trigo à los señores Caralt, Mathen y Segarra.\n" 
                + "De Tavire en 14 d. laud Almas, de 56 t., p. J. M. Martinez, con 940 quintales algarrobas à don 1. Moreu.\n" 
                + "De Alicante en 6 d. laud Camila, de 47 t., p. J. Fornos, con 1125 fanegas trigo à D. J. Estrany y 81 fardos pleita á la señora viuda Lejust. \n" 
                + "Además 11 buques de la costa de este Principado, con 20 cargas obra de barro à D. S. Gili, 120 fanegas centeno y 1/2 pipa aceite para Blanes, 18 carretadas madera à D. J. Cubi, 50 docenas tablones à D. J. Gurri, 500 botijuelas aceite y 110 pipas vino trasbordo\n" 
                + "Se dirige al puerto la polacra española Constancia, procedente de Marsella.\n" 
                + "Id. danesa.\n" 
                + "De Calmau y Elseneur en 40 d. bergantin Terpsicore, de 132, t. c. P. N. Thave, con 286 docenas tablones de pino y 8 piezas arboladura á los Sres. Ortembach y C.\n" 
                + "Mercante francesa.\n" 
                + "De Marsella en 21 horas vapor Elba, de 210 t., c. S. Gabriel , con 69,000 francos á los señores Vidal y Cuadras hermanos, 83,000 id. á los señores Girona hermanos, Clavé y compañía , 23,000 id. D. J. M. Serra, 21,900 id. á los señores Serra y Parladé, 13.000 id. á D. B. Roca y Cortada, 15.000 id. á los señores Staguo, Torrens y compañia, 1500 id. á D. 1. Domenech, 3 cajas sangnijuclas á don B. Solá y Amat, otros efertos para esta, y 103 baltos de varios géneros de tránsito y 30 pasajeros, consignado á los señores Martorell y Bosill.\n" 
                + "Id. inglesas.\n" 
                + "De Sunderland en 41 d. corbeta Scott, de 370 t., c. Johu Stainobuyh, con 504 toneladas carbon de piedra á D. J. Gil.\n" 
                + "De Newcastle en id. d. bergantin Thoburn, de 287 t., c. Robert Browo, con 395 toneladas carbon de piedra à D. J. Gil.\n"
                + "inglesa.\n" 
                + "De Newcastle en 28 d. bergantin Socret, de 273 t., c. W. Storey, con 400 toneladas carbon de piedra á los señores Martorell y Bofill.\n" 
                + "Se dirige al puerto el bergantin español Constante.";
        MainAutoNewsExtractorParser instance = new MainAutoNewsExtractorParser();
        instance.init(configuration);
        List<NewsExtractedData> result = instance.parseFromString(bonText, 0,
                new PublicationInfo(BoatFactFields.getCurrentModelVersion(), "1855_11_09"));
        JsonFileFormatterForExtractedData ff = new JsonFileFormatterForExtractedData(result);
        System.out.println(ff.toString());
        BoatFactCsvFormatter csvf = new BoatFactCsvFormatter();
        JSONObject header = instance.getCsvProperties(0);
        System.out.println(csvf.configHeaderFields(header).format(result).toString());
        csvf.toFile("sortida.csv");
        assertEquals(21, result.size());
        // TODO review the generated test code and remove the default call to fail.
    }    
    
}
