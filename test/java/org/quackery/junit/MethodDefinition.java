package org.quackery.junit;

import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

class MethodDefinition {
  public final List<Annotation> annotations;
  public final int modifiers;
  public final Class<?> returnType;
  public final String name;
  public final List<Class<?>> parameters;
  public final Object returning;
  public final Class<? extends Throwable> throwing;

  private MethodDefinition(
      List<Annotation> annotations,
      int modifiers,
      Class<?> returnType,
      String name,
      List<Class<?>> parameters,
      Object returning,
      Class<? extends Throwable> throwing) {
    this.annotations = annotations;
    this.modifiers = modifiers;
    this.returnType = returnType;
    this.name = name;
    this.parameters = parameters;
    this.returning = returning;
    this.throwing = throwing;
  }

  public MethodDefinition() {
    this(Arrays.<Annotation> asList(), 0, null, null, Arrays.<Class<?>> asList(), null, null);
  }

  public MethodDefinition annotations(Annotation... annotations) {
    return new MethodDefinition(asList(annotations), modifiers, returnType, name, parameters, returning, throwing);
  }

  public MethodDefinition modifiers(int modifiers) {
    return new MethodDefinition(annotations, modifiers, returnType, name, parameters, returning, throwing);
  }

  public MethodDefinition returnType(Class<?> returnType) {
    return new MethodDefinition(annotations, modifiers, returnType, name, parameters, returning, throwing);
  }

  public MethodDefinition name(String name) {
    return new MethodDefinition(annotations, modifiers, returnType, name, parameters, returning, throwing);
  }

  public MethodDefinition parameters(Class<?>... parameters) {
    return new MethodDefinition(annotations, modifiers, returnType, name, asList(parameters), returning, throwing);
  }

  public MethodDefinition returning(Object returning) {
    return new MethodDefinition(annotations, modifiers, returnType, name, parameters, returning, throwing);
  }

  public MethodDefinition throwing(Class<? extends Throwable> throwing) {
    return new MethodDefinition(annotations, modifiers, returnType, name, parameters, returning, throwing);
  }
}
