package com.lorabit.cloud;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author com.lorabit
 * @since 16-2-16
 */
public class RuntimeConfig extends Observable implements InitializingBean {
  private boolean isHalt;
  private List<Observer> observers;

  public boolean isHalt() {
    return isHalt;
  }

  public void halt() {
    this.isHalt = true;
    setChanged();
    notifyObservers();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    for(Observer observer : observers){
      this.addObserver(observer);
    }
  }

  public void setObservers(List<Observer> observers) {
    this.observers = observers;
  }
}
