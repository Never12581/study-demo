package com.hzx.juc.threadlocal;

public class TestUser {
        private String userName;
        private String userPwd;
        //省去全参构造方法和toString()方法

        public TestUser(String userName, String userPwd) {
            this.userName = userName;
            this.userPwd = userPwd;
        }

        public String getUserName() {
            return userName;
        }

        public TestUser setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getUserPwd() {
            return userPwd;
        }

        public TestUser setUserPwd(String userPwd) {
            this.userPwd = userPwd;
            return this;
        }
    }