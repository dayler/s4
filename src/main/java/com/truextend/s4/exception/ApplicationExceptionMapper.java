/**
 * 
 */
package com.truextend.s4.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Responsible to map all custom exceptions with an Response, it is used by
 * Jersey for creating a response based on the exception.
 * 
 * @author arielsalazar
 */
public class ApplicationExceptionMapper implements ExceptionMapper<OperationException> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(OperationException ex) {
        if (ex instanceof DuplicateEntryException) {
            return Response.status(Response.Status.NOT_FOUND)
                           .type(MediaType.TEXT_PLAIN)
                           .entity(ex.getMessage())
                           .build();
        }
        if (ex instanceof NotFoundEntryException) {
            return Response.status(Response.Status.NOT_FOUND)
                           .type(MediaType.TEXT_PLAIN)
                           .entity(ex.getMessage())
                           .build();
        }
        // undefined exception.
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .type(MediaType.TEXT_PLAIN)
                       .entity(getStackTrace(ex))
                       .build();
    }

    /**
     * @param ex
     * @return Stack trace on a String.
     */
    public static String getStackTrace(Throwable ex) {
        if (ex == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
