package com.centurylink.cloud.sdk.base.exceptions;

import org.testng.annotations.Test;

public class ClcExceptionTest {

    @Test
    public void testConstructionWithFormatting() {
        Exception e = new ClcException("%s formatting works", "param");

        assert e.getMessage().equals("param formatting works");
    }

    @Test
    public void testConstructorWithFormattingAndSuppressedException() {
        Exception e = new ClcException("%s formatting works", "parameters", new Exception("Cause"));

        assert e.getMessage().equals("parameters formatting works");
        assert e.getCause().getMessage().equals("Cause");
    }

}