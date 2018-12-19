package com.lowang.ormquerydsl.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  // @GeneratedValue
  // 可以用string,也可以用bigdecimal
  @Column(name = "id")
  protected String id;

  @Column(name = "name")
  private String name;

  @Column(name = "mobile")
  private String mobile;

  @Column(name = "locked")
  private boolean locked = false;

  @Column(name = "address")
  private String address;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @OrderBy("id asc")
  private List<Order> orders;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the mobile */
  public String getMobile() {
    return mobile;
  }

  /** @param mobile the mobile to set */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /** @return the locked */
  public boolean isLocked() {
    return locked;
  }

  /** @param locked the locked to set */
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  /** @return the address */
  public String getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(String address) {
    this.address = address;
  }

  /** @return the orders */
  public List<Order> getOrders() {
    return orders;
  }

  /** @param orders the orders to set */
  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format(
        "User [id=%s, name=%s, mobile=%s, locked=%s, address=%s]",
        id, name, mobile, locked, address);
  }
}
