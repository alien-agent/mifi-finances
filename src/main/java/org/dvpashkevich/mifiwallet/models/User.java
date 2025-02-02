package org.dvpashkevich.mifiwallet.models;

public class User implements java.io.Serializable{
    private String login;
    private String password;
    private Wallet wallet;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.wallet = new Wallet();
    }

    // Геттеры и сеттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public Wallet getWallet() { return wallet; }
    public void setWallet(Wallet wallet) { this.wallet = wallet; }
}