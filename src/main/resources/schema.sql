-- ==============================================
-- 로컬 개발을 위한 USER, DEPT 테이블 초기화 스크립트
-- ==============================================

create table if not exists users (
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

-- 부서
create table if not exists dept (
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