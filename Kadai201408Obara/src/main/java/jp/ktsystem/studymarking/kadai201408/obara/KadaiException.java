package jp.ktsystem.studymarking.kadai201408.obara;

/**
 * <h3>
 *  課題エラーの例外クラス.
 * </h3>
 *
 * @author ryoichi-obara
 * @since 2014/08/13
 */
public class KadaiException extends Exception {

    private static final long serialVersionUID = 6383968668529811463L;

    private KadaiErrorCodes errorCode;

    /**
     * コンストラクター.<br>
     * @param errorCode KadaiErrorCodes
     */
    public KadaiException(KadaiErrorCodes errorCode) {
        super();
        this.errorCode = errorCode;
    }

    /**
     * コンストラクター(cause有).<br>
     * @param errorCode KadaiErrorCodes
     */
    public KadaiException(KadaiErrorCodes errorCode, Exception cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    // ----- Getters and Setters.

    public KadaiErrorCodes getErrorCode() {
        return this.errorCode;
    }

}
