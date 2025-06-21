README-ja.md
============

Spvelocity ― Paper / Spigot サーバーから **Velocity プロキシ** をダウンロード＆起動できるプラグイン
----------------------------------------------------------------------------------------------------

![Java](https://img.shields.io/badge/Java-21-blue) ![Paper](https://img.shields.io/badge/Paper-1.21.x-green) ![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## これは何？

Spvelocity は次のことを自動で行う Paper / Spigot プラグインです。  
1. 指定 URL から Velocity の JAR を取得  
2. 別プロセスとして Velocity を起動  
3. `/proxy start|stop|status` でゲーム内／コンソールから制御  

ミニネットワークの検証や CI、テストサーバーに最適です。

---

## 特長

* 最初の `/proxy start` だけで **velocity.jar 自動 DL**  
* Paper / Spigot 1.12 〜 最新まで動作（ビルドは 1.21.4 API）  
* Java 11 以上なら OK（推奨 17 か 21）  
* ノンブロッキング起動：バックグラウンドスレッドで実行  
* Paper サーバー停止時に Velocity も自動で停止  
* `plugins/Spvelocity/config.yml` で簡単設定  

---

## 必要環境

* Paper または Spigot サーバー  
  （1.12 〜 最新。ビルドは Paper-1.21.4 API に対して）  
* Java 11 以上（Velocity が Java11+ 必須）  
* 初回のみインターネット接続（Velocity JAR を取得）

---

## 導入手順（配布 JAR 利用）

1. Releases から `spvelocity-<version>-shaded.jar` をダウンロード  
2. `<サーバー>/plugins` に配置  
3. サーバーを **Java 17 / 21** などで起動  
4. ゲーム内またはコンソールで  
   ```
   /proxy start
   ```  
   → `plugins/Spvelocity/proxy/velocity.jar` が DL され、ポート 25577 で起動

---

## ソースからビルド

```bash
git clone https://github.com/<YOUR-USER>/spvelocity.git
cd spvelocity
mvn clean package       # target/spvelocity-*-shaded.jar が生成
```

---

## 設定ファイル `config.yml`

```yml
velocity:
  download-url: "https://api.papermc.io/v2/projects/velocity/versions/3.3.0-SNAPSHOT/builds/261/downloads/velocity-3.3.0-SNAPSHOT-261.jar"
  jar-name: "velocity.jar"
  port: 25577
```

* ダウンロード URL は PaperMC の Downloads ページ  
  <https://papermc.io/downloads#Velocity> でコピー可能  
* 安定版 (`3.2.0` など) は URL が変わりません  
* SNAPSHOT を常に最新にしたい場合は、API でビルド番号を取得して URL を生成するスクリプトを用意するか、手動で更新してください  

---

## コマンド & 権限

| コマンド               | 説明                         | 権限                    | デフォルト |
|-----------------------|------------------------------|-------------------------|-----------|
| `/proxy start`        | Velocity をダウンロード＆起動 | `spvelocity.command`    | OP        |
| `/proxy stop`         | 起動中の Velocity を停止     | `spvelocity.command`    | OP        |
| `/proxy status`       | Proxy の稼働状況を表示       | `spvelocity.command`    | OP        |

---

## 仕組み

1. `SpvelocityManager` が `plugins/Spvelocity/proxy/velocity.jar` の有無を確認  
2. 無ければ **Apache Commons IO** を使って URL から保存  
3. `java -jar velocity.jar --port <port>` を **ProcessBuilder** で起動  
4. 出力は Paper コンソールへリダイレクト  
5. Paper 停止時に子プロセスを `destroy()` して後処理

---

## ライセンス

MIT License。`LICENSE` を参照してください。  
自由に改造・再配布どうぞ！
