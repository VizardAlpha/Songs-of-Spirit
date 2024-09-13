package vizardalpha.songsofspirit.log.writer;

import snake2d.LOG;
import vizardalpha.songsofspirit.util.StringUtil;


/**
 * Writes to the game {@link LOG#ln(Object)} or {@link LOG#err(Object)}
 */
public class GameLOG extends AbstractLogWriter {

    public GameLOG(String prefix, String messageFormat, String name) {
        super(prefix, messageFormat, name);
    }

    @Override
    public void error(String msgPrefix, String formatMsg, Object[] args) {
        try {
            LOG.err(String.format(messageFormat,
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
            LOG.ln(String.format(messageFormat,
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
        LOG.err(exceptionString);
    }
}
