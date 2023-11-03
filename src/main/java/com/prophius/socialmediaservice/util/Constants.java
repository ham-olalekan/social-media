package com.prophius.socialmediaservice.util;

public class Constants {
    public final static String JDBC_H2_URL = "jdbc:h2:mem:prophius_social_service;DB_CLOSE_DELAY=-1;MODE=MySQL";
    public final static String DRIVER_CLASS_NAME = "org.h2.Driver";

    public final static String PROCESSED_AUTH_TOKEN = "Processed-Auth";

    public final static String X_ACCESS_TOKEN = "X-Access-Token";

    public final static String X_REFRESH_TOKEN = "X-Refresh-Token";

    public static final String SCOPES = "scopes";

    public final static String ENABLED = "enabled";

    public static final String TYPE = "type";

    public static final String BASIC_AUTHS = "basic_auths";

    public final static String BASIC_AUTH_PREFIX = "PRO_";

    public final static String IAPPENDABLE_REF_SEPARATOR = "_";

    public static class EntityColumns {

        public final static String ID = "id";
        public final static String GOAL_ID = "goal_id";
        public final static String USERNAME = "username";
        public final static String EMAIL = "email";
        public final static String PHONE_NO = "phone_no";

        public final static String USER_ID = "user_id";

        public final static String TOKEN_STORE = "tokens";

        public final static String USERS = "users";
    }
}
