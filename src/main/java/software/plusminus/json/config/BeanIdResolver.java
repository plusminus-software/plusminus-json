package software.plusminus.json.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import software.plusminus.util.ClassUtils;

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
        Class<?> foundClass = ClassUtils.findClassBySimpleName(id);
        if (foundClass == null) {
            throw new IllegalArgumentException("Unknown class " + id);
        }
        return context.constructSpecializedType(superType, foundClass);
    }
}
