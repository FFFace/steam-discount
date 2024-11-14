## 개요

게임 플랫폼 스팀의 주요 할인 정보를 소개하고 해당 게임과 관련된 내용을 공유할 수 있는 커뮤니티

## 프로젝트

### 참여 인원

- 1명

### 제작 기간

- 약 6주

## 기능


### 스팀 할인 목록

- 스팀 상점 페이지 할인 목록 크롤링
- 게임 타이틀, 이미지, 정가, 할인가 등 주요 내용 DB 저장
- 메인 페이지에서 현재 할인 중인 5가지 항목 랜덤으로 표기
- 주요 할인 게임 100가지 목록으로 표기

### 회원

- 회원 가입
- 로그인 시 Access Token 및 Refresh Token 발급
- Access Token 만료 시 Refresh Token을 통해 새로운 Access Token 발급
- Access Token 재발급 시, Refresh Token도 함께 재발급
- 회원은 USER, ADMIN 중 하나의 Role(권한)을 소지
- ADMIN은 사용자를 관리할 수 있는 관리자 페이지 접근 가능
- 회원 탈퇴

### 커뮤니티

- 모든 회원은 할인 목록에 있는 게임의 게시판을 생성 가능
- 게임 이름과 동일한 게시판 생성
- 각 게시판에는 게시글을 작성할 수 있고 이미지 첨부 가능
- 게시글에는 댓글 및 대댓글 작성 가능
- ADMIN은 모든 게시판에 대해 게시글 작성이 가능하며 USER는 공지사항을 제외한 모든 게시판에 대해 게시글 작성 가능
- ADMIN은 모든 게시글에 대해 수정, 삭제가 가능하며 USER는 자신이 작성한 게시글만 수정, 삭제 가능
- ADMIN과 USER는 모든 게시글에 대해 댓글 및 대댓글 작성 가능
- ADMIN은 모든 댓글 및 대댓글에 대해 수정, 삭제가 가능하며 USER는 자신이 작성한 댓글 및 대댓글만 수정, 삭제 가능

## 기술 스택

### Back-End

- SpringBoot
- Spring Data JPA
- Spring Security
- MySQL
- JWT
- Firebase
- Selenium

## Front-End

- React
- Axios
- MUI
- Nginx
- Toast UI Editor

### 배포

- AWS EC2
- Github Action

## Page-Link

https://steam-discount.p-e.kr/
