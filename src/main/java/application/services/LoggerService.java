package application.services;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectArrayMessage;


public class LoggerService {

    private static Logger logger;

    private LoggerService(){
    }

    public static void log(Level level, String message){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        logger = LogManager.getLogger(stackTraceElements[2].getClassName());
        logger.log(level, new ObjectArrayMessage(stackTraceElements[2].getMethodName(), message));
    }


}
