/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import remotecommunicators.RemoteQuizCommunicator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 *
 * @author Wouter
 */
public class Unused_ApplicationBinder extends AbstractBinder{
        @Override
    protected void configure() {
        bind(RemoteQuizCommunicator.class).to(RemoteQuizCommunicator.class);
    }
}
