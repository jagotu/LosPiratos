package com.vztekoverflow.lospiratos.viewmodel.logs;

import com.vztekoverflow.lospiratos.util.AxialCoordinate;
import com.vztekoverflow.lospiratos.viewmodel.ResourceReadOnly;
import com.vztekoverflow.lospiratos.viewmodel.Ship;
import com.vztekoverflow.lospiratos.viewmodel.Team;
import com.vztekoverflow.lospiratos.viewmodel.actions.Action;

public abstract class LogFormatter {
    public static LogFormatter hezkyČesky() {
        return new LogFormatter() {

            char quotesL = '"';
            char quotesR = '"';
            char braceL = '(';
            char braceR = ')';

            @Override
            String format(Ship s) {
                if (s == null) return "loď null";
                return s.getShipType().getČeskéJméno() + s + quotesL + s.getName() + quotesR
                        + space() + braceL + "pod vedením " + quotesL + s.getCaptainName() + quotesR + braceR
                        ;
            }

            @Override
            String format(AxialCoordinate c) {
                if (c == null) return "null";
                return c.toString();
            }

            @Override
            String format(Action a) {
                if (a == null) return "akce null";
                return "akce " + quotesL + a.getČeskéJméno() + quotesR;
            }

            @Override
            String format(Team t) {
                if (t == null) return "tým null";
                return "tým " + quotesL + t.getName() + quotesR;
            }

            @Override
            String format(ResourceReadOnly r) {
                return "suroviny (peníze, látka, kov, rum, dřevo):" + r.toString();
            }

            @Override
            String formatDamage(int value) {
                return value + " poškození";
            }

            @Override
            String formatIteration(int i) {
                return i + ". iterace";
            }

            @Override
            String const_AttacksVia() {
                return "útočí pomocí";
            }

            @Override
            String const_AttacksOn() {
                return "na";
            }

            @Override
            String const_AttackCauses() {
                return "způsobuje";
            }

            @Override
            String const_And() {
                return " a ";
            }


            @Override
            String const_Gains() {
                return "získává";
            }

            @Override
            String const_ThanksTo() {
                return "díky";
            }

            @Override
            String const_Dies() {
                return "umírá";
            }

            @Override
            String const_CollisionOn() {
                return "Kolize na";
            }
        };

    }

    public static LogFormatter stručně() {
        return new LogFormatter() {
            @Override
            String format(Ship s) {
                if (s == null || s.getName() == null) return "null";
                int length = Math.min(10, s.getName().length());
                return s.getName().substring(0, length);
            }

            @Override
            String format(AxialCoordinate c) {
                if (c == null) return "null";
                return c.toString();
            }

            @Override
            String format(Action a) {
                if (a == null) return "null";
                return a.getČeskéJméno();
            }

            @Override
            String format(Team t) {
                if (t == null || t.getName() == null) return "null";
                int length = Math.min(10, t.getName().length());
                return t.getName().substring(0, length);
            }

            @Override
            String format(ResourceReadOnly r) {
                if (r == null) return "null";
                return r.toString();
            }

            @Override
            String formatDamage(int value) {
                return value + "↯";
            }

            @Override
            String formatIteration(int i) {
                return "i " + i;
            }

            @Override
            String const_AttacksVia() {
                return ":";
            }

            @Override
            String const_AttacksOn() {
                return "\uD83D\uDCA3:"; //bomb symbol
            }

            @Override
            String const_AttackCauses() {
                return "↠";
            }

            @Override
            String const_And() {
                return ", ";
            }

            @Override
            String const_Gains() {
                return "⇇➕";
            }

            @Override
            String const_ThanksTo() {
                return ":";
            }

            @Override
            String const_Dies() {
                return "†";
            }

            @Override
            String const_CollisionOn() {
                return "\uD83D\uDCA5:";
            }
        };
    }

    abstract String format(Ship s);

    abstract String format(AxialCoordinate c);

    abstract String format(Action a);

    abstract String format(Team t);

    abstract String format(ResourceReadOnly r);

    abstract String formatDamage(int value);

    abstract String formatIteration(int i);

    abstract String const_AttacksVia();

    abstract String const_AttacksOn();

    abstract String const_AttackCauses();

    abstract String const_Gains();

    abstract String const_ThanksTo();

    abstract String const_And();

    abstract String const_Dies();

    abstract String const_CollisionOn();

    String space() {
        return " ";
    }

    String newLine() {
        return System.lineSeparator();
    }

}

