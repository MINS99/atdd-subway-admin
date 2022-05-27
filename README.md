<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```
<br>

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-admin/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-admin/blob/master/LICENSE.md) licensed.

---

## 🚀 미션 소개 - 지하철 노선도 관리
## 미션 설명
+ **지하철 노선도를 관리**할 수 있는 어드민 서비스를 단계별로 구현하세요.
+ 인수 테스트 주도 개발 프로세스를 단계별로 경험하세요.

## 도메인 설명
<img src="https://nextstep-storage.s3.ap-northeast-2.amazonaws.com/155885260e20466497cbf3f344cf7a5d">

### 지하철 역(station)
+ 지하철 역 속성:
  + 이름(name)

### 지하철 구간(section)
+ 지하철 (상행 방향)역과 (하행 방향)역 사이의 연결 정보
+ 지하철 구간 속성:
  + 길이(distance)

### 지하철 노선(line)
+ 지하철 구간의 모음으로 구간에 포함된 지하철 역의 연결 정보
+ 지하철 노선 속성:
  + 노선 이름(name)
  + 노선 색(color)

---

<details>
	<summary>🚀 1단계 - 엔티티 매핑</summary>

## 🚀 1단계 - 지하철역 인수 테스트 작성
## 요구사항
### 기능 요구사항
+ 지하철역 인수 테스트를 완성하세요.
  + [x] 지하철역 목록 조회 인수 테스트 작성하기
  + [x] 지하철역 삭제 인수 테스트 작성하기

### 프로그래밍 요구사항
+ 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.
  
## 힌트
### 인수 테스트 리팩터링
+ JsonPath
  + Json 문서를 읽어오는 DSL
  + JsonPath를 사용하면 Response Dto 객체로 받을 필요 없이 필요한 값만 추출하여 검증에 사용할 수 있음
</details>

## 🚀 2단계 - 지하철 노선 기능
## 요구사항
### 기능 요구사항
+ [x] `요구사항 설명`에서 제공되는 인수 조건을 기반으로 지하철 노선 관리 기능을 구현하세요.
+ [x] 인수 조건을 검증하는 인수 테스트를 작성하세요.
  + [x] 지하철 노선 생성
  + [x] 지하철 노선 목록 조회
  + [x] 지하철 노선 조회
  + [x] 지하철 노선 수정
  + [x] 지하철 노선 삭제

### 프로그래밍 요구사항
+ 아래의 순서로 기능을 구현하세요.
  + 인수 조건을 검증하는 인수 테스트 작성
  + 인수 테스트를 충족하는 기능 구현
+ 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리 시키세요.
+ 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.

## 힌트
### 인수 테스트 격리
#### @DirtiesContext
+ Spring Context를 이용한 테스트 동작 시 스프링 빈의 상태가 변경되면 해당 컨텍스트의 재사용이 불가하여 컨택스트를 다시 로드해야함
+ 스프링빈의 상태가 변경되었다는 설정을 하는 애너테이션
+ 테스트 DB는 메모리 디비로 컨테이너에 띄워져있는 상태이므로 컨텍스트가 다시 로드되면 기존 DB의 내용이 초기화됨

#### @Sql
+ 테스트 수행 시 특정 쿼리를 동작시키는 애너테이션

#### Table Truncate
+ 테이블을 조회하여 각 테이블을 Truncate시켜주는 방법

### 인수 테스트 리팩터링
#### 중복 코드 처리
+ 지하철역과 노선 인수 테스트를 작성하면서 중복되는 부분이 발생하는데 이 부분을 리팩터링 하면 부가적인 코드는 테스트로부터 분리되어 테스트에 조금 더 집중할 수 있게됨
