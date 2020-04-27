package pl.coco.util;

import java.util.ArrayList;
import java.util.List;

public class TestCompilerInput {

    List<SourceFile> compilationUnits = new ArrayList<>();
    private String pluginArg;

    public List<SourceFile> getCompilationUnits() {
        return compilationUnits;
    }

    public void setCompilationUnits(List<SourceFile> compilationUnits) {
        this.compilationUnits = compilationUnits;
    }

    public String getPluginArg() {
        return pluginArg;
    }

    public void setPluginArg(String pluginArg) {
        this.pluginArg = pluginArg;
    }

    public static class Builder {

        private TestCompilerInput input = new TestCompilerInput();

        public Builder addCompilationUnit(String qualifiedClassName, String source) {
            input.getCompilationUnits().add(new SourceFile(qualifiedClassName, source));
            return this;
        }

        public Builder withPluginArg(String pluginArg) {
            input.setPluginArg(pluginArg);
            return this;
        }

        public TestCompilerInput build() {
            return input;
        }
    }
}
