package pl.coas.compiler.arguments;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentParser {

    private static final String DISABLE_ASPECT_INSTRUMENTATION_OPT = "disabled";

    private CommandLineParser parser = new DefaultParser();
    private Options options = new Options();

    public ArgumentParser() {
        options.addOption("d", "disabled", false, "Disable aspect instrumentation");
    }

    public CoasArgs parseArgs(String[] args) {
        try {
            return doParseArgs(args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private CoasArgs doParseArgs(String[] args) throws ParseException {
        CoasArgs result = new CoasArgs();
        CommandLine parse = parser.parse(options, args);
        boolean isAspectInstrumentationEnabled =
                !parse.hasOption(DISABLE_ASPECT_INSTRUMENTATION_OPT);

        result.setAspectInstrumentationEnabled(isAspectInstrumentationEnabled);
        return result;
    }
}
