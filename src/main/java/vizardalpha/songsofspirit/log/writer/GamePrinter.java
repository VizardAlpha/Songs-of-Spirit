package vizardalpha.songsofspirit.log.writer;

import snake2d.Printer;
import vizardalpha.songsofspirit.util.StringUtil;


/**
 * Writes to the game {@link Printer#ln(Object)} or {@link Printer#err(Object)}
 */
public class GamePrinter extends AbstractLogWriter {

    public GamePrinter(String prefix, String messageFormat, String name) {
        super(prefix, messageFormat, name);
    }

    @Override
    public void error(String msgPrefix, String formatMsg, Object[] args) {
        try {
            Printer.err(String.format(messageFormat,
                prefix,
                timestamp(),
                name,
                msgPrefix,
                String.format(formatMsg, StringUtil.stringifyValues(args))));
        } catch (Exception e) {
            problemLogging(formatMsg, args, e);
        }
    }

    @Override
    public void log(String msgPrefix, String formatMsg, Object[] args) {
        try {
            Printer.ln(String.format(messageFormat,
                prefix,
                timestamp(),
                name,
                msgPrefix,
                String.format(formatMsg, StringUtil.stringifyValues(args))));
        } catch (Exception e) {
            problemLogging(formatMsg, args, e);
        }
    }

    @Override
    public void exception(Throwable ex) {
        String exceptionString = StringUtil.stringify(ex);
        Printer.err(exceptionString);
    }
}
