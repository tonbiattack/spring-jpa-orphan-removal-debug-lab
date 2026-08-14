# orphanRemoval未設定による子エンティティ削除漏れのデバッグ記録

## 期待する契約

注文 `SO-001` にある二件の明細から `PEN` を外した後、注文の明細数と子テーブルの総件数はどちらも1件であることが期待値です。

| 観測項目 | 期待値 |
| --- | --- |
| 注文の明細数 | 1 |
| 子テーブルの総件数 | 1 |
| 外した明細 | 削除されている |

## バグ状態での観測

初期コミットで `mvn test` を実行しました。実行ログは [01-bug-reproduction.log](01-bug-reproduction.log) です。

| 観測項目 | 実測値 | 判断 |
| --- | --- | --- |
| 親注文の明細数 | 1 | Javaオブジェクト上の関連は外れている |
| 子テーブルの総件数 | 2 | 外した子レコードが残っている |
| 子テーブルへのSQL | `update` | 外部キーをnullにしている |
| `delete` SQL | なし | 子レコード削除は実行されていない |

ログでは、`PEN` 明細の `sales_order_id` がnullへ更新されました。その後の全件カウントは2件です。親から見えない状態と、DBから削除された状態が異なることを確認できました。

## 原因

親側の `@OneToMany` は `cascade = CascadeType.ALL` を指定していましたが、`orphanRemoval` はデフォルトの `false` でした。親コレクションから子を外すことは、子エンティティを削除する指示ではありません。

## 修正

関連マッピングへ `orphanRemoval = true` を追加しました。

```java
@OneToMany(
    mappedBy = "salesOrder",
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private List<SalesOrderLine> lines = new ArrayList<>();
```

修正後のログには `delete from sales_order_lines where id = ?` が出力されます。親コレクションから外した子は削除され、全件カウントは1件になります。

## 回帰検証

統合テストは、親注文を再読込した明細数と、子テーブルを全件カウントした結果を別々に検証します。これにより、関連表示だけが正しい状態と、子レコードも削除された状態を区別して確認します。
