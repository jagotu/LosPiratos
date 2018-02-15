package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;

public class ResourceGained extends LoggedEvent {
    Ship subject;
    ResourceReadOnly amount;
    Action cause;

    @Override
    public String getTextualDescription(LogFormatter f) {
        String s = f.space();
        return f.format(subject) + s + f.const_Gains() + s + f.format(amount)
                + s + f.const_ThanksTo() + s + f.format(cause);
    }

    public ResourceGained(Ship subject, ResourceReadOnly amount, Action cause) {
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

    public Action getCause() {
        return cause;
    }
}
