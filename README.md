# 뉴스 큐레이션 서비스, 시점

**뉴스 소비 과정에서 불편을 해결하기 위한 뉴스 큐레이션 서비스, 시점**

## 주요 기능
뉴스 볼 시간이 없는 2030을 위해 관심있는 사건의 전체 맥락을 빠르게 파악할 수 있도록 **2가지 핵심 기능** 제공
   - **토픽 타임라인** : 하나의 사건을 시작부터 현재까지 타임라인 형태로 제공하여 사건의 맥락을 파악하는 기능
   - **후속기사 알림** : 관심 토픽에 새로운 전개가 있을 때마다 알림을 발송

## 아키텍처

## 기술 스택

| 분류 | 기술 |
|------|------|
| **Backend Framework** | ![Kotlin](https://img.shields.io/badge/Kotlin-1.9.25-7F52FF?style=flat&logo=kotlin) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-6DB33F?style=flat&logo=spring-boot) |
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat&logo=postgresql)|
| **External Services** | ![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat&logo=firebase) ![Spring Cloud OpenFeign](https://img.shields.io/badge/Spring_Cloud_OpenFeign-6DB33F?style=flat) |
| **Testing & Documentation** | ![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=flat&logo=junit5) ![Spring REST Docs](https://img.shields.io/badge/Spring_REST_Docs-6DB33F?style=flat&logo=spring) |
| **Build & DevOps** | ![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker) ![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat&logo=github) ![Ktlint](https://img.shields.io/badge/Ktlint-7F52FF?style=flat&logo=kotlin) |
| **Tools** | ![Claude Code](https://img.shields.io/badge/Claude_Code-FF6B35?style=flat&logo=anthropic) |

## 패키지 구조

```
src/main/kotlin/com/flownews/
├── FlowNewsApplication.kt
│
├── api/                           
│   ├── common/                    
│   ├── event/                     
│   │   ├── api/                   
│   │   ├── app/                   
│   │   ├── domain/               
│   │   └── infra/                
│   ├── interaction/               
│   ├── push/                      
│   ├── topic/                     
│   └── user/                      
│
└── config/                       
    ├── FirebaseConfig.kt         
    ├── logger/                   
    └── security/                 
```

### 테스트 구조

```
src/test/kotlin/
├── api/                     
├── config/                        
└── testutils/                     
```

## 추가 링크
- 📚 [**API 문서**](https://redstone-swm.github.io/flownews/)
- 📱 [**플레이스토어**](https://play.google.com/store/apps/details?id=kr.sijeom&hl=ko)