package com.how_hard_can_it_be.layleadership.services;

import com.how_hard_can_it_be.layleadership.service_interfaces.Greeter;

public class HelloGreeter implements Greeter
{
    @Override
    public String greet( String aGreetingRecipient )
    {
        return "Hello, " + aGreetingRecipient;
    }
}
