package it.pagopa.interop.probing.eservice.registry.updater.util.logging;

import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.MDC;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;
import it.pagopa.interop.probing.eservice.registry.updater.util.ProjectConstants;

public class LoggerLayout extends LayoutBase<ILoggingEvent> {

	@Override
	public String doLayout(ILoggingEvent event) {
		StringBuilder sbuf = new StringBuilder();
		sbuf.append(new Timestamp(event.getTimeStamp()));
		sbuf.append(" ");
		sbuf.append(event.getLevel());
		sbuf.append(" [");
		sbuf.append(event.getLoggerName());
		sbuf.append("]");
		try {
			sbuf.append(" - [CID=");
			MDC.put(ProjectConstants.TRACE_ID_PLACEHOLDER, UUID.randomUUID().toString().toLowerCase());
			sbuf.append(MDC.get(ProjectConstants.TRACE_ID_PLACEHOLDER));
			sbuf.append("] - ");
		} finally {
			MDC.remove(ProjectConstants.TRACE_ID_PLACEHOLDER);
		}

		sbuf.append(event.getFormattedMessage());
		sbuf.append("\n");
		return sbuf.toString();
	}

}
