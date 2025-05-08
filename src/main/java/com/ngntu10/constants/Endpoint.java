package com.ngntu10.constants;

public class Endpoint {
    public static final String API_PREFIX = "/api";
    
    public static final class Auth{
        public static final String BASE = API_PREFIX + "/auth";
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
        public static final String EMAIL_VERIFICATION_TOKEN = "/email-verification/{token}";
        public static final String REFRESH = "/refresh";
        public static final String RESET_PASSWORD = "/reset-password";
        public static final String RESET_PASSWORD_EMAIL = "/reset-password/{email}";
        public static final String LOGOUT = "/logout";
    }

    public static final class Account {
        public static final String BASE = API_PREFIX + "/account";
        public static final String ME = "/me";
        public static final String PASSWORD = "/password";
        public static final String RESEND_EMAIL_VERIFICATION = "/resend-email-verification"; 
    }

    public static final class User {
        public static final String BASE = API_PREFIX + "/users";
        public static final String ID = "{userId}";
        public static final String DELETE_MANY = "/delete-many";

    }
    public static final class Role {
        public static final String BASE = API_PREFIX + "/roles";
        public static final String ID = "{roleId}";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class Product{
        public static final String BASE = API_PREFIX + "/products";
        public static final String SLUG = "{slug}";
        public static final String ID = "/id/{productId}";
        public static final String CHANGE_IMAGES = "/images";
        public static final String DELETE_MANY = "/delete-many";
    }

    public static  class Blog{
        public static final String BASE = API_PREFIX + "/blogs";
        public static final String SLUG = "{slug}";
        public static final String ID = "/id/{blogId}";
        public static final String CHANGE_IMAGES = "/images";
    }
    
    public static class Document{
        public static final String BASE = API_PREFIX + "/documents";
        public static final String ID = "/id/{documentId}";
        public static final String DOWNLOAD = "/download/{documentId}"; 
        public static final String DELETE_MANY = "/delete-many";
    }
        
    public static class Dashboard{
        public static final String BASE = API_PREFIX + "/dashboard";
    }

}

