package kz.itbc.docviewhub.entity;

public class User {
    private long id_User;
    private String login;
    private String password;

    public User(long id_User, String login, String password) {
        this.id_User = id_User;
        this.login = login;
        this.password = password;
    }

    public User() {}

    public long getId_User() {
        return id_User;
    }

    public void setId_User(long id_User) {
        this.id_User = id_User;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
