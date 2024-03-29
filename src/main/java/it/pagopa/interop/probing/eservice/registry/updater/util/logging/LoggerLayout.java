package it.pagopa.interop.probing.eservice.registry.updater.util.logging;

import java.sql.Timestamp;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class LoggerLayout extends LayoutBase<ILoggingEvent> {

  @Override
  public String doLayout(ILoggingEvent event) {
    StringBuilder sbuf = new StringBuilder();
    sbuf.append(new Timestamp(event.getTimeStamp()));
    sbuf.append(" ");
    sbuf.append(event.getLevel());
    sbuf.append(" [");
    sbuf.append(event.getLoggerName());
    sbuf.append("] - ");
    sbuf.append(event.getFormattedMessage());
    sbuf.append("\n");
    return sbuf.toString();
  }

}
