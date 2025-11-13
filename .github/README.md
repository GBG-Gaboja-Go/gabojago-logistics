##  🚚 gabojago 

> 이 프로젝트는 MSA 기반 B2B 물류 관리 및 배송 시스템을 구현한 백엔드 애플리케이션입니다.  
각 허브를 중심으로 회사, 제품, 주문, 배송, 사용자 등 다양한 요소를 통합적으로 관리할 수 있는 기능을 제공하며,  
허브 간 최적 경로 산출, 실시간 배송 추적, 자동 재고 관리 등을 통해 운영 효율을 높이는 것을 목표로 합니다.

<br>

## :raised_hands: 프로젝트 소개
- Gabojago 물류는 각 지역에 허브센터를 가지고 있으며 각 허브 센터는 여러 업체의 물건을 보관합니다.
- 해당 상품의 배송 요청이 들어오면 목적지 허브로 물품을 이동시켜 목적지에 배송합니다.
- AI를 활용하여 최종 발송시한을 계산하여 배송 담당자 Slack으로 알림을 보냅니다.

<br>

## ⚙️ 기술 스택

**Backend & Core**

* 언어 : ![Static Badge](https://img.shields.io/badge/Java-red?style=flat-square)  
* 프레임워크 : ![Static Badge](https://img.shields.io/badge/SpringBoot-%23FFFF00?logo=springboot)  
* JDK : ![Static Badge](https://img.shields.io/badge/JDK-17-yellow?style=flat-square)  
* DB : ![Static Badge](https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql&logoColor=white) ![Static Badge](https://img.shields.io/badge/Redis-DC382D?style=flat&logo=redis&logoColor=white)  
* 인프라 : ![Static Badge](https://img.shields.io/badge/docker-%230db7ed.svg?style=flat&logo=docker&logoColor=white) ![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=flat&logo=swagger&logoColor=white) JitPack  
* API Gateway : Spring Cloud Gateway
* Service Discovery : Netflix Eureka
* Configuration : Spring Cloud Config
* 외부 API : Gemini, Slack, KakaoMap  

**Communication & Messaging**
* Inter-Service Communication (Synchronous): OpenFeign 또는 WebClient

**Security & DevOps**
* Security: Spring Security (JWT)



<br>

## 👥 유스케이스  
<img width="700" height="1000" alt="image" src="https://github.com/user-attachments/assets/43794c0f-e36d-425a-9928-b73938e483f9" />

<br>

## 🧬 시스템 아키텍처 
<img width="700" height="531" alt="image" src="https://github.com/user-attachments/assets/7927424a-6bae-455c-a9e0-14799423aa14" />

<br>

## 💿 ERD  
<img width="700" height="1000" alt="Gabojago (2)" src="https://github.com/user-attachments/assets/7e5db71a-e6cc-4865-a324-b426b77b89cc" />


## 📄 API 명세서  
https://www.notion.so/teamsparta/API-29f2dc3ef51480af947ac8c43ea9e7a3

<br>


## 🔗 MSA 구조
### 🏛️ 핵심 인프라 서비스  

| SERVICE         | PORT           | 설명           |
|------------|--------------|--------------|
| Eureka | 8761       | 서비스 등록 및 검색 (Service Discovery) |
| Config | 8888       | 각 서비스의 중앙 집중식 설정 관리 (Configuration Management) |
| Gateway   | 8080     | API 라우팅 및 공통 기능 처리 (Routing & Filtering) |



### 🔎 비즈니스 서비스

| SERVICE         | PORT           | 설명           |
|------------|--------------|--------------|
| User | 20000       | 고객/운영자 관리 - 사용자 정보(회원가입, 인증 등) 관리 및 권한 처리 |
| Order  | 20100     | 주문 관리 - 고객의 상품 주문 및 결제 상태 처리, 주문 내역 조회|
| Hub  | 20200     |물류 허브/업체 관리 - 물품 입고, 보관, 출고 등의 재고 및 위치 관리 |
| Product  | 20300     | 상품 정보 관리 - 업체의 판매 상품의 상세 정보, 가격, 재고 수량 제공. |
| Delivery | 20400       | 배송 프로세스 관리 - 주문에 따른 배송 상태 추적 및 기사 배정 로직 처리 |
| Vendor  | 20500     | 공급/수령 업체 관리 - 상품을 제공하는 외부 판매처 정보 관리 |
| Slack  | 20600     | 알림 서비스 - 배송 담당자에게 최종발송시간 알림 발송  |

<br>


## ✨ 주요 기능

* **🌐 Hub 간 최적 경로 및 시간 계산**
  * Kakao Local API를 활용하여 물류 허브(Hub)의 주소를 좌표(Coordinates)로 정확하게 변환합니다.
  * 변환된 좌표를 기반으로 허브 간의 실제 이동 거리 및 예상 소요 시간을 계산하여 배송 계획에 반영합니다.

* **🛵 배달 담당자 자동 배정 로직**

  * 순번 기반의 순환 배정(Round-Robin) 시스템을 적용하여 담당자를 지정합니다.
  * 배정 시 최근 배달 담당자 ID를 조회하여, 해당 순번보다 큰 담당자 중 가장 순번이 빠른 담당자를 우선 지정합니다.
  * 결번(퇴사/미근무) 등으로 순번이 비어도 시스템이 자동으로 다음 순번을 찾아 배정하여 누락을 방지합니다.

* **🤖 AI 활용 최종 발송 시한 예측 및 Slack 알림**

  * 상품 주문 시 Gemini API를 활용하여 배송 이동경로와 고객 요청사항을 고려한 최종 발송 시한을 도출합니다.
  * 도출된 예측 시간을 포맷팅하여, 담당자 및 관련 채널에 Slack 메시지로 즉시 전송합니다.

<br>

## 🚀 프로젝트 실행 방법  

**1. 환경 설정 (선행 작업)**

- 코드 다운로드:
```bash
git clone <레포지토리 URL>
cd <프로젝트 디렉토리>
```

- 데이터베이스 준비:  
PostgreSQL에 각 서비스별로 독립된 데이터베이스를 생성합니다. (예: user_db, order_db 등)  
⚠️ 중요: 데이터베이스 연결 정보(DB_USER, DB_PASSWORD)를 각 서비스의 설정 파일(예: application.yml 또는 환경 변수)에 설정해야 합니다.

<br>

**2. 환경 변수 설정**
```bash
# 터미널에 환경 변수 설정
export DB_USER='{DB 사용자명}'
export DB_PASSWORD='{DB 비밀번호}'
```
**이 외에 필요한 경우, 서비스별로 특화된 환경 변수(예: GOOGLE_API_KEY)를 설정합니다.**  

<br>

**3. 서비스 실행 순서(권장)**
* **인프라 서비스 먼저 실행**


1. **Config Server** : 가장 먼저 기동되어야 다른 서비스들이 설정을 가져올 수 있습니다.  
2. **Eureka Server** : 서비스 디스커버리. 다른 서비스들이 자신을 등록할 수 있도록 Config 다음으로 실행합니다.  
3. **Gateway** : Eureka에 등록된 서비스 정보를 참고하여 라우팅을 시작합니다.  
4. **비즈니스 서비스**(User, Order, Hub 등) : Gateway 및 인프라가 모두 준비된 후, User Server부터 순서대로 실행합니다.  

<br>

**4. 서비스 상태 확인**
* Eureka Dashboard: http://localhost:8761
* Gateway: http://localhost:8080

<br>

## 👻 팀원

**프로젝트 기간** : 2025.10.31(금) ~ 2025.11.14(금)  
**포지션**      :  백엔드 개발자 5명                    


| 이름         | 역할           |
|------------|--------------|
| 팀장 **류창희** | 유저, 인증, Slack       |
| 팀원 권준성 | 업체, 상품        |
| 팀원 박준형   | 허브, Kakao MapAPI     |
| 팀원 이가현     | 주문, Gemini AI      |
| 팀원 이태경     | 배송 |


