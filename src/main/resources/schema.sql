-- ==============================================
-- 유저(USERS) 테이블
-- ==============================================

create table if not exists users
(
    no          identity primary key,
    id          varchar(100) unique not null,
    password    varchar(255) not null,
    name        varchar(100),
    dept_code   varchar(50),
    company     varchar(100),
    roles       varchar(200),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

-- 인덱스
create index if not exists idx_users_id   on users (id);
create index if not exists idx_users_dept on users (dept_code);

-- ==============================================
-- 부서(DEPT) 테이블
-- ==============================================
create table if not exists dept
(
    dept_code varchar(20) primary key,
    dept_name varchar(100) not null
);

merge into dept (dept_code, dept_name) key (dept_code) values
('A001','IT계정계전환추진팀'),
('A002','디지털플랫폼전환팀'),
('A003','디지털코어팀');

merge into users (id, password, name, dept_code, company, roles) key (id) values
-- admin / secret
('admin', '$2a$10$ZQYzljp.kXPjf/9.h.lVZeoBFgLFmQhDiGDyM7WyEaK4OCib.kW0e', '홍길동','A001','NH','ADMIN'),
-- 1 / 1
('1', '$2a$10$de0SlH54mg7aPpBDVglmqeToedtT3yCAckbSqILtAIz.oot9pAXJS', '김길동','A002','SK','DEVELOPER');


-- ==============================================
-- 권한(ROLES) 테이블 및 초기 데이터
-- ==============================================

create table if not exists roles
(
    role_name   varchar(50) primary key,        -- 권한명 (예: ADMIN, DEVELOPER)
    menu_scope  varchar(200) not null,          -- 메뉴 권한(표시/범위, 예: 'ALL', '로그 조회')
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

-- 인덱스(주키로 충분하지만, 조회 패턴에 맞춰 보강 가능)
create index if not exists idx_roles_menu_scope on roles (menu_scope);

-- 초기 권한 데이터
merge into roles (role_name, menu_scope) key (role_name) values
('ADMIN',     'ALL'),
('DEVELOPER', 'LOG01');

-- ==============================================
-- 메뉴(MENU) 테이블
-- ==============================================
create table if not exists menu
(
    menu_id     varchar(64) primary key,             -- 계층형 ID: <CAT>.<NN>[.<NN>...]
    parent_id   varchar(64),                          -- 부모 메뉴ID (null이면 루트)
    menu_name   varchar(200) not null,                -- 메뉴명
    is_leaf     char(1) not null default 'N',         -- Leaf 메뉴 여부: 'Y' or 'N'
    created_at  timestamp default current_timestamp,  -- 최초생성일자
    updated_at  timestamp default current_timestamp,  -- 최종변경일자
    constraint fk_menu_parent
    foreign key (parent_id) references menu(menu_id),
    constraint ck_menu_is_leaf
    check (is_leaf in ('Y','N'))
);

-- 조회용 인덱스
create index if not exists idx_menu_parent   on menu (parent_id);
create index if not exists idx_menu_name     on menu (menu_name);

-- 동일 부모 아래 동일 이름 방지(선택)
create unique index if not exists uq_menu_parent_name on menu (parent_id, menu_name);

-- ==============================================
-- 메뉴 초기 데이터 (H2 기준 MERGE … KEY 구문)
-- 계층 규칙: [카테고리코드][2자리]… (예: ADM01, LOG02)
-- ROOT(비표시 루트) ──> 카테고리(비리프) ──> 실제 메뉴(리프)
-- ==============================================

-- 루트
merge into menu (menu_id, parent_id, menu_name, is_leaf) key (menu_id) values
    ('ROOT', null, 'ROOT', 'N');

-- ========== ADMIN 관리 ==========
merge into menu (menu_id, parent_id, menu_name, is_leaf) key (menu_id) values
    ('ADM',   'ROOT', 'ADMIN 관리', 'N'),
    ('ADM01', 'ADM',  '사용자 관리', 'Y'),
    ('ADM02', 'ADM',  '권한 관리',   'Y'),
    ('ADM03', 'ADM',  '메뉴 관리',   'Y');

-- ========== NGW 관리 ==========
merge into menu (menu_id, parent_id, menu_name, is_leaf) key (menu_id) values
    ('NGW',   'ROOT', 'NGW 관리',     'N'),
    ('NGW01', 'NGW',  'instance 관리', 'Y');

-- ========== 로그 관리 ==========
merge into menu (menu_id, parent_id, menu_name, is_leaf) key (menu_id) values
    ('LOG',   'ROOT', '로그 관리',     'N'),
    ('LOG01', 'LOG',  '로그 조회',     'Y'),
    ('LOG02', 'LOG',  '로그 레벨 변경', 'Y'),
    ('LOG03', 'LOG',  '샘플페이지',     'Y');

-- ========== 프로퍼티 ==========
merge into menu (menu_id, parent_id, menu_name, is_leaf) key (menu_id) values
    ('PRP',   'ROOT', '프로퍼티',        'N'),
    ('PRP01', 'PRP',  '프로퍼티 관리',    'Y'),
    ('PRP02', 'PRP',  '프로퍼티 리로드',  'Y');

-- ========== 모니터링 ==========
merge into menu (menu_id, parent_id, menu_name, is_leaf) key (menu_id) values
    ('MON',   'ROOT', '모니터링',    'N'),
    ('MON01', 'MON',  '전체 통계',  'Y'),
    ('MON02', 'MON',  'NGW 통계',   'Y'),
    ('MON03', 'MON',  '노드 상세',  'Y');
