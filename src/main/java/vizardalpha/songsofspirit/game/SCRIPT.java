package vizardalpha.songsofspirit.game;

public interface SCRIPT<T> extends script.SCRIPT {
    /**
     * Executed when the game is running
     */
    void initGameRunning();

    /**
     * Executed right after the game UI is present
     */
    void initGamePresent();

    /**
     * Executed right after the settlement is present
     */
    void initSettlementViewPresent();

    /**
     * @param config nullable
     */
    void initGameSaveLoaded(T config);
}