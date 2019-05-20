package space.mightly;

import java.io.File;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Need destination and dictionary argument!");
            exit(1);
        }

        File path = new File(args[0]);
        FileUtils fu = new FileUtils();
        var au = new AlphabetMappingUtil();
        au.loadDictionary(new File(args[1]));
        fu.listFiles(path);
        fu.renameFiles(au.getDictionary());
        //fu.dryRun();
    }

}

