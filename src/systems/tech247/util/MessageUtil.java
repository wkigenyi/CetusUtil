/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.util;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author Wilfred
 */
public class MessageUtil {
    
    public static DialogDisplayer getDialogDisplayer(){
        return DialogDisplayer.getDefault();
    }
    
    public static void show(String message, MessageType messageType){
        getDialogDisplayer().notify(new NotifyDescriptor.Message(message,messageType.notifyDescriptorType));
    }
    
    /**
    * Show an information dialog
    * @param message
    */
    public static void info(String message) {
        show(message, MessageType.INFO);
    }

    /**
    * Show an error dialog
    * @param message
    */
    public static void error(String message) {
        show(message, MessageType.ERROR);
    }

    /**
    * Show an error dialog for an exception
    * @param message
    * @param exception
    */
    /**
    * Show an exception message dialog
    *
    * @param message
    * @param exception
    */
    public static void showException(String message, Throwable exception) {
        getDialogDisplayer().notify(new NotifyDescriptor.Exception(exception, message));
    }
    
    
    
    public static void error(String message, Throwable exception) {
        showException(message, exception);
    }
    
        /**
    * Show an question dialog
    * @param message
    */
    public static void question(String message) {
        show(message, MessageType.QUESTION);
    }

    /**
    * Show an warning dialog
    * @param message
    */
    public static void warn(String message) {
        show(message, MessageType.WARNING);
    }

    /**
    * Show an plain dialog
    * @param message
    */
    public static void plain(String message) {
        show(message, MessageType.PLAIN);
    }
    
}
