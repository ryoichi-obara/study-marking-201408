package jp.ktsystem.studymarking.kadai201408.obara;

import java.io.Serializable;

/**
 * <h3>
 *  データ格納クラス.
 * </h3>
 *
 * @author ryoichi-obara
 * @since 2014/08/14
 */
public class DataBean implements Serializable, Comparable<DataBean> {

    private static final long serialVersionUID = 9163237229377188497L;

    private final int sequence; // 要素の順番
    private final String data; // 分割されたデータ
    private final long score; // 計算後の点数

    /**
     * コンストラクター.<br>
     *
     * @param sequence 順番
     * @param data データの内容
     * @throws KadaiException
     */
    public DataBean(int sequence, String data) throws KadaiException {
        this.sequence = sequence;
        this.data = data;
        this.score = calcScore();
    }

    // ----- Public methods.

    @Override
    public String toString() {
        return sequence + ":" + data + ":" + score;
    }

    // ----- Protected methods.

    /**
     * データの点数を計算する.<br>
     *
     * スコアの計算方法が変更になったらこのメソッドをオーバライドすればよい。
     *
     * @return データの点数
     * @throws KadaiException
     */
    protected long calcScore() throws KadaiException {
        long score = 0L;
        String upperCase = data.toUpperCase();
        for (int index = 0; index < upperCase.length(); index++) {
            char c = upperCase.charAt(index);
            if (c < 'A' || 'Z' < c) {
                throw new KadaiException(KadaiErrorCodes.INVALID_CONTENTS);
            }
            score += (c - 'A' + 1);
        }
        return sequence * score;
    }

    /**
     * スコアの降順(次にデータ出現順)に並べる.<br>
     * @param o DataBean
     */
    @Override
    public int compareTo(DataBean o) {
        if (this.score == o.score) {
            if (this.sequence == o.sequence) {
                return 0;
            } else if (this.sequence < o.sequence) {
                return -1;
            } else {
                return 1;
            }
        } else if (this.score < o.score) {
            return 1;
        } else {
            return -1;
        }
    }

    // ----- Getters and Setters.

    public int getSequence() {
        return sequence;
    }
    public String getData() {
        return data;
    }
    public long getScore() {
        return score;
    }

}
