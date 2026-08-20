# Spring Data JPA orphanRemovalデバッグラボ

親コレクションから外した子エンティティがDBに残る不具合を再現し、`orphanRemoval = true` で修正するSpring Bootプロジェクトです。

## 対象となる契約

注文 `SO-001` は二つの明細を持ちます。`PEN` の明細を注文から外した後、注文の明細数とDB上の全明細レコード数はどちらも1件になることが契約です。

| 確認項目 | 期待値 |
| --- | --- |
| 注文の明細数 | 1 |
| `sales_order_lines` の総件数 | 1 |
| 外した `PEN` 明細 | DBに存在しない |

## 必要な環境

Java 21とMavenが必要です。このプロジェクトはSpring Boot 3.3.12、Spring Data JPA、H2を使用しています。

## 最新状態の確認

最新のmainブランチには `orphanRemoval = true` を追加済みです。

```bash
mvn test
```

親コレクションから明細を外すと、子テーブルの総件数も減ることを統合テストで確認します。

## バグ状態の再現

不具合状態はGit履歴に残しています。初期コミットをチェックアウトすると、親から外した明細の外部キーだけがnullになり、子レコードがDBに残るためテストが失敗します。

```bash
git checkout 81f7659
mvn test
```

確認後はmainブランチへ戻します。

```bash
git switch main
mvn test
```

## 修正内容

修正前の関連マッピングは、親への操作を子へ連鎖させますが、親コレクションから外した子の削除は指定していません。

```java
@OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL)
private List<SalesOrderLine> lines = new ArrayList<>();
```

修正後は、関連から外れた子を削除対象にします。

```java
@OneToMany(
    mappedBy = "salesOrder",
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private List<SalesOrderLine> lines = new ArrayList<>();
```

## 関連資料

調査過程は [docs/debugging-record.md](docs/debugging-record.md)、SQLで表したテーブル定義と削除処理は [docs/orphan-removal-model.sql](docs/orphan-removal-model.sql)、`UPDATE ... sales_order_id = NULL` の挙動は [docs/hibernate-update-nullable-foreign-key.md](docs/hibernate-update-nullable-foreign-key.md)、テスト実行ログは `docs/01-bug-reproduction.log` と `docs/02-fixed-verification.log`、公式資料の確認メモは [docs/references.md](docs/references.md) にあります。
