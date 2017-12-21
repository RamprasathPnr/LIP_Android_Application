package com.omneagate.lip.Service;


import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sathya Rathinavelu on 5/9/2016.
 */

public abstract class Controller {

    @SuppressWarnings("unused")
    private static final String TAG = Controller.class.getSimpleName();
    private final List<Handler> outboxHandlers = new ArrayList<Handler>();


    public Controller() {

    }

    public void dispose() {
    }

    abstract public boolean handleMessage(int what, HashMap<String , Object> inputParams , Object data);

    abstract public boolean handleMessage_(int what, String inputParams , Object data);

    public boolean handleMessage(int what) {
        return handleMessage(what, null , null);
    }

    public final void addOutboxHandler(Handler handler) {
        outboxHandlers.add(handler);
    }

    public final void removeOutboxHandler(Handler handler) {
        outboxHandlers.remove(handler);
    }

    protected final void notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
        try {
            if (!outboxHandlers.isEmpty()) {
                for (Handler handler : outboxHandlers) {
                    Message msg = Message.obtain(handler, what, arg1, arg2, obj);
                    msg.sendToTarget();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
