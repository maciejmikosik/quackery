package org.quackery.contract.collection;

public interface Creator {
  <T> T create(Class<T> cast, Object original) throws Throwable;
}
