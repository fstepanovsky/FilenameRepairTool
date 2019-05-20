package space.mightly;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Logger;

class AlphabetMappingUtil {
    private HashMap<String, String> chars = new HashMap<>();
    private final static Logger logger = Logger.getLogger(AlphabetMappingUtil.class.getName());

    HashMap<String, String> getDictionary() {
        return chars;
    }

    void loadDictionary(File dictionary) {
        if (!dictionary.exists() && !dictionary.isFile()) {
            logger.severe("Dictionary error!");
            throw new RuntimeException();
        }
        try {
            BufferedReader br = Files.newBufferedReader(dictionary.toPath());
            String line;
            int ln = 1;
            while ( (line = br.readLine()) != null) {
                String[] dic = line.split(" ");

                if (dic.length != 2) {
                    logger.warning("Skipping line: " + ln);
                    ln++;
                    continue;
                }

                chars.put(dic[0], dic[1]);
                ln++;
            }
        } catch (IOException e) {
            logger.severe("Error occurred while reading dictionary file!");
            throw new RuntimeException();
        }
    }
}
