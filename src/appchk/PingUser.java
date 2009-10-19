package appchk;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PingUser {
    
    @Id
    private String email;
    private Date createdAt;

    public PingUser(String name) {
        this.email=name;
        this.createdAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = name;
    }

}