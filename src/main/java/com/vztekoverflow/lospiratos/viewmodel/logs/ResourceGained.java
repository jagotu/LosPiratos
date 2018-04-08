package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;

public class ResourceGained extends LoggedEvent {
    private Ship subject;
    private ResourceReadOnly amount;
    private Action cause;

    @Override
    public String getTextualDescription(LogFormatter f) {
        String s = f.space();
        String thanksTo = "";
        if (cause != null) {
            thanksTo = s + f.const_ThanksTo() + s + f.format(cause);
        }
        return f.format(subject) + s + f.const_Gains() + s + f.format(amount)
                + thanksTo;
    }

    ResourceGained(Ship subject, ResourceReadOnly amount, Action cause) {
        this.subject = subject;
        this.amount = amount;
        this.cause = cause;
    }

    public Ship getSubject() {
        return subject;
    }

    public ResourceReadOnly getAmount() {
        return amount;
    }

    /**
     * @return null if the cause is unknown or couldn't be unambiguously determined
     */
    public Action getCause() {
        return cause;
    }
}
