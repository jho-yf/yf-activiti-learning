package cn.jho.activiti.variable;

import cn.jho.activiti.model.Car;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.activiti.engine.impl.variable.ValueFields;
import org.activiti.engine.impl.variable.VariableType;

/**
 * <p>JsonType</p>
 *
 * @author JHO xu-jihong@qq.com
 */
public class JsonType implements VariableType {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String getTypeName() {
        return "customJson";
    }

    @Override
    public boolean isCachable() {
        return false;
    }

    @Override
    public boolean isAbleToStore(Object value) {
        if (value == null) {
            return true;
        }
        return value.getClass().isAnnotationPresent(Json.class);
    }

    @Override
    public void setValue(Object value, ValueFields valueFields) {
        if (value == null) {
            valueFields.setTextValue("{}");
        } else {
            try {
                valueFields.setTextValue(OBJECT_MAPPER.writeValueAsString(value));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public Object getValue(ValueFields valueFields) {
        try {
            return OBJECT_MAPPER.readValue(valueFields.getTextValue(), Car.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
