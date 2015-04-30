package smartie_dissertation.helperlib.generic;
import java.lang.reflect.ParameterizedType;

public class GenericClass<T> {

    private Class<T> genericClass;
    
    @SuppressWarnings("unchecked")
    public GenericClass() {
    	ParameterizedType pt = ((ParameterizedType) getClass().getGenericSuperclass());
      this.genericClass = ((Class<T>) ((ParameterizedType) getClass()
          .getGenericSuperclass()).getActualTypeArguments()[0]);
    }
    
    public Class<T> getGenericClass() {
        return genericClass;
    }
}
