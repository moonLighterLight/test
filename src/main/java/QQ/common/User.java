package QQ.common;

import java.io.Serializable;

/**
 * 用户信息
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String password;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
