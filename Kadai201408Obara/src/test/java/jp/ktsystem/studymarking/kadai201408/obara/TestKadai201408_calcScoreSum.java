package jp.ktsystem.studymarking.kadai201408.obara;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
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
public class TestKadai201408_calcScoreSum {

//    @Rule public ExpectedException thrown = ExpectedException.none();
    @Rule
    public TestName name= new TestName();

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String INPUT_DIR  = USER_DIR + "/src/test/resources/input/";
    private static final String EXTENSION = ".txt";

    private String defaultInputPath;

    // ----- Public methods.

    @Before
    public void before() {
        int found = name.getMethodName().indexOf("_");
        if (-1 == found) {
            Assert.fail("Invalid test method name:" + name.getMethodName());
        }
        defaultInputPath = INPUT_DIR + name.getMethodName().subSequence(0, found) + EXTENSION;
    }

//    @Test(expected = KadaiException.class)
    @Test
    public void TC101_ファイルパスにnull() {
//      thrown.expect(KadaiException.class);
//      thrown.expectMessage("");
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, null);
    }
    @Test
    public void TC102_ファイルパスに空文字() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, "");
    }
    @Test
    public void TC103_ファイルパス不正() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, "/invalid-path");
    }
    @Test
    public void TC104_指定パスがディレクトリ() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, INPUT_DIR);
    }
    @Test
    public void TC105_指定パスが存在しない() {
        callForError__(KadaiErrorCodes.FILE_IO_ERROR, INPUT_DIR + "non-existent-file.txt");
    }

    @Test
    public void TC201_空のファイル() {
        callForSuccess(0L);
    }
    @Test
    public void TC202_空行() {
        callForSuccess(0L);
    }
    @Test
    public void TC203_データ数1() { // A
        callForSuccess(1L);
    }
    @Test
    public void TC204_データ数2() { // b,c
        callForSuccess(8L);
    }
    @Test
    public void TC205_カンマで終わる() { // AA,b,D,
        callForSuccess(18L);
    }
    @Test
    public void TC206_不正なデータが含まれる() { // B,$,C
        callForError__(KadaiErrorCodes.INVALID_CONTENTS);
    }
    @Test
    public void TC207_2行目以降に不正なデータが含まれる() { // B,C
        callForSuccess(8L);
    }
    @Test
    public void TC208_2行目以降にデータが含まれる() { // B,C ¥r¥n D
        callForSuccess(8L);
    }
    @Test
    public void TC209_最高得点が複数ある() { // F,c,b,A,A,a
        callForSuccess(33L);
    }

    // ----- Private methods.

    private void callForError__(KadaiErrorCodes expected) {
        callForError__(expected, defaultInputPath);
    }
    private void callForError__(KadaiErrorCodes expected, String inputPath) {
        try {
            call(inputPath); // エラーが起きること前提なので、-1を渡す.
            fail(inputPath);
        } catch (KadaiException e) {
            assertThat(expected, is(e.getErrorCode()));
        }
    }
    private void callForSuccess(long expected) {
        callForSuccess(expected, defaultInputPath);
    }
    private void callForSuccess(long expected, String inputPath) {
        try {
            assertEquals(expected, call(inputPath));
        } catch (KadaiException e) {
            throw new RuntimeException(e);
        }
    }
    private long call(String inputPath) throws KadaiException {
        return Kadai201408.calcScoreSum(inputPath);
    }

}
