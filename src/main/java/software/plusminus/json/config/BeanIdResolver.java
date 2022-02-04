package software.plusminus.json.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import org.springframework.core.annotation.AnnotationUtils;
import software.plusminus.util.ClassUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanIdResolver extends TypeIdResolverBase {

    private JavaType superType;

    @Override
    public void init(JavaType baseType) {
        superType = baseType;
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }

    @Override
    public String idFromValue(Object obj) {
        return idFromValueAndType(obj, obj.getClass());
    }

    @Override
    public String idFromValueAndType(Object obj, Class<?> subType) {
        return subType.getSimpleName();
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        if (superType.getRawClass().getSimpleName().equals(id)) {
            return superType;
        }
        List<Class<?>> foundClasses = ClassUtils.findAllClassesBySimpleName(id).stream()
                .filter(c -> AnnotationUtils.findAnnotation(c, JsonTypeIdResolver.class) != null)
                .collect(Collectors.toList());
        if (foundClasses.isEmpty()) {
            throw new IllegalArgumentException("Unknown class " + id);
        } else if (foundClasses.size() > 1) {
            throw new IllegalArgumentException("Multiple classes found with name '" + id + "': " + foundClasses);
        }
        return context.constructSpecializedType(superType, foundClasses.get(0));
    }
}
