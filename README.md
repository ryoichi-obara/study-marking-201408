# 教育課題2014年度8月

あるテキストファイルがあります。  
このファイルの仕様は以下のとおりです。  

1. ファイルはテキスト形式で1行のみ書かれています。  
  2行以上のデータがあった場合、2行目以降は無視してください。  
1. ファイルは半角英数記号のみで記述されています(文字コード：UTF-8)。  
  半角英数記号以外が存在したことによって生じる不具合は考慮しなくても結構です。  
1. データはそれぞれカンマで区切られています。  
1. データの内部は半角英字のみ0字以上です。  
  半角英字以外が存在した場合はエラーとしてください。  
1. 半角英字の小文字が存在した場合は大文字に変換した後に処理を行ってください。  
1. ファイルサイズの上限は1MBとします。  
  ファイルサイズの上限を超えた場合に発生する不具合に関しては考慮しなくても結構です。  
1. ファイル内部に存在するデータの個数の上限は定めません。  
  （ファイルサイズの上限からデータの個数の上限を求めることは可能ですが、  
  あえて上限を定めるようなコーディングを禁止します。）  

このテキストファイルを読み込み、以下の問題を解いてください。  

問題文中に出てくる「データの点数」は以下のように定義します。  
* 文字の点数を A が 1, B が 2, ... Z が 26 と定義します。  
* データに存在するすべての文字の点数の合計をデータの点数とします。  
* データが空の文字列の場合、データの点数は0とします。  


## 問１

データの出現位置(最初のデータが1で、以下2,3...と増えていきます) をnとします。  
nとデータの点数の積の合計を求める関数を作成してください。  

関数は以下の定義に従ってください。  
例)
「AA,b,D,」とファイルに記載されていた場合は
```math
(1+1)*1 + 2*2 + 4*3 + 0*4 = 18
```
となります。

```Java
package jp.ktsystem.studymarking.kadai201407.xxxx; // xxxxの部分は受講者のドメインアカウント名

public class Kadai {
  public static long calcScoreSum(String anInputPath);
}
```

```C#
namespace jp.ktsystem.studymarking.kadai201407.xxxx; // xxxxの部分は受講者のドメインアカウント名

public class Kadai {
  public static long CalcScoreSum(string anInputPath);
}
```

引数
* `anInputPath` : フルパスで****.txtまで指定する  

戻り値
* データの点数の積の合計  


## 問２

全データのデータの点数のうち、最大の点数を持つデータをファイルに出力する関数を作成してください。  
出力形式は以下の通りとします。  

> [データの出現位置(最初のデータが1で、以下2,3...と増えていきます)]:[データの内容]:[データの点数]

なお、最大値が複数存在した場合はそのすべてのデータを改行して出力してください。文字コードは入力ファイルと同様です。  
エラー時はエラー情報をファイルには出力しなくて結構です。  
関数は以下の定義に従ってください。  

```Java
package jp.ktsystem.studymarking.kadai201407.xxxx; // xxxxの部分は受講者のドメインアカウント名

public class Kadai {
  public static void printMaxScore(String anInputPath, String anOutputPath);
}
```

```C#
namespace jp.ktsystem.studymarking.kadai201407.xxxx; // xxxxの部分は受講者のドメインアカウント名

public class Kadai {
  public static void PrintMaxScore(string anInputPath, string anOutputPath);
}
```

引数
* `anInputPath` : フルパスで****.txtまで指定する  
* `anOutputPath` : フルパスで****.txtまで指定する  

戻り値
* なし  


### エラーコード

エラーコードは以下のとおり定めます。

| エラーコード | 内容 |
|--:|:-----------------------|
| 1 | ファイルの入出力エラー |
| 2 | データ内部に半角英字以外の文字が存在した |
| 3 | その他のエラー |
