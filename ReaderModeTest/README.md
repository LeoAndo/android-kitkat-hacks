# 実行結果の確認方法

まだ確認していない！<br>
2台の端末にそれぞれアプリをインストールし、以下の状態で端末同士を近づける。<br>

- ReaderModeアプリ側の端末 : アプリを起動しておく
- HCEアプリ側の端末: 画面をON状態にしておく

## HCEアプリ

processCommandApdu()で受信したADPUコマンドと、ReaderModeアプリが送信したAPDUコマンドの値が一致していること<br>

## Readerアプリ

onTagDiscovered()内で受信したAPDUコマンドと、HCEアプリから送信している応答値が一致していること<br>