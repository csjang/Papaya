package net.ddns.hyena.papaya;

public class UserInfo {

    private String userId;
    private String password;
    private String sessionId;
    private String lang;
    private boolean tempPass;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isTempPass() {
        return tempPass;
    }

    public void setTempPass(boolean tempPass) {
        this.tempPass = tempPass;
    }
}
