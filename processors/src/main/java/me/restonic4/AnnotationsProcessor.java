package me.restonic4;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedOptions("isServerBuild")
@SupportedAnnotationTypes("me.restonic4.ClientSide")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class AnnotationsProcessor extends AbstractProcessor  {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Custom annotation processor starting");

        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Custom annotation processor started");

        String isServerBuildOption = processingEnv.getOptions().get("isServerBuild");
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "isServerBuild option: " + isServerBuildOption);

        boolean isServerBuild = Boolean.parseBoolean(isServerBuildOption);

        if (isServerBuild) {
            roundEnv.getElementsAnnotatedWith(ClientSide.class).forEach(element -> {

            });
        }

        return false;
    }
}
