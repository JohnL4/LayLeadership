package com.how_hard_can_it_be.layleadership.servlet;

public class HelloGreeter implements Greeter
{
    @Override
    public String greet( String aGreetingRecipient )
    {
        return "Hello, " + aGreetingRecipient;
    }
}
