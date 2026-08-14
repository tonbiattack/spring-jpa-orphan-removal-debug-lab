# 参照資料メモ

## Jakarta Persistence API

URL: https://jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/OneToMany.html

`@OneToMany` の `orphanRemoval` は省略可能な属性であり、関連から外れたエンティティへremove操作を適用し、そのエンティティへremove操作をカスケードするかを指定します。デフォルト値は `false` です。

## Hibernate ORM User Guide

URL: https://docs.hibernate.org/orm/6.5/userguide/html_single/#associations-one-to-many

Hibernate ORMの関連マッピング節を確認しました。この再現プロジェクトでは、双方向関連の整合性を保つために、親コレクションから子を削除するとき、子の親参照もnullへ更新します。削除自体を期待する要件には、Jakarta Persistenceの `orphanRemoval` を使用します。
