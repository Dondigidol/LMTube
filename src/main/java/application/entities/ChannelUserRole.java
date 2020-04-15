package application.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "channel_user_role")
public class ChannelUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long channelId;
    private long userId;
    private byte roleId;
    private Date date;
    private boolean isActive;

    public ChannelUserRole(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte getRoleId() {
        return roleId;
    }

    public void setRoleId(byte roleId) {
        this.roleId = roleId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "ChannelUserRole{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", userId=" + userId +
                ", roleId=" + roleId +
                ", date=" + date +
                ", isActive=" + isActive +
                '}';
    }
}
