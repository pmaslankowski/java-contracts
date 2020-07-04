package pl.coas.compiler.instrumentation;

import javax.inject.Singleton;

import com.sun.source.util.TreeScanner;

@Singleton
public class AnnotationScanningVisitor extends TreeScanner<Void, Void> {
}
