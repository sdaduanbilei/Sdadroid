package com.scorpio.frame.event;

/**
 * Created by scorpio on 15-2-2.
 */
public class PostEvent {
    int action ;
    String content ;
    Object object ;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
