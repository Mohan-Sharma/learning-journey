package com.sap.concur.coreint.configurations.spring.scope;

import java.util.Objects;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.SimpleThreadScope;

/**
 * @author Mohan Sharma
 */
public class ConcurThreadScope extends SimpleThreadScope {

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        var o = super.get(name, objectFactory);
        var sessionId = ((RemoteWebDriver)o).getSessionId();
        if(Objects.isNull(sessionId)){
            super.remove(name);
            o = super.get(name, objectFactory);
        }
        return o;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {}
}
