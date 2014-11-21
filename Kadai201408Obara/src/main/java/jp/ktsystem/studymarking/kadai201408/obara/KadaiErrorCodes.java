package jp.ktsystem.studymarking.kadai201408.obara;

/**
 * <h3>
 *  課題エラーの列挙型.
 * </h3>
 *
 * @author ryoichi-obara
 * @since 2014/08/13
 */
public enum KadaiErrorCodes {

    /** ファイルの入出力エラー. */
    FILE_IO_ERROR(1),
    /** データ内部に半角英字以外の文字が存在した. */
    INVALID_CONTENTS(2),
    /** その他のエラー. */
    OTHERS(3);

    /** エラーコード　*/
    private int errorCode;

    /**
     * コンストラクター.<br>
     * @param errorCode エラーコード
     */
    KadaiErrorCodes(int errorCode) {
        this.errorCode = errorCode;
    }

    // ----- Getters and Setters.

    public int getErrorCode() {
        return errorCode;
    }

}
