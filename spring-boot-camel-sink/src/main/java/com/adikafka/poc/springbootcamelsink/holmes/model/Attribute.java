package com.adikafka.poc.springbootcamelsink.holmes.model;

public class Attribute {
  private String name;
  private Object value;

  public Attribute(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "Attribute{" +
        "name='" + name + '\'' +
        ", value=" + value +
        '}';
  }
}
