/**
 * 
 */
package com.truextend.s4.exception;

/**
 * Teh entry is duplicated.
 * @author arielsalazar
 */
public class DuplicateEntryException extends OperationException {

    private static final long serialVersionUID = 20180115L;
    
    /**
     * DuplicateEntryException's constructor.
     * @param predicate
     */
    public DuplicateEntryException(String predicate) {
        super("Duplicate entry. " + predicate);
    }
}
