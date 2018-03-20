/**
 * 
 */
package com.truextend.s4.exception;

/**
 * The entry does not found.
 * @author arielsalazar
 */
public class NotFoundEntryException extends OperationException {

    private static final long serialVersionUID = 20180115L;

    /**
     * NotFoundEntryException's constructor.
     * @param predicate
     */
    public NotFoundEntryException(String predicate) {
        super("Not found entry. " + predicate);
    }
}
