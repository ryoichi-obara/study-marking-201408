package jp.ktsystem.studymarking.kadai201408.obara;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * <h3>
 *  Test class for TestKadai201408#calcScoreSum.
 * </h3>
 *
 * @author ryoichi-obara
 * @since 2014/08/16
 */
public class TestKadai201408_printMaxScore {

//    @Rule public ExpectedException thrown = ExpectedException.none();
    @Rule
    public TestName name= new TestName();

    private static final String USER_DIR = System.getProperty("user.dir"); // ex. /Users/ryoichi0102/git/study-marking-201408-obara/Kadai201408Obara
    private static final String INPUT_DIR  = USER_DIR + "/src/test/resources/input/";
    private static final String OUTPUT_DIR = USER_DIR + "/src/test/resources/output/";
    private static final String CHECK_DIR  = USER_DIR + "/src/test/resources/check/";
    private static final String EXTENSION = ".txt";

    private String defaultInputPath;
    private String defaultOutputPath;
    private String checkPath;

    // ----- Public methods.

    /**
     * 出力ディレクトリを空にしておく.<br>
     */
    @BeforeClass
    public static void beforeClass() { // @BeforeClassはstaticでなければいけないそうです.

        try {
            FileUtils.cleanDirectory(Paths.get(OUTPUT_DIR).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * テストメソッド名からデフォルトの入力・出力・チェック用ファイルパスを生成.<br>
     */
    @Before
    public void before() {
        int found = name.getMethodName().indexOf("_");
        if (-1 == found) {
            Assert.fail("Invalid test method name:" + name.getMethodName());
        }
        defaultInputPath  = INPUT_DIR  + name.getMethodName().subSequence(0, found) + EXTENSION;
        defaultOutputPath = OUTPUT_DIR + name.getMethodName().subSequence(0, found) + EXTENSION;
        checkPath         = CHECK_DIR  + name.getMethodName().subSequence(0, found) + EXTENSION;
    }

    @Test
    public void TC101_ファイルパスにnull() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, null, null);
    }
    @Test
    public void TC102_ファイルパスに空文字() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, "", "");
    }
    @Test
    public void TC103_ファイルパス不正() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, "/invalid-path", "/invalid-path");
    }
    @Test
    public void TC104_指定パスがディレクトリ() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, INPUT_DIR, OUTPUT_DIR);
    }
    @Test
    public void TC105_指定パスが存在しない() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, INPUT_DIR + "non-existent-file.txt", OUTPUT_DIR + "non-existent-file.txt");
    }

    @Test
    public void TC111_出力ファイルパスにnull() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, null);
    }
    @Test
    public void TC112_出力ファイルパスに空文字() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, "");
    }
    @Test
    public void TC113_出力ファイルパス不正() {
        // 多分,拡張子無のファイルで出力されちゃうのと、ルートに作成されると迷惑なので、とりあえずコメントアウト.
//        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, "/invalid-path");
    }
    @Test
    public void TC114_出力指定パスが既存ディレクトリ() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, OUTPUT_DIR);
    }
    @Test
    public void TC115_出力指定パスが存在しない() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, OUTPUT_DIR + "non-existent-file.txt");
    }

    @Test
    public void TC201_空のファイル() {
        callForSuccess();
    }
    @Test
    public void TC202_空行() {
        callForSuccess();
    }
    @Test
    public void TC203_データ数1() { // A
        callForSuccess();
    }
    @Test
    public void TC204_データ数2() { // b,c
        callForSuccess();
    }
    @Test
    public void TC205_カンマで終わる() { // AA,b,D,
        callForSuccess();
    }
    @Test
    public void TC206_不正なデータが含まれる() { // B,$,C
        callForError__(KadaiErrorCodes.INVALID_CONTENTS);
    }
    @Test
    public void TC207_2行目以降に不正なデータが含まれる() { // B,C ¥r¥n $
        callForSuccess();
    }
    @Test
    public void TC208_2行目以降にデータが含まれる() { // B,C ¥r¥n D
        callForSuccess();
    }
    @Test
    public void TC209_最高得点が複数ある() { // F,c,b,A,A,a
        callForSuccess();
    }

    @Test
    public void TC301_ファイルパスにnull() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, null);
    }
    @Test
    public void TC302_ファイルパスに空文字() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, "");
    }
//    @Test
//    public void TC303_ファイルパス不正() {
//        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, "/invalid-path");
//    }
    @Test
    public void TC304_指定パスがディレクトリ() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, OUTPUT_DIR);
    }
//    @Test
//    public void TC305_指定パスが存在しない() {
//        callForError__(KadaiErrorCodes.FILE_IO_ERROR, defaultInputPath, OUTPUT_DIR + "non-existent-file.txt");
//    }

    /**
     * 出力されたファイルの中身が、チェック用のものと同じかどうかチェックする.<br>
     */
    @After
    public void after() {

        // ファイル存在チェック.両方存在しない場合はOKとする.
        if (!Paths.get(checkPath).toFile().exists() && !Paths.get(defaultOutputPath).toFile().exists()) {
            return;
        }
        
        try (
            BufferedReader checkReader  = Files.newBufferedReader(Paths.get(checkPath), Charset.defaultCharset());
            BufferedReader outputReader = Files.newBufferedReader(Paths.get(defaultOutputPath), Charset.defaultCharset());
        ) {
            assertTrue(IOUtils.contentEquals(checkReader, outputReader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ----- Private methods.

    private void callForError__(KadaiErrorCodes expected) {
        callForError__(expected, defaultInputPath, defaultOutputPath);
    }
    private void callForError__(KadaiErrorCodes expected, String inputPath, String outputPath) {
        try {
            call(inputPath, outputPath);
        } catch (KadaiException e) {
            assertThat(expected, is(e.getErrorCode()));
        }
    }
    private void callForSuccess() {
        callForSuccess(defaultInputPath, defaultOutputPath);
    }
    private void callForSuccess(String inputPath, String outputPath) {
        try {
            call(inputPath, outputPath);
        } catch (KadaiException e) {
            throw new RuntimeException(e);
        }
    }
    private void call(String inputPath, String outputPath) throws KadaiException {
        Kadai201408.printMaxScore(inputPath, outputPath);
    }

}
