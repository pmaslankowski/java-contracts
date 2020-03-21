package pl.coco.compiler.arguments;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentParser {

    private static final String DISABLE_CONTRACT_CHECKING_OPT = "disabled";

    private CommandLineParser parser = new DefaultParser();
    private Options options = new Options();

    public ArgumentParser() {
        options.addOption("d", "disabled", false, "Disable contract checking");
    }

    public CocoArgs parseArgs(String[] args) {
        try {
            return doParseArgs(args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private CocoArgs doParseArgs(String[] args) throws ParseException {
        CocoArgs result = new CocoArgs();
        CommandLine parse = parser.parse(options, args);
        boolean isContractCheckingEnabled = !parse.hasOption(DISABLE_CONTRACT_CHECKING_OPT);
        result.setContractCheckingEnabled(isContractCheckingEnabled);
        return result;
    }
}
