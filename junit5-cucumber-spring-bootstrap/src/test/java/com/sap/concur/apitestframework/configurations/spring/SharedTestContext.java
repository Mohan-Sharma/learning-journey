package com.sap.concur.apitestframework.configurations.spring;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

/**
 * @author Mohan Sharma
 */
@Slf4j
public enum SharedTestContext {
    CONTEXT;

    private static final String PAYLOAD = "PAYLOAD";
    private static final String RESPONSE = "RESPONSE";
    private final ThreadLocal<Map<String, Object>> contexts = ThreadLocal.withInitial(HashMap::new);

    public <T> T get(String name) {
        return (T) contexts.get().get(name);
    }

    public <T> T set(String name, T object) {
        contexts.get().put(name, object);
        return object;
    }

    public ResponseEntity<Object> getResponse() {
        return get(RESPONSE);
    }

    public ResponseEntity<Object> setResponse(ResponseEntity<Object> response) {
        return set(RESPONSE, response);
    }

    public Object getPayload() {
        return get(PAYLOAD);
    }

    public <T> T getPayload(Class<T> clazz) {
        return clazz.cast(get(PAYLOAD));
    }

    public <T> void setPayload(T object) {
        set(PAYLOAD, object);
    }

    public void reset() {
        contexts.get()
                .clear();
    }
}
