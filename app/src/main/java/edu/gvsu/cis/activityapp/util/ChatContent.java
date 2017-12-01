package edu.gvsu.cis.activityapp.util;

import java.util.ArrayList;

/**
 * Created by daniel on 11/20/17.
 */

public class ChatContent {

    public static ArrayList<Chat> GROUPS = new ArrayList<>();

    public static void addChat (Chat c) {
        GROUPS.add(c);
    }

    public static class Chat {
        private String mEventName;
        private String mLastMessage;
        private String mSender;

        public Chat() { }

        public Chat(String eventName, String message, String sender) {
            this.mEventName = eventName;
            this.mLastMessage = message;
            this.mSender = sender;
        }


        public String getmEventName() {
            return mEventName;
        }

        public void setmEventName(String mEventName) {
            this.mEventName = mEventName;
        }

        public String getmLastMessage() {
            return mLastMessage;
        }

        public void setmLastMessage(String mLastMessage) {
            this.mLastMessage = mLastMessage;
        }

        public String getmSender() {
            return mSender;
        }

        public void setmSender(String mSender) {
            this.mSender = mSender;
        }
    }
}
