package com.omega.Util;

public interface ISwitchToFragment {
    void switchToCreateFlashCard(String group);
    void switchToPlayMode(String group);
    void switchToCheckoutFlashCard();

    void switchToPlayModeCasual(String group);

    void switchToPlayModeReverseMatch(String group);

    void switchToPlayModeTrueFalse(String group);
}