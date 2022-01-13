package Server;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//Class to be able to handle all the logs comming from the Server and from the Client
public class MyLogger {
    private Logger logger;
    private FileHandler fileHandlr;

    /**
     * Contructor of the log
     *
     * @param pathname
     * @param nameLog
     * @throws IOException
     */
    public MyLogger(String pathname, String nameLog) throws IOException {

        //Check if the file where the logs are stored already exist or not
        File f = new File(pathname);
        if (!f.exists()){
            f.createNewFile();
        }

        //Get the logger
        logger = Logger.getLogger(nameLog);

        try {
            // define a new file handler and its log
            //using the same pathname as the file we created above
            fileHandlr = new FileHandler(pathname, true);

            //add the handle to the log
            logger.addHandler(fileHandlr);

            //use a  formatr and set it for the handler
            SimpleFormatter formatr = new SimpleFormatter();
            fileHandlr.setFormatter(formatr);

        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return logger;
    }
}
