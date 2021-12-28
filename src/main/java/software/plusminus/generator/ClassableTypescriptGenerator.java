package software.plusminus.generator;

import org.springframework.stereotype.Component;
import software.plusminus.generator.typescript.TypescriptGenerator;
import software.plusminus.generator.typescript.model.TypescriptClass;
import software.plusminus.json.model.Classable;

@Component
public class ClassableTypescriptGenerator extends TypescriptGenerator {

    @Override
    public TypescriptClass generateModel(Class<?> sourceClass) {
        TypescriptClass typescriptClass = super.generateModel(sourceClass);
        typescriptClass.fields().add("class: string;");
        typescriptClass.constructor("  this.class = this.constructor.name;");
        return typescriptClass;
    }

    @Override
    public boolean supports(Class<?> sourceClass) {
        return sourceClass == Classable.class;
    }
}
