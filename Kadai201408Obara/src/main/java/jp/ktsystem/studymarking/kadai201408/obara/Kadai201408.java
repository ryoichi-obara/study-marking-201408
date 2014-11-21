package jp.ktsystem.studymarking.kadai201408.obara;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h3>
 *  教育課題2014/08.
 * </h3>
 *
 * @author ryoichi-obara
 * @since 2014/08/13
 */
public class Kadai201408 {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final char BOM_BIG_ENDIAN = 0xFEFF;
    private static final long MAX_FILESIZE = 1 * 1024 * 1024; // 1MB
    private static final String DELIMITOR = ",";

    // ----- Public methods.

    /**
     * テキストファイルを読み込み、データの点数の積の合計を求める.<br>
     *
     * @param anInputPath フルパスで****.txtまで指定する
     * @return データの点数の積の合計
     * @throws KadaiException
     */
    public static long calcScoreSum(String anInputPath) throws KadaiException {

        // ファイルより、先頭の1行のデータを読み込む.
        List<DataBean> dataList = read1stLineFromFile(anInputPath);

        // 得点の計算.
        long totalScore = 0;
        for (DataBean bean : dataList) {
            totalScore += bean.getScore();
        }

        return totalScore;
    }

    /**
     * 全データのデータの点数のうち、最大の点数を持つデータをファイルに出力する.<br>
     *
     * 最大値が複数存在した場合はそのすべてのデータを改行して出力してください。文字コードは入力ファイルと同様.<br>
     * エラー時はエラー情報をファイルには出力しない.<br>
     *
     * @param anInputPath フルパスで****.txtまで指定する
     * @param anOutputPath フルパスで****.txtまで指定する
     * @throws KadaiException
     */
    public static void printMaxScore(String anInputPath, String anOutputPath) throws KadaiException {

        // ファイル読み込み.
        List<DataBean> dataList = read1stLineFromFile(anInputPath);

        // ファイル書き出し準備.
        Path path = Paths.get(anOutputPath);

        // 何も読み込めなかった場合は、処理終了.
        if (dataList.isEmpty()) {
            // ファイル内容が空の場合、空データが存在するということで正常処理(Lv2も最大値0で出力)をしてくださいとのこと...
            try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET)) {
                writer.write(new DataBean(0, "").toString());
                writer.newLine();
            } catch (IOException e) {
                throw new KadaiException(KadaiErrorCodes.FILE_IO_ERROR, e);
            } catch (Exception e) {
                throw new KadaiException(KadaiErrorCodes.OTHERS, e);
            }
            return;
        }

        // 高得点順に並び替える.
        Collections.sort(dataList);

        // データ内の最高得点を算出.
        long maxScore;
        if (dataList.isEmpty()) {
            maxScore = 0;
        } else {
            // 先ほど並び替えたので、先頭の要素が最高得点.
            maxScore = dataList.get(0).getScore();
        }

        // try-with-resourcesでファイル出力.
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET)) {
            for (DataBean bean : dataList) {
                // 最高得点でなくなったら書き出しをやめる.
                if (bean.getScore() < maxScore) {
                    break;
                } else { 
                    writer.write(bean.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new KadaiException(KadaiErrorCodes.FILE_IO_ERROR, e);
        } catch (Exception e) {
            throw new KadaiException(KadaiErrorCodes.OTHERS, e);
        }
    }

    // ----- Private methods.

    /**
     * ファイルから読み込んで、先頭行のデータをListで返す.<br>
     *
     * @param anInputPath 
     * @return List<List<DataBean>>
     * @throws KadaiException
     */
    private static List<DataBean> read1stLineFromFile(String anInputPath) throws KadaiException {

        if (anInputPath == null) {
            throw new KadaiException(KadaiErrorCodes.FILE_IO_ERROR);
        }

        Path path = Paths.get(anInputPath);

        // 今回はファイルサイズのチェックは不要とのことだが、チェックする場合はこのようなロジックになる。
        if (MAX_FILESIZE < path.toFile().length()) {
//            throw new KadaiException(KadaiErrorCodes.FILE_IO_ERROR);
        }

        // Files.readAllLines()を呼び出し、ファイルの内容を一気に読み込む.
        List<String> lines;
        try {
            lines = Files.readAllLines(path, CHARSET);
        } catch (IOException e) {
            throw new KadaiException(KadaiErrorCodes.FILE_IO_ERROR, e);
        } catch (Exception e) {
            throw new KadaiException(KadaiErrorCodes.OTHERS, e);
        }

        if (lines.isEmpty()) {
            return Collections.<DataBean>emptyList();
        }

        // UTF-8なのにBig endianのBOM(Byte Order Mark)が含まれていたら、読み飛ばす.
        String firstLine = lines.get(0);
        if (firstLine.charAt(0) == BOM_BIG_ENDIAN) {
            firstLine = firstLine.substring(1);
        }

        // 今回は1行だけ処理すればよいので、ループにはしない.
        // 1行分処理して返す.
        return processLine(firstLine);
    }

    /**
     * 1行分のデータを処理してListで返す.<br>
     *
     * @param line 1行分のデータ
     * @return List<DataBean>
     * @throws KadaiException
     */
    private static List<DataBean> processLine(String line) throws KadaiException {
        // 区切り文字で分割.
        String[] dataArray = line.split(DELIMITOR, -1);

        List<DataBean> dataList = new ArrayList<>();
        for (int sequence = 0; sequence < dataArray.length; sequence++) {
            // Beanに詰める段階で1オリジンとする.
            dataList.add(new DataBean(sequence + 1, dataArray[sequence]));
        }

        return dataList;
    }

}
