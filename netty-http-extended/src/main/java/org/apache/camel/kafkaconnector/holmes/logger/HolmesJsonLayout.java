package org.apache.camel.kafkaconnector.holmes.logger;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

import java.nio.charset.Charset;

@Plugin(name = "HolmesJsonLayout", category = "Core", elementType = "layout", printObject = true)
public class HolmesJsonLayout extends AbstractStringLayout {

  private static final String DEFAULT_EOL = "\r\n";

  protected HolmesJsonLayout(Charset charset) {
    super(charset);
  }

  @PluginFactory
  public static HolmesJsonLayout createLayout(
      @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset) {
    return new HolmesJsonLayout(charset);
  }

  @Override
  public String toSerializable(LogEvent logEvent) {
    return logEvent.getMessage().getFormattedMessage() + DEFAULT_EOL;
  }
}