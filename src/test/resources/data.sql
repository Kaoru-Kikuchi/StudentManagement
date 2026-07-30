INSERT INTO students
(
    id,
    name,
    kana_name,
    nickname,
    email,
    area,
    age,
    sex,
    remark,
    is_deleted
)
VALUES
(
    '61e9a148-4fea-4735-8065-0539daa9f3fc',
    '徳水進',
    'トクミズ',
    'トックン',
    'TT@outlook.jp',
    '永野',
    50,
    '男性',
    NULL,
    FALSE
),
(
    '7cc22a59-7ae9-4cc0-853a-c10b3b43922a',
    '小原寛',
    'オバラ',
    'ヒロシ',
    'OH@outlook.jp',
    '埼玉',
    42,
    '男性',
    NULL,
    FALSE
),
(
    '88c6624b-f736-4ed0-9729-b5476068ddab',
    '中村 次郎（更新）',
    'ナカムラ ジロウ',
    'じろうくん',
    'nakamura_update@example.com',
    '埼玉県',
    29,
    '男性',
    '更新テスト',
    FALSE
),
(
    'a19296b7-4856-40e0-aa81-c38428e13030',
    '菊池馨',
    'キクチ',
    'カオル',
    'pp@outlook.jp',
    '埼玉県',
    38,
    '女性',
    '',
    FALSE
),
(
    'ada1f007-7942-11f1-b4d0-b81ea42bf144',
    '山田 太郎',
    'ヤマダ タロウ',
    'たろう',
    'yamada@example.com',
    '東京都',
    37,
    '男性',
    '',
    FALSE
),
(
    'ada3625d-7942-11f1-b4d0-b81ea42bf144',
    '佐藤 花子',
    'サトウ ハナコ',
    'はな',
    'sato@example.com',
    '埼玉県',
    22,
    '女性',
    NULL,
    FALSE
),
(
    'ada3f5bb-7942-11f1-b4d0-b81ea42bf144',
    '鈴木 一郎',
    'スズキ イチロウ',
    'いっちー',
    'suzuki@example.com',
    '千葉県',
    19,
    '男性',
    NULL,
    FALSE
),
(
    'ada48e27-7942-11f1-b4d0-b81ea42bf144',
    '高橋 美咲',
    'タカハシ ミサキ',
    'みさき',
    'takahashi@example.com',
    '神奈川県',
    23,
    '女性',
    NULL,
    FALSE
),
(
    'b4919cd8-7942-11f1-b4d0-b81ea42bf144',
    '田中 健',
    'タナカ ケン',
    'けん',
    'tanaka@example.com',
    '茨城県',
    21,
    '男性',
    '',
    FALSE
);

INSERT INTO students_courses
(
    id,
    student_id,
    course_name,
    course_start_at,
    course_end_at
)
VALUES
(
    '20e961da-5517-4d61-8a63-d85b3561bfdd',
    '61e9a148-4fea-4735-8065-0539daa9f3fc',
    'データベース',
    '2026-07-22 22:34:40',
    '2027-07-22 22:34:40'
),
(
    '245a9da6-084c-4700-aaf9-c0318c5985e9',
    '7cc22a59-7ae9-4cc0-853a-c10b3b43922a',
    'HTML/CSS',
    '2026-07-25 22:48:56',
    '2027-07-25 22:48:56'
),
(
    '38f0af3c-a356-4108-b936-0993603c2b6c',
    'a19296b7-4856-40e0-aa81-c38428e13030',
    'Java基礎',
    '2026-07-22 22:34:13',
    '2027-07-22 22:34:13'
),
(
    '736d38b3-a8c8-4751-bebd-810fa721d370',
    '88c6624b-f736-4ed0-9729-b5476068ddab',
    'Spring Boot応用',
    '2026-07-26 17:18:39',
    '2027-07-26 17:18:39'
),
(
    'ebed9c78-7942-11f1-b4d0-b81ea42bf144',
    'ada1f007-7942-11f1-b4d0-b81ea42bf144',
    'Java基礎',
    '2026-07-01 09:00:00',
    '2026-09-30 18:00:00'
),
(
    'ebef77ea-7942-11f1-b4d0-b81ea42bf144',
    'ada3625d-7942-11f1-b4d0-b81ea42bf144',
    'Spring Boot',
    '2026-07-15 09:00:00',
    '2026-10-15 18:00:00'
),
(
    'ebf2c1ca-7942-11f1-b4d0-b81ea42bf144',
    'ada3f5bb-7942-11f1-b4d0-b81ea42bf144',
    'データベース',
    '2026-08-01 09:00:00',
    '2026-11-30 18:00:00'
),
(
    'ebf38c91-7942-11f1-b4d0-b81ea42bf144',
    'ada48e27-7942-11f1-b4d0-b81ea42bf144',
    'HTML/CSS',
    '2026-07-10 09:00:00',
    '2026-09-10 18:00:00'
),
(
    'ed1d5269-7942-11f1-b4d0-b81ea42bf144',
    'b4919cd8-7942-11f1-b4d0-b81ea42bf144',
    'Java応用',
    '2026-08-15 09:00:00',
    '2026-12-15 18:00:00'
);