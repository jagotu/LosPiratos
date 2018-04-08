package com.vztekoverflow.lospiratos.viewmodel;

/**
 * Abstract interface of a board's figure that can move and be damaged by other figures.
 */
public interface DamageableFigure {
    DamageSufferedResponse takeDamage(int value);
}
