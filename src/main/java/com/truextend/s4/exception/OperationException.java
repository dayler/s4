/**
 * 
 */
package com.truextend.s4.exception;

/**
 * Base exception for Rest services.
 * @author arielsalazar
 */
public abstract class OperationException extends Exception {

    private static final long serialVersionUID = 20180115L;

    /**
     * OperationException's constructor.
     * @param msg
     */
    public OperationException(String msg) {
        super(msg);
    }
}
