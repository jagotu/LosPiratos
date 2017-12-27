package com.vztekoverflow.lospiratos.util;

import java.io.PrintStream;

public class Warnings {

    private static boolean enabled = true;
    private static boolean soundEnabled = false;
    private static PrintStream output = System.err;

    public static boolean isEnabled() {
        return enabled;
    }
    public static void setEnabled(boolean enabled) {
        Warnings.enabled = enabled;
    }
    public static void enable() {enabled = true;}
    public static void disable() {enabled = false;}

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
    public static void setSoundEnabled(boolean soundEnabled) {
        Warnings.soundEnabled = soundEnabled;
    }

    public static PrintStream getOutput() {
        return output;
    }

    public static void setOutput(PrintStream output) {
        Warnings.output = output;
    }
    public static void setOutputStdErr() {setOutput(System.err);}

    public static void makeWarning(String sender, String message){
        if(!enabled) return;
        output.println("Los WARNINGos Piratos: " + sender + ": " + message);
        if(soundEnabled) beep();
    }
    public static void makeStrongWarning(String sender, String message){
        if(!enabled) return;
        output.println("  !!! Strong warning: " + sender + ": " + message + " !!! ");
        if(soundEnabled) beep();
    }
    public static void panic(String sender, String message){
        output.println("  !!!!! PPH: " + sender + ": " + message + " !!!!! ");
        if(output != System.err)
            System.err.println("  !!!!! PPH: " + sender + ": " + message + " !!!!! ");
        beep();
    }

    private static void beep(){
        java.awt.Toolkit.getDefaultToolkit().beep();
    }
}
