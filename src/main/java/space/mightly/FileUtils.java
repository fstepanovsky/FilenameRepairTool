package space.mightly;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

class FileUtils {
    private static final Logger logger = Logger.getLogger(FileUtils.class.getName());
    private List<File> list = new ArrayList<>();

    void listFiles(File file) {
        var files = file.listFiles();
        if (files == null)
            return;
        for (var f : files) {
            if (f.isDirectory()) {
                list.add(f);
                listFiles(f);
            }
            else {
                list.add(f);
            }
        }
    }

    void renameFiles(HashMap<String, String> dict) {
        if (list.isEmpty()) {
            logger.severe("No files to rename!");
            return;
        }
        for (var file : list) {
            String name = file.getName();
            StringBuilder newName = new StringBuilder();
            // Reconstruct new version of string
            for(int i = 0; i < name.length(); i++) {
                String character = String.valueOf(name.charAt(i));
                newName.append(dict.getOrDefault(character, character));
            }

            if (!name.equals(newName.toString())) {
                var newAbsName = file.getAbsolutePath().replaceAll(name, newName.toString());
                if(!file.renameTo(new File(newAbsName)))
                    logger.warning("Unable to rename file: " + file.getAbsolutePath());
            }
        }
    }

    void dryRun() {
        if (list.isEmpty()) {
            logger.severe("No files to rename!");
            return;
        }
        sortFiles();
        for (var file : list) {
            System.out.println(file.getAbsolutePath());
        }
    }
    private void sortFiles() {
        SortFiles comparator = new SortFiles();
        this.list.sort(comparator.reversed());
    }
}

class SortFiles implements Comparator<File> {
    public int compare(File f1, File f2) {
        Integer len1;
        Integer len2;
        if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
            len1 = f1.toString().split("\\\\").length;
            len2 = f2.toString().split("\\\\").length;
        } else {
            len1 = f1.toString().split("/").length;
            len2 = f2.toString().split("/").length;
        }


        return len1.compareTo(len2);
    }
}

final class OsCheck {
    /**
     * types of Operating Systems
     */
    public enum OSType {
        Windows, MacOS, Linux, Other
    };

    // cached result of OS detection
    static OSType detectedOS;

    /**
     * detect the operating system from the os.name System property and cache
     * the result
     *
     * @returns - the operating system detected
     */
    static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}