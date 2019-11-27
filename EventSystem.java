import java.util.function.Consumer;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class EventSystem {
    Map<Class<?>, ArrayList<Consumer<?>>> listeners=new HashMap<Class<?>, ArrayList<Consumer<?>>>();
    
    @SuppressWarnings("unchecked")
    public <T> void emit(T event){
        for(Map.Entry<Class<?>, ArrayList<Consumer<?>>> e : listeners.entrySet()){
            if(e.getKey().isInstance(event)){
                for(Consumer<?> callback : e.getValue()){
                    ((Consumer<T>)callback).accept(event);
                }
            }
        }
    }
    public <T> void subscribe(Class<T> event, Consumer<T> callback){
        if(listeners.containsKey(event)){
            listeners.get(event).add(callback);
        } else {
            ArrayList<Consumer<?>> callbackList=new ArrayList<Consumer<?>>();
            callbackList.add(callback);
            listeners.put(event, callbackList);
        }
    }
    public <T> void unsubscribe(Class<T> event, Consumer<T> callback){
        ArrayList<Consumer<?>> callbacksList=listeners.get(event);
        if(callbacksList!=null){
            callbacksList.remove(callback);
            if(callbacksList.size()==0){
                listeners.remove(event);
            }
        }
    }
}