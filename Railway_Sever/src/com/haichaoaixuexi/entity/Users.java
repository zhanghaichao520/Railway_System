package com.haichaoaixuexi.entity;
public class Users {
    private int USER_ID;

    private int ROLE_ID;
    
    private String USER_ROLE;

    private String USER_NUM;

    private String USER_NAME;

    private String USER_PWD;

    private String USER_TEL;

    private String USER_ADDR;

    private String POSITION;

    private int GROUP_ID;
    
    private String GROUP_NAME;
    
    private String USER_SEX;
    
    public String getUSER_SEX() {
		return USER_SEX;
	}
	public void setUSER_SEX(String uSER_SEX) {
		USER_SEX = uSER_SEX;
	}
	public int getGROUP_ID() {
		return GROUP_ID;
	}
	public void setGROUP_ID(int gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}

	public String getGROUP_NAME() {
		return GROUP_NAME;
	}
	public void setGROUP_NAME(String gROUP_NAME) {
		GROUP_NAME = gROUP_NAME;
	}
	public int getUSER_ID(){

        return this.USER_ID;
    }
    public void setUSER_ID(int USER_ID){

        this.USER_ID = USER_ID;
    }
    public int getROLE_ID(){

        return this.ROLE_ID;
    }
    public void setROLE_ID(int ROLE_ID){

        this.ROLE_ID = ROLE_ID;
    }
    public String getUSER_NUM(){

        return this.USER_NUM;
    }
    public void setUSER_NUM(String USER_NUM){

        this.USER_NUM = USER_NUM;
    }
    public String getUSER_NAME(){

        return this.USER_NAME;
    }
    public void setUSER_NAME(String USER_NAME){

        this.USER_NAME = USER_NAME;
    }
    public String getUSER_PWD(){

        return this.USER_PWD;
    }
    public void setUSER_PWD(String USER_PWD){

        this.USER_PWD = USER_PWD;
    }
    public String getUSER_TEL(){

        return this.USER_TEL;
    }
    public void setUSER_TEL(String USER_TEL){

        this.USER_TEL = USER_TEL;
    }
    public String getUSER_ADDR(){

        return this.USER_ADDR;
    }
    public void setUSER_ADDR(String USER_ADDR){

        this.USER_ADDR = USER_ADDR;
    }
    public String getPOSITION(){

        return this.POSITION;
    }
    public void setPOSITION(String POSITION){

        this.POSITION = POSITION;
    }
	public String getUSER_ROLE() {
		return USER_ROLE;
	}
	public void setUSER_ROLE(String uSER_ROLE) {
		USER_ROLE = uSER_ROLE;
	}
}
