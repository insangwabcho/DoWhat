package com.comnawa.dowhat.sangjin;

/**
 * Created by sangjin on 2017-06-28.
 */

public class MemberDTO {
    private String friendId;
    private String friendName;

    public MemberDTO() {
    }

    public MemberDTO(String friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "friendId='" + friendId + '\'' +
                ", friendName='" + friendName + '\'' +
                '}';
    }
}
