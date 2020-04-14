package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class ResultHolder implements Consumer<Object> {

    private static Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

    @JsonRawValue
    protected String result;
    protected String error;

    public String getResult() {
        return this.result;
    }

    public String getError() {
        return this.error;
    }

    @JsonIgnore
    public abstract boolean isEmpty();


    public static class Success extends ResultHolder {

        @Override
        public void accept(Object obj) {
            LOG.trace("success result {} \n", obj);
            this.result = obj.toString();
        }

        @Override
        public boolean isEmpty() {
            return this.result == null;
        }

    }

    public static class Failure extends ResultHolder {
        //Object consumed here are of runtime type com.oracle.truffle.polyglot.PolyglotMap (can not be handled)

        @Override
        public void accept(Object obj) {
            LOG.error("error result {} \n", obj);
            this.error = obj.toString();
        }

        @Override
        public boolean isEmpty() {
            return isBlank(this.error);
        }

    }

}
