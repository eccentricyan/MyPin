package com.eccentricyan.mypin.common.command;

import android.util.Log;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class ReplyCommand<T> {
    private Action execute0;
    private Consumer<T> execute1;

    private Predicate<Boolean> canExecute0;

    public ReplyCommand(Action execute) {
        this.execute0 = execute;
    }


    public ReplyCommand(Consumer<T> execute) {
        this.execute1 = execute;
    }

    /**
     *
     * @param execute callback for event
     * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
     */
    public ReplyCommand(Action execute, Predicate<Boolean> canExecute0) {
        this.execute0 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     *
     * @param execute callback for event,this callback need a params
     * @param canExecute0 if this function return true the action execute would be invoked! otherwise would't invoked!
     */
    public ReplyCommand(Consumer<T> execute, Predicate<Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }


    public void execute() {
        if (execute0 != null && canExecute0()) {
            try {
                execute0.run();
            } catch (Exception e) {
                Log.e("replyCommand excute", e.getMessage());
            }
        }
    }

    private boolean canExecute0() {
        Boolean rst;
        try {
            rst = canExecute0.test(true);
        } catch(Exception e) {
            return true;
        }
        return rst;
    }


    public void execute(T parameter) {
        if (execute1 != null && canExecute0()) {
            try {
                execute1.accept(parameter);
            } catch (Exception e) {
                Log.e("replyCommand excute T", e.getMessage());
            }

        }
    }

}