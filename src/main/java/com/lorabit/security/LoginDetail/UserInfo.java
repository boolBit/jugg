package com.lorabit.security.LoginDetail;

/**
 * @author com.lorabit
 * @since 16-2-15
 */
public class UserInfo {
  private long userId;
  private String nickname;
  private String avatar;

  public UserInfo(long userId, String nickname, String avatar) {
    this.userId = userId;
    this.nickname = nickname;
    this.avatar = avatar;
  }

  public UserInfo(long userId, String nickname) {
    this.userId = userId;
    this.nickname = nickname;
    this.avatar = "";
  }


  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public static UserInfo create(long id, String name) {
    UserInfo info = new UserInfo(id, name);
    return info;
  }
}
